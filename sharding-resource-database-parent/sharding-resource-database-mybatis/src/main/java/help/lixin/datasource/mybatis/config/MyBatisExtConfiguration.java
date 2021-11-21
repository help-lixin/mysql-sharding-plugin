package help.lixin.datasource.mybatis.config;

import help.lixin.datasource.mybatis.LanguageDriverCustomizer;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean({SqlSessionFactory.class})
public class MyBatisExtConfiguration {

    @Bean
    public ConfigurationCustomizer languageDriverCustomizer() {
        return new LanguageDriverCustomizer();
    }
}
