package com.kcwl.framework.datasource.multi;

import com.kcwl.framework.datasource.masterslave.MasterSlaveDataSourceProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 主从数据库配置<p>
 * slave和slaves只能配置一个，如果两个都配置，保留slaves失效。这是为了简化一主一从的配置
 *
 * @author 姚华成
 * @date 2017-12-15
 */

@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class MultiDataSourceProperties {
    private Map<String, MasterSlaveDataSourceProperties> others;
}
