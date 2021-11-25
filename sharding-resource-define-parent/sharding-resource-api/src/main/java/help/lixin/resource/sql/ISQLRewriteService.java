package help.lixin.resource.sql;

public interface ISQLRewriteService {

    /**
     * 对sql语句进行处理
     *
     * @param sql
     * @return
     */
    String rewrite(String sql, Object... args);

}
