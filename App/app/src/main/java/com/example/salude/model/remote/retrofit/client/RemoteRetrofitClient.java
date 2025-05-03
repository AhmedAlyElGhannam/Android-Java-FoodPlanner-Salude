package com.example.salude.model.remote.retrofit.client;

import android.content.Context;
import android.util.Log;

import com.example.salude.model.remote.retrofit.callback.RemoteRetrofitCallback;
import com.example.salude.model.remote.retrofit.response.AreaResponse;
import com.example.salude.model.remote.retrofit.response.CategoryResponse;
import com.example.salude.model.remote.retrofit.response.FilteredMealResponse;
import com.example.salude.model.remote.retrofit.response.IngredientResponse;
import com.example.salude.model.remote.retrofit.response.MealResponse;
import com.example.salude.model.remote.retrofit.service.RemoteRetrofitService;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteRetrofitClient {
    private static final String baseUrl = "https://www.themealdb.com/api/json/v1/1/";

    private static RemoteRetrofitClient client;

    private static final int TIMEOUT_SECONDS = 30;

    private final RemoteRetrofitService service;

    private RemoteRetrofitClient(Context _context) {
        // 10 MB cache
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(new File(String.valueOf(_context), "http_cache"), cacheSize);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .cache(cache)
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(RemoteRetrofitService.class);
    }

    public static synchronized RemoteRetrofitClient getInstance(Context _context) {
        if (client == null) {
            client = new RemoteRetrofitClient(_context);
        }

        return client;
    }

    public void getMealOfTheDay(RemoteRetrofitCallback.RemoteRetrofitMealCallback cbf) {
        service.getMealOfTheDay().enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if ((response.isSuccessful()) && (response.body() != null)) {
                    // maybe check if meal id is null
                    cbf.onSuccess(response.body().getMeals());
                }
                else {
                    try {
                        cbf.onFailure("corrupted response" + response.errorBody().string());
                    } catch (IOException e) {
                        // maybe handle that?!
                    }
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable throwable) {
                cbf.onFailure("failure");
            }
        });
    }

    public void getMealByName(RemoteRetrofitCallback.RemoteRetrofitMealCallback cbf, String name) {
        service.getMealByName(name).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if ((response.isSuccessful()) && (response.body() != null)) {
                    // maybe check if meal id is null
                    cbf.onSuccess(response.body().getMeals());
                }
                else {
                    try {
                        cbf.onFailure("corrupted response" + response.errorBody().string());
                    } catch (IOException e) {
                        // maybe handle that?!
                    }
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable throwable) {
                cbf.onFailure("failure");
            }
        });
    }

    public void getMealByID(RemoteRetrofitCallback.RemoteRetrofitMealCallback cbf, String id) {
        service.getMealDetailsByID(id).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if ((response.isSuccessful()) && (response.body() != null)) {
                    // maybe check if meal id is null
                    cbf.onSuccess(response.body().getMeals());
                }
                else {
                    try {
                        cbf.onFailure("corrupted response" + response.errorBody().string());
                    } catch (IOException e) {
                        // maybe handle that?!
                    }
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable throwable) {
                cbf.onFailure("failure");
            }
        });
    }

    public void getMealAreas(RemoteRetrofitCallback.RemoteRetrofitAreaCallback cbf) {
        service.getAllMealAreas().enqueue(new Callback<AreaResponse>() {
            @Override
            public void onResponse(Call<AreaResponse> call, Response<AreaResponse> response) {
                if ((response.isSuccessful()) && (response.body() != null)) {
                    // maybe check if meal id is null
                    cbf.onSuccess(response.body().getAreas());
                    Log.i("TAG", "onResponse: areas success" + response.body().getAreas());
                }
                else {
                    try {
                        cbf.onFailure("corrupted response" + response.errorBody().string());
                    } catch (IOException e) {
                        // maybe handle that?!
                    }
                }
            }

            @Override
            public void onFailure(Call<AreaResponse> call, Throwable throwable) {
                cbf.onFailure("failure");
            }
        });
    }

    public void getMealsIngredients(RemoteRetrofitCallback.RemoteRetrofitIngredientCallback cbf) {
        service.getAllMealIngredients().enqueue(new Callback<IngredientResponse>() {
            @Override
            public void onResponse(Call<IngredientResponse> call, Response<IngredientResponse> response) {
                if ((response.isSuccessful()) && (response.body() != null)) {
                    // maybe check if meal id is null
                    cbf.onSuccess(response.body().getIngredients());
                }
                else {
                    try {
                        cbf.onFailure("corrupted response" + response.errorBody().string());
                    } catch (IOException e) {
                        // maybe handle that?!
                    }
                }
            }

            @Override
            public void onFailure(Call<IngredientResponse> call, Throwable throwable) {
                cbf.onFailure("failure");
            }
        });
    }

    public void getMealsCategories(RemoteRetrofitCallback.RemoteRetrofitCategoryCallback cbf) {
        service.getAllMealCategories().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if ((response.isSuccessful()) && (response.body() != null)) {
                    // maybe check if meal id is null
                    cbf.onSuccess(response.body().getCategories());
                }
                else {
                    try {
                        cbf.onFailure("corrupted response" + response.errorBody().string());
                    } catch (IOException e) {
                        // maybe handle that?!
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable throwable) {
                cbf.onFailure("failure");
            }
        });
    }

    public void getMealsFilteredByCategory(RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback cbf, String category) {
        service.getMealsFilteredByCategory(category).enqueue(new Callback<FilteredMealResponse>() {
            @Override
            public void onResponse(Call<FilteredMealResponse> call, Response<FilteredMealResponse> response) {
                if ((response.isSuccessful()) && (response.body() != null)) {
                    // maybe check if meal id is null
                    cbf.onSuccess(response.body().getFilteredMeals());
                }
                else {
                    try {
                        cbf.onFailure("corrupted response" + response.errorBody().string());
                    } catch (IOException e) {
                        // maybe handle that?!
                    }
                }
            }

            @Override
            public void onFailure(Call<FilteredMealResponse> call, Throwable throwable) {
                cbf.onFailure("failure");
            }
        });
    }

    public void getMealsFilteredByArea(RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback cbf, String area) {
        service.getMealsFilteredByArea(area).enqueue(new Callback<FilteredMealResponse>() {
            @Override
            public void onResponse(Call<FilteredMealResponse> call, Response<FilteredMealResponse> response) {
                if ((response.isSuccessful()) && (response.body() != null)) {
                    // maybe check if meal id is null
                    cbf.onSuccess(response.body().getFilteredMeals());
                }
                else {
                    try {
                        cbf.onFailure("corrupted response" + response.errorBody().string());
                    } catch (IOException e) {
                        // maybe handle that?!
                    }
                }
            }

            @Override
            public void onFailure(Call<FilteredMealResponse> call, Throwable throwable) {
                cbf.onFailure("failure");
            }
        });
    }

    public void getMealsFilteredByIngredient(RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback cbf, String ingredient) {
        service.getMealsFilteredByIngredient(ingredient).enqueue(new Callback<FilteredMealResponse>() {
            @Override
            public void onResponse(Call<FilteredMealResponse> call, Response<FilteredMealResponse> response) {
                if ((response.isSuccessful()) && (response.body() != null)) {
                    // maybe check if meal id is null
                    cbf.onSuccess(response.body().getFilteredMeals());
                }
                else {
                    try {
                        cbf.onFailure("corrupted response" + response.errorBody().string());
                    } catch (IOException e) {
                        // maybe handle that?!
                    }
                }
            }

            @Override
            public void onFailure(Call<FilteredMealResponse> call, Throwable throwable) {
                cbf.onFailure("failure");
            }
        });
    }

    public void getMealsFilteredByFirstLetter(RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback cbf, String letter) {
        service.getMealsFilteredByFirstLetter(letter).enqueue(new Callback<FilteredMealResponse>() {
            @Override
            public void onResponse(Call<FilteredMealResponse> call, Response<FilteredMealResponse> response) {
                if ((response.isSuccessful()) && (response.body() != null)) {
                    // maybe check if meal id is null
                    cbf.onSuccess(response.body().getFilteredMeals());
                }
                else {
                    try {
                        cbf.onFailure("corrupted response" + response.errorBody().string());
                    } catch (IOException e) {
                        // maybe handle that?!
                    }
                }
            }

            @Override
            public void onFailure(Call<FilteredMealResponse> call, Throwable throwable) {
                cbf.onFailure("failure");
            }
        });
    }
}
