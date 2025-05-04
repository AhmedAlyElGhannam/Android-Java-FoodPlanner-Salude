package com.example.salude.model.local.repo;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.salude.model.local.dao.MealDAO;
import com.example.salude.model.pojo.Meal;

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

        public LiveData<List<Meal>> getListOfFavouriteMeals(String userID) {
            return dao.getFavouriteMeals(userID);
        }

        public void addMealToFavourites(Meal meal) {
            new Thread(() -> {
                String userID = meal.getUserID();
                if (userID == null) {
                    Log.e("RoomLocalRepository", "User ID is null when adding to favorites");
                    return;
                }

                if (dao.isMealInDB(meal.getIdMeal(), userID)) {
                    dao.updateMealFavouriteStatus(meal.getIdMeal(), true, userID);
                } else {
                    meal.setIsFavouriteMeal(true);
                    dao.insert(meal); // You'll need to add this method to your DAO
                }
            }).start();
        }

        public void removeMealFromFavourites(Meal meal) {
            new Thread(() -> {
                String userID = meal.getUserID();
                if (userID == null) {
                    Log.e("RoomLocalRepository", "User ID is null when removing from favorites");
                    return;
                }

                dao.updateMealFavouriteStatus(meal.getIdMeal(), false, userID);
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

        public LiveData<List<Meal>> getListOfPlannedMeals(String userID) {
            return dao.getPlannedMeals(userID);
        }

        public void addToPlannedMeals(Meal meal, String date, String userID) {
            new Thread(() -> {
                if (userID == null) {
                    Log.e("RoomLocalRepository", "User ID is null when adding to planned");
                    return;
                }

                meal.setUserID(userID);
                meal.setPlannedMealDate(date);

                if (dao.isMealInDB(meal.getIdMeal(), userID)) {
                    dao.updateMealPlannedStatus(meal.getIdMeal(), date, userID);
                } else {
                    dao.insertPlannedMeal(meal); // You'll need to add this method to your DAO
                }
            }).start();
        }

        public void removeFromPlannedMeals(Meal meal) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (dao.isMealInDB(meal.getIdMeal(), meal.getUserID())) {
                        dao.updateMealPlannedStatus(meal.getIdMeal(), null, meal.getUserID());
                    }
                    else {
                        meal.setPlannedMealDate(null);
                        dao.removeMealFromPlanned(meal.getIdMeal(), meal.getUserID());
                    }
                }
            }).start();
        }
    }
}