package com.example.salude.features.auth_firebase.register.presenter;

import android.util.Log;

import com.example.salude.contracts.RegistrationContract;
import com.example.salude.model.remote.user.datasource.UserRegAndAuthDataSource;

public class RegisterAuthFirebasePresenter implements RegistrationContract.Presenter, RegistrationContract.OnRegistrationFinishedListener {
    RegistrationContract.View view;
    UserRegAndAuthDataSource repo;
    public RegisterAuthFirebasePresenter(RegistrationContract.View _view, UserRegAndAuthDataSource _repo) {
        repo = _repo;
        view = _view;
    }

    @Override
    public void callRegisterModelAction(String name, String email, String password) {
        if (repo != null) {
            view.showProgress();
            repo.registerUser(name, email, password, this);
        }
        else {
            Log.i("RegPresenter", "callRegisterModelAction: " + "repo is null!");
        }
    }

    @Override
    public void OnRegistrationSuccess() {
        if (repo != null) {
            view.hideProgress();
            view.onSuccessUIAction();
        }
        else {
            Log.i("RegPresenter", "callRegisterModelAction: " + "view is null!");
        }
    }

    @Override
    public void OnRegistrationFailure(String err) {
        if (repo != null) {
            view.hideProgress();
            view.onErrorUIAction(err);
        }
        else {
            Log.i("RegPresenter", "callRegisterModelAction: " + "view is null!");
        }
    }
}
