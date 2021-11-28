package help.lixin.sharding.resource;

import help.lixin.datasource.context.DBResourceContext;
import help.lixin.resource.context.ResourceContextHolder;
import help.lixin.resource.context.ResourceContext;
import help.lixin.resource.route.IResourceContextCustomizer;
import help.lixin.sharding.resource.mapper.OrderMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    /**
     * TODO lixin
     *  预留出这个接口的实现出来,给我们的开发人员使用.
     *  比如:拿着用户登录后的信息,换取数据源出来.并绑定到线程上下文中.
     *
     * @return
     */
    @Bean
    public IResourceContextCustomizer businessResourceContextCustomizer() {
        return (invocation, ctxBuild) -> {
            if (!(ctxBuild instanceof DBResourceContext.Build)) {
                return;
            }

            // TODO lixin
            // 模拟换数据源的信息.
            DBResourceContext.Build ctx = (DBResourceContext.Build) ctxBuild;
            ctx.instanceName("127.0.0.1:3306")
                    .dataSourceName("user-service")
                    .database("order_db_1")
                    .tablePrefix("tb1_");
        };
    }
}
