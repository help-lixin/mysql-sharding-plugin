package help.lixin.datasource.sql.override;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import help.lixin.datasource.context.DBResourceContext;
import help.lixin.resource.context.ResourceContextHolder;
import help.lixin.resource.context.ResourceContext;
import help.lixin.resource.sql.ISQLOverrideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DefaultSQLOverrideService implements ISQLOverrideService {

    private Logger logger = LoggerFactory.getLogger(DefaultSQLOverrideService.class);

    private static final String DB_TYPE = "mysql";

    @Override
    public String override(String sql, Object... args) {
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, DB_TYPE);
        MySqlASTVisitorAdapter visitor = new TableOverrideMySqlASTVisitorAdapter();
        for (SQLStatement sqlStatement : sqlStatements) {
            sqlStatement.accept(visitor);
        }
        String newSQL = SQLUtils.toSQLString(sqlStatements, DB_TYPE);
        if (logger.isDebugEnabled()) {
            logger.debug("override sql before:[{}] , after:[{}]", sql, newSQL);
        }
        return newSQL;
    }
}

class TableOverrideMySqlASTVisitorAdapter extends MySqlASTVisitorAdapter {
    public boolean visit(SQLExprTableSource x) {
        ResourceContext tmp = ResourceContextHolder.get();
        if (null != tmp && tmp instanceof DBResourceContext
        ) {
            DBResourceContext ctx = (DBResourceContext) tmp;
            String database = ctx.getDatabase();
            String tablePrefix = ctx.getTablePrefix();
            // 上下文中包含这些内容时,才会进行处理
            if (null != database && null != database) {
                // 现有的表名称
                String tableName = x.getName().getSimpleName();
                String expr = new StringBuilder()
                        .append(database)
                        .append(".")
                        .append(tablePrefix)
                        .append(tableName)
                        .toString();
                x.setExpr(expr);
            }
        }
        return true;
    }
}

