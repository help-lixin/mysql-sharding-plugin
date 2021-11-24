package help.lixin.datasource.store;

import help.lixin.datasource.WrapperDataSourceMeta;

import java.util.List;

public interface IDataSourceStoreService {

    /**
     * 为什么是多个数据源.在关系型数据库,架构下,一般为:一主多从,要允许只读的请求,可以指定策略用哪一个SLAVE.
     *
     * @param key
     * @param dataSources
     * @return
     */
    boolean register(String key, List<WrapperDataSourceMeta> dataSources);

    boolean register(String key, WrapperDataSourceMeta dataSource);

    List<WrapperDataSourceMeta> unRegister(String key);

    List<WrapperDataSourceMeta> getDataSources(String key);
}
