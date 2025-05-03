package com.example.salude.model.remote.retrofit.response;

import com.example.salude.model.pojo.Ingredient;

import java.util.List;

public class IngredientResponse {
    private List<Ingredient> meals;

    public List<Ingredient> getIngredients() {
        return meals;
    }
}
