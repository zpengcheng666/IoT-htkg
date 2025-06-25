package org.jeecg.common.util.minio;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadMinioService {
    /**
     * 分片上传初始化
     *
     * @param path        路径
     * @param filename    文件名
     * @param partCount   分片数量
     * @param contentType /
     * @return /
     */
    MultiPartUploadResp initMultiPartUpload(String path, String filename, Integer partCount, String contentType,String md5);
    /**
     * 本地分片上传初始化
     *
     * @param path        路径
     * @param filename    文件名
     * @param partCount   分片数量
     * @param contentType /
     * @return /
     */
    MultiPartUploadResp initLocalMultiPartUpload(String path, String filename, Integer partCount, String contentType,String md5) throws IOException;

    /**
     * 完成分片上传
     *
     * @param objectName 文件名
     * @param uploadId 标识
     * @return /
     */
    boolean mergeMultipartUpload(String objectName, String uploadId);


    /**
     * 完成分片上传
     *
     * @param uploadId 标识
     * @return /
     */
    boolean mergeLocalMultipartUpload(  String uploadId) throws IOException;


    boolean uploadLccalPartFile(MultipartFile file,  String upId,String partNum) throws IOException;

}
