package com.kcwl.framework.datasource.masterslave;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * 主从数据库配置<p>
 * slave和slaves只能配置一个，如果两个都配置，slaves失效。这是为了简化一主一从的配置
 *
 * @author 姚华成
 * @date 2017-12-15
 */
@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class MasterSlaveDataSourceProperties {
    private MasterSlave masterslave = new MasterSlave();
    private Properties master;
    private Properties slave;
    private Properties[] slaves;

    @Data
    private static class MasterSlave {
        private boolean enabled = true;
    }
}
