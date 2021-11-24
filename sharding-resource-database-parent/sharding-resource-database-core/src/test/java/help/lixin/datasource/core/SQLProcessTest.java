package help.lixin.datasource.core;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;

import java.util.List;

public class SQLProcessTest {
    public static void main(String[] args) {
//        String sql = " SELECT * FROM user t WHERE t.id = 1 ";
//        String sql = " INSERT INTO user(id,name,age) VALUES(?,?,?)";
//        String sql = "  UPDATE user SET name=? WHERE id = ? ";
        String sql = " SELECT t.* FROM user t , menu m WHERE t.id = m.u_id  AND t.id = 1 ";
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, "mysql");
        MySqlASTVisitorAdapter visitor = new TestMySqlASTVisitorAdapter();
        for (SQLStatement sqlStatement : sqlStatements) {
            sqlStatement.accept(visitor);
        }
        String newSQL = SQLUtils.toSQLString(sqlStatements, "mysql");
        System.out.println(newSQL);
    }
}

class TestMySqlASTVisitorAdapter extends MySqlASTVisitorAdapter {
    public boolean visit(SQLExprTableSource x) {
        String database = "db1.";
        String tablePrefix = "t1_";
        String tableName = x.getName().getSimpleName();
        x.setExpr(database + tablePrefix + tableName);
        return true;
    }
}
