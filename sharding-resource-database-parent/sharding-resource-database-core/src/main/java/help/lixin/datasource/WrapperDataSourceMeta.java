package help.lixin.datasource;

import help.lixin.datasource.model.DatabaseResource;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * 把DataSource进行一次包裹(扩展更多的元信息).
 */
public class WrapperDataSourceMeta implements DataSource {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(VirtualDataSource.class);

    private PrintWriter out;
    private int seconds;

    private DataSource targetDataSource;

    private DatabaseResource metaDatabaseResource;

    public WrapperDataSourceMeta(DataSource targetDataSource, DatabaseResource metaDatabaseResource) {
        this.targetDataSource = targetDataSource;
        this.metaDatabaseResource = metaDatabaseResource;
    }

    public DatabaseResource getMetaDatabaseResource() {
        return metaDatabaseResource;
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
        return targetDataSource.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return targetDataSource.getConnection(username, password);
    }
}
