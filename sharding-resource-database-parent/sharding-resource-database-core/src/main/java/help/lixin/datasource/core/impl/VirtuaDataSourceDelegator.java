package help.lixin.datasource.core.impl;

import help.lixin.datasource.context.DBResourceContext;
import help.lixin.datasource.core.IVirtuaDataSourceDelegator;
import help.lixin.datasource.service.loadbalancer.ILoadBalancerDataSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;


public class VirtuaDataSourceDelegator implements IVirtuaDataSourceDelegator {

    private Logger logger = LoggerFactory.getLogger(VirtuaDataSourceDelegator.class);

    private ILoadBalancerDataSourceService loadBalancerDataSourceService;

    public VirtuaDataSourceDelegator(ILoadBalancerDataSourceService loadBalancerDataSourceService) {
        this.loadBalancerDataSourceService = loadBalancerDataSourceService;
    }


    @Override
    public Optional<Connection> getConnection(DBResourceContext ctx) throws SQLException {
        Optional<DataSource> optionalDataSource = loadBalancerDataSourceService.chooseDataSource(ctx);
        if (optionalDataSource.isPresent()) {
            DataSource dataSource = optionalDataSource.get();
            return Optional.of(dataSource.getConnection());
        }
        return Optional.empty();
    }
}
