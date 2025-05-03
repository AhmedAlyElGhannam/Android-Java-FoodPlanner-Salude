package com.example.salude.model.remote.retrofit.repository;

import com.example.salude.model.remote.retrofit.callback.RemoteRetrofitCallback;
import com.example.salude.model.remote.retrofit.client.RemoteRetrofitClient;

public class RemoteRetrofitRepository {
    private final RemoteRetrofitClient client;
    private static RemoteRetrofitRepository repo = null;

    private RemoteRetrofitRepository(RemoteRetrofitClient _client) {
        client = _client;
    }

    public static RemoteRetrofitRepository getInstance(RemoteRetrofitClient _client) {
        if (repo == null) {
            repo = new RemoteRetrofitRepository(_client);
        }

        return repo;
    }

    public void getMealOfTheDay(RemoteRetrofitCallback.RemoteRetrofitMealCallback cbf) {
        client.getMealOfTheDay(cbf);
    }

    public void getMealByName(RemoteRetrofitCallback.RemoteRetrofitMealCallback cbf, String name) {
        client.getMealByName(cbf, name);
    }

    public void getMealByID(RemoteRetrofitCallback.RemoteRetrofitMealCallback cbf, String id) {
        client.getMealByID(cbf, id);
    }

    public void getMealAreas(RemoteRetrofitCallback.RemoteRetrofitAreaCallback cbf) {
        client.getMealAreas(cbf);
    }

    public void getMealsIngredients(RemoteRetrofitCallback.RemoteRetrofitIngredientCallback cbf) {
        client.getMealsIngredients(cbf);
    }

    public void getMealsCategories(RemoteRetrofitCallback.RemoteRetrofitCategoryCallback cbf) {
        client.getMealsCategories(cbf);
    }

    public void getMealsFilteredByCategory(RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback cbf, String category) {
        client.getMealsFilteredByCategory(cbf, category);
    }

    public void getMealsFilteredByArea(RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback cbf, String area) {
        client.getMealsFilteredByArea(cbf, area);
    }

    public void getMealsFilteredByIngredient(RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback cbf, String ingredient) {
        client.getMealsFilteredByIngredient(cbf, ingredient);
    }

    public void getMealsFilteredByFirstLetter(RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback cbf, String letter) {
        client.getMealsFilteredByFirstLetter(cbf, letter);
    }

}
