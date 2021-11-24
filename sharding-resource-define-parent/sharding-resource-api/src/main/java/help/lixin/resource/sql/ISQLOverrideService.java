package help.lixin.resource.sql;


import java.sql.Connection;

public interface ISQLOverrideService {

    /**
     * 对sql语句进行处理
     *
     * @param sql
     * @return
     */
    String override(Connection connection, String sql);

}
