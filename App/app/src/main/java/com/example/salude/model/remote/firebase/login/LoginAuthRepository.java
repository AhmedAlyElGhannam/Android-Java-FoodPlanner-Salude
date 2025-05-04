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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            listener.OnLoginSuccess();
                        }
                        else {
                            listener.OnLoginFailure("Failed to login!");
                            Log.i("OnLoginFail", "onComplete: " + task.getException().getMessage());
                        }
                    }
                });
    }

    @Override
    public void userGoogleLogin(String idToken, LoginContract.OnLoginFinishedListener listener) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.OnLoginSuccess();
                    } else {
                        String error = task.getException() != null ?
                                task.getException().getMessage() : "Unknown error";
                        listener.OnLoginFailure("Google authentication failed: " + error);
                    }
                });
    }
}
