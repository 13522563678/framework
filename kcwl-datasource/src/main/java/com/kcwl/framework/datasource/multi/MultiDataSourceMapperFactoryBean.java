package com.kcwl.framework.datasource.multi;

import com.kcwl.framework.datasource.masterslave.MasterSlaveDataSource;
import com.kcwl.framework.datasource.masterslave.MasterSlaveDataSourceProperties;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 姚华成
 * @date 2017-12-19
 */
public class MultiDataSourceMapperFactoryBean<T> extends MapperFactoryBean<T> implements ResourceLoaderAware {
    private static Map<String, SqlSessionTemplate> sqlSessionMap = new HashMap<>();
    @Resource
    private MultiDataSourceProperties properties;
    @Resource
    private MybatisProperties mybatisProperties;
    private ResourceLoader resourceLoader;

    public MultiDataSourceMapperFactoryBean() {
        super();
    }

    public MultiDataSourceMapperFactoryBean(Class<T> mapperInterface) {
        super(mapperInterface);
    }

    @Override
    public SqlSession getSqlSession() {
        MultiDataSource ann = getMapperInterface().getAnnotation(MultiDataSource.class);
        if (ann == null || sqlSessionMap == null) {
            return super.getSqlSession();
        }
        String dataSourceName = ann.value();
        if (StringUtils.isEmpty(dataSourceName)) {
            dataSourceName = ann.datasource();
        }
        SqlSessionTemplate sqlSession = sqlSessionMap.get(dataSourceName);
        if (sqlSession == null) {
            return super.getSqlSession();
        }
        return sqlSession;
    }

    @PostConstruct
    public void init() throws Exception {
        if (!sqlSessionMap.isEmpty()) {
            return;
        }
        Map<String, MasterSlaveDataSourceProperties> otherPropsMap = properties.getOthers();
        if (otherPropsMap == null || otherPropsMap.isEmpty()) {
            // 没有多数据源，不需要处理
            return;
        }
        for (Map.Entry<String, MasterSlaveDataSourceProperties> entry : otherPropsMap.entrySet()) {
            if (entry.getValue() == null || entry.getValue().getMaster() == null) {
                continue;
            }
            MasterSlaveDataSourceProperties masterSlave = entry.getValue();
            MasterSlaveDataSource otherDs = new MasterSlaveDataSource(masterSlave);
            otherDs.afterPropertiesSet();
            sqlSessionMap.put(entry.getKey(), new SqlSessionTemplate(createSqlSessionFactory(otherDs)));
        }
    }

    private SqlSessionFactory createSqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setVfs(SpringBootVFS.class);
        if (mybatisProperties != null) {
            if (StringUtils.hasText(this.mybatisProperties.getConfigLocation())) {
                factory.setConfigLocation(this.resourceLoader.getResource(this.mybatisProperties.getConfigLocation()));
            }
            org.apache.ibatis.session.Configuration configuration = this.mybatisProperties.getConfiguration();
            if (configuration == null && !StringUtils.hasText(this.mybatisProperties.getConfigLocation())) {
                configuration = new org.apache.ibatis.session.Configuration();
            }
            factory.setConfiguration(configuration);
            if (this.mybatisProperties.getConfigurationProperties() != null) {
                factory.setConfigurationProperties(this.mybatisProperties.getConfigurationProperties());
            }
            if (StringUtils.hasLength(this.mybatisProperties.getTypeAliasesPackage())) {
                factory.setTypeAliasesPackage(this.mybatisProperties.getTypeAliasesPackage());
            }
            if (StringUtils.hasLength(this.mybatisProperties.getTypeHandlersPackage())) {
                factory.setTypeHandlersPackage(this.mybatisProperties.getTypeHandlersPackage());
            }
            if (!ObjectUtils.isEmpty(this.mybatisProperties.resolveMapperLocations())) {
                factory.setMapperLocations(this.mybatisProperties.resolveMapperLocations());
            }
        }

        return factory.getObject();
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
