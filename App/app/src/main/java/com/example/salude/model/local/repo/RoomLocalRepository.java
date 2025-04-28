package com.example.salude.model.local.repo;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.salude.model.local.dao.MealDAO;
import com.example.salude.model.pojo.Meal;

import java.util.List;

public class RoomLocalRepository {
    static class RoomLocalFavouriteRepository {
        private final MealDAO.FavouriteMealDAO dao;
        private static RoomLocalFavouriteRepository repo = null;

        private RoomLocalFavouriteRepository(MealDAO.FavouriteMealDAO dao) {
            this.dao = dao;
        }

        public static RoomLocalFavouriteRepository getInstance(MealDAO.FavouriteMealDAO dao) {
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
                    if (!dao.isMealInDB(meal.getIdMeal())) {
                        dao.insertFavouriteMeal(meal);
                    }

                    // set meal as favourite
                    dao.updateMealFavouriteStatus(meal.getIdMeal(), true);
                }
            }).start();
        }

        public void removeMealFromFavourites(Meal meal) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // set meal as favourite
                    dao.updateMealFavouriteStatus(meal.getIdMeal(), false);
                    // add to favs
                    dao.removeMealFromFavourites(meal);
                }
            }).start();
        }
    }

    static class RoomLocalPlannedRepository {
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
                    meal.setPlannedMealDate(date);
                    if (!dao.isMealInDB(meal.getIdMeal())) {
                        dao.insertPlannedMeal(meal);
                    }
//                    dao.updateMealPlannedStatus(meal.getIdMeal(), date);
                }
            }).start();
        }

        public void removeFromPlannedMeals(Meal meal) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    meal.setPlannedMealDate("");
//                    dao.updateMealPlannedStatus(meal.getIdMeal(), "");
                }
            }).start();
        }
    }
}
