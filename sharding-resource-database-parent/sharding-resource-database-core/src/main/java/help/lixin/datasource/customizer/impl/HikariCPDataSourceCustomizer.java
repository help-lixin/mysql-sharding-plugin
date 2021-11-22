package help.lixin.datasource.customizer.impl;

import help.lixin.datasource.customizer.IDataSourceCustomizer;
import help.lixin.datasource.model.DatabaseResource;

import javax.sql.DataSource;

public class HikariCPDataSourceCustomizer implements IDataSourceCustomizer {

    private static final String HIKARI_CP_DATASOURCE = "com.zaxxer.hikari.HikariDataSource";

    @Override
    public boolean support(DataSource dataSource) {
        if (null != dataSource) {
            return dataSource.getClass().equals(HIKARI_CP_DATASOURCE);
        }
        return false;
    }

    @Override
    public void customize(DatabaseResource databaseResource, DataSource dataSource) {
        // TODO lixin
    }
}
