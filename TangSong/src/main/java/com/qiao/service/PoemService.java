package com.qiao.service;

import com.qiao.entity.Poem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PoemService {

    public List<Poem> findAll();
    public List<Poem> findByPage(Integer start,Integer size);

    public Integer findCounts();
    public void add(Poem poem);
    public void update(Poem poem);
    public void delete(String id);

    public Integer findMHC(String searchField,String searchString,String searchOper);
    public List<Poem> findMH(String searchField,String searchString,String searchOper,Integer page,Integer size);

}
