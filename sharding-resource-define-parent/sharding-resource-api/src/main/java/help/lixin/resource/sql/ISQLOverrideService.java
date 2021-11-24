package help.lixin.resource.sql;


public interface ISQLOverrideService {

    /**
     * 对sql语句进行处理
     *
     * @param sql
     * @return
     */
    String override(String sql);

}
