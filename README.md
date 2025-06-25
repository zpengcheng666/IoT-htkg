项目介绍
-----------------------------------
HSS BOOT
当前最新版本： 1.0.0

##### 项目说明
目录结构
-----------------------------------
```
项目结构
├─hss-boot-parent（父POM： 项目依赖、modules组织）
│  ├─hss-boot-base-core（共通模块： 工具类、config、权限、查询过滤器、注解等）
│  ├─hss-module-system  System系统管理目录
│  │  ├─hss-system-impl   System系统管理权限等功能
│  │  ├─hss-system-start  System单体启动项目(8080）
|  |  |    ├─ Dockerfile   build docker image file
|  |  |    ├─ src/main/resources/application.yml  配置文件
|  |  |    ├─ src/main/java/com/hss/HssSystemApplication.java SpringBoot启动程序
│  │  ├─hss-system-api    System系统管理模块对外api
│  │  │  ├─hss-system-cloud-api   System模块对外提供的微服务接口
│  │  │  ├─hss-system-local-api   System模块对外提供的单体接口
│  ├─hss-module-scada      --组态模块
|     ├─hss-scada-impl        --组态的逻辑实现
|    
```
启动
-----------------------------------

IDE中启动项目
-----------------------------------
在hss-system-start模块下
com.hss.HssSystemApplication.java
直接启动这个启动程序

jar包启动项目
-----------------------------------
在hss-system-start模块下
```
编译项目
mvn clean install -DskipTests

执行项目
java -jar xxxx.jar
```

Docker启动项目
-----------------------------------
```


build image

arm*64架构
docker buildx build -t hss/hss-starter:v1 --platform=linux/arm64 --build-arg lib_path="arm64/arm64" .

x86*64架构
docker buildx build -t hss-starter:v1 --platform=linux/amd64 --build-arg lib_path="x86x64/x64" .

```

```
导出images
docker save -o hss.tar hss-starter:v1

导入images
docker load < hss-starter.tar

```
技术架构：
-----------------------------------
#### 开发环境

- 语言：Java 8+ (小于17)

- IDE(JAVA)： IDEA (必须安装lombok插件 )

- 依赖管理：Maven

- 缓存：Redis

- 数据库：达梦8.0

#### 后端

- 基础框架：Spring Boot 2.6.6

- 持久层框架：MybatisPlus 3.5.1

- 安全框架：Apache Shiro 1.8.0，Jwt 3.11.0

- 数据库连接池：阿里巴巴Druid 1.1.22

- 日志打印：logback

- 其他：autopoi, fastjson，poi，Swagger-ui，quartz, lombok（简化代码）等。

#### 支持库
| 数据库      | 支持 |
|------------|-----|
| 达梦        | √   |
