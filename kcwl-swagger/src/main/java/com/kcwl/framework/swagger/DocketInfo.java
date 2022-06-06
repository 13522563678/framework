package com.kcwl.framework.swagger;

import lombok.Data;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.service.AllowableValues;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 姚华成
 * @date 2017-12-27
 */
@Data
public class DocketInfo {
    /**
     * 标题
     **/
    private String title = "";
    /**
     * 描述
     **/
    private String description = "";
    /**
     * 版本
     **/
    private String version = "";
    /**
     * 许可证
     **/
    private String license = "";
    /**
     * 许可证URL
     **/
    private String licenseUrl = "";
    /**
     * 服务条款URL
     **/
    private String termsOfServiceUrl = "";
    /**
     * swagger会解析的包路径
     **/
    private String basePackage = "";
    private PathType pathType = PathType.ANT;
    /**
     * swagger会解析的url规则
     **/
    private List<String> basePath = new ArrayList<>();
    /**
     * 在basePath基础上需要排除的url规则
     **/
    private List<String> excludePath = new ArrayList<>();
    /**
     * 支持的协议
     */
    private List<String> protocal = new ArrayList<>();
    private List<String> produce = new ArrayList<>();
    /**
     * 联系人信息
     */
    private Contact contact = new Contact();

    /**
     * 全局参数
     */
    private List<Parameter> parameter = new ArrayList<>();

    public DocketInfo() {
        protocal.add("http");
        produce.add("application/json");
    }

    public enum PathType {
        /***
         * ant：表达式
         */
        ANT,
        /**
         * regex：正则表达式
         */
        REGEX
    }

    @Data
    public static class Contact {
        /**
         * 联系人
         **/
        private String name = "";
        /**
         * 联系人url
         **/
        private String url = "";
        /**
         * 联系人email
         **/
        private String email = "";
    }

    @Data
    public static class Parameter {
        private String name;
        private String description;
        private String defaultValue;
        private Boolean required = true;
        private Boolean allowMultiple = false;
        private ModelReference modelRef = new ModelRef("string");
        private AllowableValues allowableValues;
        private String paramType;
        private String paramAccess;
        private Boolean hidden = false;
    }

}
