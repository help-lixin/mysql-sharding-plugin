package help.lixin.datasource.service.init.customizer;

import help.lixin.datasource.model.DatabaseResource;

import javax.sql.DataSource;

/**
 * 对DataSource进行自定义
 */
public interface IDataSourceCustomizer {

    /**
     * @param databaseResource 数据载体
     * @param dataSource       此时的是DataSource,仅仅是通过反射new出来的:DataSource
     */
    void customize(DatabaseResource databaseResource, DataSource dataSource);
    
    boolean support(DataSource dataSource);
}
