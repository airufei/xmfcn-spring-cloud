# xmfcn-spring-cloud 简介
1、springcloud 微服务，基于spring-cloud Finchley.SR1。

2、使用eurka作为注册中心，提供服务发现与注册功能。

3、使用zuul作为统一网关，提供统一入口，安全校验、路由等。

4、加入xxl-job 任务调度器，负责任务调度与任务管理。

5、当前版本api层服务和service层服务已经打通，去除了Spring Security权限验证。在spring boot2.x版本中，Security权限验证存在很多问题
暂时未能解决，再研究研究，争取把权限验证加入到每层服务中，当前服务仅保留eurka使用了权限验证，并且可用。

6、调度器job-admin 和job-handler执行器已经可用。执行任务只需要访问http://localhost:8082/jobadmin/ 添加相应任务，并且在job-handler的jobhandler包下写入具体的任务执行方法即可执行相应任务。

7、user-api和user-service，当前两个服务已经可用。

# 工程简介

1、xmfcn-spring-cloud-common 提供基础工具类相关的，如：字符串处理、常量类、http请求、文件处理等相关工具，被其他工程引用，本身不提供服务能力。

2、xmfcn-spring-cloud-eureka 作为注册中心，提供服务发现与注册功能。

3、xmfcn-spring-cloud-zuul 作为统一网关，提供统一入口，安全校验、路由等。

4、xmfcn-spring-cloud-user-api 提供对外用户信息功能接口，用户基础信息查询，用户注册、登录等，提供个性化的对外开放能力。
如：分页查询用户信息接口：http://localhost:8082/user/getList 参数：parms={"pageNo":1,"pageSize":20}

5、xmfcn-spring-cloud-user-service 提供对外用户信息功能接口，用户基础信息查询，用户注册、登录等，不对外开放，只对微服务体系内开放。

6、xmfcn-spring-cloud-sys-service 作为日志基础服务，提供包括日志、缓存、队列、字典等系统相关的基础服务，不对外开放，只对微服务体系内开放。

7、xmfcn-spring-cloud-job-core 是xxl-job的基础核心工具类工程，修改工程名称和包路径是为了整体统一，内部实现还是基于xxl-job

8、xmfcn-spring-cloud-job-admin 作为任务调度中心，也是由xxl-job-admin 演化而来，原版是MVC项目，我为了项目统一架构，全部改成了微服务系统。
   启动所需要的数据库文件在src\main\resources\db\tables_xxl_job.sql 启动之后经过zuul访问的路径是http://localhost:8085/jobadmin
   
9、xmfcn-spring-cloud-job-handler 作为任务执行器，执行具体的任务，比如定时任务等等。