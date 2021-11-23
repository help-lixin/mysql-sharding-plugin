package help.lixin.datasource.core.impl;

import help.lixin.datasource.context.DBResourceContextInfo;
import help.lixin.datasource.core.IVirtuaDataSourceDelegator;
import help.lixin.datasource.manager.IDataSourceController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;


public class VirtuaDataSourceDelegator implements IVirtuaDataSourceDelegator {

    private Logger logger = LoggerFactory.getLogger(VirtuaDataSourceDelegator.class);

    private IDataSourceController dataSourceController;

    public VirtuaDataSourceDelegator(IDataSourceController dataSourceController) {
        this.dataSourceController = dataSourceController;
    }


    @Override
    public Connection getConnection(DBResourceContextInfo ctx) throws SQLException {
        Optional<DataSource> optionalDataSource = dataSourceController.getDataSource(ctx);
        if (optionalDataSource.isPresent()) {
            DataSource dataSource = optionalDataSource.get();
            return dataSource.getConnection();
        }
        return null;
    }
}
