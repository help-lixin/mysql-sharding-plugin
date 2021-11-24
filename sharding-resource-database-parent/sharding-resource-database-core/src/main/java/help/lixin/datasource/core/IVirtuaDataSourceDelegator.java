package help.lixin.datasource.core;

import help.lixin.datasource.context.DBResourceContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface IVirtuaDataSourceDelegator {
    // 根据上下文信息,获得连接
    Optional<Connection> getConnection(DBResourceContext ctx) throws SQLException;
}
