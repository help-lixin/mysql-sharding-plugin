package help.lixin.datasource.manager;

import help.lixin.datasource.context.DBResourceContext;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

/**
 * 主要负责根据上下文信息,获得一个DataSource
 */
public interface IBorrowDataSourceService {
    /**
     * @param ctx
     * @return
     * @throws SQLException
     */
    Optional<DataSource> getDataSource(DBResourceContext ctx) throws SQLException;
}
