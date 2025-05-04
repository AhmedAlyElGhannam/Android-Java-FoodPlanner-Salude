package com.example.salude.model.remote.firebase.service;

import android.util.Log;

import com.example.salude.model.pojo.Meal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class FirebaseDataSyncService {
    private static FirebaseDataSyncService instance;
    private final DatabaseReference databaseReference;

    private FirebaseDataSyncService() {
        databaseReference = FirebaseDatabase.getInstance().getReference("userData");
    }

    public void syncFavoritesToFirebase(List<Meal> meals) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference.child(userId).child("favorites").setValue(meals)
                    .addOnSuccessListener(aVoid -> Log.d("FirebaseSync", "Favorites synced"))
                    .addOnFailureListener(e -> Log.e("FirebaseSync", "Failed to sync favorites", e));
        }
    }

    public void syncPlannedToFirebase(List<Meal> meals) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference.child(userId).child("planned").setValue(meals)
                    .addOnSuccessListener(aVoid -> Log.d("FirebaseSync", "Planned meals synced"))
                    .addOnFailureListener(e -> Log.e("FirebaseSync", "Failed to sync planned meals", e));
        }
    }

    public static synchronized FirebaseDataSyncService getInstance() {
        if (instance == null) {
            instance = new FirebaseDataSyncService();
        }
        return instance;
    }

    public void syncMealsToFirebase(List<Meal> meals) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference.child(userId).setValue(meals);
        }
    }

    public DatabaseReference getUserMealsReference() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            return databaseReference.child(userId);
        }
        return null;
    }
}