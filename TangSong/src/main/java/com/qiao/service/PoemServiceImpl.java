package com.qiao.service;

import com.qiao.dao.PoemDao;
import com.qiao.entity.Poem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PoemServiceImpl implements PoemService {
    @Autowired
    private PoemDao poemDao;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Poem> findAll() {
        return poemDao.findAll();
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Poem> findByPage(Integer start, Integer size) {
        return poemDao.findByPage(start,size);
    }

    @Override
    public Integer findCounts() {
        return poemDao.findCounts();
    }

    @Override
    public void add(Poem poem) {
        poemDao.add(poem);
    }

    @Override
    public void update(Poem poem) {
        poemDao.update(poem);
    }

    @Override
    public void delete(String id) {
        poemDao.delete(id);
    }

    @Override
    public Integer findMHC(String searchField, String searchString, String searchOper) {
        return poemDao.findMHC(searchField,searchString,searchOper);
    }

    @Override
    public List<Poem> findMH(String searchField, String searchString, String searchOper, Integer page, Integer size) {
        return poemDao.findMH(searchField,searchString,searchOper,page,size);
    }


}
