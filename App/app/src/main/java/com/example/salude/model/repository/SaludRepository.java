package com.example.salude.model.repository;

import android.content.Context;

import com.example.salude.contracts.LoginContract;
import com.example.salude.contracts.RegistrationContract;
import com.example.salude.model.local.dao.RoomLocalDB;
import com.example.salude.model.local.datasource.LocalDataSource;
import com.example.salude.model.remote.retrofit.client.RemoteRetrofitClient;
import com.example.salude.model.remote.retrofit.datasource.RemoteDataSource;
import com.example.salude.model.remote.user.datasource.UserRegAndAuthDataSource;
import com.google.firebase.auth.FirebaseUser;

public class SaludRepository implements RegistrationContract.Model, LoginContract.Model{
    private static SaludRepository salud_repo;
    private UserRegAndAuthDataSource auth_source;
    private LocalDataSource.RoomLocalFavouriteRepository local_fav_source;
    private LocalDataSource.RoomLocalPlannedRepository local_plan_source;
    private RemoteDataSource remote_source;

    private SaludRepository(Context context) {
        auth_source = UserRegAndAuthDataSource.getInstance();
        local_fav_source = LocalDataSource.RoomLocalFavouriteRepository.getInstance(RoomLocalDB.getInstance(context).getFavouriteMealDAO());
        local_plan_source = LocalDataSource.RoomLocalPlannedRepository.getInstance(RoomLocalDB.getInstance(context).getPlannedMealDAO());
        remote_source = RemoteDataSource.getInstance(RemoteRetrofitClient.getInstance(context));
    }

    public static synchronized SaludRepository getInstance(Context context) {
        if (salud_repo == null) {
            salud_repo = new SaludRepository(context);
        }

        return salud_repo;
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return auth_source.getCurrentUser();
    }

    @Override
    public void registerUser(
            String name,
            String email,
            String password,
            RegistrationContract.OnRegistrationFinishedListener listener) {
        auth_source.registerUser(name, email, password, listener);
    }

    @Override
    public void userAccountLogin(String email, String password, LoginContract.OnLoginFinishedListener listener) {
        auth_source.userAccountLogin(email, password, listener);
    }

    @Override
    public void userGoogleLogin(String idToken, LoginContract.OnLoginFinishedListener listener) {
        auth_source.userGoogleLogin(idToken, listener);
    }

}
