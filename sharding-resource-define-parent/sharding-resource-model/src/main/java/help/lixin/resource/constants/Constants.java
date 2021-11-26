package help.lixin.resource.constants;

public class Constants {

    // 标记是否曾经重写过SQL语句,防止多次重写SQL语句
    public static final String MARK_REWRITE_KEY = "_rewrite_sql";
    public static final String MARK_REWRITE_VALUE = "true";


    public static final String SHARDING_RESOURCE_KEY = "spring.sharding.resource";
    public static final String ENABLED = "enabled";
    public static final String SHARDING_RESOURCE_DATABASES = "spring.sharding.resource.databases";
}
