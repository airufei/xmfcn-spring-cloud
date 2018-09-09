# xmfcn-spring-cloud 简介
1、springcloud 微服务，基于spring-cloud Finchley.SR1。

2、使用eurka作为注册中心，提供服务发现与注册功能。

3、使用zuul作为统一网关，提供统一入口，安全校验、路由等。

4、加入xxl-job 任务调度器，负责任务调度与任务管理。

5、加入spring security，当前安全校验存在问题。

6、整体系统还在调试中，并不能直接使用！

# 工程简介

1、xmfcn-spring-cloud-common 提供基础工具类相关的，如：字符串处理、常量类、http请求、文件处理等相关工具，被其他工程引用，本身不提供服务能力。

2、xmfcn-spring-cloud-eureka 作为注册中心，提供服务发现与注册功能。

3、xmfcn-spring-cloud-zuul 作为统一网关，提供统一入口，安全校验、路由等。

4、xmfcn-spring-cloud-log-api 作为日志服务api接口，提供日志搜索等基础功能，提供个性化的对外开放能力。

5、xmfcn-spring-cloud-sys-service 作为日志基础服务，提供包括日志、缓存、队列、字典等系统相关的基础服务，不对外开放，只对微服务体系内开放。

6、xmfcn-spring-cloud-job-core 是xxl-job的基础核心工具类工程，修改工程名称和包路径是为了整体统一，内部实现还是基于xxl-job

7、xmfcn-spring-cloud-job-admin 作为任务调度中心，也是由xxl-job-admin 演化而来，原版是MVC项目，我为了项目统一架构，全部改成了微服务系统。

8、xmfcn-spring-cloud-job-handler 作为任务执行器，执行具体的任务。