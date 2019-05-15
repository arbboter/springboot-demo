# 【从零入门系列-2】Sprint Boot 之 数据库实体类

## 文章系列

* [【从零入门系列-0】Sprint Boot 之 Hello World](https://segmentfault.com/a/1190000019137607)
* [【从零入门系列-1】Sprint Boot 之 程序结构设计说明](https://segmentfault.com/a/1190000019165644)

---

## 前言

本篇文章开始代码实践，系统设计从底向上展开，因此本篇先介绍如何实现数据库表实体类的设计实现。



SpringBoot数据库的持久层框架主要分为两种架构模式，即以`JDBC Template`为代表的SQL类和以`Spring Data JPA`为代表的ORM对象类。其中：

> Spring Data JPA 是 Spring 基于 ORM 框架、JPA 规范的基础上封装的一套JPA应用框架，可使开发者用极简的代码即可实现对数据的访问和操作。它提供了包括增删改查等在内的常用功能，且易于扩展！学习并使用 Spring Data JPA 可以极大提高开发效率！spring data jpa让我们解脱了DAO层的操作，基本上所有CRUD都可以依赖于它来实现，自己写个仓储接口后继承JpaRepository即可实现最基本的增删改查功能！



在使用`@Entity`进行实体类的持久化操作，当`JPA`检测到我们的实体类当中有@Entity 注解的时候，会在数据库中生成关联映射对应的表结构信息，因此针对本项目情况最底层的设计实现一个`@Entity`注解的书籍对象定义即可。



项目开始前，先按上一篇文章[【从零入门系列-1】Sprint Boot 之 程序结构设计说明](https://segmentfault.com/a/1190000019165644)后台程序结构建立相对应的目录：

```
控制层：前端路由和后端处理关系处理，目录：Controller
数据服务层：自定义对数据库的访问操作方法，目录：Service
数据访问层：实现通用的数据库访问功能，SpringData的JPA方案，目录：Dao
数据实体层：定义数据库表的属性方法，目录：Domain
```

根据结构，我们需要在Domain目录下编写项目表实体类，右键`Domain`文件夹，`New->Java Class`。

---



## 编写实体类

1. 新建`Book`类

   ```java
   package com.arbboter.demolibrary.Domain;
   
   import javax.persistence.Entity;
   import javax.persistence.Table;
   
   /**
    * @Entity 注解该类为数据库表实体类，JPA可自动扫描识别到
    * @Table 注解数据表信息，其中name指定表名
    */
   @Entity
   @Table(name = "library_book")
   public class Book{
   }
   ```

   注意添加的类需要使用`@Entity`注解，其中`@Table`是可选的，默认不配置生成的表名和类名相同，如果上述类中不使用`@Table`，那么本类对应的表名为`book`。

   

2. 添加表字段信息

   ```java
   public class Book{
       /**
        * ID，唯一主键，按Alt+Enter可以快速导入引入
        */
       @Id
       @GeneratedValue
       private Integer id;
   
       /**
        * 书名
        */
       private String name;
   
       /**
        * 作者
        */
       private String author;
   
       /**
        * 封面
        */
       private String image;
   }
   ```

   `@Id`注解用于声明一个实体类的属性映射为数据库的主键列，该属性通常置于属性声明语句之前，可与声明语句同行，也可写在单独行上。 

   `@GeneratedValue`用于注解主键的生成策略，通过strategy 属性指定。默认情况下，JPA 自动选择一个最适合底层数据库的主键生成策略：SqlServer对应identity，MySQL 对应 auto increment。
   
   - TABLE：使用一个特定的数据库表格来保存主键。
   - SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列。
   - IDENTITY：主键由数据库自动生成（主要是自动增长型）
   - AUTO：主键由程序控制。
   
   该注解的strategy属性默认值为`GenerationType.AUTO`。

​    

3. 利用IDEA自动添加`getter` `setter` `toString`方法

   ```java
   @Entity
   @Table(name = "library_book")
   public class Book{
       /**
        * 属性此处未列出
        */
   
       /**
        * 按Alt+Insert 或者 在此文件空白处右键选择Generate...
        * 自动生成getter和setter及toString方法
        */
       public Integer getId() {
           return id;
       }
   
       public void setId(Integer id) {
           this.id = id;
       }
   
       public String getName() {
           return name;
       }
   
       public void setName(String name) {
           this.name = name;
       }
   
       public String getAuthor() {
           return author;
       }
   
       public void setAuthor(String author) {
           this.author = author;
       }
   
       public String getImage() {
           return image;
       }
   
       public void setImage(String image) {
           this.image = image;
       }
   
       @Override
       public String toString() {
           return "Book{" +
                   "id=" + id +
                   ", name='" + name + '\'' +
                   ", author='" + author + '\'' +
                   ", image='" + image + '\'' +
                   '}';
       }
   }
   ```

   建议按`Alt+Insert` 或者 在此文件空白处右键选择`Generate...`自动生成`getter`和`setter`及`toString`方法，其中`toString`方便输出查阅。

   

4. 启动测试

   此时启动main函数发现，程序正常启动，但是查看数据库我们可以发现数据库表`library_book`并没有自动生成，因为到这里我们还没有自定义配置JPA。

   这里先配置上JPA的配置，本项目使用的SQL Server 2008数据库，在`application.properties`配置文件中新增以下配置信息：

   ```properties
   # JPA配置
   spring.jpa.database=sql_server
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.database-platform=org.hibernate.dialect.SQLServer2008Dialect
   ```
   
   建议以上配置项都设置上，我在学习过程中因没有配置`spring.jpa.database-platform`项，导致项目启动失败。配置`spring.jpa.hibernate.ddl-auto`取值含义为：
   
   - ddl-auto:create----每次运行该程序，没有表格会新建表格，表内有数据会清空
   - ddl-auto:create-drop----每次程序结束的时候会清空表
   - ddl-auto:update----每次运行程序，没有表格会新建表格，表内有数据不会清空，只会更新
   - ddl-auto:validate----运行程序会校验数据与数据库的字段类型是否相同，不同会报错
   
   新增该配置后再次启动程序，我们可以在输出栏看到类似这样的输出：
   
   ```verilog
   2019-05-14 12:09:34.977  INFO 115652 --- [           main] org.hibernate.dialect.Dialect            : HHH000400: Using dialect: org.hibernate.dialect.SQLServer2008Dialect
   Hibernate: create table library_book (id int not null, author varchar(255), image varchar(255), name varchar(255), primary key (id))
   ```
   
   从日志中我们可以看出，程序已经自动创建了`book`的实体类了，可以在数据库中验证发现，该表已创建OK。

---

## [注解说明](<https://www.kancloud.cn/cxr17618/springboot/435779>)

- @Table - 映射表名

  @Table注解用来标识实体类与数据表的对应关系，默认和类名一致。

- @Id - 主键

- @GeneratedValue(strategy=GenerationType.IDENTITY) - 自动递增生成

- @Column(name = "dict_name",columnDefinition="varchar(100) COMMENT '字典名'") - 字段名、类型、注释

  Column注解来标识实体类中属性与数据表中字段的对应关系，其属性均为可选属性：

  - name属性定义了被标注字段在数据库表中所对应字段的名称；
  - unique属性表示该字段是否为唯一标识，默认为false。如果表中有一个字段需要唯一标识，则既可以使用该标记，也可以使用@Table标记中的@UniqueConstraint。
  - nullable属性表示该字段是否可以为null值，默认为true。如果属性里使用了验证类里的@NotNull注释，这个属性可以不写。
  - insertable属性表示在使用“INSERT”脚本插入数据时，是否需要插入该字段的值。
  - updatable属性表示在使用“UPDATE”脚本插入数据时，是否需要更新该字段的值。insertable和updatable属性一般多用于只读的属性，例如主键和外键等。这些字段的值通常是自动生成的。
  - columnDefinition属性表示创建表时，该字段创建的SQL语句，一般用于通过Entity生成表定义时使用。若不指定该属性，通常使用默认的类型建表，若此时需要自定义建表的类型时，可在该属性中设置。（也就是说，如果DB中表已经建好，该属性没有必要使用。）
  - table属性定义了包含当前字段的表名。
  - length属性表示字段的长度，当字段的类型为varchar时，该属性才有效，默认为255个字符。
  - precision属性和scale属性表示精度，当字段类型为double时，precision表示数值的总长度，scale表示小数点所占的位数。

- @UpdateTimestamp - 更新时自动更新时间

- @CreationTimestamp - 创建时自动更新时间

- @Version - 版本号，更新时自动加1

---

## 结束语

本章内容记录了数据库实体类的简单的实现方式，并通过`application.properties`配置`JPA`的数据库配置项，使得程序运行后能自动关联并操作数据库表。



下一章将介绍对数据库的增删改查（CRUD）操作相关内容，请继续关注。