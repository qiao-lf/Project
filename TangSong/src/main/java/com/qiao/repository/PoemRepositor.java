package com.qiao.repository;

import com.qiao.entity.Poem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PoemRepositor extends ElasticsearchRepository<Poem,String> {
    //书写复杂类型的
//term查询高亮
    List<Poem> findAllByPage(int page, int size);

    List<Poem> findAllByTrem(String msg,int page,int size);
}
