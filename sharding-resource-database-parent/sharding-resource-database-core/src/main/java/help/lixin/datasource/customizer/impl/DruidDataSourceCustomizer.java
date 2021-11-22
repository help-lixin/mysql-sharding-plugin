package help.lixin.datasource.customizer.impl;

import help.lixin.datasource.customizer.IDataSourceCustomizer;
import help.lixin.datasource.model.DatabaseResource;

import javax.sql.DataSource;

public class DruidDataSourceCustomizer implements IDataSourceCustomizer {
    private static final String DRUID_DATASOURCE = "com.alibaba.druid.pool.DruidDataSource";

    @Override
    public boolean support(DataSource dataSource) {
        if (null != dataSource) {
            return dataSource.getClass().equals(DRUID_DATASOURCE);
        }
        return false;
    }

    @Override
    public void customize(DatabaseResource databaseResource, DataSource dataSource) {
        // TODO lixin
    }
}
