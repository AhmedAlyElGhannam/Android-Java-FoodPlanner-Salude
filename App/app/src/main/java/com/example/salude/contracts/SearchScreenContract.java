package com.example.salude.contracts;

import com.example.salude.model.pojo.Area;
import com.example.salude.model.pojo.Category;
import com.example.salude.model.pojo.FilteredMeal;
import com.example.salude.model.pojo.Ingredient;
import com.example.salude.model.pojo.Meal;
import com.example.salude.model.remote.retrofit.callback.RemoteRetrofitCallback;

import java.util.List;

public interface SearchScreenContract {
    public interface View {
        public void onMealItemClickListener(Meal meal);
        public void showMealWithName(List<Meal> meals);
        public void showMealDetails(Meal meal);
        public void onFilteredMealItemClickListener(FilteredMeal meal);
        public void onIngredientClickListener(String ingredient);
        public void onCategoryClickListener(String category);
        public void onAreaClickListener(String area);
        public void showFilteredMeals(List<FilteredMeal> filteredMeals);
        public void showMealAreas(List<Area> areas);
        public void showMealIngredients(List<Ingredient> ingredients);
        public void showMealCategories(List<Category> categories);

    }
    public interface Presenter {
        public void getAllCategories();
        public void getAllAreas();
        public void getAllIngredients();
        public void getMealsFilteredByFirstLetter(String query);
        public void getMealByName(String name);
        public void getMealsFilteredByArea(String area);
        public void getMealsFilteredByCategory(String category);
        public void getMealsFilteredByIngredient(String ingredient);
        public void getMealByID(String mealID);
    }
    public interface Model {
        public void getMealByName(RemoteRetrofitCallback.RemoteRetrofitMealCallback cbf, String name);
        public void getMealByID(RemoteRetrofitCallback.RemoteRetrofitMealCallback cbf, String id);
        public void getMealAreas(RemoteRetrofitCallback.RemoteRetrofitAreaCallback cbf);
        public void getMealsIngredients(RemoteRetrofitCallback.RemoteRetrofitIngredientCallback cbf);
        public void getMealsCategories(RemoteRetrofitCallback.RemoteRetrofitCategoryCallback cbf);
        public void getMealsFilteredByCategory(RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback cbf, String category);
        public void getMealsFilteredByArea(RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback cbf, String area);
        public void getMealsFilteredByIngredient(RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback cbf, String ingredient);
        public void getMealsFilteredByFirstLetter(RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback cbf, String letter);
    }
}
