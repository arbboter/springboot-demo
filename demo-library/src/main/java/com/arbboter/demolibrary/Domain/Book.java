package com.arbboter.demolibrary.Domain;

import javax.persistence.*;

@Entity
@Table(name = "library_book")
public class Book{
    /**
     * ID，唯一主键，按Alt+Enter可以快速导入引入
     */
    @Id
    @GeneratedValue // (strategy = GenerationType.IDENTITY)
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
