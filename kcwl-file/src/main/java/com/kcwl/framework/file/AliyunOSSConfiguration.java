package com.kcwl.framework.file;

import com.kcwl.framework.file.biz.service.dfs.aliyun.OSSManageService;
import com.kcwl.framework.file.biz.service.dfs.aliyun.OSSProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OSSProperties.class)
public class AliyunOSSConfiguration {

    @Autowired
    private OSSProperties oSSProperties;

    @Bean
    public OSSManageService oSSManageService(){
        //(String endpoint, String imgEndpoint, String accessKeyId, String accessKeySecret, String bucketName, String ossAccessUrl, String key) {
       /* return new OSSManageServiceImpl(oSSProperties.getOssEndpoint(),oSSProperties.getImgEndpoint(),oSSProperties.getOssAccessKeyid(),oSSProperties.getOssAccessKeysecret(),
                oSSProperties.getOssBucketName(),oSSProperties.getOssAccessUrl(),oSSProperties.getOssKey());*/
        //return new OSSManageServiceImpl("1","1","1","1","1","1","1");
        return new OSSManageService(oSSProperties.getEndpoint(),oSSProperties.getImgEndpoint(),
                oSSProperties.getAccessKeyid(),oSSProperties.getAccessKeysecret(),
                oSSProperties.getBucketName(),oSSProperties.getAccessUrl(),oSSProperties.getKey(),oSSProperties.getExpiresTime());
    }

}