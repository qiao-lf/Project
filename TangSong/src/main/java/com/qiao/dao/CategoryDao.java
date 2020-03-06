package com.qiao.dao;

import com.qiao.entity.Category;

import java.util.List;

public interface CategoryDao {

    List<Category> findAll();
    Category findById(String id);
}
