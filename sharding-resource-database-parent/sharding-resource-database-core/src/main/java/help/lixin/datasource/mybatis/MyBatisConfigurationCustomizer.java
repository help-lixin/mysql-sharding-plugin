package help.lixin.datasource.mybatis;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import help.lixin.datasource.mybatis.interceptor.SQLRewriteInterceptor;

import com.baomidou.mybatisplus.core.MybatisConfiguration;

public class MyBatisConfigurationCustomizer implements ConfigurationCustomizer {
    @Override
    public void customize(MybatisConfiguration configuration) {
        configuration.addInterceptor(new SQLRewriteInterceptor());
    }
}
