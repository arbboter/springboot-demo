# 【从零入门系列-1】Spring Boot 程序结构设计

## 文章系列

* [【从零入门系列-0】Spring Boot 之 Hello World](https://github.com/arbboter/springboot-demo/blob/master/demo-library/doc/Spring%20Boot%20%E4%B9%8B%20Hello%20World.md)
* [【从零入门系列-1】Spring Boot 之 程序结构设计说明](https://github.com/arbboter/springboot-demo/blob/master/demo-library/doc/%E3%80%90%E4%BB%8E%E9%9B%B6%E5%85%A5%E9%97%A8%E7%B3%BB%E5%88%97-1%E3%80%91Spring%20Boot%20%E7%A8%8B%E5%BA%8F%E8%AE%BE%E8%AE%A1%E8%AF%B4%E6%98%8E.md)
* [【从零入门系列-2】Spring Boot 之 数据库实体类](https://github.com/arbboter/springboot-demo/blob/master/demo-library/doc/%E3%80%90%E4%BB%8E%E9%9B%B6%E5%85%A5%E9%97%A8%E7%B3%BB%E5%88%97-2%E3%80%91Spring%20Boot%20%E6%95%B0%E6%8D%AE%E5%BA%93%E8%A1%A8%E8%AE%BE%E8%AE%A1.md)
* [【从零入门系列-3】Spring Boot 之 数据库操作类](https://github.com/arbboter/springboot-demo/blob/master/demo-library/doc/%E3%80%90%E4%BB%8E%E9%9B%B6%E5%85%A5%E9%97%A8%E7%B3%BB%E5%88%97-3%E3%80%91Spring%20Boot%20%E4%B9%8B%20%E6%95%B0%E6%8D%AE%E5%BA%93%E6%93%8D%E4%BD%9C%E7%B1%BB.md)
* [【从零入门系列-4】Spring Boot 之 WEB接口设计实现]([https://github.com/arbboter/springboot-demo/blob/master/demo-library/doc/%E3%80%90%E4%BB%8E%E9%9B%B6%E5%85%A5%E9%97%A8%E7%B3%BB%E5%88%97-4%E3%80%91Spring%20Boot%20%E4%B9%8BWEB%E6%8E%A5%E5%8F%A3%E8%AE%BE%E8%AE%A1%E5%AE%9E%E7%8E%B0.md)
* [【从零入门系列-5】Spring Boot 之 前端展示](https://github.com/arbboter/springboot-demo/blob/master/demo-library/doc/%E3%80%90%E4%BB%8E%E9%9B%B6%E5%85%A5%E9%97%A8%E7%B3%BB%E5%88%97-5%E3%80%91Spring%20Boot%20%E4%B9%8B%20%E5%89%8D%E7%AB%AF%E5%B1%95%E7%A4%BA.md)

---

## 设计效果图

* 页面展示

  ![1557733235595](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SpringBoot/20190513-程序结构设计说明/1557733235595.png)

* 增

  ![1557733316590](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SpringBoot/20190513-程序结构设计说明/1557733316590.png)

* 删

  ![1557733470698](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SpringBoot/20190513-程序结构设计说明/1557733470698.png)

* 改

  ![1557733441965](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SpringBoot/20190513-程序结构设计说明/1557733441965.png)

* 查

  ![1557733387960](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SpringBoot/20190513-程序结构设计说明/1557733387960.png)

* 搜索

![1557733526730](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SpringBoot/20190513-程序结构设计说明/1557733526730.png)



## 页面程序结构

* 图书馆结构分布图

        1-WEB
            bootstrap
            bootstrap-table
        
        2-后台程序
            控制层：前端路由和后端处理关系处理
            数据服务层：自定义对数据库的访问操作方法
            数据访问层：实现通用的数据库访问功能，SpringData的JPA方案
            数据实体层：定义数据库表的属性方法
            
        3-数据库
            Book表：名字，作者，封面

![1557736062821](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SpringBoot/20190513-程序结构设计说明/1557736062821.png)

* 说明

  程序的主要结构比较简单，主要难点在于初次接触很多东西理解不到位，各个层次间的交互链路以及Java基础薄弱，使得中间会有磕磕碰碰，且即使完成了整个项目，也不确定其中实现方案或者编码是否不合理。

  但，先做出来再说，其他问题等以后熟练了再慢慢改进。

---

## 结束语  

本章预先提供了项目实际效果图以及项目的整体结构设计，后续文章会根据本篇章设计依次实现各个模块，请持续关注。



[下一篇](https://github.com/arbboter/springboot-demo/blob/master/demo-library/doc/%E3%80%90%E4%BB%8E%E9%9B%B6%E5%85%A5%E9%97%A8%E7%B3%BB%E5%88%97-2%E3%80%91Spring%20Boot%20%E6%95%B0%E6%8D%AE%E5%BA%93%E8%A1%A8%E8%AE%BE%E8%AE%A1.md)