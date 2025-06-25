package org.jeecg.common.util.minio;

import com.google.common.collect.Multimap;
import io.minio.CreateMultipartUploadResponse;
import io.minio.ListPartsResponse;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.errors.*;
import io.minio.messages.Part;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CustomMinioClient extends MinioClient {

    public CustomMinioClient(MinioClient client) {
        super(client);
    }

    /**
     * 初始化多部分上传
     *
     * 此方法用于在指定的存储桶和区域中初始化一个大文件的多部分上传请求它还会生成一个唯一的上传ID，
     * 该ID用于在后续的上传部分中标识和管理上传过程中的各个部分
     *
     * @param bucket 存储桶名称，指定要上传文件的目标存储桶
     * @param region 区域信息，指示存储桶所在的地理区域
     * @param object 对象键，即上传文件在存储桶中的名称
     * @param headers 多值映射，包含请求的头信息，如内容类型、内容长度等
     * @param extraQueryParams 多值映射，包含额外的查询参数，用于扩展请求的灵活性
     * @return 返回上传ID，一个唯一的字符串，用于跟踪和管理多部分上传的过程
     *
     * 此方法可能抛出多种异常，包括但不限于：IOException, InvalidKeyException, NoSuchAlgorithmException,
     * InsufficientDataException, ServerException, InternalException, XmlParserException, InvalidResponseException,
     * ErrorResponseException这些异常需要在调用此方法时进行适当的处理
     */
    public String initMultiPartUpload(String bucket, String region, String object, Multimap<String, String> headers, Multimap<String, String> extraQueryParams) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, ServerException, InternalException, XmlParserException, InvalidResponseException, ErrorResponseException {
        // 创建一个multipart upload响应对象，用于存储初始化上传请求的响应数据
        CreateMultipartUploadResponse response = this.createMultipartUpload(bucket, region, object, headers, extraQueryParams);

        // 返回上传ID，这是后续上传过程中必需的关键信息
        return response.result().uploadId();
    }

    /**
     * 合并分片上传
     *
     * 此方法用于合并之前已上传的分片，完成整个文件的上传过程它模拟了一个“合并”操作，
     * 实际上调用的是completeMultipartUpload方法，这表明上传过程已经进入完成阶段
     *
     * @param bucketName 存储桶名称
     * @param region 存储桶所在地域
     * @param objectName 文件名称或路径
     * @param uploadId 分片上传的唯一标识符
     * @param parts 已上传分片的数组，包含各分片的信息
     * @param extraHeaders 额外的HTTP头信息，用于自定义请求
     * @param extraQueryParams 额外的查询参数，用于自定义请求
     * @return 返回上传响应对象，包含上传结果的相关信息
     * @throws IOException 当发生I/O错误时
     * @throws InvalidKeyException 当提供的访问密钥无效时
     * @throws NoSuchAlgorithmException 当指定的加密算法不存在时
     * @throws InsufficientDataException 当响应数据不完整时
     * @throws ServerException 当服务器发生错误时
     * @throws InternalException 当内部发生错误时
     * @throws XmlParserException 当解析XML响应时发生错误
     * @throws InvalidResponseException 当响应无效时
     * @throws ErrorResponseException 当服务器返回错误响应时
     */
    public ObjectWriteResponse mergeMultipartUpload(String bucketName, String region, String objectName,
                                                    String uploadId, Part[] parts, Multimap<String, String> extraHeaders,
                                                    Multimap<String, String> extraQueryParams) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, ServerException, InternalException, XmlParserException, InvalidResponseException, ErrorResponseException {

        // 调用completeMultipartUpload方法来完成分片上传的合并操作
        return this.completeMultipartUpload(bucketName, region, objectName, uploadId, parts, extraHeaders, extraQueryParams);
    }

    /**
     * 列出一个对象的多部分上传的各个部分
     * 此方法用于获取指定上传ID的多部分上传信息，可以帮助用户追踪和管理大文件的上传过程
     *
     * @param bucketName 存储桶名称，用于指定要上传对象的存储桶
     * @param region 存储桶所在地域，用于确定请求的路由
     * @param objectName 对象名称，用于指定要上传的对象
     * @param maxParts 指定每次返回的最大部分数目，用于分页查询
     * @param partNumberMarker 指定从哪个部分编号开始返回结果，用于分页查询
     * @param uploadId 上传ID，用于标识特定的多部分上传事件
     * @param extraHeaders 额外的请求头，用于处理特定的请求场景
     * @param extraQueryParams 额外的查询参数，用于处理特定的请求场景
     * @return 返回包含多部分上传信息的响应对象
     * @throws NoSuchAlgorithmException 当无法使用安全算法时抛出
     * @throws InsufficientDataException 当数据不足时抛出
     * @throws IOException 当发生I/O错误时抛出
     * @throws InvalidKeyException 当密钥无效时抛出
     * @throws ServerException 当服务器发生错误时抛出
     * @throws XmlParserException 当解析XML响应时抛出
     * @throws ErrorResponseException 当收到错误响应时抛出
     * @throws InternalException 当发生内部错误时抛出
     * @throws InvalidResponseException 当响应无效时抛出
     */
    public ListPartsResponse listMultipart(String bucketName, String region, String objectName,
                                           Integer maxParts, Integer partNumberMarker, String uploadId,
                                           Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws NoSuchAlgorithmException, InsufficientDataException, IOException, InvalidKeyException, ServerException, XmlParserException, ErrorResponseException, InternalException, InvalidResponseException {
        return this.listParts(bucketName, region, objectName, maxParts, partNumberMarker, uploadId, extraHeaders, extraQueryParams);
    }
}
