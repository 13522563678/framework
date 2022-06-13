package com.kcwl.framework.file;
import com.kcwl.framework.file.biz.service.IFileService;
import com.kcwl.framework.file.biz.service.dfs.aliyun.AliyunOSSServiceImpl;
import com.kcwl.framework.file.biz.service.dfs.hwyun.HwyunOBSServiceImpl;
import com.kcwl.framework.file.biz.service.dfs.local.LocalFileServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author 姚华成
 * @date 2018-06-12
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties(FileProperties.class)
public class FileAutoConfiguration {
    @Bean
    public IFileService fileService(FileProperties fileProperties) {
        try {
            /*
            if (fileProperties.getType() == FileProperties.Type.FASTDFS) {
                FileProperties.Fastdfs fastdfs = fileProperties.getFastdfs();
                ClientGlobal.initByTrackers(fastdfs.getTrackerServers());
                ClientGlobal.setG_connect_timeout(fastdfs.getConnectTimeoutInSeconds() * 1000);
                ClientGlobal.setG_network_timeout(fastdfs.getNetworkTimeoutInSeconds() * 1000);
                ClientGlobal.setG_anti_steal_token(fastdfs.isHttpAntiStealToken());
                ClientGlobal.setG_charset(fastdfs.getCharset());
                ClientGlobal.setG_secret_key(fastdfs.getHttpSecretKey());
                ClientGlobal.setG_tracker_http_port(fastdfs.getHttpTrackerHttpPort());
                StorageClientPool storageClientPool = new StorageClientPool();
                storageClientPool.setConfig(fastdfs.getPoolConfig());
                return new FastDfsServiceImpl(storageClientPool, fastdfs);
            }
            */
            IFileService fileService = null;
            if ( fileProperties.getType() == FileProperties.Type.ALIYUN ) {
                fileService = new AliyunOSSServiceImpl(fileProperties.getAliyun());
            } else if ( fileProperties.getType() == FileProperties.Type.HWYUN  ) {
                fileService = new HwyunOBSServiceImpl(fileProperties.getHwyun());
            } else {
                // 配置不支持的文件存储系统，则使用本地文件存储系统
                fileService =  new LocalFileServiceImpl();
            }
            return fileService;
        } catch (Exception ex) {
            throw new IllegalStateException("加载文件系统服务出错！" + ex.getMessage(), ex);
        }
    }
}
