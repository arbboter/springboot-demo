package com.arbboter.demolibrary.Service;

import com.arbboter.demolibrary.Dao.BookJpaRepository;
import com.arbboter.demolibrary.Domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
                String matchMode = para.getOrDefault("matchMode", "and");
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
        Sort.Direction sortDir = para.getOrDefault("sortDir", "desc") == "desc" ? Sort.Direction.DESC : Sort.Direction.ASC;
        String ordName = para.getOrDefault("ordName", "id");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortDir, ordName);

        return bookJpaRepository.findAll(specification, pageable);
    }
}
