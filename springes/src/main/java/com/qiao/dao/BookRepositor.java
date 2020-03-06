package com.qiao.dao;

import com.qiao.entity.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Date;
import java.util.List;

public interface BookRepositor extends ElasticsearchRepository<Book,String> {

    //s胡写自定义的
    public List<Book> findByNameAndAutor(String name,String autor);
    public List<Book> findByPriceBefore(Double d);

    public List<Book> findByNameLike(String name);
}
