package help.lixin.datasource;

import help.lixin.datasource.context.DBResourceContext;
import help.lixin.datasource.core.IVirtuaDataSourceDelegator;
import help.lixin.datasource.properties.ShardingResourceProperties;
import help.lixin.resource.context.ResourceContextHolder;
import help.lixin.resource.context.ResourceContext;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Optional;
import java.util.logging.Logger;

public class VirtualDataSource implements DataSource, ApplicationContextAware {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(VirtualDataSource.class);

    private PrintWriter out;
    private int seconds;

    private ApplicationContext applicationContext;

    private IVirtuaDataSourceDelegator virtuaDataSourceDelegator;
    private ShardingResourceProperties shardingResourceProperties;

    public VirtualDataSource() {
        System.out.println("");
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.virtuaDataSourceDelegator = applicationContext.getBean(IVirtuaDataSourceDelegator.class);
        this.shardingResourceProperties = applicationContext.getBean(ShardingResourceProperties.class);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return out;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.out = out;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        this.seconds = seconds;
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return seconds;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }


    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(null, null);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        ResourceContext ctx = ResourceContextHolder.get();
        if (null != ctx && (ctx instanceof DBResourceContext)) {
            DBResourceContext dbCtx = (DBResourceContext) ctx;
            // 真实连接
            Optional<Connection> targetConnection = virtuaDataSourceDelegator.getConnection(dbCtx);
            if (targetConnection.isPresent()) {
                RewriteSQLConnection rewriteSQLConnection = new RewriteSQLConnection(targetConnection.get());
                return rewriteSQLConnection;
            } else {
                logger.error("从DBResourceContextInfo[{}]上下文获取信息失败.", dbCtx);
                throw new SQLException("请求获取连接失败,不存在DBResourceContextInfo对象.");
            }
        } else {
            logger.error("请求获取连接失败,不存在DBResourceContextInfo对象.");
            throw new SQLException("请求获取连接失败,不存在DBResourceContextInfo对象.");
        }
    }
}
