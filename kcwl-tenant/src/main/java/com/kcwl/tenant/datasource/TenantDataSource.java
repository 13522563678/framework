package com.kcwl.tenant.datasource;

import com.kcwl.tenant.TenantDataHolder;
import com.kcwl.tenant.constants.TenantConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ckwl
 */
public class TenantDataSource extends AbstractRoutingDataSource {

    private static final Logger logger = LoggerFactory.getLogger(TenantDataSource.class);
    private static final String UNDERLINE = "_";

    private static TenantDataSource instance;
    private static byte[] lock=new byte[0];
    private Random random = new Random();
    private Map<String, List<Integer>> tenantDataSourceId = new ConcurrentHashMap<String, List<Integer>>();
    private Map<Object, DataSource> tenantDataSources = new ConcurrentHashMap<>();
    private Map<String, String>  supportTenants = new ConcurrentHashMap<String, String>();

    private String primaryDsKey = "";

    /**
     * @param tenantId
     * @param dataSource
     * @param dataSourceType
     * @param dataSourceId
     */
    public synchronized String addDataSource(String tenantId, DataSource dataSource, DataSourceType dataSourceType, int dataSourceId, boolean isPrimary) {
        String dsKey = genDataSourceKey(tenantId, dataSourceType, dataSourceId);
        if (!tenantDataSources.containsKey(dsKey)) {
            tenantDataSources.put(dsKey, dataSource);
            saveDataSourceId(tenantId, dataSourceType, dataSourceId);
            if ( isPrimary ) {
                primaryDsKey = dsKey;
            }
            logger.info("tenant-datasource - load a datasource named [{}] success", dsKey);
        } else {
            logger.warn("tenant-datasource - load a datasource named [{}] failed, because it already exist", dsKey);
        }
        return dsKey;
    }

    @Override
    protected DataSource determineDataSource() {
        Assert.notNull(this.tenantDataSources, "DataSource router not initialized");
        Object lookupKey = this.determineCurrentLookupKey();
        DataSource dataSource = tenantDataSources.get(lookupKey);
        if ( (dataSource == null) && !StringUtils.isEmpty(primaryDsKey) ) {
            dataSource = tenantDataSources.get(primaryDsKey);
        }
        if (dataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
        } else {
            return dataSource;
        }
    }

    protected Object determineCurrentLookupKey() {
        //优先从注解中选择
        String tenantId = MultiDataSourceContextHolder.getDataSourceName();       ;
        if ( StringUtils.isEmpty(tenantId) ) {
            String currentTenantId = TenantDataHolder.get();
            if (!StringUtils.isEmpty(currentTenantId)) {
                tenantId = supportTenants.get(currentTenantId);
            }
        }
        if ( tenantId == null ) {
            tenantId = TenantConstant.UNIOIN_TENANT_ID;
        }
        return selectDataSourceKey(tenantId);
    }

    private String selectDataSourceKey(String tenantId) {
        DataSourceType dataSourceType = getCurrentDataSourceType();
        List<Integer> dataSourceIdList = tenantDataSourceId.get(genDataSourceIdKey(tenantId, dataSourceType));
        int index = 0;
        if ( (dataSourceType == DataSourceType.SLAVE) && (dataSourceIdList != null) ) {
            index = dataSourceIdList.get(random.nextInt(dataSourceIdList.size()));
        } else {
            dataSourceType = DataSourceType.MASTER;
        }
        return genDataSourceKey(tenantId, dataSourceType, index);
    }

    public void addSupportTenant(String tenantId, Set<String> supportTenantSet) {
        supportTenants.put(tenantId, tenantId);
        if ( supportTenantSet != null ) {
            for ( String tenant : supportTenantSet ) {
                supportTenants.put(tenant, tenantId);
            }
        }
    }

    public void addSupportTenant(String tenantId, String supportTenant) {
        supportTenants.put(supportTenant, tenantId);
    }

    private void saveDataSourceId(String tenantId, DataSourceType dataSourceType, int dataSourceId) {
        String idKey = genDataSourceIdKey(tenantId, dataSourceType);
        List<Integer> dsIdList = tenantDataSourceId.get(idKey);
        if ( dsIdList == null ) {
            dsIdList = new  ArrayList<Integer>();
            tenantDataSourceId.put(idKey, dsIdList);
        }
        dsIdList.add(dataSourceId);
    }

    private DataSourceType getCurrentDataSourceType() {
        DataSourceType dataSourceType = MasterSlaveDataSourceContextHolder.getDataSourceType();
        return (dataSourceType != null ) ? dataSourceType : DataSourceType.MASTER;
    }

    private String genDataSourceKey(String tenantId, DataSourceType dataSourceType, int index) {
        StringBuilder sb = new StringBuilder();
        sb.append(tenantId).append(UNDERLINE).append(dataSourceType).append(UNDERLINE).append(index);
        return sb.toString();
    }

    private String genDataSourceIdKey(String tenantId, DataSourceType dataSourceType) {
        StringBuilder sb = new StringBuilder();
        sb.append(tenantId).append(UNDERLINE).append(dataSourceType);
        return sb.toString();
    }

    /**
     * 单例方法
     * @return
     */
    public static synchronized TenantDataSource getInstance(){
        if(instance==null){
            synchronized (lock){
                if(instance==null){
                    instance=new TenantDataSource();
                }
            }
        }
        return instance;
    }
}
