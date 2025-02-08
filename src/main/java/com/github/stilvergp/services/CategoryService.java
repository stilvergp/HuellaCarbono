package com.github.stilvergp.services;

import com.github.stilvergp.model.dao.CategoryDAO;
import com.github.stilvergp.model.entities.Category;

import java.util.List;

public class CategoryService {

    public List<Category> getCategories() {
        CategoryDAO categoryDAO = new CategoryDAO();
        return categoryDAO.getCategories();
    }
}
