package com.example.salude.features.main_screen.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.salude.contracts.HomeScreenContract;
import com.example.salude.model.pojo.Category;
import com.example.salude.model.pojo.Meal;
import com.example.salude.model.remote.retrofit.callback.RemoteRetrofitCallback;
import com.example.salude.model.remote.retrofit.repository.RemoteRetrofitRepository;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeScreenPresenter implements HomeScreenContract.Presenter {
    private final HomeScreenContract.View view;
    private final RemoteRetrofitRepository repo;
    private final SharedPreferences sharedPreferences;

    public HomeScreenPresenter(HomeScreenContract.View _view, RemoteRetrofitRepository _repo, Context _context) {
        view = _view;
        repo = _repo;
        sharedPreferences = _context.getSharedPreferences("Meal_Preferences", Context.MODE_PRIVATE);
    }

    @Override
    public void getMealOfTheDay() {
        String savedDate = sharedPreferences.getString("mealDate", "");
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (savedDate.equals(today)) {
            // get meal from shared preferences instead of fetching a new one
            Meal savedMeal = new Gson().fromJson(sharedPreferences.getString("mealOfTheDay", null), Meal.class);
            view.showMealOfTheDay(savedMeal);
        }
        else {
            repo.getMealOfTheDay(new RemoteRetrofitCallback.RemoteRetrofitMealCallback() {
                @Override
                public void onSuccess(List<Meal> meals) {
                    if (meals != null && !meals.isEmpty()) {
                        Meal todaysMeal = meals.get(0);
                        view.showMealOfTheDay(todaysMeal);

                        // save meal and current date
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("mealOfTheDay", new Gson().toJson(todaysMeal));
                        editor.putString("meal_date", sharedPreferences.getString("mealOfTheDay", null));
                        editor.apply();
                    }
                }

                @Override
                public void onFailure(String err) {

                }
            });
        }
    }

    @Override
    public void getAllCategories() {
        repo.getMealsCategories(new RemoteRetrofitCallback.RemoteRetrofitCategoryCallback() {
            @Override
            public void onSuccess(List<Category> categories) {
                if (categories != null && !categories.isEmpty()) {
                    view.showMealCategories(categories);
                } else {
                    Log.i("TAG", "Categories list is null or empty");
                    // Optionally, show an error message in the UI
                }
            }

            @Override
            public void onFailure(String err) {
                Log.i("TAG", "on failure");
            }
        });
    }
}
