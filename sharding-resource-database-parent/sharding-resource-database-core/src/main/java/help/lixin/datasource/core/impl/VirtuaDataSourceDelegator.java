package help.lixin.datasource.core.impl;

import help.lixin.datasource.context.DBResourceContext;
import help.lixin.datasource.core.IVirtuaDataSourceDelegator;
import help.lixin.datasource.manager.IBorrowDataSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;


public class VirtuaDataSourceDelegator implements IVirtuaDataSourceDelegator {

    private Logger logger = LoggerFactory.getLogger(VirtuaDataSourceDelegator.class);

    private IBorrowDataSourceService borrowDataSourceService;

    public VirtuaDataSourceDelegator(IBorrowDataSourceService borrowDataSourceService) {
        this.borrowDataSourceService = borrowDataSourceService;
    }


    @Override
    public Optional<Connection> getConnection(DBResourceContext ctx) throws SQLException {
        Optional<DataSource> optionalDataSource = borrowDataSourceService.getDataSource(ctx);
        if (optionalDataSource.isPresent()) {
            DataSource dataSource = optionalDataSource.get();
            return Optional.of(dataSource.getConnection());
        }
        return Optional.empty();
    }
}
