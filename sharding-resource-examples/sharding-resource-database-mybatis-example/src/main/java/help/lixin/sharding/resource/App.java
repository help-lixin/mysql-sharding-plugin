package help.lixin.sharding.resource;

import help.lixin.datasource.context.DBResourceContextInfo;
import help.lixin.resource.context.ResourceContext;
import help.lixin.resource.context.ResourceContextInfo;
import help.lixin.sharding.resource.mapper.OrderMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// TODO lixin
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(App.class, args);
//        System.out.println();
        OrderMapper orderMapper = ctx.getBean(OrderMapper.class);
        try {
            ResourceContextInfo info = DBResourceContextInfo.newBuild()
                    .instanceName("127.0.0.1:3306")
                    .dataSourceName("user-service")
                    .database("order_db_1")
                    .tablePrefix("tb1_")
                    .build();
            // 绑定上下文
            ResourceContext.bind(info);

            List<Long> ids = new ArrayList<>();
            ids.add(565585450073325568L);
            ids.add(565585450987683840L);
            List<Map> mapList = orderMapper.selectOrderbyIds(ids);
            System.out.println(mapList);
        } finally {
            // 清空上下文
            ResourceContext.unBind();
        }
        System.out.println("=======================================================================================");
        try {
            ResourceContextInfo info = DBResourceContextInfo.newBuild()
                    .instanceName("127.0.0.1:3306")
                    .dataSourceName("user-service")
                    .database("order_db_1")
                    .readOnly(true)
                    .tablePrefix("tb2_")
                    .build();
            // 绑定上下文
            ResourceContext.bind(info);

            List<Long> ids = new ArrayList<>();
            ids.add(565585451373559809L);
            ids.add(565585451507777537L);
            List<Map> mapList = orderMapper.selectOrderbyIds(ids);
            System.out.println(mapList);
        } finally {
            // 清空上下文
            ResourceContext.unBind();
        }
    }
}
