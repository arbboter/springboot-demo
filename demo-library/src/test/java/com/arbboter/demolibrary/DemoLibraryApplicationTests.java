package com.arbboter.demolibrary;

import com.arbboter.demolibrary.Dao.BookJpaRepository;
import com.arbboter.demolibrary.Domain.Book;
import com.arbboter.demolibrary.Service.BookService;
import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

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
    }

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp (){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void webApi(){
        try {
            String urlRoot = "/library";
            String urlApi = urlRoot + "/view/1";
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(urlApi)
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();
            System.out.println("WEB测试返回:" + urlApi + "]:" + mvcResult.getResponse().getContentAsString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
