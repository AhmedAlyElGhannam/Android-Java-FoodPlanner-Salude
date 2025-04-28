package com.example.salude.model.remote.retrofit.response;

import com.example.salude.model.pojo.Category;
import com.example.salude.model.pojo.FilteredMeal;

import java.util.List;

public class CategoryResponse {
    private List<Category> categories;

    public List<Category> getCategories() {
        return categories;
    }
}
