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
