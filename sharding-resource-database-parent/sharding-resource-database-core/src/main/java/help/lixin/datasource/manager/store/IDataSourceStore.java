package help.lixin.datasource.manager.store;

import javax.sql.DataSource;
import java.util.List;

public interface IDataSourceStore {

    /**
     * 为什么是多个数据源.在关系型数据库,架构下,一般为:一主多从,要允许只读的请求,可以指定策略用哪一个SLAVE.
     *
     * @param key
     * @param dataSources
     * @return
     */
    boolean register(String key, List<DataSource> dataSources);

    boolean register(String key, DataSource dataSource);

    List<DataSource> unRegister(String key);

    List<DataSource> getDataSources(String key);
}
