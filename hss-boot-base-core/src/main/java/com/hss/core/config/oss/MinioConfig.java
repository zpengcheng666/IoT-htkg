package com.hss.core.config.oss;

//import com.hss.core.common.constant.CommonConstant;
//import com.hss.core.common.constant.SymbolConstant;
//import com.hss.core.common.util.MinioUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;

/**
 * Minio文件上传配置文件
 * @author: jeecg-boot
 */
//@Slf4j
//@Configuration
//public class MinioConfig {
//    @Value(value = "${minio.url}")
//    private String minioUrl;
//    @Value(value = "${minio.port}")
//    private int port;
//    @Value(value = "${minio.accessKey}")
//    private String minioName;
//    @Value(value = "${minio.secretKey}")
//    private String minioPass;
//    @Value(value = "${minio.bucketName}")
//    private String bucketName;
//
//    @Bean
//    public void initMinio(){
//        if(!minioUrl.startsWith(CommonConstant.STR_HTTP)){
//            minioUrl = "http://" + minioUrl + ":" +port;
//        }
//        if(!minioUrl.endsWith(SymbolConstant.SINGLE_SLASH)){
//            minioUrl = minioUrl.concat(SymbolConstant.SINGLE_SLASH);
//        }
//        MinioUtil.setMinioUrl(minioUrl);
//        MinioUtil.setMinioName(minioName);
//        MinioUtil.setMinioPass(minioPass);
//        MinioUtil.setBucketName(bucketName);
//    }
//
//}
