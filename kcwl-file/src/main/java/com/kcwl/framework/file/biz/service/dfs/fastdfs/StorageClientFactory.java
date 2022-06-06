package com.kcwl.framework.file.biz.service.dfs.fastdfs;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;

/**
 * TrackerServer 工厂类，创建对象池需要 BasePooledObjectFactory 对象或子类.
 *
 * @author jiangzhou.bo@hand-china.com
 * @version 1.0
 * @date 2017-10-14 14:45
 */
public class StorageClientFactory extends BasePooledObjectFactory<StorageClient1> {
    @Override
    public StorageClient1 create() throws Exception {
        return new StorageClient1(new TrackerClient().getConnection(),null);
    }

    @Override
    public PooledObject<StorageClient1> wrap(StorageClient1 trackerServer) {
        return new DefaultPooledObject<>(trackerServer);
    }
}
