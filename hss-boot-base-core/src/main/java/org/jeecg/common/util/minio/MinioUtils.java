package org.jeecg.common.util.minio;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.HashMultimap;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import io.minio.messages.Part;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * minio 文件服务器工具类
 * @Author zpc
 * @Date 2024/11/18 20:19
 * @ClassName: MinioUtil
 */
@Slf4j
@Component
public class MinioUtils {
    public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    public static final Long DEFAULT_PART_SIZE = 10 * 5242880L; // 50MB-part size
    public static final String URL_SEPRATOR = "/";

    @Value("${minio.url}")
    @Getter
    private String url;

    @Value("${minio.port}")
    @Getter
    private Integer port;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.secure}")
    private Boolean secure;

    @Value("${minio.bucketName}")
    @Getter
    private String bucketName;

    @Getter
    @Value("${minio.enable}")
    private boolean enable;

    private CustomMinioClient buildMinIoClient() {
        MinioClient minioClient = MinioClient.builder().endpoint(url, port, secure).credentials(accessKey, secretKey).build();
        return new CustomMinioClient(minioClient);
    }

    /**
     * 列出目录下所有文件
     */
    public List<String> listFiles(String directoryPath){
        List<String> fileList = new ArrayList<>();
        try {
            // 确保目录路径以 / 结尾
            String prefix = directoryPath.endsWith("/") ? directoryPath : directoryPath + "/";

            Iterable<Result<Item>> results = buildMinIoClient().listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(prefix)
                            .recursive(true)
                            .build()
            );

            // 收集所有文件路径
            for (Result<Item> result : results) {
                Item item = result.get();
                if (!item.isDir()) {
                    fileList.add(item.objectName());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to list files", e);
        }
        return fileList;
    }

    /**
     * 桶是否存在
     *
     * @param bucket 桶名 @NotNull
     * @return boolean true 存在； false 不存在；
     * @operation add
     * @date 14:00 2020/10/29
     **/
    public boolean bucketExists(String bucket) throws MinioException {
        try {
            return buildMinIoClient().bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        } catch (Exception ex) {
            throw new MinioException(ex.getMessage());
        }
    }


    /**
     * 如果桶不存在则创建
     *
     * @param bucket 桶名 @NotNull
     * @return void
     * @operation add
     * @date 14:00 2020/10/29
     **/
    public void createBucketByNotExists(String bucket) throws MinioException {
        if (!bucketExists(bucket)) {
            try {
                buildMinIoClient().makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            } catch (Exception ex) {
                throw new MinioException(ex.getMessage());
            }
        }
    }

    /**
     * 流式上传文件:桶不存在会抛异常
     *
     * @param bucket 桶名称 @NotNull
     * @param objectName 存储目标名称 @NotNull 多路径存储实例： package/subpackage/.../objectName 开头不要有/
     * @param is 输入流 @NotNull
     * @param objectSize 文件大小
     * @param partSize 分片大小
     * @param contentType 文本类型 默认类型：application/octet-stream
     * @return void
     * @operation add
     * @date 14:01 2020/10/29
     **/
    public String putObjectThrows(String bucket, String objectName, InputStream is, Long objectSize, Long partSize,
                                  String contentType, String folderDir) throws MinioException {
        if (!bucketExists(bucket)) {
            throw new MinioException("The bucket not exists.");
        }
        return putObject(bucket, objectName, is, objectSize, partSize, contentType,folderDir);
    }

    /**
     * 流式上传文件
     * @param objectName 存储目标名称 @NotNull 多路径存储实例
     * @param is 文件流
     * @param objectSize 文件大小
     * @return
     * @throws MinioException
     */
    public String putObjectSafe( String objectName, InputStream is, Long objectSize, String folderDir) throws MinioException{
        return   putObjectSafe(bucketName, objectName, is, objectSize, DEFAULT_PART_SIZE, DEFAULT_CONTENT_TYPE,folderDir);
    }

    /**
     * 流式上传文件:桶不存在会创建桶
     *
     * @param bucket 桶名称 @NotNull
     * @param objectName 存储目标名称 @NotNull 多路径存储实例： package/subpackage/.../objectName 开头不要有/
     * @param is 输入流 @NotNull
     * @param objectSize 文件大小
     * @param partSize 分片大小
     * @param contentType 文本类型 默认类型：application/octet-stream
     * @param folderDir 文件夹路径
     * @return void
     * @operation add
     * @date 14:01 2020/10/29
     **/
    public String putObjectSafe(String bucket, String objectName, InputStream is, Long objectSize, Long partSize,
                                String contentType, String folderDir) throws MinioException {
        createBucketByNotExists(bucket);
        return putObject(bucket, objectName, is, objectSize, partSize, contentType, folderDir);
    }


    /**
     * 根据文件地址上传文件：桶不存在抛出异常
     *
     * @param bucket 桶名称 @NotNull
     * @param objectName 存储目标名称 @NotNull 多路径存储实例： package/subpackage/.../objectName 开头不要有/
     * @param filePath 文件存储路径 @NotNull
     * @param contentType 文本类型 默认类型：application/octet-stream
     * @return void
     * @operation add
     * @date 14:01 2020/10/29
     **/
    public void uploadObjectThrows(String bucket, String objectName, String filePath, String contentType) throws MinioException {
        if (!bucketExists(bucket)) {
            throw new MinioException("The bucket not exists.");
        }
        try {
            buildMinIoClient().uploadObject(UploadObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .filename(filePath)
                    .contentType(OptionalUtil.convertBlankToDef(contentType, "application/octet-stream"))
                    .build());
        } catch (Exception ex) {
            throw new MinioException(ex.getMessage());
        }
    }


    public InputStream getObject( String objectName) throws MinioException {
        return getObject(bucketName, objectName);
    }

    /**
     * 获取文件流
     *
     * @param bucket 桶名称 @NotNull
     * @param objectName 存储目标名称 @NotNull
     * @return java.io.InputStream
     * @operation add
     * @date 14:39 2020/10/29
     **/
    public InputStream getObject(String bucket, String objectName) throws MinioException {
        try {
            return buildMinIoClient().getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build());
        } catch (Exception ex) {
            throw new MinioException(ex.getMessage());
        }
    }


    public byte[] getObjectWithByteArray(String objectName) throws MinioException {
        return getObjectWithByteArray(bucketName, objectName);
    }

    /**
     * 字节形式读取文件
     *
     * @param bucket 桶名称 @NotNull
     * @param objectName 存储目标名称 @NotNull
     * @return java.io.InputStream
     * @operation add
     * @date 14:39 2020/10/29
     **/
    public byte[] getObjectWithByteArray(String bucket, String objectName) throws MinioException {
        ArrayList<byte[]> partList = new ArrayList<>();
        try {
            InputStream is = buildMinIoClient().getObject(GetObjectArgs.builder()
                    .bucket(bucket).object(objectName).build());
            byte[] buffered = new byte[1024];
            int len;
            while ((len = is.read(buffered)) > 0) {
                byte[] temp = new byte[len];
                System.arraycopy(buffered, 0, temp, 0, len);
                partList.add(temp);
            }
        } catch (Exception ex) {
            throw new MinioException(ex.getMessage());
        }
        if (partList.isEmpty()) {
            return new byte[]{0};
        }
        Integer size = partList.stream().map(x -> x.length).reduce(0, Integer::sum);
        byte[] result = new byte[size];
        int currentIndex = 0;
        for (byte[] bytes : partList) {
            for (byte aByte : bytes) {
                result[currentIndex++] = aByte;
            }
        }
        return result;
    }

    public void removeFile(String objectName) throws MinioException {
        removeFile(bucketName, objectName);
    }

    /**
     * 删除文件
     *
     * @param bucket 桶名称 @NotNull
     * @param objectName 存储目标名称 @NotNull
     * @return void
     * @operation add
     * @date 10:49 2020/10/30
     **/
    public void removeFile(String bucket, String objectName) throws MinioException {
        try {
            buildMinIoClient().removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectName).build());
        } catch (Exception ex) {
            throw new MinioException(ex.getMessage());
        }
    }


    /**
     * 复制文件
     *
     * @param sourceBucket 源文件桶 @NotNull
     * @param sourceObject 源文件名 @NotNull
     * @param bucket 目标桶 @NotNull
     * @param object 目标文件 @NotNull
     * @return void
     * @operation add
     * @date 11:04 2020/10/30
     **/
    public void copyObject(String sourceBucket, String sourceObject, String bucket, String object) throws MinioException {
        try {
            CopySource source = CopySource.builder().bucket(sourceBucket).object(sourceObject).build();
            buildMinIoClient().copyObject(CopyObjectArgs.builder().bucket(bucket).object(object).source(source).build());
        } catch (Exception ex) {
            throw new MinioException(ex.getMessage());
        }
    }



    /**
     * 流式上传文件
     *
     * @param bucket 桶名称 @NotNull
     * @param objectName 存储目标名称 @NotNull 多路径存储实例： package/subpackage/.../objectName 开头不要有/
     * @param is 输入流 @NotNull
     * @param objectSize 文件大小
     * @param partSize 分片大小
     * @return void
     * @author GYP
     * @operation add
     * @date 14:01 2020/10/29
     **/
    private String putObject(String bucket, String objectName, InputStream is, Long objectSize, Long partSize,
                             String contentType, String folderDir) throws MinioException {
        StringBuilder fileNameSb = new StringBuilder();
        try {
            /* 暂时注释掉，不需要按照日期形式生成目录*/
//            String nowTime = DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
//            if(!folderDir.endsWith("/"))
//                folderDir += "/";
//            fileNameSb.append(folderDir).append(DateUtil.format(DateUtil.date(), "yyy-MM-dd"))
//                    .append("/").append(nowTime).append("/").append(objectName);

            fileNameSb.append(objectName);
            objectSize = OptionalUtil.getValueOrDef(objectSize, (long) is.available());
            partSize = OptionalUtil.getValueOrDef(partSize, -1L);
            buildMinIoClient().putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileNameSb.toString())
                    .stream(is, objectSize, partSize)
                    .contentType(OptionalUtil.convertBlankToDef(contentType, "application/octet-stream"))
                    .build());
        } catch (Exception ex) {
            throw new MinioException(ex.getMessage());
        }
        return fileNameSb.toString();
    }


    /**
     * 处理并上传zip文件及其内部结构到MinIO
     * 该方法读取zip文件，保持其内部结构，并将所有内容上传到指定的MinIO桶
     *
     * @param bucketName  MinIO桶的名称
     */
    public String processAndUploadZipFileWithStructure(MultipartFile zipFile, String bucketName) {
        String entryName = null;
        try (ZipInputStream zipInputStream = new ZipInputStream(zipFile.getInputStream())) {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                entryName = zipEntry.getName();
                // 创建"文件夹"对象
                if (zipEntry.isDirectory()) {
                    createDirectoryInMinIO(bucketName, entryName);
                } else {
                    // 读取文件内容到ByteArrayInputStream
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = zipInputStream.read(buffer)) != -1) {
                        baos.write(buffer, 0, len);
                    }
                    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                    // 上传文件到MinIO
                    uploadToMinIO(bais, bucketName, entryName);
                }
                zipInputStream.closeEntry();
            }
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage());
            throw new RuntimeException("文件上传处理失败", e);
        }
        return entryName;
    }

    /**
     * 跳过zip文件中的条目直到目标条目
     * 该方法用于在上传特定条目之前，跳过zip文件中所有不匹配的条目
     *
     * @param inputStream 输入流
     * @param targetEntry 目标条目
     * @throws IOException 如果在处理输入流时发生I/O错误
     */
    private void skipEntries(InputStream inputStream, ZipEntry targetEntry) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (zipEntry.equals(targetEntry)) {
                break;
            }
            zipInputStream.closeEntry();
        }
    }

    /**
     * 上传文件到MinIO
     * 该方法将输入流中的内容上传到指定的MinIO桶和对象名称
     *
     * @param inputStream 输入流
     * @param bucketName  桶名称
     * @param objectName 对象名称
     * @throws Exception 如果上传过程中发生错误
     */
    public void uploadToMinIO(InputStream inputStream, String bucketName, String objectName) throws Exception {
        MinioClient minioClient = buildMinIoClient();
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream,  inputStream.available(), 10485760) // 10MB 的块大小
                        .contentType("application/octet-stream")
                        .build()
        );
    }

    /**
     * 在MinIO中创建目录
     * 该方法通过创建一个以正斜线（/）结尾，大小为0的Object来模拟文件夹
     *
     * @param bucketName  桶名称
     * @param directoryName 目录名称
     * @throws Exception 如果创建过程中发生错误
     */
    public void createDirectoryInMinIO(String bucketName, String directoryName) throws Exception {
        buildMinIoClient().putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(directoryName.endsWith("/") ? directoryName : directoryName + "/")
                        .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build()
        );
    }

    /**
     * 上传文件到MinIO
     * 该方法将输入流中的内容上传到指定的MinIO桶和对象名称
     *
     * @param inputStream 输入流
     * @param bucketName  桶名称
     * @param fileName 文件名
     * @param directory 文件目录
     * @throws Exception 如果上传过程中发生错误
     */
    public void uploadToDirectoryMinIO(InputStream inputStream, String bucketName, String directory, String fileName) throws Exception {
        MinioClient minioClient = buildMinIoClient();
        // 处理目录路径格式
        String objectName;
        if (directory != null && !directory.isEmpty()) {
            // 确保目录以 "/" 开头和结尾
            directory = directory.startsWith("/") ? directory : "/" + directory;
            directory = directory.endsWith("/") ? directory : directory + "/";
            // 去除文件名开头的 "/" (如果有)
            fileName = fileName.startsWith("/") ? fileName.substring(1) : fileName;
            objectName = directory + fileName;
        } else {
            objectName = fileName;
        }

        // 去除对象名称开头的 "/" (如果有)
        objectName = objectName.startsWith("/") ? objectName.substring(1) : objectName;

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(inputStream, inputStream.available(), 10485760) // 10MB 的块大小
                .contentType("application/octet-stream")
                .build()
        );
    }

    /**
     * 创建目录
     * @param directoryPath 目录路径
     * @return 是否创建成功
     */
    public boolean createDirectory(String directoryPath) {
        try {
            // 1. 标准化目录路径
            String normalizedPath = normalizePath(directoryPath);

            // 2. 检查目录是否已存在
            if (isDirectoryExists(normalizedPath)) {
                log.info("Directory already exists: {}", normalizedPath);
                return true;
            }

            // 3. 创建空对象作为目录标识
            try (ByteArrayInputStream emptyContent = new ByteArrayInputStream(new byte[0])) {
                buildMinIoClient().putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(normalizedPath)
                        .stream(emptyContent, 0, -1)
                        .contentType("application/x-directory")// 设置特殊的内容类型标识目录
                        .build());
            }

            log.info("Successfully created directory: {}", normalizedPath);
            return true;

        } catch (Exception e) {
            log.error("Failed to create directory: {}", directoryPath, e);
            return false;
        }
    }

    /**
     * 检查目录是否存在
     * @param directoryPath 目录路径
     * @return 是否存在
     */
    public boolean isDirectoryExists(String directoryPath) {
        try {
            String normalizedPath = normalizePath(directoryPath);

            // 1. 尝试获取目录对象的状态
            try {
                StatObjectResponse stat = buildMinIoClient().statObject(
                        StatObjectArgs.builder()
                                .bucket(bucketName)
                                .object(normalizedPath)
                                .build()
                );
                return stat != null;
            } catch (Exception e) {
                // 如果对象不存在，检查是否有以此路径为前缀的对象
                Iterable<Result<Item>> results = buildMinIoClient().listObjects(
                        ListObjectsArgs.builder()
                                .bucket(bucketName)
                                .prefix(normalizedPath)
                                .maxKeys(1)
                                .build()
                );

                return results.iterator().hasNext();
            }

        } catch (Exception e) {
            log.error("Error checking directory existence: {}", directoryPath, e);
            return false;
        }
    }

    /**
     * 标准化路径
     * @param path 原始路径
     * @return 标准化后的路径
     */
    private String normalizePath(String path) {
        // 1. 替换反斜杠为正斜杠
        String normalized = path.replace("\\", "/");

        // 2. 移除开头的斜杠
        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }

        // 3. 确保路径以斜杠结尾
        if (!normalized.endsWith("/")) {
            normalized += "/";
        }

        return normalized;
    }

    /**
     * 列出目录内容
     * @param directoryPath 目录路径
     * @return 目录内容列表
     */
    public List<String> listDirectory(String directoryPath) {
        List<String> contents = new ArrayList<>();
        try {
            String normalizedPath = normalizePath(directoryPath);

            Iterable<Result<Item>> results = buildMinIoClient().listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(normalizedPath)
                            .recursive(false)
                            .build()
            );

            for (Result<Item> result : results) {
                Item item = result.get();
                contents.add(item.objectName());
            }

        } catch (Exception e) {
            log.error("Error listing directory contents: {}", directoryPath, e);
        }
        return contents;
    }

    /**
     * 删除目录及其内容
     * @param directoryPath 目录路径
     * @return 是否删除成功
     */
    public boolean deleteDirectory(String directoryPath) {
        try {
            String normalizedPath = normalizePath(directoryPath);

            // 1. 列出目录中的所有对象
            Iterable<Result<Item>> results = buildMinIoClient().listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(normalizedPath)
                            .recursive(true)
                            .build()
            );

            // 2. 删除所有对象
            for (Result<Item> result : results) {
                Item item = result.get();
                buildMinIoClient().removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucketName)
                                .object(item.objectName())
                                .build()
                );
            }

            return true;

        } catch (Exception e) {
            log.error("Failed to delete directory: {}", directoryPath, e);
            return false;
        }
    }



    /**
     * 单文件签名上传
     *
     * @param objectName 文件全路径名称
     * @return /
     */
    public String getUploadObjectUrl(String objectName) {
        try {
            return  buildMinIoClient().getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(1, TimeUnit.DAYS)
                            //.extraHeaders(headers)
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  初始化分片上传
     *
     * @param objectName 文件全路径名称
     * @param partCount 分片数量
     * @param contentType 类型，如果类型使用默认流会导致无法预览
     * @return /
     */
    public MultiPartUploadResp initMultiPartUpload(String objectName, int partCount, String contentType) {
        MultiPartUploadResp result = new MultiPartUploadResp();
        try {
            if (StrUtil.isBlank(contentType)) {
                contentType = "application/octet-stream";
            }
            HashMultimap<String, String> headers = HashMultimap.create();
            headers.put("Content-Type", contentType);
            String uploadId =  buildMinIoClient().initMultiPartUpload(bucketName, null, objectName, headers, null);

            result.setUploadId(uploadId);

            Map<String, String> reqParams = new HashMap<>();
            reqParams.put("response-content-type", "application/json");
            reqParams.put("uploadId", uploadId);

            for (int i = 1; i <= partCount; i++) {
                reqParams.put("partNumber", String.valueOf(i));
                String uploadUrl =  buildMinIoClient().getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.PUT)
                                .bucket(bucketName)
                                .object(objectName)
                                .expiry(1, TimeUnit.DAYS)
                                .extraQueryParams(reqParams)
                                .build());
                PartFileResp part = new PartFileResp(i, uploadUrl);
                result.getParts().add(part);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * 分片上传完后合并
     *
     * @param objectName 文件全路径名称
     * @param uploadId 返回的uploadId
     * @return /
     */
    public boolean mergeMultipartUpload(String objectName, String uploadId) {
        try {
            Part[] parts = new Part[1000];//目前仅做了最大1000分片
            ListPartsResponse partResult =  buildMinIoClient().listMultipart(bucketName, null, objectName, 1000, 0, uploadId, null, null);
            int partNumber = 1;
            for (Part part : partResult.result().partList()) {
                parts[partNumber - 1] = new Part(partNumber, part.etag());
                partNumber++;
            }
            buildMinIoClient().mergeMultipartUpload(bucketName, null, objectName, uploadId, parts, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
