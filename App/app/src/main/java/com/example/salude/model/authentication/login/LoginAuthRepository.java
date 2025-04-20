package com.example.salude.model.authentication.login;

import com.example.salude.contracts.LoginContract;
import com.example.salude.contracts.RegistrationContract;

public class LoginAuthRepository implements LoginContract.Model {
    private static LoginAuthRepository repo;
    private LoginAuthRepository() {}
    public static LoginAuthRepository getInstance() {
        if (repo == null) {
            repo = new LoginAuthRepository();
        }
        return repo;
    }
    @Override
    public void userAccountLogin(String email, String password, RegistrationContract.OnRegistrationFinishedListener listener) {

    }

    @Override
    public void userGoogleLogin(RegistrationContract.OnRegistrationFinishedListener listener) {

    }
}
