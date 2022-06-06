package com.kcwl.framework.swagger;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 姚华成
 * @date 2017-12-27
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties extends DocketInfo {

    /**
     * 是否显示文档
     **/
    private boolean enabled = true;

    /**
     * host信息
     **/
    private String host = "";

    /**
     * 分组文档
     **/
    private Map<String, DocketInfo> docket = new LinkedHashMap<>();
}


