package com.kcwl.framework.file;
import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.csource.fastdfs.StorageClient1;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 姚华成
 * @date 2018-06-12
 */
@Data
@ConfigurationProperties("kcwl.common.file")
public class FileProperties {
    private Type type;
    private Fastdfs fastdfs;
    private Scloud scloud;
    private Aliyun aliyun;
    private Hwyun hwyun;


    public enum Type {
        /**
         *
         */
        HWYUN,
        ALIYUN,
        FASTDFS,
        SCLOUD,
        QINIU,
        MINIO
    }

    @Data
    public static class Fastdfs {
        private String trackerServers;
        private String httpUrlRoot;

        private int connectTimeoutInSeconds = 5;
        private int networkTimeoutInSeconds = 30;
        private String charset = "UTF-8";
        /**
         * 是否使用防盗链功能
         */
        private boolean httpAntiStealToken = false;
        /**
         * Http密钥
         */
        private String httpSecretKey = "SecretKey1234567890";
        private int httpTrackerHttpPort = 80;
        private int maxFileSize = 100 * 1024 * 1024;
        //使用fastdfs的时候把此语句加上
        //private GenericObjectPoolConfig<StorageClient1> poolConfig = new GenericObjectPoolConfig<>();
    }

    @Data
    public static class Scloud {
        private int appId;
        private String lightServerIp;
        private String accessKeySecret;
        private String accessKeyId;
        private String location = "111.132,123.123";
        private String pub = "1";
    }

    @Data
    public static class Aliyun {
        private   String endpoint;
        private   String imgEndpoint;
        private  String accessKey;
        private  String secretKey;
        private int maxFileSize = 500 * 1024 * 1024;
        private  String bucketName;
        private long expiredTime = 60*1000;
    }

    @Data
    public static class Hwyun{
        private   String endpoint;
        private   String imgEndpoint;
        private  String accessKey;
        private  String secretKey;
        private int maxFileSize = 500 * 1024 * 1024;
        private  String bucketName;
        private long expiredTime = 60*1000;
    }
}
