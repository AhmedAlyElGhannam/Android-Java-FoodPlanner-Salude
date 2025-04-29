package com.example.salude.model.remote.retrofit.callback;

import com.example.salude.model.pojo.*;

import java.util.List;

public interface RemoteRetrofitCallback {
    public interface RemoteRetrofitAreaCallback {
        public void onSuccess(List<Area> areas);
        public void onFailure(String err);
    }

    public interface RemoteRetrofitCategoryCallback {
        public void onSuccess(List<Category> categories);
        public void onFailure(String err);
    }

    public interface RemoteRetrofitFilteredMealCallback {
        public void onSuccess(List<FilteredMeal> filteredMeals);
        public void onFailure(String err);
    }

    public interface RemoteRetrofitIngredientCallback {
        public void onSuccess(List<Ingredient> ingredients);
        public void onFailure(String err);
    }

    public interface RemoteRetrofitMealCallback {
        public void onSuccess(List<Meal> meals);
        public void onFailure(String err);
    }

}
