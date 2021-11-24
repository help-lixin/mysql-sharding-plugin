package help.lixin.datasource.core;

import help.lixin.resource.sql.ISQLOverrideService;

import java.sql.*;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.concurrent.Executor;

/**
 * 对javax.sql.Connection进行代理,拦截SQL,进行处理.
 */
public class ConnectionProxy implements Connection {

    private Connection connectionDelegator;

    public ConnectionProxy(Connection connectionDelegator) {
        this.connectionDelegator = connectionDelegator;
    }

    protected Optional<ISQLOverrideService> getSQLOverrideService() {
        ServiceLoader<ISQLOverrideService> load = ServiceLoader.load(ISQLOverrideService.class);
        if (load.iterator().hasNext()) {
            return Optional.of(load.iterator().next());
        }
        return Optional.empty();
    }


    @Override
    public Statement createStatement() throws SQLException {
        return connectionDelegator.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        if (getSQLOverrideService().isPresent()) {
            ISQLOverrideService sqlOverrideService = getSQLOverrideService().get();
            sql = sqlOverrideService.override(connectionDelegator, sql);
        }
        return connectionDelegator.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        if (getSQLOverrideService().isPresent()) {
            ISQLOverrideService sqlOverrideService = getSQLOverrideService().get();
            sql = sqlOverrideService.override(connectionDelegator, sql);
        }
        return connectionDelegator.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        if (getSQLOverrideService().isPresent()) {
            ISQLOverrideService sqlOverrideService = getSQLOverrideService().get();
            sql = sqlOverrideService.override(connectionDelegator, sql);
        }
        return connectionDelegator.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connectionDelegator.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return connectionDelegator.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        connectionDelegator.commit();
    }

    @Override
    public void rollback() throws SQLException {
        connectionDelegator.rollback();
    }

    @Override
    public void close() throws SQLException {
        connectionDelegator.close();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return connectionDelegator.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return connectionDelegator.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        connectionDelegator.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return connectionDelegator.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        connectionDelegator.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return connectionDelegator.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        connectionDelegator.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return connectionDelegator.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return connectionDelegator.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        connectionDelegator.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return connectionDelegator.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        if (getSQLOverrideService().isPresent()) {
            ISQLOverrideService sqlOverrideService = getSQLOverrideService().get();
            sql = sqlOverrideService.override(connectionDelegator, sql);
        }
        return connectionDelegator.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        if (getSQLOverrideService().isPresent()) {
            ISQLOverrideService sqlOverrideService = getSQLOverrideService().get();
            sql = sqlOverrideService.override(connectionDelegator, sql);
        }
        return connectionDelegator.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return connectionDelegator.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        connectionDelegator.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        connectionDelegator.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return connectionDelegator.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return connectionDelegator.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return connectionDelegator.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        connectionDelegator.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        connectionDelegator.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return connectionDelegator.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        if (getSQLOverrideService().isPresent()) {
            ISQLOverrideService sqlOverrideService = getSQLOverrideService().get();
            sql = sqlOverrideService.override(connectionDelegator, sql);
        }
        return connectionDelegator.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        if (getSQLOverrideService().isPresent()) {
            ISQLOverrideService sqlOverrideService = getSQLOverrideService().get();
            sql = sqlOverrideService.override(connectionDelegator, sql);
        }
        return connectionDelegator.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        if (getSQLOverrideService().isPresent()) {
            ISQLOverrideService sqlOverrideService = getSQLOverrideService().get();
            sql = sqlOverrideService.override(connectionDelegator, sql);
        }
        return connectionDelegator.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        if (getSQLOverrideService().isPresent()) {
            ISQLOverrideService sqlOverrideService = getSQLOverrideService().get();
            sql = sqlOverrideService.override(connectionDelegator, sql);
        }
        return connectionDelegator.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        if (getSQLOverrideService().isPresent()) {
            ISQLOverrideService sqlOverrideService = getSQLOverrideService().get();
            sql = sqlOverrideService.override(connectionDelegator, sql);
        }
        return connectionDelegator.prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return connectionDelegator.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return connectionDelegator.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return connectionDelegator.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return connectionDelegator.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return connectionDelegator.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        connectionDelegator.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        connectionDelegator.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return connectionDelegator.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return connectionDelegator.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return connectionDelegator.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return connectionDelegator.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        connectionDelegator.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return connectionDelegator.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        connectionDelegator.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        connectionDelegator.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return connectionDelegator.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return connectionDelegator.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return connectionDelegator.isWrapperFor(iface);
    }
}
