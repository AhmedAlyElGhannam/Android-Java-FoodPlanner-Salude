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

    private RegistrationAuthRepository() {}
    public static synchronized RegistrationAuthRepository getInstance() {
        if (repo == null) {
            repo = new RegistrationAuthRepository();
        }

        return repo;
    }

    @Override
    public void registerUser(String name, String email, String password, RegistrationContract.OnRegistrationFinishedListener listener) {
        // create an object from FirebaseAuth singleton class
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Update Firebase user profile with name
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            if (user != null) {
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(updateTask -> {
                                            if (updateTask.isSuccessful()) {
                                                listener.OnRegistrationSuccess();
                                            }
                                        });
                            }
                        }
                        else {
                            listener.OnRegistrationFailure("Failed to register!");
                            Log.i("OnRegFail", "onComplete: " + task.getException().getMessage());
                        }
                    }
                });
    }
}
