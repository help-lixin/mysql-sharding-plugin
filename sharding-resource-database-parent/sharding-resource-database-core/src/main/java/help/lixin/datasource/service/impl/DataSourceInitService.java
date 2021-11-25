package help.lixin.datasource.service.impl;

import help.lixin.datasource.WrapperDataSourceMeta;
import help.lixin.datasource.service.customizer.IDataSourceCustomizer;
import help.lixin.datasource.keygen.IKeyGenerateService;
import help.lixin.datasource.service.IDataSourceInitService;
import help.lixin.datasource.service.store.IDataSourceStoreService;
import help.lixin.datasource.meta.IDataSourceMetaService;
import help.lixin.datasource.model.DatabaseResource;
import help.lixin.datasource.util.DataSourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DataSourceInitService implements IDataSourceInitService {

    private Logger logger = LoggerFactory.getLogger(DataSourceInitService.class);

    // 元数据存储中心
    private IDataSourceMetaService dataSourceMetaService;
    // id生成策略
    private IKeyGenerateService contextKeyGenerateService;
    // 数据源的存储中心
    private IDataSourceStoreService dataSourceStore;
    // 自定义创建DataSource的详细过程
    private List<IDataSourceCustomizer> dataSourceCustomizers;

    public DataSourceInitService(IDataSourceMetaService dataSourceMetaService,
                                 IKeyGenerateService contextKeyGenerateService,
                                 IDataSourceStoreService dataSourceStore,
                                 List<IDataSourceCustomizer> dataSourceCustomizers) {
        this.dataSourceMetaService = dataSourceMetaService;
        this.contextKeyGenerateService = contextKeyGenerateService;
        this.dataSourceStore = dataSourceStore;
        this.dataSourceCustomizers = dataSourceCustomizers;
    }

    @Override
    public void initDataSources() {
        List<DatabaseResource> metas = dataSourceMetaService.getMeta();
        if (null != metas) {
            for (DatabaseResource databaseResource : metas) {
                // 1. 初始化数据源.
                // 2. 通过IDataSourceCustomizer进行自定义.
                // 3. 保存到存储介质中心.
                initDataSource(databaseResource)
                        .ifPresent(datasource -> {
                            this.customizer(databaseResource, datasource);

                            // 2. 生成key
                            String key = contextKeyGenerateService.generate(databaseResource);
                            if (logger.isInfoEnabled()) {
                                logger.info("初始化数据源名称:[{}]成功.", key);
                            }

                            // 3. 通过DataSourceWrapper包裹一次DataSource
                            WrapperDataSourceMeta wrapperDataSourceMeta = new WrapperDataSourceMeta(datasource, databaseResource);
                            dataSourceStore.register(key, wrapperDataSourceMeta);
                        });
            }
        }
    }


    protected Optional<DataSource> initDataSource(DatabaseResource databaseResource) {
        String dataSourceClassName = databaseResource.getType();
        Map<String, Object> properties = databaseResource.getProperties();
        try {
            DataSource dataSource = DataSourceUtil.getDataSource(dataSourceClassName, properties);
            return Optional.of(dataSource);
        } catch (ReflectiveOperationException e) {
            logger.error("初始化DataSource:[{}]失败,失败详细内容:[{}]", databaseResource.getDriver(), e);
        }
        return Optional.empty();
    }

    protected void customizer(DatabaseResource databaseResource, DataSource dataSource) {
        if (null != dataSourceCustomizers) {
            for (IDataSourceCustomizer dataSourceCustomizer : dataSourceCustomizers) {
                // 1. 先验证是否支持
                boolean support = dataSourceCustomizer.support(dataSource);
                if (support) {
                    // 2. 才进行自定义.
                    dataSourceCustomizer.customize(databaseResource, dataSource);
                }
            }
        }
    }
}
