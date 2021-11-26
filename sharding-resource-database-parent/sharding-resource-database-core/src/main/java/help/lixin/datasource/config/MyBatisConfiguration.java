package help.lixin.datasource.config;

import help.lixin.datasource.mybatis.MyBatisConfigurationCustomizer;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfiguration {
    @Bean
    @ConditionalOnClass(SqlSessionFactory.class)
    public MyBatisConfigurationCustomizer myBatisConfigurationCustomizer() {
        return new MyBatisConfigurationCustomizer();
    }
}
