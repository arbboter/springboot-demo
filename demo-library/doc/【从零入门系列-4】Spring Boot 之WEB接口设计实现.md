# 【从零入门系列-4】Sprint Boot 之 WEB接口设计实现

## 文章系列

* [【从零入门系列-0】Sprint Boot 之 Hello World](https://github.com/arbboter/springboot-demo/blob/master/demo-library/doc/Sprint%20Boot%20%E4%B9%8B%20Hello%20World.md)
* [【从零入门系列-1】Sprint Boot 之 程序结构设计说明](https://github.com/arbboter/springboot-demo/blob/master/demo-library/doc/%E3%80%90%E4%BB%8E%E9%9B%B6%E5%85%A5%E9%97%A8%E7%B3%BB%E5%88%97-1%E3%80%91Sprint%20Boot%20%E7%A8%8B%E5%BA%8F%E8%AE%BE%E8%AE%A1%E8%AF%B4%E6%98%8E.md)
* [【从零入门系列-2】Sprint Boot 之 数据库实体类](https://github.com/arbboter/springboot-demo/blob/master/demo-library/doc/%E3%80%90%E4%BB%8E%E9%9B%B6%E5%85%A5%E9%97%A8%E7%B3%BB%E5%88%97-2%E3%80%91Sprint%20Boot%20%E6%95%B0%E6%8D%AE%E5%BA%93%E8%A1%A8%E8%AE%BE%E8%AE%A1.md)
* [【从零入门系列-3】Sprint Boot 之 数据库操作类](https://github.com/arbboter/springboot-demo/blob/master/demo-library/doc/%E3%80%90%E4%BB%8E%E9%9B%B6%E5%85%A5%E9%97%A8%E7%B3%BB%E5%88%97-3%E3%80%91Sprint%20Boot%20%E4%B9%8B%20%E6%95%B0%E6%8D%AE%E5%BA%93%E6%93%8D%E4%BD%9C%E7%B1%BB.md)

---

## 前言

前一章简述了已经实现了对数据库的增删改查以及复杂查询的功能，这一步将对相应的功能方法封装成WEB接口，对外提供WEB接口服务。



---

## 控制层类设计及测试

控制层的角色是负责对访问路由到处理过程的关联映射和封装，在这里我们队`Book`新建一个控制类即可，在文件夹`Controller`上右键，`New->Java Class`新建`BookController`类。作为控制类，该类需要使用`@Controller`注解使之能够被识别为控制类对象，在这里我们使用`@RestController`，该注解包含了`@Controller`，相当于@Controller+@ResponseBody两个注解的结合，适合返回Json格式的控制器使用。

### 类定义

```java
@RestController
@RequestMapping(path = "/library")
public class BookController {
    @Autowired
    private BookJpaRepository bookJpaRepository;

    @Autowired
    private BookService bookService;
}
```

考虑到数据库的操作需要用到`BookJpaRepository`和`BookService`，这里首先声明这两个属性，并使用`@Autowired`注解自动装配。

在类上使用`@RequestMapping(path = "/library")`注解后，定义了该类的路径都是`/library`开始，可以统一接口路径，避免重复书写。




#### 新增接口

```java
/**
* 新增书籍
* @param name
* @param author
* @param image
* @return
*/
@PostMapping("/save")
public Map<String, Object> save(@RequestParam String name, @RequestParam String author, @RequestParam String image){
   Book book = new Book();
   Map<String, Object> rsp = new HashMap<>();
   book.setName(name);
   book.setAuthor(author);
   book.setImage(image);
   bookJpaRepository.save(book);
   rsp.put("data", book);
   rsp.put("code", "0");
   rsp.put("info", "成功");
   return rsp;
}
```

   使用`@PostMapping`表示接口只接受POST请求，WEB接口路径为`/library/save`，该接口返回的是一个Map类型对象，但是由于类使用`@RestController`注解后，使得返回结果会自动转换成Json字符串格式。

   接口参数`@RequestParam`的注解用于将指定的请求参数赋值给方法中的形参，默认根据参数名匹配，也可以使用`value`指定参数名，支持的参数如下：

   - name：形参绑定的请求参数名，与`value`功能一样，默认与形参名相同自动关联
   - required：指定该参数是否必输，默认为True
   - defaultValue：指定该参数的默认值
   - value：与`name`功能相同

   在该接口中，通过形参自动绑定取的入参，然后通过`BookJpaRepository`直接`save`保存新增数据，`save`新增后，该记录自动生成的`id`值已经被设置到`book`变量。

   为了接口通用，返回值增加了字段`code`和`info`分别用来返回错误码和错误信息，返回数据放在字段`data`。

   

   **单元测试代码**

   ```java
   @Autowired
   private WebApplicationContext wac;
   
   private MockMvc mockMvc;
   
   @Before
   public void setUp (){
       mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
   }
   ```

   引入Web的MVC单元测试对象，然后编写Web新增接口测试用例：

   ```java
   @Test
   public void webApi(){
       try {
           String urlRoot = "/library";
           String urlApi = urlRoot + "/view/1";
           MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(urlApi)
                   .contentType(MediaType.APPLICATION_JSON_UTF8))
                   .andExpect(MockMvcResultMatchers.status().isOk())
                   .andReturn();
           System.out.println("WEB测试返回[" + urlApi + "]:" + mvcResult.getResponse().getContentAsString());
   
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   ```

   执行结果：

   ![1557907574221](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SprintBoot/20190515-WEB接口设计实现/1557907574221.png)

   

#### 删除接口

   根据数据ID删除书籍，且数据id作为请求路径的一部分，不通过`@RequestParam`获取，而是通过`@PathVariable("id")`，代码如下：

   ```java
   /**
    * 删除书籍
    * @param id
    * @return
    */
   @GetMapping("/remove/{id}")
   public Map<String, Object> removeById(@PathVariable("id") Integer id){
       Map<String, Object> rsp = new HashMap<>();
       Optional<Book> book = bookJpaRepository.findById(id);
       if(!book.isPresent()) {
           rsp.put("code", 1001);
           rsp.put("info", "书籍ID[" + id + "]不存在");
       }else {
           bookJpaRepository.deleteById(id);
           rsp.put("code", 0);
           rsp.put("info", "书籍ID[" + id + "]删除成功");
           rsp.put("data", book);
       }
       return rsp;
   }
   ```

   `@PathVariable`只支持一个属性value，类型是为String，代表绑定的属性名称，默认绑定为同名的形参。

   在接口中，我们使用`@GetMapping`接收处理GET请求，如果成功返回书籍信息，否则返回错误信息。

   

   使用浏览器测试结果如下：

   ![1557907999084](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SprintBoot/20190515-WEB接口设计实现/1557907999084.png)
   删除不存在的书籍时

   ![1557908059948](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SprintBoot/20190515-WEB接口设计实现/1557908059948.png)
   正常删除数据

   

#### 更新接口

根据书籍ID更新书籍信息，参数信息使用`HttpServletRequest`和路径参数相配合

```java
/**
 * 更新书籍
 * @param id
 * @param request
 * @return
 */
@PostMapping("/edit/{id}")
public Map<String, Object> updateById(@PathVariable("id") Integer id, HttpServletRequest request){
    Map<String, Object> rsp = new HashMap<>();
    Optional<Book> book = bookJpaRepository.findById(id);
    if(!book.isPresent()) {
        rsp.put("code", 1001);
        rsp.put("info", "书籍ID[" + id + "]不存在");
    }else {
        Book bookUpd = book.get();
        if(request.getParameter("name") != null){
            bookUpd.setName(request.getParameter("name"));
        }
        if(request.getParameter("author") != null){
            bookUpd.setAuthor(request.getParameter("author"));
        }
        if(request.getParameter("image") != null){
            bookUpd.setImage(request.getParameter("image"));
        }
        rsp.put("code", 0);
        rsp.put("info", "书籍ID[" + id + "]更新成功");
        rsp.put("data", bookUpd);
    }
    return rsp;
}
```

`HttpServletRequest`对象代表客户端的请求，当客户端通过HTTP协议访问服务器时，HTTP请求头中的所有信息都封装在这个对象中，通过这个对象提供的方法，可以获得客户端请求的所有信息。



单元测试用例：

```java
@Test
public void webBookEdit() throws Exception {
    String url = "/library/edit/2";
    // 只修改名字
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url)
            .param("name", "webBookEdit1")
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
    System.out.println("1-WEB测试返回[" + url + "]:" + mvcResult.getResponse().getContentAsString());

    // 修改名字和作者
    mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url)
            .param("name", "webBookEdit2")
            .param("author", "webBookEdit2")
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
    System.out.println("2-WEB测试返回[" + url + "]:" + mvcResult.getResponse().getContentAsString());
}
```

执行结果（JSON格式化处理过）

```json
Hibernate: select book0_.id as id1_0_0_, book0_.author as author2_0_0_, book0_.image as image3_0_0_, book0_.name as name4_0_0_ from library_book book0_ where book0_.id=?
1-WEB测试返回[/library/edit / 2]: 
{
	"code": 0,
	"data": {
		"id": 2,
		"name": "webBookEdit1",
		"author": "arbboter",
		"image": "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2656353677,2997395625&fm=26&gp=0.jpg"
	},
	"info": "书籍ID[2]更新成功"
}
Hibernate: select book0_.id as id1_0_0_, book0_.author as author2_0_0_, book0_.image as image3_0_0_, book0_.name as name4_0_0_ from library_book book0_ where book0_.id=?
2-WEB测试返回[/library/edit/2]:
{
	"code": 0,
	"data": {
		"id": 2,
		"name": "webBookEdit2",
		"author": "webBookEdit2",
		"image": "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2656353677,2997395625&fm=26&gp=0.jpg"
	},
	"info": "书籍ID[2]更新成功"
}
```

从上述执行结果我们可以看到，在使用`JpaRepository`的`save`更新数据时，只会更新非`null`字段，且返回结果包括完整的更新后的数据内容，即默认支持按设定的字段更新，而不是每次需要全字段更新。



#### 查询接口

使用路径参数根据书籍ID获取书籍内容信息，代码如下：

```java
/**
 * 查看书籍
 * @param id
 * @return
 */
@GetMapping("/view/{id}")
public Map<String, Object> findById(@PathVariable("id") Integer id){
    Map<String, Object> rsp = new HashMap<>();
    Optional<Book> book = bookJpaRepository.findById(id);
    if(!book.isPresent()) {
        rsp.put("code", 1001);
        rsp.put("info", "书籍ID[" + id + "]不存在");
    }else {
        rsp.put("code", 0);
        rsp.put("info", "成功");
        rsp.put("data", book);
    }
    return rsp;
}
```

测试执行结果如下：

![1557909406789](https://raw.githubusercontent.com/arbboter/resource/master/segmentfault/image/SprintBoot/20190515-WEB接口设计实现/1557909406789.png)



#### 搜索接口

由于我们之前的搜索接口的入参类型为`Map`，但是Web接口的入参信息都是从`HttpServletRequest`获取，因此首先需要将需要的入参信息从`HttpServletRequest`转换到`Map`类型，然再使用。考虑到该转换功能为通用型，因此可以将该函数封装到系统的工具包下面，新建`Util`包，然后右键新建`Util`文件，完成数据的转换函数，代码如下：

```java
public class Util {
    /**
     * 把 @HttpServletRequest 转换成普通的字典
     * @param request
     * @return
     */
    public static Map getParameterMap(HttpServletRequest request) {
        // 参数Map
        Map properties = request.getParameterMap();
        // 返回值Map
        Map returnMap = new HashMap();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if(null == valueObj){
                value = "";
            }else if(valueObj instanceof String[]){
                String[] values = (String[])valueObj;
                for(int i=0;i<values.length;i++){
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length()-1);
            }else{
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }
}
```
然后使用我们`BookService`实现的封装的复杂查询接口即可，代码如下：

```java
/**
 * 搜索查询接口
 * @param request
 * @return
 */
@PostMapping("/search")
public Map<String, Object> search(HttpServletRequest request){
    Map<String, String> map = new HashMap<>();
    map = Util.getParameterMap(request);
    Page<Book> books = bookService.search(map);

    Map<String, Object> rsp = new HashMap<>();
    rsp.put("code", 0);
    rsp.put("info", "成功");
    rsp.put("rows", books.getContent());
    rsp.put("total", books.getTotalElements());
    return rsp;
}
```

此处返回`rows`和`total`是为了后续Web页面的`bootstrap-table`需要，该控件根据这两个数据以表格化的形式展示查询结果数据。

由于此处使用POST请求类型，测试时依旧使用`MockMvc`和`WebApplicationContext`，测试代码如下：

```java
@Test
public void webSearch() throws Exception{
    String url = "/library/search";
    // 1-无条件
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
    System.out.println("1-无条件-WEB测试返回[" + url + "]:" + mvcResult.getResponse().getContentAsString());

    // 2-根据作者名查询
    mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url)
            .param("author", "作者_3")
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
    System.out.println("2-根据作者名(作者_3)查询-WEB测试返回[" + url + "]:" + mvcResult.getResponse().getContentAsString());
}
```

测试执行结果如下:

```sql
Hibernate: select TOP(?) book0_.id as id1_0_, book0_.author as author2_0_, book0_.image as image3_0_, book0_.name as name4_0_ from library_book book0_ where 1=1 order by book0_.id desc
Hibernate: select count(book0_.id) as col_0_0_ from library_book book0_ where 1=1
1-无条件-WEB测试返回[/library/search]:
{
	"total": 22,
	"code": 0,
	"rows": [{
			"id": 26,
			"name": "书名_19",
			"author": "作者_4",
			"image": "img19"
		}, {
			"id": 25,
			"name": "书名_18",
			"author": "作者_3",
			"image": "img18"
		}, {
			"id": 24,
			"name": "书名_17",
			"author": "作者_2",
			"image": "img17"
		}, {
			"id": 23,
			"name": "书名_16",
			"author": "作者_1",
			"image": "img16"
		}, {
			"id": 22,
			"name": "书名_15",
			"author": "作者_0",
			"image": "img15"
		}, {
			"id": 21,
			"name": "书名_14",
			"author": "作者_4",
			"image": "img14"
		}, {
			"id": 20,
			"name": "书名_13",
			"author": "作者_3",
			"image": "img13"
		}, {
			"id": 19,
			"name": "书名_12",
			"author": "作者_2",
			"image": "img12"
		}, {
			"id": 18,
			"name": "书名_11",
			"author": "作者_1",
			"image": "img11"
		}, {
			"id": 17,
			"name": "书名_10",
			"author": "作者_0",
			"image": "img10"
		}
	],
	"info": "成功"
}
Hibernate: select TOP(?) book0_.id as id1_0_, book0_.author as author2_0_, book0_.image as image3_0_, book0_.name as name4_0_ from library_book book0_ where book0_.author=? order by book0_.id desc
2-根据作者名(作者_3)查询-WEB测试返回[/library/search]:
{
	"total": 4,
	"code": 0,
	"rows": [{
			"id": 25,
			"name": "书名_18",
			"author": "作者_3",
			"image": "img18"
		}, {
			"id": 20,
			"name": "书名_13",
			"author": "作者_3",
			"image": "img13"
		}, {
			"id": 15,
			"name": "书名_8",
			"author": "作者_3",
			"image": "img8"
		}, {
			"id": 10,
			"name": "书名_3",
			"author": "作者_3",
			"image": "img3"
		}
	],
	"info": "成功"
}
```




## 结束语

到这里，整个项目的所有服务器后端部分已经完成，已经可以提供给前端使用各种常用的Web接口，下一篇我们将从前端一起整合整个项目，实现数据的展示和管理，敬请期待。



[下一篇]()