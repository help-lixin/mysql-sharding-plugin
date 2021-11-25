package help.lixin.datasource.service.loadbalancer.impl;

import help.lixin.datasource.context.DBResourceContext;
import help.lixin.datasource.WrapperDataSourceMeta;
import help.lixin.datasource.keygen.IKeyGenerateService;
import help.lixin.datasource.service.loadbalancer.ILoadBalancerDataSourceService;
import help.lixin.datasource.service.loadbalancer.IRuleService;
import help.lixin.datasource.service.store.IDataSourceStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * 总体思想是这样的:<br/>
 * 1. 应用启动时做以下几件事情:<br/>
 * &nbsp;&nbsp; 1.1 读取配置信息(本地/网络),转换成业务模型(DatabaseResource),并根据DatabaseResource创建DataSource.<br/>
 * &nbsp;&nbsp; 1.2 启动一个后台线程,监听配置的变化,当配置有变化时.更新DataSource信息,注意:仅仅做局部更新(或新增),禁止销毁所有的连接. <br/>
 */
public class DefaultLoadBalancerDataSourceService implements ILoadBalancerDataSourceService {

    private Logger logger = LoggerFactory.getLogger(DefaultLoadBalancerDataSourceService.class);

    private IRuleService ruleService;

    public DefaultLoadBalancerDataSourceService(IRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @Override
    public Optional<DataSource> chooseDataSource(DBResourceContext ctx) throws SQLException {
        return ruleService.choose(ctx);
    }
}