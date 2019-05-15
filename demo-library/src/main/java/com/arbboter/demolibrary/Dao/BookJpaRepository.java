package com.arbboter.demolibrary.Dao;

import com.arbboter.demolibrary.Domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BookJpaRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor {
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
