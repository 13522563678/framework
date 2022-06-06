package com.kcwl.framework.swagger;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

import java.util.*;

/**
 * @author 姚华成
 * @date 2017-12-27
 */

@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerConfiguration implements BeanFactoryAware {

    private ConfigurableBeanFactory configurableBeanFactory;

    @Bean
    public UiConfiguration uiConfig() {
        // jsonEditor选项能够让json输入更方便，而默认是关闭的，因此设置这个UiConfiguration
        return UiConfigurationBuilder.builder()
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public List<Docket> createRestApi(SwaggerProperties swaggerProperties) {

        List<Docket> docketList = new LinkedList<>();
        // 没有分组
        if (swaggerProperties.getDocket().size() == 0) {
            String docketName = "defaultDocket";
            Docket docket = createDocket(docketName, swaggerProperties, swaggerProperties,
                    swaggerProperties.getHost(), swaggerProperties.isEnabled());
            docketList.add(docket);
            return docketList;
        }

        // 分组创建
        for (Map.Entry<String, DocketInfo> entry : swaggerProperties.getDocket().entrySet()) {
            String groupName = entry.getKey();
            DocketInfo docketInfo = entry.getValue();
            Docket docket = createDocket(groupName, docketInfo, swaggerProperties,
                    swaggerProperties.getHost(), swaggerProperties.isEnabled());
            docketList.add(docket);
        }
        return docketList;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
    }

    private Docket createDocket(String docketName, DocketInfo docketInfo, DocketInfo defaultDocketInfo,
                                String host, boolean swaggerEnable) {
        Set<String> protocolSet = Sets.newHashSet(docketInfo.getProtocal());
        Set<String> produceSet = Sets.newHashSet(docketInfo.getProduce());

        ApiInfo apiInfo = getApiInfo(docketInfo, defaultDocketInfo);

        // base-path处理
        // 当没有配置任何path的时候，解析/**
        if (docketInfo.getBasePath().isEmpty()) {
            docketInfo.getBasePath().add("/**");
        }
        List<Predicate<String>> basePath = Lists.newArrayList();
        for (String path : docketInfo.getBasePath()) {
            if (docketInfo.getPathType() == DocketInfo.PathType.ANT) {
                basePath.add(PathSelectors.ant(path));
            } else {
                basePath.add(PathSelectors.regex(path));
            }
        }
        // exclude-path处理
        List<Predicate<String>> excludePath = Lists.newArrayList();
        for (String path : docketInfo.getExcludePath()) {
            if (docketInfo.getPathType() == DocketInfo.PathType.ANT) {
                excludePath.add(PathSelectors.ant(path));
            } else {
                excludePath.add(PathSelectors.regex(path));
            }
        }
        Predicate<String> pathPredicate = Predicates.and(
                Predicates.not(Predicates.or(excludePath)),
                Predicates.or(basePath));

        Predicate<RequestHandler> requestHandlerPredicate;
        if (StringUtils.isEmpty(docketInfo.getBasePackage())) {
            requestHandlerPredicate = RequestHandlerSelectors.withClassAnnotation(Api.class);
        } else {
            requestHandlerPredicate = RequestHandlerSelectors.basePackage(docketInfo.getBasePackage());
        }

        List<Parameter> globalParameters = new ArrayList<>(docketInfo.getParameter().size());
        for (DocketInfo.Parameter parameter : docketInfo.getParameter()) {
            globalParameters.add(new ParameterBuilder()
                    .name(parameter.getName())
                    .description(parameter.getDescription())
                    .defaultValue(parameter.getDefaultValue())
                    .required(parameter.getRequired())
                    .allowMultiple(parameter.getAllowMultiple())
                    .modelRef(parameter.getModelRef())
                    .allowableValues(parameter.getAllowableValues())
                    .parameterType(parameter.getParamType())
                    .parameterAccess(parameter.getParamAccess())
                    .hidden(parameter.getHidden())
                    .build());
        }

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerEnable)
                .groupName(docketName)
                .apiInfo(apiInfo)
                .produces(produceSet)
                .protocols(protocolSet)
                .host(host)
                .globalOperationParameters(globalParameters)
                .useDefaultResponseMessages(false)
                .select()
                .apis(requestHandlerPredicate)
                .paths(pathPredicate)
                .build();
        configurableBeanFactory.registerSingleton(docketName, docket);
        return docket;
    }

    private ApiInfo getApiInfo(DocketInfo docketInfo, DocketInfo defaultDocketInfo) {
        DocketInfo.Contact contact = docketInfo.getContact();
        DocketInfo.Contact defaultContact = defaultDocketInfo.getContact();
        return new ApiInfoBuilder()
                .title(StringUtils.isEmpty(docketInfo.getTitle()) ?
                        defaultDocketInfo.getTitle() : docketInfo.getTitle())
                .description(StringUtils.isEmpty(docketInfo.getDescription()) ?
                        defaultDocketInfo.getDescription() : docketInfo.getDescription())
                .version(StringUtils.isEmpty(docketInfo.getVersion()) ?
                        defaultDocketInfo.getVersion() : docketInfo.getVersion())
                .license(StringUtils.isEmpty(docketInfo.getLicense()) ?
                        defaultDocketInfo.getLicense() : docketInfo.getLicense())
                .licenseUrl(StringUtils.isEmpty(docketInfo.getLicenseUrl()) ?
                        defaultDocketInfo.getLicenseUrl() : docketInfo.getLicenseUrl())
                .termsOfServiceUrl(StringUtils.isEmpty(docketInfo.getTermsOfServiceUrl()) ?
                        defaultDocketInfo.getTermsOfServiceUrl() : docketInfo.getTermsOfServiceUrl())
                .contact(new Contact(
                        StringUtils.isEmpty(contact.getName()) ? defaultContact.getName() : contact.getName(),
                        StringUtils.isEmpty(contact.getUrl()) ? defaultContact.getUrl() : contact.getUrl(),
                        StringUtils.isEmpty(contact.getEmail()) ? defaultContact.getEmail() : contact.getEmail()))
                .build();
    }
}
