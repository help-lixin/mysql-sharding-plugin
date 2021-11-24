package help.lixin.datasource.context;

import help.lixin.resource.context.AbstractResourceContext;
import help.lixin.resource.context.ResourceContext;

/**
 * 数据库资源上下文
 */
public class DBResourceContext extends AbstractResourceContext implements ResourceContext {
    // 192.168.1.100:3306
    private String instanceName;
    // schema
    private String database;
    // tb1_
    private String tablePrefix;
    // user-service-datasource
    private String dataSourceName;
    // 是否只读
    private boolean readOnly;

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public static Build newBuild() {
        return new Build();
    }

    public static class Build extends AbstractResourceContext.Build {
        // 192.168.1.100:3306
        private String instanceName;
        // schema
        private String database;
        // tb1_
        private String tablePrefix;
        // user-service-datasource
        private String dataSourceName;
        // 是否只读
        private boolean readOnly;

        public Build readOnly(boolean readOnly) {
            this.readOnly = readOnly;
            return this;
        }

        public Build instanceName(String instanceName) {
            this.instanceName = instanceName;
            return this;
        }

        public Build database(String database) {
            this.database = database;
            return this;
        }

        public Build tablePrefix(String tablePrefix) {
            this.tablePrefix = tablePrefix;
            return this;
        }

        public Build dataSourceName(String dataSourceName) {
            this.dataSourceName = dataSourceName;
            return this;
        }

        @Override
        public ResourceContext build() {
            DBResourceContext ctx = new DBResourceContext();
            ctx.setTenantId(this.tenantId);
            ctx.setMicroServiceName(this.microServiceName);
            ctx.setEnv(this.env);
            ctx.setProperties(this.properties);
            ctx.setRegion(this.region);
            ctx.setZone(this.zone);

            ctx.setInstanceName(this.instanceName);
            ctx.setDatabase(this.database);
            ctx.setTablePrefix(this.tablePrefix);
            ctx.setDataSourceName(this.dataSourceName);
            ctx.setReadOnly(this.readOnly);
            return ctx;
        }
    }
}
