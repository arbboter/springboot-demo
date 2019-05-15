package com.arbboter.demolibrary.Controller;

import com.arbboter.demolibrary.Dao.BookJpaRepository;
import com.arbboter.demolibrary.Domain.Book;
import com.arbboter.demolibrary.Service.BookService;
import com.arbboter.demolibrary.Util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/library")
public class BookController {
    @Autowired
    private BookJpaRepository bookJpaRepository;

    @Autowired
    private BookService bookService;

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
        rsp.put("code", 0);
        rsp.put("info", "成功");
        return rsp;
    }

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
}
