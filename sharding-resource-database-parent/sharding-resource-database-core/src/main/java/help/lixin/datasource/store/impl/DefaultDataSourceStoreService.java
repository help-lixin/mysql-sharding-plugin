package help.lixin.datasource.store.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import help.lixin.datasource.WrapperDataSourceMeta;
import help.lixin.datasource.store.IDataSourceStoreService;

import java.util.ArrayList;
import java.util.List;

/**
 * 存储数据源信息
 */
public class DefaultDataSourceStoreService implements IDataSourceStoreService {

    /**
     * key:   instanceName +  dataSourceName <br/>
     * value: List<DataSource> <br/>
     */
    private final Cache<String, List<WrapperDataSourceMeta>> cache = CacheBuilder
            .newBuilder()
            .build();

    public synchronized boolean register(String key, List<WrapperDataSourceMeta> newDataSources) {
        List<WrapperDataSourceMeta> dataSources = cache.getIfPresent(key);
        // 先清空,后再构建
        if (null != dataSources) {
            cache.invalidate(key);
        }
        cache.put(key, newDataSources);
        return true;
    }

    public synchronized boolean register(String key, WrapperDataSourceMeta newDataSource) {
        List<WrapperDataSourceMeta> dataSources = cache.getIfPresent(key);
        if (null == dataSources) { //key相应的数据源不存在的情况下,构建:List
            dataSources = new ArrayList<>();
            dataSources.add(newDataSource);
            cache.put(key, dataSources);
        } else {
            dataSources.add(newDataSource);
        }
        return true;
    }

    @Override
    public synchronized List<WrapperDataSourceMeta> unRegister(String key) {
        List<WrapperDataSourceMeta> dataSources = cache.getIfPresent(key);
        cache.invalidate(key);
        return dataSources;
    }

    public List<WrapperDataSourceMeta> getDataSources(String key) {
        return cache.getIfPresent(key);
    }
}
