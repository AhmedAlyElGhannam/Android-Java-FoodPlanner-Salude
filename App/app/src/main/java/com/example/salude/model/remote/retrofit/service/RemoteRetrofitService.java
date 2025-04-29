package com.example.salude.model.remote.retrofit.service;

import com.example.salude.model.remote.retrofit.response.AreaResponse;
import com.example.salude.model.remote.retrofit.response.CategoryResponse;
import com.example.salude.model.remote.retrofit.response.FilteredMealResponse;
import com.example.salude.model.remote.retrofit.response.IngredientResponse;
import com.example.salude.model.remote.retrofit.response.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RemoteRetrofitService {
    @GET("random.php")
    Call<MealResponse> getMealOfTheDay();

    @GET("list.php?c=list")
    Call<CategoryResponse> getAllMealCategories();

    @GET("list.php?a=list")
    Call<AreaResponse> getAllMealAreas();

    @GET("list.php?i=list")
    Call<IngredientResponse> getAllMealIngredients();

    @GET("filter.php")
    Call<FilteredMealResponse> getMealsFilteredByCategory(@Query("c") String category);

    @GET("filter.php")
    Call<FilteredMealResponse> getMealsFilteredByArea(@Query("a") String area);

    @GET("filter.php")
    Call<FilteredMealResponse> getMealsFilteredByIngredient(@Query("i") String ingredient);

    @GET("search.php")
    Call<FilteredMealResponse> getMealsFilteredByFirstLetter(@Query("f") String letter);

    @GET("search.php")
    Call<MealResponse> getMealByName(@Query("s") String name);

    @GET("lookup.php")
    Call<MealResponse> getMealDetailsByID(@Query("i") String id);
}
