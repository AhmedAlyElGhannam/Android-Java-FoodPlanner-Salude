package com.example.salude.model.authentication.register;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.salude.contracts.RegistrationContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationAuthRepository implements RegistrationContract.Model {
    private static RegistrationAuthRepository repo = null;

    private RegistrationAuthRepository() {}
    public static RegistrationAuthRepository getInstance() {
        if (repo == null) {
            repo = new RegistrationAuthRepository();
        }

        return repo;
    }

    @Override
    public void registerUser(String email, String password, RegistrationContract.OnRegistrationFinishedListener listener) {
        // create an object from FirebaseAuth singleton class
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            listener.OnRegistrationSuccess();
                        }
                        else {
                            listener.OnRegistrationFailure("Failed to register!");
                            Log.i("OnRegFail", "onComplete: " + task.getException().getMessage());
                        }
                    }
                });
    }
}
