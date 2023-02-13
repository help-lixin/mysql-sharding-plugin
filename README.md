### 1. 现有数据分片比较
现在市面上的数据分片框架各有哪些优缺点?
1) 代理模式(MyCat/ShardingProxy)优缺点:        
   1.1) 优点:开发透明(无侵入式)     
   1.2) 优点:支持多种编程语言.             
   1.3) 缺点:代理模式是一种中心式管理模式,代理机器后面的任何一台机器出现问题,都有可能造成代理机器出现问题.                     
   1.4) 缺点:代理模式机器的配置要求比较高(CPU/内存),而且,需要多台机器配置VIP飘移.    
   1.5) 缺点:分片算法变化数据需要重新入库(rehash).    

2) 边车模式(Sharding JDBC/TDDL)优缺点:         
   2.1) 优点:解决了代理模式的问题.     
   2.2) 缺点:与具体的编程语言相关.  
   2.3) 缺点:分片算法变化数据需要重新入库(rehash).     

### 2. 为什么需要自研
完全是因为业务需求驱动自研,需求列表如下:
1) 数据是敏感信息,租户要求数据是完全隔离的.        
2) 租户刚开始使用SAAS系统,后来,从SAAS迁移到自家机房独立部署(涉及数取的迁移问题),后来,由于某些原因,又要迁回SAAS系统.
MyCat和Sharding JDBC在遇到上述需求时,不太适合业务,所以,才会造成自研的一个主要原因.    

### 3. 实现思路
1) DBA定义N个数据库实例(3306),每个实例上N(100)个库.     
2) App(应用程序)启动时,根据条件(spring.sharding.resource.env=jd,taobao)初始化数据源(本地配置或远程拉取配置),远程拉取配置需要你自己实现接口:IDataSourceMetaService.            
3) 租户注册时,为租户分配相应的信息(数据库实例/Schema/Table前缀).   
4) 租户登录时,都会带上这些信息,建议无状态化(比如JWT),可以做到跨地域(而非中央式去交换信息),只需要做到加密这些数据即可.         
5) 租户请求API时,向上下文(DBResourceContext)填充这些信息:数据库实例/Schema/Table前缀等信息.      
6) 对DataSource和Connection进行了代理,拦截SQL语句进行改写,添加:schema/table前缀,能更好的支持:Hibernate/MyBatis/JdbcTemplate...   
7) 为MyBatis添加Interceptor,拦截SQL语句进行改写,添加:schema/table前缀,原因:开发(生产环境)可能会打开MyBatis的日志控制台,给开发一个更加友好的体验.       

### 4. 项目工程结构介绍

```shell
lixin-macbook:sharding-resource-parent lixin$ tree -L 2
.
├── LICENSE
├── README.md
├── docs              # 详细设计文档介绍
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
<!-- 数据分片引擎 -->
<dependency>
    <groupId>help.lixin.sharding.resource</groupId>
    <artifactId>sharding-resource-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>

<!-- 创建的DataSource,并没有交给Spring,而是统一放在Guava缓存里 -->
<dependency>
   <groupId>com.google.guava</groupId>
   <artifactId>guava</artifactId>
   <version>30.1.1-jre</version>
</dependency>
<!-- 底层利用了Druid对SQL进行解析,并重写SQL语句. --> 
<dependency>
   <groupId>com.alibaba</groupId>
   <artifactId>druid</artifactId>
   <version>1.1.22</version>
</dependency>

<!-- 以下内容,根据你自己的情况添加 -->
<!-- HikariCP数据源 -->
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

> 默认情况下定位数据源的唯一定位是通过:instanceName(数据库实例的名称),你可以实现:IKeyGenerateService接口,交给Spring即可变更数据的名称.
> 在这里主要配置了3个数据源(一主两从). 

```yaml
spring:
   sharding:
      resource:
         enabled: true
         databases:
            - instanceName: 127.0.0.1:3306
              type: com.alibaba.druid.pool.DruidDataSource
              driver: com.mysql.jdbc.Driver
              url: jdbc:mysql://127.0.0.1:3306/db?characterEncoding=utf-8&useSSL=false
              username: root
              password: 123456
              properties:
                 initialSize: 1
                 maxIdle: 100
              slaves:
                 - type: com.alibaba.druid.pool.DruidDataSource
                   driver: com.mysql.jdbc.Driver
                   url: jdbc:mysql://127.0.0.1:3306/db?characterEncoding=utf-8&useSSL=false
                   username: root
                   password: 123456
                   properties:
                      initialSize: 1
                      maxIdle: 100
                 - type: com.zaxxer.hikari.HikariDataSource
                   driver: com.mysql.jdbc.Driver
                   url: jdbc:mysql://127.0.0.1:3306/db?characterEncoding=utf-8&useSSL=false
                   username: root
                   password: 123456
                   properties:
                      initialSize: 2
                      maxIdle: 100

```
4) 上下文绑定回调函数,自行配置数据源信息.
```
@Bean
 public IResourceContextCustomizer businessResourceContextCustomizer() {
     return (invocation, ctxBuild) -> {
         if (!(ctxBuild instanceof DBResourceContext.Build)) {
             return;
         }
         // 设置这次请求对应的数据源信息.
         // 可以从用户上下文(TheadLocal)中获取,又或者远程API请求获取(切记要缓存下).
         DBResourceContext.Build ctx = (DBResourceContext.Build) ctxBuild;
         ctx.instanceName("127.0.0.1:3306")
                 .database("order_db_1")
                 .tablePrefix("tb1_");
     };
 }
```

5) MyBatis原始SQL语句
```
# 原始SQL语句(order为虚拟表)
SELECT * FROM order t WHERE t.order_id IN (?, ?)
```

6) MyBatis控制台SQL
```
# 改写后的SQL语句(增加了库前缀和表前缀)
#  order               为虚拟表
# order_db_1.tb1_order 为真实表
SELECT * FROM order_db_1.tb1_order t WHERE t.order_id IN (?, ?)
```