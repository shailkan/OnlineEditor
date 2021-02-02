package com.dao;

import java.util.ArrayList;

import com.model.Category;

public interface CategoryDao {
    ArrayList<Category> getCategories();

    void addNewCategory(String newCategory);

    Category getCategory(int catId);
}
