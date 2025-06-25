package org.jeecg.common.util.minio;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.ImmutableList;
import com.hss.core.common.util.Md5Util;
import com.hss.core.config.RelatedConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadMinioServiceImpl  implements UploadMinioService{

    @Autowired
    private final MinioUtils minIoUtils;

    /**
     * 初始化分片上传
     *
     * @param path 文件路径
     * @param filename 文件名
     * @param partCount 分片数量
     * @param contentType 文件内容类型
     * @param md5 文件的MD5值
     * @return 分片上传响应对象
     */
    @Override
    public MultiPartUploadResp initMultiPartUpload(String path, String filename, Integer partCount, String contentType,String md5) {
        // 规范化路径，避免重复的斜杠
        path = path.replaceAll("/+", "/");
        if (path.indexOf("/") == 0) {
            path = path.substring(1);
        }
        String filePath = path + "/" + filename;

        MultiPartUploadResp result;
        // 单文件，直接上传
        if (partCount == 1) {
            String uploadObjectUrl = minIoUtils.getUploadObjectUrl(filePath);
            result = new MultiPartUploadResp();
            result.setParts(ImmutableList.of(new PartFileResp(0,uploadObjectUrl)));
        } else {//多文件，分片上传
            result = minIoUtils.initMultiPartUpload(filePath, partCount, contentType);
        }
        // 设置创建时间、路径、MD5值和分片数量
        result.setCreatedAt(new Date());
        result.setPath(filePath);
        result.setMd5(md5);
        result.setPartCount(partCount);

        return result;
    }

    /**
     * 初始化本地分片上传
     *
     * @param path 文件路径
     * @param filename 文件名
     * @param partCount 分片数量
     * @param contentType 文件内容类型
     * @param md5 文件的MD5值
     * @return 分片上传响应对象
     * @throws IOException 如果创建目录失败或写入临时文件失败
     */
    @Override
    public MultiPartUploadResp initLocalMultiPartUpload(String path, String filename, Integer partCount, String contentType,String md5) throws IOException {
        // 规范化路径，避免重复的斜杠
        path = path.replaceAll("/+", "/");
        if (path.indexOf("/") == 0) {
            path = path.substring(1);
        }
        String filePath = path + "/" + filename;

        MultiPartUploadResp result = new MultiPartUploadResp();
        // 设置创建时间、路径、MD5值和分片数量
        result.setCreatedAt(new Date());
        result.setPath(filePath);
        result.setMd5(md5);
        result.setPartCount(partCount);
        result.setOriginalFileName(filename);

        // 生成唯一的上传ID
        String upId = UUID.randomUUID().toString();
        result.setUploadId(upId);

        // 创建分片信息列表
        List<PartFileResp> parts = new ArrayList<>();
        for(int i=1; i<=partCount; i++){
            String partFile = filePath + "/" + i;
            parts.add(new PartFileResp(i, partFile));
        }
        result.setParts(parts);

        // 创建临时目录
        String absPath = RelatedConfig.getUploadPath() + "/tmp/" + upId;
        File tempFile = new File(absPath);

        if (!tempFile.exists() && !tempFile.mkdirs()) {
            throw new IOException("创建目录失败: " + tempFile);
        }

        // 将上传信息保存为JSON文件
        String json = JSONUtil.toJsonPrettyStr(result);
        String schema = absPath + "/" + "schema.json";
        FileUtils.writeStringToFile(new File(schema), json, "UTF-8");

        return result;
    }

    /**
     * 合并分片上传
     *
     * @param objectName 对象名称
     * @param uploadId 上传ID
     * @return 合并是否成功
     */
    @Override
    public boolean mergeMultipartUpload(String objectName, String uploadId) {
        return minIoUtils.mergeMultipartUpload(objectName, uploadId);
    }

    /**
     * 合并本地分片上传
     *
     * @param uploadId 上传ID
     * @return 合并是否成功
     * @throws IOException 如果读写文件失败
     */
    @Override
    public boolean mergeLocalMultipartUpload( String uploadId) throws IOException {
        String absPath = RelatedConfig.getUploadPath() + "/tmp/" + uploadId;
        String contentAbsPath = absPath + "/content";
        String schema = absPath + "/schema.json";
        File schemaFile = new File(schema);
        if (!schemaFile.exists() ) {
            return false;
        }
        String content = FileUtils.readFileToString(schemaFile, "UTF-8");
        MultiPartUploadResp result = JSONUtil.toBean(content, MultiPartUploadResp.class);

        String mergedFilePath = absPath + "/" + result.getOriginalFileName();
        if(result.getPartCount() <= 0){
            return false;
        }
        try (FileOutputStream fos = new FileOutputStream(mergedFilePath)) {
            for(int i=1; i<=result.getPartCount(); i++){
                File chunk = new File(contentAbsPath + "/" + i);
                if(!chunk.exists()){
                    // 任何一个文件不存在，则返回失败
                    return false;
                }
                // 合并所有文件
                try (FileInputStream fis = new FileInputStream(chunk)) {
                    byte[] buffer = new byte[1024 * 1024]; // 1MB 缓冲区
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    log.error("Error reading file chunk: " + chunk);
                    return  false;
                }
            }
        }catch (IOException e){
            log.error("Error writing merged file: " + mergedFilePath);
            return  false;
        }
        boolean ret = false;

        // 校验合并后的文件MD5值
        if(Objects.equals(Md5Util.calculateFileMD5(mergedFilePath), result.getMd5())){
            ret = true;
            FileUtils.deleteDirectory(new File(contentAbsPath));
        }

        return ret;
    }

    /**
     * 上传本地分片文件
     *
     * @param file 分片文件
     * @param upId 上传ID
     * @param partNum 分片编号
     * @return 上传是否成功
     * @throws IOException 如果创建目录失败或文件传输失败
     */
    @Override
    public boolean uploadLccalPartFile(MultipartFile file, String upId,String partNum) throws IOException {

        String absPath = RelatedConfig.getUploadPath() + "/tmp/" + upId;
        File tempFile = new File(absPath);

        if (!tempFile.exists() && !tempFile.mkdirs()) {
            throw new IOException("创建目录失败: " + tempFile);
        }

        String schema = absPath + "/" + "schema.json";
        File schemaFile = new File(schema);
        if (!schemaFile.exists() ) {
            return false;
        }

        String contentPath = absPath + "/content/";
        new File(contentPath).mkdirs();

        File dest = new File(contentPath + partNum);
        // 使用临时文件转移
        file.transferTo(dest);
        if(dest.exists() && dest.isFile() && dest.length()>0){
            return true;
        }

        return false;
    }
}
