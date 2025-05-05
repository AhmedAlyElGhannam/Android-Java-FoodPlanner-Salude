package com.example.salude.model.remote.firebase.register;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.salude.contracts.RegistrationContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistrationAuthRepository implements RegistrationContract.Model {
    private static RegistrationAuthRepository repo = null;


    @Override
    public void registerUser(String name, String email, String password, RegistrationContract.OnRegistrationFinishedListener listener) {

    }
}
