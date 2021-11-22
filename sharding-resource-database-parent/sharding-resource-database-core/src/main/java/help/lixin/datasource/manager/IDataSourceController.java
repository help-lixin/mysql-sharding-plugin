package help.lixin.datasource.manager;

import help.lixin.datasource.context.DBResourceContextInfo;
import help.lixin.resource.listener.IEventListener;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 主要负责数据的初始化,以及数据源的更新
 */
public interface IDataSourceController extends IEventListener {
    DataSource getDataSource(DBResourceContextInfo ctx) throws SQLException;
}
