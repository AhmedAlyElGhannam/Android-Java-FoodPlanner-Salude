package com.example.salude.model.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.salude.model.pojo.Meal;

import java.util.List;

public interface MealDAO {

    @Dao
    interface FavouriteMealDAO {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(Meal meal);

        // get all favorites for current user
        @Query("SELECT * FROM meals WHERE isFavouriteMeal = 1 AND userID = :userId")
        LiveData<List<Meal>> getFavouriteMeals(String userId);

        // remove favorite for current user
        @Query("DELETE FROM meals WHERE idMeal = :mealId AND userID = :userId")
        void removeMealFromFavourites(String mealId, String userId);

        // isFav for current user
        @Query("SELECT isFavouriteMeal FROM meals WHERE idMeal = :id AND userID = :userId")
        boolean isMealFavourite(String id, String userId);

        // statUpdate for current user
        @Query("UPDATE meals SET isFavouriteMeal = :state WHERE idMeal = :id AND userID = :userId")
        void updateMealFavouriteStatus(String id, boolean state, String userId);

        // if meal exists in db for current user
        @Query("SELECT COUNT(*) > 0 FROM meals WHERE idMeal = :id AND userID = :userId")
        boolean isMealInDB(String id, String userId);

    }

    @Dao
    interface PlannedMealDAO {
        // insert
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        void insertPlannedMeal(Meal meal);

        // get all planned meals for current user
        @Query("SELECT * FROM meals WHERE plannedMealDate IS NOT NULL AND userID = :userId")
        LiveData<List<Meal>> getPlannedMeals(String userId);

        // remove planned meal for current user
        @Query("DELETE FROM meals WHERE idMeal = :mealId AND userID = :userId")
        void removeMealFromPlanned(String mealId, String userId);

        // isPlanned for current user
        @Query("SELECT plannedMealDate FROM meals WHERE idMeal = :id AND userID = :userId")
        String isMealPlanned(String id, String userId);

        // statUpdate for current user
        @Query("UPDATE meals SET plannedMealDate = :date WHERE idMeal = :id AND userID = :userId")
        void updateMealPlannedStatus(String id, String date, String userId);

        // if meal exists in db for current user
        @Query("SELECT COUNT(*) > 0 FROM meals WHERE idMeal = :id AND userID = :userId")
        boolean isMealInDB(String id, String userId);
    }
}
