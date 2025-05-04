package com.example.salude.model.remote.firebase.service;

import android.util.Log;

import com.example.salude.model.pojo.Meal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class FirebaseDataSyncService {
    private static FirebaseDataSyncService instance;
    private final DatabaseReference databaseReference;

    private FirebaseDataSyncService() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://salud-98c8c-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = database.getInstance().getReference("userData");
    }

    public static synchronized FirebaseDataSyncService getInstance() {
        if (instance == null) {
            instance = new FirebaseDataSyncService();
        }
        return instance;
    }

    public Task<Void> syncFavorites(List<Meal> meals) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            return databaseReference.child(userId).child("favorites").setValue(meals);
        }
        return Tasks.forException(new Exception("User not authenticated"));
    }

    public Task<Void> syncPlannedMeals(List<Meal> meals) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            return databaseReference.child(userId).child("planned").setValue(meals);
        }
        return Tasks.forException(new Exception("User not authenticated"));
    }

    public Task<DataSnapshot> loadUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            return databaseReference.child(userId).get();
        }
        return Tasks.forException(new Exception("User not authenticated"));
    }
}