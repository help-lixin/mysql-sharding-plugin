package help.lixin.datasource.service.customizer.impl;

import com.zaxxer.hikari.HikariDataSource;
import help.lixin.datasource.service.customizer.IDataSourceCustomizer;
import help.lixin.datasource.model.DatabaseResource;

import javax.sql.DataSource;
import java.util.Properties;

public class HikariCPDataSourceCustomizer implements IDataSourceCustomizer {

    private static final String HIKARI_CP_DATASOURCE = "com.zaxxer.hikari.HikariDataSource";

    @Override
    public boolean support(DataSource dataSource) {
        if (null != dataSource) {
            return dataSource.getClass().getName().equals(HIKARI_CP_DATASOURCE);
        }
        return false;
    }

    @Override
    public void customize(DatabaseResource databaseResource, DataSource dataSource) {
        HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
        hikariDataSource.setJdbcUrl(databaseResource.getUrl());
        hikariDataSource.setDriverClassName(databaseResource.getDriver());
        hikariDataSource.setUsername(databaseResource.getUsername());
        hikariDataSource.setPassword(databaseResource.getPassword());

        Properties properties = new Properties();
        properties.putAll(databaseResource.getProperties());
        hikariDataSource.setDataSourceProperties(properties);
    }
}
