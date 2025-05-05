package com.example.salude.model.remote.firebase.login;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.salude.contracts.LoginContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginAuthRepository implements LoginContract.Model {
    private static LoginAuthRepository repo;
    private LoginAuthRepository() {}
    public static synchronized LoginAuthRepository getInstance() {
        if (repo == null) {
            repo = new LoginAuthRepository();
        }
        return repo;
    }

    @Override
    public void userAccountLogin(String email, String password, LoginContract.OnLoginFinishedListener listener) {

    }

    @Override
    public void userGoogleLogin(String idToken, LoginContract.OnLoginFinishedListener listener) {

    }
}
