package com.example.salude.model.remote.retrofit.client;

import com.example.salude.model.remote.retrofit.callback.RemoteRetrofitCallback;
import com.example.salude.model.remote.retrofit.response.MealResponse;
import com.example.salude.model.remote.retrofit.service.RemoteRetrofitService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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

    private RemoteRetrofitClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(RemoteRetrofitService.class);
    }

    public static RemoteRetrofitClient getInstance() {
        // maybe make it synchonized
        if (client == null) {
            client = new RemoteRetrofitClient();
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
}
