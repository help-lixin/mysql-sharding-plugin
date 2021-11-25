package help.lixin.datasource.mybatis;

import help.lixin.datasource.mybatis.interceptor.SQLRewriteInterceptor;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;

public class MyBatisConfigurationCustomizer implements ConfigurationCustomizer {
    @Override
    public void customize(Configuration configuration) {
        configuration.addInterceptor(new SQLRewriteInterceptor());
    }
}
