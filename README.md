
# 框架结构图
![img](https://github.com/airufei/xmfcn-spring-cloud/blob/master/imgs/4AB893CF-8AAE-4a49-A961-B6D153644027.png)

# 工程简介

## 1、xmfcn-spring-cloud-common 
1、提供基础工具类相关功能，如：字符串工具类、常量类、http请求、文件相关工具、时间转换工具、枚举，model、vo等。

2、被其他工程引用，本身不提供服务能力。

## 2、xmfcn-spring-cloud-eureka 
1、作为注册中心，提供服务发现与注册功能。

2、计划引入阿里开源的Nacos进行替换(eureka 开始闭源)。

## 3、xmfcn-spring-cloud-zuul 
1、作为统一网关，提供网关统一入口，负责服务转发、安全校验、路由等。

2、计划引入spring cloud gateway 进行替换，spring boot2.x版本以后官方自研版本。

## 4、xmfcn-spring-cloud-user-api 
1、提供对外用户信息功能接口，用户基础信息查询，用户注册、登录等，提供个性化的对外API能力。

2、如：分页查询用户信息接口：http://localhost:8082/user/getList 参数：parms={"pageNo":1,"pageSize":20}，当前代码由https://github.com/airufei/xCode 生成

## 5、xmfcn-spring-cloud-user-service 
1、提供对外用户信息功能接口，用户基础信息查询，用户注册、登录等，不对外开放，只对微服务体系内开放。

2、当前代码由 https://github.com/airufei/xCode 生成 所需数据库文件在xmfcn-spring-cloud/db/job-admin/xcode.sql

3、计划引入Sharding-JDBC实现分库分表以及读写分离功能。
   
## 6、xmfcn-spring-cloud-sys-service 
1、作为日志基础服务，提供包括日志、缓存、队列、字典等系统相关的基础服务，不对外开放，只对微服务体系内开放。

2、所需数据库文件在xmfcn-spring-cloud/db/job-admin/xcode.sql 

3、新增功能包括ES的存储于搜索功能，日志统计功能。当前实现都是基于Java api 来实现，也加入了ES SQL的支持。

4、redis 这块的基础功能使用没有问题，分布式锁功能尚未完成，不能使用。

5、集成了kafka生产者功能，其他服务直接调用此服务即可发送数据到对应的topic。

## 7、xmfcn-spring-cloud-job-core 
1、xxl-job的基础核心工具类工程，修改工程名称和包路径是为了整体统一，内部实现还是基于xxl-job。

2、可以无需引入此功能，用maven引入相关的jar包也可以。

## 8、xmfcn-spring-cloud-job-admin 

1、作为任务调度中心，也是由xxl-job-admin 演化而来，原版是MVC项目，为了项目统一架构，全部改成了微服务系统。

2、在原项目基础上进行了UI层的优化，代码优化和数据库表扩展

3、修改原有报警方式，采用钉钉邮件报警，可配置。

4、变更了登录方式，采用手机号登录18610000006 密码abc123

5、新增了用户管理功能、菜单功能、角色功能、字典功能

6、需要配置根域名或者IP，在application-prod或者dev中配置，如：webRoot: http://localhost:8082

7、所需要的数据库文件在父项目的db文件夹 xmfcn-spring-cloud/db/job-admin/xmfjob.sql启动

8、启动之后经过zuul访问的路径是http://localhost:8082/job

9、新增加了日志搜索功能和日志报表功能，省去了单独部署kibana服务，且kibana没有登录功能，不能很好的在生产环境使用。
   
## 9、xmfcn-spring-cloud-job-service 

1、作为任务执行器，执行具体的任务，比如定时任务等等。

2、引入sys-service，引入包括队列、缓存以及分布式锁功能。

3、需要配置调度中心地址，如：http://localhost:8082/job。

4、集成了kafka消费功能，此类消费任务无需由调度中心来调度，因此也可以单出拆分一个服务来做。

## 10、其他说明
1、集成EFK统一日志系统方案：

   &nbsp;&nbsp;1）logback收集的日志通过整理过滤直接写入kafka，logback需要扩展一个appender，通过自定义appender写入kafka队列。 
   
   &nbsp;&nbsp;2）job-service 执行kafka消费任务，处理日志队里数据到Elasticsearch系统中进行存储。
   
   &nbsp;&nbsp;3）job-admin 通过通用的api调用从sys-service中获取写入到Elasticsearch中的日志数据，然后展示在网页上，
   当前支持系统名称、执行方法名、日志级别、日志关键词、日志标识（调用唯一标示）进行搜索。
   
   &nbsp;&nbsp;4）kibana 当前没有登录功能，因此暂时集成到job-admin进行日志搜索。
   
2、欢迎留言讨论：https://github.com/airufei/xmfcn-spring-cloud/issues/1

# 项目截图
1、登录界面
![img](https://github.com/airufei/xmfcn-spring-cloud/blob/master/imgs/20190625172030.png)

2、任务管理界面
![img](https://github.com/airufei/xmfcn-spring-cloud/blob/master/imgs/20190625175639.png)

3、任务日志界面
![img](https://github.com/airufei/xmfcn-spring-cloud/blob/master/imgs/20190625175549.png)

4、系统日志搜索界面
![img](https://github.com/airufei/xmfcn-spring-cloud/blob/master/imgs/20190625174804.png)

5、系统日志报表界面
![img](https://github.com/airufei/xmfcn-spring-cloud/blob/master/imgs/20190625175224.png)

6、Redis运行状态界面
![img](https://github.com/airufei/xmfcn-spring-cloud/blob/master/imgs/20190625175735.png)

6、数据字典界面
![img](https://github.com/airufei/xmfcn-spring-cloud/blob/master/imgs/20190625175815.png)
