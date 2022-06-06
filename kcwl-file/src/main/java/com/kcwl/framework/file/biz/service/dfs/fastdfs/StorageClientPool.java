package com.kcwl.framework.file.biz.service.dfs.fastdfs;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.csource.fastdfs.StorageClient1;

/**
 * StorageClient 对象池
 * <p>
 *
 * @author jiangzhou.bo@hand-china.com
 * @version 1.0
 * @date 2017-10-14 15:23
 */
public class StorageClientPool extends GenericObjectPool<StorageClient1> {
    public StorageClientPool() {
        super(new StorageClientFactory());
    }
}
