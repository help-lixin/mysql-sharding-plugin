package help.lixin.datasource.mybatis.customizer;

import help.lixin.datasource.mybatis.interceptor.SQLOverrideInterceptor;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;

public class MyBatisConfigurationCustomizer implements ConfigurationCustomizer {
    @Override
    public void customize(Configuration configuration) {
        configuration.addInterceptor(new SQLOverrideInterceptor());
    }
}
