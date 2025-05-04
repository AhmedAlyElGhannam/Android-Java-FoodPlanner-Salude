package com.example.salude.model.remote.firebase.logout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.salude.model.local.dao.MealDAO;
import com.example.salude.model.local.dao.RoomLocalDB;
import com.example.salude.model.local.repo.RoomLocalRepository;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirebaseLogoutHelper {
    public static void logout(Runnable onComplete) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                // Get DAOs and repositories
                MealDAO.FavouriteMealDAO favDao = RoomLocalDB.getInstance(null).getFavouriteMealDAO();
                MealDAO.PlannedMealDAO plannedDao = RoomLocalDB.getInstance(null).getPlannedMealDAO();

                RoomLocalRepository.RoomLocalFavouriteRepository favRepo =
                        RoomLocalRepository.RoomLocalFavouriteRepository.getInstance(favDao);
                RoomLocalRepository.RoomLocalPlannedRepository plannedRepo =
                        RoomLocalRepository.RoomLocalPlannedRepository.getInstance(plannedDao);

                // Execute syncs sequentially
                Task<Void> favSyncTask = favRepo.syncFavoriteMealsToFirebase();
                Task<Void> plannedSyncTask = plannedRepo.syncPlannedMealsToFirebase();

                // Wait for both syncs to complete
                Tasks.await(Tasks.whenAll(favSyncTask, plannedSyncTask));

                // Sign out
                FirebaseAuth.getInstance().signOut();

                // Notify completion on main thread
                new Handler(Looper.getMainLooper()).post(onComplete);

            } catch (Exception e) {
                Log.e("FirebaseLogout", "Logout failed", e);
                // Even if sync failed, proceed with logout
                FirebaseAuth.getInstance().signOut();
                new Handler(Looper.getMainLooper()).post(onComplete);
            } finally {
                executor.shutdown();
            }
        });
    }
}