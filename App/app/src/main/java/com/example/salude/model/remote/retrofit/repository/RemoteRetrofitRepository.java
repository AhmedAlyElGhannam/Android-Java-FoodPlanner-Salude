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
}
