package help.lixin.datasource.service.loadbalancer;

import help.lixin.datasource.context.DBResourceContext;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

/**
 * 主要负责根据上下文信息,获得一个DataSource
 */
public interface ILoadBalancerDataSourceService {
    /**
     * @param ctx
     * @return
     * @throws SQLException
     */
    Optional<DataSource> chooseDataSource(DBResourceContext ctx) throws SQLException;
}
