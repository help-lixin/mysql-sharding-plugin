package help.lixin.datasource.sql.rewrite;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import help.lixin.datasource.context.DBResourceContext;
import help.lixin.resource.constants.Constants;
import help.lixin.resource.context.ResourceContextHolder;
import help.lixin.resource.context.ResourceContext;
import help.lixin.resource.sql.ISQLRewriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DefaultSQLRewriteService implements ISQLRewriteService {
    private Logger logger = LoggerFactory.getLogger(DefaultSQLRewriteService.class);
    private static final String DB_TYPE = "mysql";

    @Override
    public String rewrite(String sql, Object... args) {
        ResourceContext ctx = ResourceContextHolder.get();
        if (null != ctx) {
            String markRewrite = ctx.getProperties().getOrDefault(Constants.MARK_REWRITE_KEY, "false");
            // 使得这个方法只执行一次.
            if (markRewrite.equalsIgnoreCase("false")) {
                List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, DB_TYPE);
                MySqlASTVisitorAdapter visitor = new TableRewriteMySqlASTVisitorAdapter();
                for (SQLStatement sqlStatement : sqlStatements) {
                    sqlStatement.accept(visitor);
                }
                String newSQL = SQLUtils.toSQLString(sqlStatements, DB_TYPE);
                if (logger.isDebugEnabled()) {
                    logger.debug("override sql before:[{}] , after:[{}]", sql, newSQL);
                }
                // 标记曾经重写过SQL语句,下次就不会再进入该业务逻辑了
                ctx.getProperties().put(Constants.MARK_REWRITE_KEY, Constants.MARK_REWRITE_VALUE);
                return newSQL;
            }
        }
        return sql;
    }
}

class TableRewriteMySqlASTVisitorAdapter extends MySqlASTVisitorAdapter {
    public boolean visit(SQLExprTableSource x) {
        ResourceContext tmp = ResourceContextHolder.get();
        if (null != tmp && tmp instanceof DBResourceContext
        ) {
            DBResourceContext ctx = (DBResourceContext) tmp;
            String database = ctx.getDatabase();
            String tablePrefix = ctx.getTablePrefix();
            // 上下文中包含这些内容(database/tablePrefix)时,才会进行处理
            if (null != database && null != tablePrefix) {
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

