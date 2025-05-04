package com.example.salude.model.remote.firebase.logout;

import com.example.salude.model.local.dao.MealDAO;
import com.example.salude.model.local.dao.RoomLocalDB;
import com.example.salude.model.local.repo.RoomLocalRepository;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseLogoutHelper {
    public static void logout(Runnable onComplete) {
        MealDAO.FavouriteMealDAO favDao = RoomLocalDB.getInstance(null).getFavouriteMealDAO();
        MealDAO.PlannedMealDAO plannedDao = RoomLocalDB.getInstance(null).getPlannedMealDAO();

        // Get repository instances with proper DAOs
        RoomLocalRepository.RoomLocalFavouriteRepository favRepo =
                RoomLocalRepository.RoomLocalFavouriteRepository.getInstance(favDao);
        RoomLocalRepository.RoomLocalPlannedRepository plannedRepo =
                RoomLocalRepository.RoomLocalPlannedRepository.getInstance(plannedDao);

        // Create a counter to track sync completion
        final int[] syncCounter = {2}; // We have 2 sync operations

        Runnable checkComplete = () -> {
            syncCounter[0]--;
            if (syncCounter[0] == 0) {
                // Both syncs completed, now logout
                FirebaseAuth.getInstance().signOut();
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        };

        // sync data before logout
        new Thread(() -> {
            favRepo.syncFavoriteMealsToFirebase();
            checkComplete.run();
        }).start();

        new Thread(() -> {
            plannedRepo.syncPlannedMealsToFirebase();
            checkComplete.run();
        }).start();
    }
}
