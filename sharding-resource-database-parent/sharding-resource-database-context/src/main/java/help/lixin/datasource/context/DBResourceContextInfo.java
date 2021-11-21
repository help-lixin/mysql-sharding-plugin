package help.lixin.datasource.context;

import help.lixin.resource.context.AbstractResourceContextInfo;
import help.lixin.resource.context.ResourceContextInfo;

/**
 * 数据库资源上下文
 */
public class DBResourceContextInfo extends AbstractResourceContextInfo implements ResourceContextInfo {
    // 192.168.1.100:3306
    private String instanceName;
    // schema
    private String database;
    // tb1_
    private String tablePrefix;
    // user-service-datasource
    private String dataSourceName;

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

    public static class Build extends AbstractResourceContextInfo.Build {
        // 192.168.1.100:3306
        private String instanceName;
        // schema
        private String database;
        // tb1_
        private String tablePrefix;
        // user-service-datasource
        private String dataSourceName;

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
        public ResourceContextInfo build() {
            DBResourceContextInfo ctx = new DBResourceContextInfo();
            ctx.setTenantId(this.tenantId);
            ctx.setMicroServiceName(this.microServiceName);
            ctx.setEnv(this.env);
            ctx.setProperties(this.properties);

            ctx.setInstanceName(this.instanceName);
            ctx.setDatabase(this.database);
            ctx.setTablePrefix(this.tablePrefix);
            ctx.setDataSourceName(this.dataSourceName);
            return ctx;
        }
    }
}
