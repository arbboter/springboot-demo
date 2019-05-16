# 【从零入门系列-0】Spring Boot 之 Hello World

## 文章系列

* [【从零入门系列-0】Spring Boot 之 Hello World](https://github.com/arbboter/springboot-demo/blob/master/demo-library/doc/Spring%20Boot%20%E4%B9%8B%20Hello%20World.md)
* [【从零入门系列-1】Spring Boot 之 程序结构设计说明](https://github.com/arbboter/springboot-demo/blob/master/demo-library/doc/%E3%80%90%E4%BB%8E%E9%9B%B6%E5%85%A5%E9%97%A8%E7%B3%BB%E5%88%97-1%E3%80%91Spring%20Boot%20%E7%A8%8B%E5%BA%8F%E8%AE%BE%E8%AE%A1%E8%AF%B4%E6%98%8E.md)
* [【从零入门系列-2】Spring Boot 之 数据库实体类](https://github.com/arbboter/springboot-demo/blob/master/demo-library/doc/%E3%80%90%E4%BB%8E%E9%9B%B6%E5%85%A5%E9%97%A8%E7%B3%BB%E5%88%97-2%E3%80%91Spring%20Boot%20%E6%95%B0%E6%8D%AE%E5%BA%93%E8%A1%A8%E8%AE%BE%E8%AE%A1.md)
* [【从零入门系列-3】Spring Boot 之 数据库操作类](https://github.com/arbboter/springboot-demo/blob/master/demo-library/doc/%E3%80%90%E4%BB%8E%E9%9B%B6%E5%85%A5%E9%97%A8%E7%B3%BB%E5%88%97-3%E3%80%91Spring%20Boot%20%E4%B9%8B%20%E6%95%B0%E6%8D%AE%E5%BA%93%E6%93%8D%E4%BD%9C%E7%B1%BB.md)
* [【从零入门系列-4】Spring Boot 之 WEB接口设计实现]([https://github.com/arbboter/springboot-demo/blob/master/demo-library/doc/%E3%80%90%E4%BB%8E%E9%9B%B6%E5%85%A5%E9%97%A8%E7%B3%BB%E5%88%97-4%E3%80%91Spring%20Boot%20%E4%B9%8BWEB%E6%8E%A5%E5%8F%A3%E8%AE%BE%E8%AE%A1%E5%AE%9E%E7%8E%B0.md)
* [【从零入门系列-5】Spring Boot 之 前端展示](https://github.com/arbboter/springboot-demo/blob/master/demo-library/doc/%E3%80%90%E4%BB%8E%E9%9B%B6%E5%85%A5%E9%97%A8%E7%B3%BB%E5%88%97-5%E3%80%91Spring%20Boot%20%E4%B9%8B%20%E5%89%8D%E7%AB%AF%E5%B1%95%E7%A4%BA.md)



## 环境准备

* java 开发环境 JDK1.8 安装

* Maven 安装，jar自动依赖及包管理工具

* IDE编辑器：IntelliJ IDEA 2019

---
## 说明

  本项目为从零入门示例，目标为构建一个书籍增删改查管理页，力争记录一个无java基础的程序员学习笔记，不足之处请多多指教。

---
## 创建项目

1.打开`FIle->New->Project...`，选择`Spring Initializr`，然后选择`next`配置项目属性：
![1557466868361](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SpringBoot/20190510-HelloWorld/1557466868361.png)

2.设置项目组织及名称
![1557467025015](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SpringBoot/20190510-HelloWorld/1557467025015.png)

3.设置依赖

   * Web：web （内置支持Web）

   * Template Engines：Thymeleaf（Web页面模版引擎）

   * SQL：JPA（数据库的CRUD操作），SQL Server（本示例使用的数据库为Sql Server依赖)

配置为：
![1557467447980](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SpringBoot/20190510-HelloWorld/1557467447980.png)

4.然后后续的直接按`Next`到最后的`Finish`完成项目创建，项目创建完后，会自动下载依赖项
![1557468114996](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SpringBoot/20190510-HelloWorld/1557468114996.png)

---
## 项目结构说明

> Spring Boot 推荐目录结构
* 代码层的结构
　　根目录：src/main/java/com/org/project-name
   1.工程启动类(DemoLibraryApplication.java)置于包下
   2.实体类(domain)置于project-name.domain
   3.数据访问层(Dao)置于project-name.repository
   4.数据服务层(Service)置于com,project-name.service,数据服务的实现接口(serviceImpl)至于project-name.service.impl
   5.前端控制器(Controller)置于project-name.controller
   6.工具类(utils)置于project-name.utils
   7.常量接口类(constant)置于project-name.constant
   8.配置信息类(config)置于project-name.config
   9.数据传输类(vo)置于project-name.vo
* 资源文件的结构
　　根目录:src/main/resources
    1.配置文件(.properties/.json等)置于config文件夹下
    2.国际化(i18n))置于i18n文件夹下
    3.spring.xml置于META-INF/spring文件夹下
    4.页面以及js/css/image等置于static文件夹下的各自文件下
* idea默认新建项目文件说明
    * DemoLibraryApplication.java：程序`main`函数所在文件入口
    * application.properties：自动创建的程序配置文件
    * pom.xml：项目对象模型，通过xml表示maven项目，主要描述了项目：包括配置文件；开发者需要遵循的规则，缺陷管理系统，组织和licenses，项目的url，项目的依赖性，以及其他所有的项目相关因素。

---
## HelloWorld

* 为保证简单，第一步实现`Hello，World`，让项目跑起来，编辑项目生成的`main`文件即可：
![1557469632884](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SpringBoot/20190510-HelloWorld/1557469632884.png)

  代码：

  ```java
  package com.arbboter.demolibrary;
  
  import org.springframework.boot.SpringApplication;
  import org.springframework.boot.autoconfigure.SpringBootApplication;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.RestController;
  
  /**
   * @RestController 引入Web的Rest请求返回
   */
  @RestController
  @SpringBootApplication
  public class DemoLibraryApplication {
  
      /**
       * @RequestMapping("/") 设置Web访问路径及其相应处理函数
       * @return 返回Hello,World的消息内容
       */
      @RequestMapping("/")
      public String hello(){
          return "Hello,World";
      }
  
      public static void main(String[] args) {
          SpringApplication.run(DemoLibraryApplication.class, args);
      }
  
  }
  ```

* 运行程序，点击`main`函数左侧的绿色三角形，选择运行即可，或者按快捷键`Ctrl+Shift+F10`
![1557469977409](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SpringBoot/20190510-HelloWorld/1557469977409.png)

  发现程序运行后退出，查看运行输出以排查问题：
  ![1557470151195](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SpringBoot/20190510-HelloWorld/1557470151195.png)
  
  因为在新建项目时选择了`JPA`和`Sql Server`依赖，导致项目启动时自动去连接数据库，但是本项目又没有配置数据库连接信息，所以出现问题，有两个方案解决该问题：
  
  * 删除`JPA`和`Sql Server`的项目依赖（考虑到项目后续需要连接数据库，不采用该方案）
  
  * 配置数据库连接信息，在配置文件`application.properties`新增如下配置：
  
    ```
    spring.datasource.driver-class-name = com.microsoft.sqlserver.jdbc.SQLServerDriver
    spring.datasource.url = jdbc:sqlserver://127.0.0.1:1433;Databasename=dev
    spring.datasource.username = arbboter
    spring.datasource.password = arbboter
    ```
  
  重新运行项目后，发现此时项目作为服务器程序正常运行
  ![1557470503145](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SpringBoot/20190510-HelloWorld/1557470503145.png)
  
  可以查看到程序运行正常运行后，监听的端口号为`8080`，且也可从运行信息中找到连接数据库相关输出。
  
* 访问网页`http://localhost:8080`，验证程序结果
  ![1557470671225](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SpringBoot/20190510-HelloWorld/1557470671225.png)
  到这里整个示例已经跑起来了，有血有肉的框架已经准备好，后续在此基础上继续集成即可。
---
## 结束语

Spring Boot 非常智能化，为开发者提供大量的默认配置细节，因此在IDEA的帮助下可以快速完成`HelloWolrd`项目的运行，极简入门。



[下一篇](https://github.com/arbboter/springboot-demo/blob/master/demo-library/doc/%E3%80%90%E4%BB%8E%E9%9B%B6%E5%85%A5%E9%97%A8%E7%B3%BB%E5%88%97-1%E3%80%91Spring%20Boot%20%E7%A8%8B%E5%BA%8F%E8%AE%BE%E8%AE%A1%E8%AF%B4%E6%98%8E.md)

