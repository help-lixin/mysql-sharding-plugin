### 1. 背景

### 2. 实现思路
1) 项目启动时,初始化数据源(本地配置/远程),数据源的资源唯一名称为(${instanceName}/${resourceName}/${resourceType}),你可以自己实现IKeyGenerateStrategy类自定义配置. 
2) 拦截@Transaction,向上下文填充这次请求(DBResourceContext),将需要使用哪个数据源.     
3) 对DataSource和Connection进行了代理,对SQL进行拦截,添加:库前缀和表前缀.  

### 3. 项目工程结构介绍
```shell
lixin-macbook:sharding-resource-parent lixin$ tree -L 2
.
├── LICENSE
├── README.md
├── docs              # 文档介绍
│   └── imgs
├── pom.xml
├── sharding-resource-database-parent      # 资源分片,针对数据库的实现
│   ├── pom.xml
│   ├── sharding-resource-database-context  # 线程上下文信息
│   ├── sharding-resource-database-core     # 针对数据库的具体实现
│   ├── sharding-resource-database-meta     # 解析配置信息到业务模型
│   ├── sharding-resource-database-model    # 业务模型
│   └── sharding-resource-database-mybatis  # 已弃用
├── sharding-resource-define-parent      # 资源分片的一些公共信息(从设计之初就有考虑db/redis/mq).
│   ├── pom.xml
│   ├── sharding-resource-api               # api
│   ├── sharding-resource-context           # 资源分片通用基础上下文
│   ├── sharding-resource-event             # 配置更新时,触发事件
│   ├── sharding-resource-model             # 资源分片通用模型
│   └── sharding-resource-route             # 资源分片路由信息定义
├── sharding-resource-examples                    # 测试案例
│   ├── pom.xml
│   └── sharding-resource-database-mybatis-example
└── sharding-resource-spring-boot-starter
    ├── pom.xml
    └── src
```
### 4. 集成步骤
1) 添加依赖
```xml
    <!-- 必须要添加的依赖为以下内容 --> 
    <dependency>
        <groupId>help.lixin.sharding.resource</groupId>    
        <artifactId>sharding-resource-spring-boot-starter</artifactId>
        <version>1.0.0</version>
    </dependency>
    <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
        <version>30.1.1-jre</version>
    </dependency>
    
    <!-- 以下内容,根据你自己的情况添加 -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.22</version>
    </dependency>
    <dependency>
        <groupId>com.zaxxer</groupId>
        <artifactId>HikariCP</artifactId>
        <version>3.2.0</version>
    </dependency>

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
```
2) 配置开启分片功能
```yaml
spring:
  sharding:
    resource:
      enabled: true
```
3) 配置数据源
> 默认情况下定位数据源的唯一定位是通过:${instanceName}/${resourceName}/${resourceType},你可以根据你自己的需求,进行组合.
> 在这里主要配置了3个数据源.
```yaml
spring:
  main:
    allow-bean-definition-overriding: true
  application:
    name: sharding-resource-database-demo
  sharding:
    resource:
      enabled: true
      databases:
        - instanceName: 127.0.0.1:3306
          resourceName: user-service
          resourceType: master
          type: com.alibaba.druid.pool.DruidDataSource
          driver: com.mysql.jdbc.Driver
          url: jdbc:mysql://127.0.0.1:3306/db?characterEncoding=utf-8&useSSL=false
          username: root
          password: 123456
          properties:
            initialSize: 1
            maxIdle: 100
        - instanceName: 127.0.0.1:3306
          resourceName: user-service
          resourceType: slave
          type: com.alibaba.druid.pool.DruidDataSource
          driver: com.mysql.jdbc.Driver
          url: jdbc:mysql://127.0.0.1:3306/db?characterEncoding=utf-8&useSSL=false
          username: root
          password: 123456
          properties:
            initialSize: 1
            maxIdle: 100
        - instanceName: 127.0.0.1:3306
          resourceName: user-service
          resourceType: slave
          type: com.zaxxer.hikari.HikariDataSource
          driver: com.mysql.jdbc.Driver
          url: jdbc:mysql://127.0.0.1:3306/db?characterEncoding=utf-8&useSSL=false
          username: root
          password: 123456
          properties:
            initialSize: 2
            maxIdle: 100
```
### 5. 总结
