package help.lixin.datasource.core;

import help.lixin.datasource.context.DBResourceContext;
import help.lixin.resource.context.ResourceContextHolder;
import help.lixin.resource.context.ResourceContext;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class VirtualDataSource implements DataSource {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(VirtualDataSource.class);

    private PrintWriter out;
    private int seconds;

    private IVirtuaDataSourceDelegator virtuaDataSourceDelegator;

    public VirtualDataSource(IVirtuaDataSourceDelegator virtuaDataSourceDelegator) {
        this.virtuaDataSourceDelegator = virtuaDataSourceDelegator;
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
            Connection targetConnection = virtuaDataSourceDelegator.getConnection(dbCtx);
            ConnectionProxy connectionProxy = new ConnectionProxy(targetConnection);
            return connectionProxy;
        } else {
            logger.warn("请求获取连接失败,不存在DBResourceContextInfo对象.");
            throw new SQLException("请求获取连接失败,不存在DBResourceContextInfo对象.");
        }
    }
}
