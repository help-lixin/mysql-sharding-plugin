package help.lixin.datasource.service.init.impl;

import help.lixin.datasource.WrapperDataSourceMeta;
import help.lixin.datasource.service.init.customizer.IDataSourceCustomizer;
import help.lixin.datasource.keygen.IKeyGenerateService;
import help.lixin.datasource.service.init.IDataSourceInitService;
import help.lixin.datasource.service.store.IDataSourceStoreService;
import help.lixin.datasource.meta.IDataSourceMetaService;
import help.lixin.datasource.model.DatabaseResource;
import help.lixin.datasource.util.DataSourceUtil;
import help.lixin.resource.ResourceMode;
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
    public synchronized void initDataSources() {
        List<DatabaseResource> metas = dataSourceMetaService.getMeta();
        if (null != metas) {
            initItems(null, metas, ResourceMode.RW);
        }
    }

    protected void initItems(String rsourceKey, List<DatabaseResource> metas, ResourceMode resourceMode) {
        for (DatabaseResource databaseResource : metas) {
            initItem(rsourceKey, databaseResource, resourceMode);
        }
    }

    protected void initItem(String rsourceKey, DatabaseResource databaseResource, ResourceMode resourceMode) {
        databaseResource.setMode(resourceMode);
        Optional<DataSource> ds = initDataSource(databaseResource);
        if (ds.isPresent()) {
            DataSource dataSource = ds.get();
            // 1. 生成key
            String key = rsourceKey;
            if (null == key) {
                key = contextKeyGenerateService.generate(databaseResource);
            }

            // 2. 仅在写模式下检查.
            //    检查key下面的DataSource,不允许有多个master
            //    注意:不允许有多个master,但是允许有多个slave
            if (resourceMode.name().equalsIgnoreCase(ResourceMode.RW.name())) {
                checkDataSources(key);
            }

            // 3. 对Datasource进行自定义配置
            this.customizer(databaseResource, dataSource);

            if (logger.isInfoEnabled()) {
                if (resourceMode.name().equalsIgnoreCase(ResourceMode.RW.name())) {
                    logger.info("初始化MASTER数据源:[{}-{}]成功.", key, ResourceMode.RW.name());
                } else {
                    logger.info("初始化SLAVE数据源:[{}-{}]成功.", key, ResourceMode.R.name());
                }
            }

            // 4. 通过DataSourceWrapper包裹一次DataSource(DataSourceWrapper承载着元数据)
            WrapperDataSourceMeta wrapperDataSourceMeta = new WrapperDataSourceMeta(dataSource, databaseResource);
            dataSourceStore.register(key, wrapperDataSourceMeta);

            // 5. 初始化所有的slave
            List<DatabaseResource> slaves = databaseResource.getSlaves();
            if (!slaves.isEmpty()) {
                initItems(key, slaves, ResourceMode.R);
            }
        }
    }

    protected void checkDataSources(String key) {
        List<WrapperDataSourceMeta> dataSources = dataSourceStore.getDataSources(key);
        if (null != dataSources) {
            long count = dataSources.stream()
                    .filter(ds -> ds.getMetaDatabaseResource().getMode().name().equalsIgnoreCase(ResourceMode.RW.name()))
                    .count();
            // master节点只允许初始化一个
            if (count > 1) {
                String format = String.format("初始化master数据源名称:[%s]失败,存在多个master节点", key);
                throw new IllegalArgumentException(format);
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
