package com.example.salude.features.main_screen.fragments.search.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.util.Log;

import com.example.salude.contracts.HomeScreenContract;
import com.example.salude.contracts.SearchScreenContract;
import com.example.salude.model.pojo.Area;
import com.example.salude.model.pojo.Category;
import com.example.salude.model.pojo.FilteredMeal;
import com.example.salude.model.pojo.Ingredient;
import com.example.salude.model.pojo.Meal;
import com.example.salude.model.remote.retrofit.callback.RemoteRetrofitCallback;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchScreenPresenter implements SearchScreenContract.Presenter {
    private final SearchScreenContract.View view;
    private final SearchScreenContract.Model repo;

    public SearchScreenPresenter(SearchScreenContract.View _view, SearchScreenContract.Model _repo) {
        view = _view;
        repo = _repo;
    }

    @Override
    public void getAllCategories() {
        repo.getMealsCategories(new RemoteRetrofitCallback.RemoteRetrofitCategoryCallback() {
            @Override
            public void onSuccess(List<Category> categories) {
                if (categories != null && !categories.isEmpty()) {
                    view.showMealCategories(categories);
                }
                else {
                    Log.i("TAG", "Categories list is null or empty");
                    // Optionally, show an error message in the UI
                }
            }

            @Override
            public void onFailure(String err) {
                Log.i("TAG", "onFailure: " + err);
            }
        });
    }

    @Override
    public void getAllAreas() {
        repo.getMealAreas(new RemoteRetrofitCallback.RemoteRetrofitAreaCallback() {
            @Override
            public void onSuccess(List<Area> areas) {
                if (areas != null && !areas.isEmpty()) {
                    view.showMealAreas(areas);
                    Log.i("TAG", "onSuccess: " + areas);
                }
                else {
                    Log.i("TAG", "Areas list is null or empty");
                }
            }

            @Override
            public void onFailure(String err) {
                Log.i("TAG", "onFailure: " + err);
            }
        });
    }

    @Override
    public void getAllIngredients() {
        repo.getMealsIngredients(new RemoteRetrofitCallback.RemoteRetrofitIngredientCallback() {
            @Override
            public void onSuccess(List<Ingredient> ingredients) {
                if (ingredients != null && !ingredients.isEmpty()) {
                    view.showMealIngredients(ingredients);
                }
                else {
                    Log.i("TAG", "Categories list is null or empty");
                }
            }

            @Override
            public void onFailure(String err) {
                Log.i("TAG", "onFailure: " + err);
            }
        });
    }

    @Override
    public void getMealsFilteredByIngredient(String ingredient) {
        repo.getMealsFilteredByIngredient(new RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback(){
            @Override
            public void onSuccess(List<FilteredMeal> filteredMeals) {
                if (filteredMeals != null && !filteredMeals.isEmpty()) {
                    view.showFilteredMeals(filteredMeals);
                }
                else {
                    Log.i("TAG", "Meals filtered by ingredient list is null or empty");
                }
            }

            @Override
            public void onFailure(String err) {
                Log.i("TAG", "onFailure: " + err);
            }
        }, ingredient);
    }

    @Override
    public void getMealsFilteredByArea(String area) {
        repo.getMealsFilteredByArea(new RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback() {
            @Override
            public void onSuccess(List<FilteredMeal> filteredMeals) {
                if (filteredMeals != null && !filteredMeals.isEmpty()) {
                    view.showFilteredMeals(filteredMeals);
                }
                else {
                    Log.i("TAG", "Meals filtered by area list is null or empty");
                }
            }

            @Override
            public void onFailure(String err) {
                Log.i("TAG", "onFailure: " + err);
            }
        }, area);
    }

    @Override
    public void getMealsFilteredByCategory(String category) {
        repo.getMealsFilteredByCategory(new RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback() {
            @Override
            public void onSuccess(List<FilteredMeal> filteredMeals) {
                if (filteredMeals != null && !filteredMeals.isEmpty()) {
                    view.showFilteredMeals(filteredMeals);
                }
                else {
                    Log.i("TAG", "Meals filtered by category list is null or empty");
                }
            }

            @Override
            public void onFailure(String err) {
                Log.i("TAG", "on failure" + err);
            }
        }, category);
    }

    @Override
    public void getMealsFilteredByFirstLetter(String str) {
        repo.getMealsFilteredByFirstLetter(new RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback() {
            @Override
            public void onSuccess(List<FilteredMeal> filteredMeals) {
                view.showFilteredMeals(filteredMeals);
            }

            @Override
            public void onFailure(String err) {
                Log.i("TAG", "onFailure: " + err);
            }
        }, str);
    }

    @Override
    public void getMealByName(String str) {
        repo.getMealByName(new RemoteRetrofitCallback.RemoteRetrofitMealCallback() {
            @Override
            public void onSuccess(List<Meal> meals) {
                view.showMealWithName(meals);
            }

            @Override
            public void onFailure(String err) {
                Log.i("TAG", "onFailure: " + err);
            }
        }, str);
    }

    @Override
    public void getMealByID(String id) {
        repo.getMealByID(new RemoteRetrofitCallback.RemoteRetrofitMealCallback() {
            @Override
            public void onSuccess(List<Meal> meals) {
                // it returned a list so I will assume the first is the one I want
                view.showMealDetails(meals.get(0));
            }

            @Override
            public void onFailure(String err) {
                Log.i("TAG", "onFailure: " + err);
            }
        }, id);
    }
}

