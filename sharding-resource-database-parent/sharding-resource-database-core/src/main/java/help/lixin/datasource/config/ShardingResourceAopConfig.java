package help.lixin.datasource.config;

import help.lixin.datasource.aop.BindResourceContextAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;


@Configuration
public class ShardingResourceAopConfig {
    private Logger logger = LoggerFactory.getLogger(ShardingResourceAopConfig.class);

    /**
     * 绑定上下文切面
     *
     * @return
     */
    @Bean
    public BindResourceContextAspect bindResourceContextAspect() {
        return new BindResourceContextAspect();
    }
}
