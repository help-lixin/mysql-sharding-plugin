package help.lixin.datasource.customizer.impl;

import com.alibaba.druid.pool.DruidDataSource;
import help.lixin.datasource.customizer.IDataSourceCustomizer;
import help.lixin.datasource.model.DatabaseResource;

import javax.sql.DataSource;

public class DruidDataSourceCustomizer implements IDataSourceCustomizer {
    private static final String DRUID_DATASOURCE = "com.alibaba.druid.pool.DruidDataSource";

    @Override
    public boolean support(DataSource dataSource) {
        if (null != dataSource) {
            return dataSource.getClass().getName().equals(DRUID_DATASOURCE);
        }
        return false;
    }

    @Override
    public void customize(DatabaseResource databaseResource, DataSource dataSource) {
        DruidDataSource druidDataSource = (DruidDataSource) dataSource;
        druidDataSource.setUrl(databaseResource.getUrl());
        druidDataSource.setDriverClassName(databaseResource.getDriver());
        druidDataSource.setUsername(databaseResource.getUsername());
        druidDataSource.setPassword(databaseResource.getPassword());
    }
}
