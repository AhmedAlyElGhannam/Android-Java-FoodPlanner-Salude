package com.example.salude.model.local.repo;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.salude.model.local.dao.MealDAO;
import com.example.salude.model.pojo.Meal;
import com.example.salude.model.remote.firebase.service.FirebaseDataSyncService;

import java.util.List;

public class RoomLocalRepository {
    public static class RoomLocalFavouriteRepository {
        private final MealDAO.FavouriteMealDAO dao;
        private static RoomLocalFavouriteRepository repo = null;

        private RoomLocalFavouriteRepository(MealDAO.FavouriteMealDAO dao) {
            this.dao = dao;
        }

        public static synchronized RoomLocalFavouriteRepository getInstance(MealDAO.FavouriteMealDAO dao) {
            if (repo == null) {
                repo = new RoomLocalFavouriteRepository(dao);
            }

            return repo;
        }

        public LiveData<List<Meal>> getListOfFavouriteMeals() {
            return dao.getFavouriteMeals();
        }

        public void addMealToFavourites(Meal meal) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // add to favs
                    if (dao.isMealInDB(meal.getIdMeal())) {
                        dao.updateMealFavouriteStatus(meal.getIdMeal(), true);
                    }
                    else {
                        meal.setIsFavouriteMeal(true);
                        dao.insertFavouriteMeal(meal);
                    }
                }
            }).start();
        }

        public void removeMealFromFavourites(Meal meal) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (dao.isMealInDB(meal.getIdMeal())) {
                        dao.updateMealFavouriteStatus(meal.getIdMeal(), false);
                    }
                    else {
                        meal.setIsFavouriteMeal(false);
                        dao.removeMealFromFavourites(meal);
                    }
                }
            }).start();
        }

        public void syncFavoriteMealsToFirebase() {
            new Thread(() -> {
                try {
                    List<Meal> favoriteMeals = dao.getFavouriteMealsSync();
                    FirebaseDataSyncService.getInstance().syncFavoritesToFirebase(favoriteMeals);
                } catch (Exception e) {
                    Log.e("SyncFavorites", "Failed to sync favorites", e);
                }
            }).start();
        }
    }

    public static class RoomLocalPlannedRepository {
        private final MealDAO.PlannedMealDAO dao;
        private static RoomLocalPlannedRepository repo = null;

        private RoomLocalPlannedRepository(MealDAO.PlannedMealDAO _dao) {
            dao = _dao;
        }

        public static RoomLocalPlannedRepository getInstance(MealDAO.PlannedMealDAO _dao) {
            if (repo == null) {
                repo = new RoomLocalPlannedRepository(_dao);
            }

            return repo;
        }

        public LiveData<List<Meal>> getListOfPlannedMeals() {
            return dao.getPlannedMeals();
        }

        public void addToPlannedMeals(Meal meal, String date) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (dao.isMealInDB(meal.getIdMeal())) {
                        dao.updateMealPlannedStatus(meal.getIdMeal(), date);
                    }
                    else {
                        meal.setPlannedMealDate(date);
                        dao.insertPlannedMeal(meal);
                    }
                }
            }).start();
        }

        public void removeFromPlannedMeals(Meal meal) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (dao.isMealInDB(meal.getIdMeal())) {
                        dao.updateMealPlannedStatus(meal.getIdMeal(), null);
                    }
                    else {
                        meal.setPlannedMealDate(null);
                        dao.removeMealFromPlanned(meal);
                    }
                }
            }).start();
        }

        public void syncPlannedMealsToFirebase() {
            new Thread(() -> {
                try {
                    List<Meal> plannedMeals = dao.getPlannedMealsSync();
                    FirebaseDataSyncService.getInstance().syncPlannedToFirebase(plannedMeals);
                } catch (Exception e) {
                    Log.e("SyncPlanned", "Failed to sync planned meals", e);
                }
            }).start();
        }
    }
}
