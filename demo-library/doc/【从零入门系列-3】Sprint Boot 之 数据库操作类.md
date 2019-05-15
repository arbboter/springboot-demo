# 【从零入门系列-3】Sprint Boot 之 数据库操作类

## 文章系列

* [【从零入门系列-0】Sprint Boot 之 Hello World](https://segmentfault.com/a/1190000019137607)

* [【从零入门系列-1】Sprint Boot 之 程序结构设计说明](https://segmentfault.com/a/1190000019165644)

* [【从零入门系列-2】Sprint Boot 之 数据库实体类](https://segmentfault.com/a/1190000019174796)

---

## 前言

前一章简述了如何设计实现数据库实体类，本篇文章在此基础上进行开发，完成对该数据库表的常用操作，主要包括使用`Spring Data JPA`进行简单的增删改查和复杂查询操作。

`Spring Data JPA`是`Spring`提供的一套简化JPA开发的框架，按照约定好的【方法命名规则】写dao层接口，就可以在不写接口实现的情况下，实现对数据库的访问和操作，同时提供了很多除了CRUD之外的功能，如分页、排序、复杂查询等等，Spring Data JPA 可以理解为 JPA 规范的再次封装抽象，底层还是使用了 Hibernate 的 JPA 技术实现。通过引入`Spring Data JPA`后，我们可以基本不用写代码就能实现对数据库的增删改查操作。

此外，由于`Spring Data JPA`自带实现了很多内置的后台操作方法，因此在调用方法时必须根据其规范使用，深刻理解`规范`和`约定`。

---

## 表的基本操作实现（CRUD）

在这里，先介绍一下`JpaRepository`，这是类型为`interface`的一组接口规范，是基于JPA的Repository接口，能够极大地减少访问数据库的代码编写，是实现Spring Data JPA技术访问数据库的关键接口。

* 编写数据操作接口

在使用时，我们只需要定义一个继承该接口类型的接口即可实现对表的基本操作方法，在此我们需要对实体类`Book`进行操作，因此在`Dao`目录上右键`New->Java Class`，然后设置名称为`BookJpaRepository`,kind类型选`Interface`即可，然后添加注解及继承自`JpaRepository`，文件`BookJpaRepository.java`内容如下所示:

```java
package com.arbboter.demolibrary.Dao;

import com.arbboter.demolibrary.Domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookJpaRepository extends JpaRepository<Book, Integer> {
}
```

`@Repository`持久层组件，用于标注数据访问组件，即DAO组件，此时配合上上一篇文章中的`JPA`配置，我们就可以进行增删改查啦，不用添加任何其他代码，因为`JpaRepository`已经帮我们实现好了。

* 编写测试用例代码

打开框架自动生成的测试代码文件`DemoLibraryApplicationTests.java`编写测试用例，测试增删改查效果，测试代码如下：

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoLibraryApplicationTests {
    /**
     * @Autowired 注释，它可以对类成员变量、方法及构造函数进行标注，完成自动装配的工作。 
     * 通过 @Autowired的使用来消除 set ，get方法，简化程序代码
     * 此处自动装配我们实现的BookJpaRepository接口，然后可以直接使用bookJpaRepository操作数据库
     * 如果不加@Autowired，直接使用bookJpaRepository，程序运行会抛出异常
     */
    @Autowired
    private BookJpaRepository bookJpaRepository;

    @Test
    public void contextLoads() {
        Book book = new Book();

        // 增
        book.setName("Spring Boot 入门学习实践");
        book.setAuthor("arbboter");
        book.setImage("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2656353677,2997395625&fm=26&gp=0.jpg");
        bookJpaRepository.save(book);
        System.out.println("保存数据成功:" + book);

        // 查
        book = bookJpaRepository.findById(book.getId()).get();
        System.out.println("新增后根据ID查询结果:" + book);

        // 修改
        book.setName("Spring Boot 入门学习实践（修改版）");
        bookJpaRepository.save(book);
        System.out.println("修改后根据ID查询结果:" + book);

        // 删除
        bookJpaRepository.deleteById(book.getId());
    }
}
```

注意在测试代码用需要对属性`bookJpaRepository`使用`@Autowired `自动注入实现初始化，`@Autowired `注解，它可以对类成员变量、方法及构造函数进行标注，完成自动装配的工作。

* 测试结果

![1557817269112](C:\Users\Tony\AppData\Roaming\Typora\typora-user-images\1557817269112.png)
通过测试结果我们可以看到，程序已经能够对表数据进行增删改查，且我们通过删除SQL可以观察到，获取生成记录ID的SQL语句为:

```sql
Hibernate: select next_val as id_val from hibernate_sequence with (updlock, holdlock, rowlock)
Hibernate: update hibernate_sequence set next_val= ? where next_val=?
```

因此可推断出，`JpaRepository`对默认的自增ID均使用表`hibernate_sequence`作为ID生成器，所有默认的ID表公用此ID生成器。

通过上述例子，我们可以发现，虽然我们没有写任何一条SQL语句，但是程序已经可以正常操作数据库了，这对苦逼的C++程序员手写SQL来说真是不要说太幸福哈。不过上述示例也存在一些问题，数据查询均是通过ID操作的，但是实际使用中，数据查询还需要根据其他条件，比如`书名`或`作者`，是不是需要手写SQL实现？答案是否定的，`JpaRepository`支持接口规范方法名查询，意思是如果在接口中定义的查询方法符合它的命名规则，就可以不用写实现，框架自动提供实现的方法，只需要声明无需自己实现即可使用。

---



## JpaRepository的规范方法名查询

在我们实现的接口中，可以只定义查询方法，如果是符合规范的，可以不用写实现，就可以直接使用。

JpaRepository会对方法名进行校验，不符合规范会报错，除非添加@Query注解。



在本示例中，我们希望通过书名和作者的常用场景提供查询方案，可按下述实现：

```java

@Repository
public interface BookJpaRepository extends JpaRepository<Book, Integer> {
    /**
     * 根据书名精准查询书籍列表
     * @param name 查询的书名
     * @return 名字为name的书籍列表
     */
    List<Book> findByName(String name);

    /**
     *
     * 根据书名模糊查询书籍列表
     * @param name 查询的书名
     * @return 查询结果
     */
    List<Book> findByNameLike(String name);

    /**
     * 根据书名和作者查询，注意参数列表顺序和名字顺序保持一致（约定!）
     * @param name 查询的书名
     * @param author 查询的作者名
     * @return 查询结果
     */
    List<Book> findByNameAndAuthor(String name, String author);

    /**
     * 根据书名或作者查询，注意参数列表顺序和名字顺序保持一致（约定!）
     * @param name 查询的书名
     * @param author 查询的作者名
     * @return 查询结果
     */
    List<Book> findByNameOrAuthor(String name, String author);

    /**
     * 根据作者集合查询
     * @param authors 书列表名
     * @return
     */
    List<Book> findByAuthorIn(Collection authors);
}
```

上述代码通，我们实现了模糊、精准、And和Or以及In的查询定义，都是根据JPA的命名规范定义方法，此时我们不用自己去实现方法，直接可以调用。



测试代码如下：

```java
// 模拟数据
for (int i=0; i<20; i++){
    Book b = new Book();
    b.setName("书名_" + i);
    b.setAuthor("作者_" + i%5);
    b.setImage("img" + i);
    bookJpaRepository.save(b);
}

List<Book> bookList;
// 根据书名精准查询
bookList = bookJpaRepository.findByName("书名_2");
System.out.println("根据书名精准查询:" + bookList);

// 根据书名模糊查询
bookList = bookJpaRepository.findByNameLike("书名_2");
System.out.println("根据书名模糊查询:" + bookList);

// 根据书名和作者名查询
bookList = bookJpaRepository.findByNameAndAuthor("书名_2", "作者_2");
System.out.println("根据书名和作者名查询:" + bookList);

// 根据书名或作者名查询
bookList = bookJpaRepository.findByNameOrAuthor("书名_2", "作者_2");
System.out.println("根据书名或作者名查询:" + bookList);

// 根据作者名集合查询
Collection c = new ArrayList();
c.add("作者_1");
c.add("作者_3");
bookList = bookJpaRepository.findByAuthorIn(c);
System.out.println("根据作者名集合查询:" + bookList);
```



运行结果为：

```cpp
Hibernate: select book0_.id as id1_0_, book0_.author as author2_0_, book0_.image as image3_0_, book0_.name as name4_0_ from library_book book0_ where book0_.name=?
根据书名精准查询:[Book{id=9, name='书名_2', author='作者_2', image='img2'}]
Hibernate: select book0_.id as id1_0_, book0_.author as author2_0_, book0_.image as image3_0_, book0_.name as name4_0_ from library_book book0_ where book0_.name like ? escape ?
根据书名模糊查询:[Book{id=9, name='书名_2', author='作者_2', image='img2'}]
Hibernate: select book0_.id as id1_0_, book0_.author as author2_0_, book0_.image as image3_0_, book0_.name as name4_0_ from library_book book0_ where book0_.name=? and book0_.author=?
根据书名和作者名查询:[Book{id=9, name='书名_2', author='作者_2', image='img2'}]
Hibernate: select book0_.id as id1_0_, book0_.author as author2_0_, book0_.image as image3_0_, book0_.name as name4_0_ from library_book book0_ where book0_.name=? or book0_.author=?
根据书名或作者名查询:[Book{id=9, name='书名_2', author='作者_2', image='img2'}, Book{id=14, name='书名_7', author='作者_2', image='img7'}, Book{id=19, name='书名_12', author='作者_2', image='img12'}, Book{id=24, name='书名_17', author='作者_2', image='img17'}]
Hibernate: select book0_.id as id1_0_, book0_.author as author2_0_, book0_.image as image3_0_, book0_.name as name4_0_ from library_book book0_ where book0_.author in (? , ?)
根据作者名集合查询:[Book{id=8, name='书名_1', author='作者_1', image='img1'}, Book{id=10, name='书名_3', author='作者_3', image='img3'}, Book{id=13, name='书名_6', author='作者_1', image='img6'}, Book{id=15, name='书名_8', author='作者_3', image='img8'}, Book{id=18, name='书名_11', author='作者_1', image='img11'}, Book{id=20, name='书名_13', author='作者_3', image='img13'}, Book{id=23, name='书名_16', author='作者_1', image='img16'}, Book{id=25, name='书名_18', author='作者_3', image='img18'}]

```

`JpaRepository`规范方法名查询规约说明：`JpaRepository`框架在进行方法名解析时，会先把方法名多余的前缀截取掉，比如 find、findBy、read、readBy、get、getBy，然后对剩下部分进行解析。

- 方法关键字必须遵循完全的驼峰形式，因为JPA的方法名称解析引擎算法是通过驼峰来解析的
- 下划线可以被用来中断解析算法的语义，但是它是一个保留字，不建议使用
- In和NotIn也可以将Collection的任何子类作为参数以及数组或可变参数。



## JpaRepository的复杂查询

在我们的图书管理系统中要提供查询功能，可以根据书籍ID、书名或者作者中的三个任意组合查询，且支持查询结果自定义分页和排序，这样的话，使用`JpaRepository`规范方法名查询可能就变得很复杂了，由于组合后方案很多，不可能每种方案区分对待，此时应该提供一种通用可自适应的方法来实现，具备动态构建相应的查询语句的能力。

`Sppring Boot JPA`通过`JpaSpecificationExecutor`提供复杂查询的能力，继承该接口后，重写接口`Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb);`来实现自定义的接口查询条件。

1. 继承JpaSpecificationExecutor

   ```java
   @Repository
   public interface BookJpaRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor {
       /*内容省略*/
   }
   ```

   在原有的`BookJpaRepository`补充继承`JpaSpecificationExecutor`即可。

   

2. 创建Service接口

   由于需要自实现`toPredicate`方法，所以这里把搜索查询功能实现放到Service层，在Service目录上右键`New->Java Class`创建`BookService.java`文件：

   ```java
   @Service
   public class BookService {
       @Autowired
       BookJpaRepository bookJpaRepository;
       /**
        * 搜索查询接口
        * @param para: 键值对包含name,id,author,pageSize,pageNumber,ordName,ordDir
        * @return
        */
       Page<Book> search(Map<String, String> para){
           return null;
       }
   }
   ```
   `@Service`注解该类为服务类。
   
   
   
3. 重写`toPredicate`方法

   ```java
   // 构造查询条件
   Specification<Book> specification = new Specification<Book>() {
       @Override
       public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
           List<Predicate> predicate = new ArrayList<>();
   
           // 根据支持参数列表获取查询参数
           String matchMode = para.getOrDefault("matchMode", "AND");
           List<String> bookFields =  Arrays.asList("id", "name", "author", "image");
           for (String p : bookFields){
               String buf = para.get(p);
               if(buf != null){
                   if(matchMode == "LIKE") {
                       predicate.add(cb.like(root.get(p).as(String.class), "%" + buf + "%"));
                   } else {
                       predicate.add(cb.equal(root.get(p).as(String.class), buf));
                   }
               }
           }
           Predicate[] pre = new Predicate[predicate.size()];
           return query.where(predicate.toArray(pre)).getRestriction();
       }
   };
   ```
   该方法返回的`Predicate`即为查询条件。
   
   


4. 分页排序及查询

   ```java
   // 分页排序
   Integer pageNumber = para.get("pageNumber") == null ? 0:Integer.valueOf(para.get("pageNumber"));
   Integer pageSize = para.get("pageSize") == null ? 10:Integer.valueOf(para.get("pageSize"));
   Sort.Direction sortDir = para.getOrDefault("sortDir", "DESC") == "DESC" ? Sort.Direction.DESC : Sort.Direction.ASC;
   String ordName = para.getOrDefault("ordName", "id");
   Pageable pageable = PageRequest.of(pageNumber, pageSize, sortDir, ordName);
   
   return bookJpaRepository.findAll(specification, pageable);
   ```

   最后`bookJpaRepository.findAll(specification, pageable)`返回的结果即为查询结果

   

5. 完整的搜索方法

   ```java
   @Service
   public class BookService {
       @Autowired
       private BookJpaRepository bookJpaRepository;
       /**
        * 搜索查询接口
        * 默认值:pageSize-10 pageNumber-0 ordName-id sortDir-ASC matchMode-EQUAL
        * @param para: 键值对包含name,id,author,pageSize,pageNumber,ordName,sortDir,matchMode
        * @return
        */
       public Page<Book> search(Map<String, String> para){
           // 构造查询条件
           Specification<Book> specification = new Specification<Book>() {
               @Override
               public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                   List<Predicate> predicate = new ArrayList<>();
   
                   // 根据支持参数列表获取查询参数
                   String matchMode = para.getOrDefault("matchMode", "AND");
                   List<String> bookFields =  Arrays.asList("id", "name", "author", "image");
                   for (String p : bookFields){
                       String buf = para.get(p);
                       if(buf != null){
                           if(matchMode == "LIKE") {
                               predicate.add(cb.like(root.get(p).as(String.class), "%" + buf + "%"));
                           } else {
                               predicate.add(cb.equal(root.get(p).as(String.class), buf));
                           }
                       }
                   }
                   Predicate[] pre = new Predicate[predicate.size()];
                   return query.where(predicate.toArray(pre)).getRestriction();
               }
           };
   
           // 分页排序
           Integer pageNumber = para.get("pageNumber") == null ? 0:Integer.valueOf(para.get("pageNumber"));
           Integer pageSize = para.get("pageSize") == null ? 10:Integer.valueOf(para.get("pageSize"));
           Sort.Direction sortDir = para.getOrDefault("sortDir", "DESC") == "DESC" ? Sort.Direction.DESC : Sort.Direction.ASC;
           String ordName = para.getOrDefault("ordName", "id");
           Pageable pageable = PageRequest.of(pageNumber, pageSize, sortDir, ordName);
   
           return bookJpaRepository.findAll(specification, pageable);
       }
   }
   ```

6. 测试代码

   ```java
   @Autowired
   private BookService bookService;
   
   @Test
   public void search(){
       Map<String, String> para = new HashMap<>();
       Page<Book> books = bookService.search(para);
       System.out.println("分页3-1降序查询：" + books.getTotalElements() + ",页元素数目:" + books.getNumberOfElements());
   
       para.put("sortDir", "DESC");
       para.put("pageSize", "3");
       para.put("pageNumber", "1");
       books = bookService.search(para);
       System.out.println("分页3-1降序查询：" + books.getTotalElements() + ",页元素数目:" + books.getNumberOfElements());
   
       para.put("author", "作者_2");
       para.put("pageNumber", "0");
       books = bookService.search(para);
       System.out.println("作者名为[作者_2]查询结果：" + books.getTotalElements() + ",页元素数目:" + books.getNumberOfElements());
   
       para.put("id", "9");
       books = bookService.search(para);
       System.out.println("作者为[作者_2] id为9的查询结果：" + books.getTotalElements() + ",页元素数目:" + books.getNumberOfElements());
   
       para.put("matchMode", "LIKE");
       books = bookService.search(para);
       System.out.println("作者为[作者_2] id为9的模糊查询结果：" + books.getTotalElements() + ",页元素数目:" + books.getNumberOfElements());
   }
   ```

   此处`BookService`对象采用`@Autowired`注解自动装配初始化，然后再测试代码中针对分页、查询模式都分别测试。

   

7. 测试执行结果

   ```log
   2019-05-14 18:44:43.422  INFO 131236 --- [           main] o.h.h.i.QueryTranslatorFactoryInitiator  : HHH000397: Using ASTQueryTranslatorFactory
   Hibernate: select TOP(?) book0_.id as id1_0_, book0_.author as author2_0_, book0_.image as image3_0_, book0_.name as name4_0_ from library_book book0_ where 1=1 order by book0_.id desc
   Hibernate: select count(book0_.id) as col_0_0_ from library_book book0_ where 1=1
   分页3-1降序查询：23,页元素数目:10
   Hibernate: WITH query AS (SELECT inner_query.*, ROW_NUMBER() OVER (ORDER BY CURRENT_TIMESTAMP) as __hibernate_row_nr__ FROM ( select TOP(?) book0_.id as id1_0_, book0_.author as author2_0_, book0_.image as image3_0_, book0_.name as name4_0_ from library_book book0_ where 1=1 order by book0_.id desc ) inner_query ) SELECT id1_0_, author2_0_, image3_0_, name4_0_ FROM query WHERE __hibernate_row_nr__ >= ? AND __hibernate_row_nr__ < ?
   Hibernate: select count(book0_.id) as col_0_0_ from library_book book0_ where 1=1
   分页3-1降序查询：23,页元素数目:3
   Hibernate: select TOP(?) book0_.id as id1_0_, book0_.author as author2_0_, book0_.image as image3_0_, book0_.name as name4_0_ from library_book book0_ where book0_.author=? order by book0_.id desc
   Hibernate: select count(book0_.id) as col_0_0_ from library_book book0_ where book0_.author=?
   作者名为[作者_2]查询结果：4,页元素数目:3
   Hibernate: select TOP(?) book0_.id as id1_0_, book0_.author as author2_0_, book0_.image as image3_0_, book0_.name as name4_0_ from library_book book0_ where cast(book0_.id as varchar(255))=? and book0_.author=? order by book0_.id desc
   作者为[作者_2] id为9的查询结果：1,页元素数目:1
   Hibernate: select TOP(?) book0_.id as id1_0_, book0_.author as author2_0_, book0_.image as image3_0_, book0_.name as name4_0_ from library_book book0_ where (cast(book0_.id as varchar(255)) like ?) and (book0_.author like ?) order by book0_.id desc
   作者为[作者_2] id为9的模糊查询结果：2,页元素数目:2
   ```
   


## 结束语

本章节篇幅较长，简单介绍了下JPA的基本增删改查功能，并进一步介绍定义了JPA的`规范方法名查询`，最后引入`JpaSpecificationExecutor`通过搜索查询接口，阐述了复杂场景下的查询搜索。



下一篇内容将整合当前方法服务，编写控制层的接口，提供WEB服务接口，请继续关注。