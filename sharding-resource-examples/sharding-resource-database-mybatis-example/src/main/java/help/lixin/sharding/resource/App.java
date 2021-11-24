package help.lixin.sharding.resource;

import help.lixin.datasource.context.DBResourceContext;
import help.lixin.resource.context.ResourceContextHolder;
import help.lixin.resource.context.ResourceContext;
import help.lixin.resource.route.IResourceContextCustomizer;
import help.lixin.sharding.resource.mapper.OrderMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// TODO lixin
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    /**
     * TODO lixin
     *  预留出这个接口的实现出来,给我们的开发人员使用.
     *  比如:拿着用户登录后的信息,换相应的信息出来(数据源).并绑定到线程上下文中.
     *
     * @return
     */
    @Bean
    public IResourceContextCustomizer businessResourceContextCustomizer() {
        return (invokeContext, ctxBuild) -> {
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

    public static void main2(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
        OrderMapper orderMapper = context.getBean(OrderMapper.class);
        try {
            ResourceContext ctx = DBResourceContext.newBuild()
                    .instanceName("127.0.0.1:3306")
                    .dataSourceName("user-service")
                    .database("order_db_1")
                    .tablePrefix("tb1_")
                    .build();
            // 绑定上下文
            ResourceContextHolder.bind(ctx);

            List<Long> ids = new ArrayList<>();
            ids.add(565585450073325568L);
            ids.add(565585450987683840L);
            List<Map> mapList = orderMapper.selectOrderbyIds(ids);
            System.out.println(mapList);
        } finally {
            // 清空上下文
            ResourceContextHolder.unBind();
        }
        System.out.println("=======================================================================================");
        try {
            ResourceContext ctx = DBResourceContext.newBuild()
                    .instanceName("127.0.0.1:3306")
                    .dataSourceName("user-service")
                    .database("order_db_1")
                    .readOnly(true)
                    .tablePrefix("tb2_")
                    .build();
            // 绑定上下文
            ResourceContextHolder.bind(ctx);

            List<Long> ids = new ArrayList<>();
            ids.add(565585451373559809L);
            ids.add(565585451507777537L);
            List<Map> mapList = orderMapper.selectOrderbyIds(ids);
            System.out.println(mapList);
        } finally {
            // 清空上下文
            ResourceContextHolder.unBind();
        }
    }
}
