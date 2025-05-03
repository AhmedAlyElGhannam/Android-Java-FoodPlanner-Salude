package com.example.salude.model.remote.retrofit.response;

import com.example.salude.model.pojo.FilteredMeal;

import java.util.List;

public class FilteredMealResponse {
    private List<FilteredMeal> meals;

    public List<FilteredMeal> getFilteredMeals() {
        return meals;
    }
}
