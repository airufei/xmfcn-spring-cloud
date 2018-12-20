# xmfcn-spring-cloud 重要变动
1、账号变更、登陆账号改为手机登陆 18610000006 密码：abc123
2、菜单变更，菜单读取数据库，正在开发菜单权限功能

# xmfcn-spring-cloud 简介
1、springcloud 微服务，基于spring-cloud Finchley.SR1,spring boot 2.0.1.RELEASE 。

2、使用eurka作为注册中心，提供服务发现与注册功能。

3、使用zuul作为统一网关，提供统一入口，安全校验、路由等。

4、加入xxl-job 任务调度器，负责任务调度与任务管理。

5、当前版本api层服务和service层服务已经打通，去除了Spring Security权限验证。在spring boot2.x版本中，Security权限验证存在很多问题
暂时未能解决，再研究研究，争取把权限验证加入到每层服务中，当前服务仅保留eurka使用了权限验证，并且可用。

6、调度器job-admin 和job-handler执行器已经可用。执行任务只需要访问http://localhost:8082/jobadmin/ 添加相应任务，并且在job-handler的jobhandler包下写入具体的任务执行方法即可执行相应任务。

7、服务系统拆分采用垂直+水平，水平方向拆分如user-api、order-api等等、垂直方向划分如：user-api和user-service。这样划分的好处是分层更清晰，每层的职责边界更清楚。
api层更专注于业务逻辑或者api的多样性，service层更加倾向于数据能力和服务稳定能力。

7、user-api和user-service，当前两个服务已经可用。

8、配合开源代码生成工具更能提高开发效率。地址：https://github.com/airufei/xCode
   根据数据库表生成基本功能代码，包含Mybatis文件，dao、servic、entity、controller以及查询页面、增加编辑页。
   springcloud生成模型是特定为springcloud体系开发的代码生成模型、可以同时生成model、api层的接口代码和调用user-service的接口代码
   service层的接口代码、数据库SQL语句等。
   
9、xxl-job 改造为spring cloud版本后已经在生产环境正常调度超过100万次。详见：https://github.com/airufei/xmfcn-spring-cloud/issues/1

# 工程简介

1、xmfcn-spring-cloud-common 提供基础工具类相关的，如：字符串处理、常量类、http请求、文件处理等相关工具，被其他工程引用，本身不提供服务能力。

2、xmfcn-spring-cloud-eureka 作为注册中心，提供服务发现与注册功能。

3、xmfcn-spring-cloud-zuul 作为统一网关，提供统一入口，安全校验、路由等。

4、xmfcn-spring-cloud-user-api 提供对外用户信息功能接口，用户基础信息查询，用户注册、登录等，提供个性化的对外开放能力。
如：分页查询用户信息接口：http://localhost:8082/user/getList 参数：parms={"pageNo":1,"pageSize":20}，当前代码由https://github.com/airufei/xCode 生成

5、xmfcn-spring-cloud-user-service 提供对外用户信息功能接口，用户基础信息查询，用户注册、登录等，不对外开放，只对微服务体系内开放。当前代码由 https://github.com/airufei/xCode 生成
   所需数据库文件在xmfcn-spring-cloud/db/job-admin/xcode.sql 
   
6、xmfcn-spring-cloud-sys-service 作为日志基础服务，提供包括日志、缓存、队列、字典等系统相关的基础服务，不对外开放，只对微服务体系内开放。
  所需数据库文件在xmfcn-spring-cloud/db/job-admin/xcode.sql 
  
7、xmfcn-spring-cloud-job-core 是xxl-job的基础核心工具类工程，修改工程名称和包路径是为了整体统一，内部实现还是基于xxl-job

8、xmfcn-spring-cloud-job-admin 作为任务调度中心，也是由xxl-job-admin 演化而来，原版是MVC项目，为了项目统一架构，全部改成了微服务系统。
   启动所需要的数据库文件在父项目的db文件夹 xmfcn-spring-cloud/db/job-admin/xmfjob.sql启动之后经过zuul访问的路径是http://localhost:8085/jobadmin
   
9、xmfcn-spring-cloud-job-handler 作为任务执行器，执行具体的任务，比如定时任务等等。

10、欢迎留言讨论：https://github.com/airufei/xmfcn-spring-cloud/issues/1