package help.lixin.datasource.core.impl;

import help.lixin.datasource.context.DBResourceContextInfo;
import help.lixin.datasource.core.IVirtuaDataSourceDelegator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;


public class VirtuaDataSourceDelegator implements IVirtuaDataSourceDelegator {

    private Logger logger = LoggerFactory.getLogger(VirtuaDataSourceDelegator.class);

    @Override
    public Connection getConnection(DBResourceContextInfo ctx) throws SQLException {

        return null;
    }
}
