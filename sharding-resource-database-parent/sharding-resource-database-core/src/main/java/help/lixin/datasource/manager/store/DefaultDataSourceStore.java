package help.lixin.datasource.manager.store;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * 存储数据源信息
 */
public class DefaultDataSourceStore implements IDataSourceStore {

    /**
     * key:   instanceName +  dataSourceName <br/>
     * value: List<DataSource> <br/>
     */
    private final Cache<String, List<DataSource>> cache = CacheBuilder
            .newBuilder()
            .build();

    public synchronized boolean register(String key, List<DataSource> newDataSources) {
        List<DataSource> dataSources = cache.getIfPresent(key);
        // 先清空,后再构建
        if (null != dataSources) {
            cache.invalidate(key);
        }
        cache.put(key, newDataSources);
        return true;
    }

    public synchronized boolean register(String key, DataSource newDataSource) {
        List<DataSource> dataSources = cache.getIfPresent(key);
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
    public List<DataSource> unRegister(String key) {
        List<DataSource> dataSources = cache.getIfPresent(key);
        cache.invalidate(key);
        return dataSources;
    }

    public List<DataSource> getDataSources(String key) {
        return cache.getIfPresent(key);
    }
}
