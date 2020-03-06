package com.qiao.dao;

import com.qiao.entity.Poem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PoemDao {

    public List<Poem> findAll();
    public void add(Poem poem);
    public void update(Poem poem);
    public void delete(String id);
    //分页展示所有
    public List<Poem> findByPage(@Param("start")Integer start,
                                 @Param("size")Integer size);
    public Integer findCounts();
    //模糊查询
    public Integer findMHC(@Param("searchField") String searchField,
                           @Param("searchString") String searchString,
                           @Param("searchOper") String searchOper);
    public List<Poem> findMH(@Param("searchField") String searchField,
                             @Param("searchString") String searchString,
                             @Param("searchOper") String searchOper,
                             @Param("start") Integer page,
                             @Param("size") Integer size);

}
