### 1. 现有数据分片比较
现在市面上的数据分片框架各有哪些优缺点?
1) 代理模式(MyCat/ShardingProxy)优缺点:        
   1.1) 优点:开发透明(无侵入式)   
   1.2) 优点:支持多种编程语言.       
   1.2) 缺点:代理模式是一种中心式管理模式,代理机器后面的任何一台机器出现问题,都有可能造成代理模式出现问题.       
   1.3) 缺点:代理模式机器的配置要求比较高(CPU/内存),而且,需要多台配置VIP飘移.

2) 边车模式(Sharding JDBC/TDDL)优缺点:         
   2.1) 优点:解决了代理模式的问题.     
   2.2) 缺点:与具体的编程语言相关.  
   2.3) 缺点:分片算法比较简单.

### 2. 为什么需要自研
完全是因为业务需求驱动自研,需求列表如下:
1) 租户刚开始使用SAAS,后来,从SAAS迁移到自家机房独立部署(涉及数取的迁移问题),后来,由于某些原因,又可能迁回SAAS.        
2) 数据是敏感信息,租户要求数据是完全隔离的.   
MyCat和Sharding JDBC在遇到上述需求时,不太符合业务,所以,才会造成自研.  

### 3. 实现思路
1) 定义N个数据库实例(3306),每个实例上N(100)个库,租户在注册时,记录租户对应的信息(数据库实例/数据库),当,这个租户下的用户在登录时,都会带上这些信息(建议无状态代,并加密).
2) 应用程序启动时,根据条件(spring.sharding.resource.env=jd,taobao)初始化数据源(本地配置/远程拉取配置).    
3) 拦截注解@Transaction,向上下文填充这次请求(DBResourceContext),将需要使用具体哪一个数据源.
4) 对DataSource和Connection进行了代理,拦截SQL语句,添加:库前缀和表前缀. 

### 4. 项目工程结构介绍

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

### 5. 集成步骤

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

### 6. 总结
