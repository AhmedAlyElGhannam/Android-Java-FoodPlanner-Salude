package com.example.salude.model.local;

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
        // insert
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        void insertFavouriteMeal(Meal meal);

        // get
        @Query("SELECT * FROM meals WHERE isFavouriteMeal = 1")
        LiveData<List<Meal>> getFavouriteMeals();

        // remove
        @Delete
        void removeMealFromFavourites(Meal meal);

        // isFav
        @Query("SELECT isFavouriteMeal FROM meals WHERE idMeal = :id")
        boolean isMealFavourite(String id);

        // statUpdate
        @Query("UPDATE meals SET isFavouriteMeal = :state WHERE idMeal = :id")
        void updateMealFavouriteStatus(String id, boolean state);

    }

    @Dao
    interface PlannedMealDAO {
        // insert
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        void insertPlannedMeal(Meal meal);

        // get
        @Query("SELECT * FROM meals WHERE plannedMealDate IS NOT NULL")
        LiveData<List<Meal>> getPlannedMeals();

        // remove
        @Delete
        void removeMealFromPlanned(Meal meal);

        // isPlanned
        @Query("SELECT plannedMealDate FROM meals WHERE idMeal = :id")
        boolean isMealPlanned(String id);

        // statUpdate
        @Query("UPDATE meals SET plannedMealDate = :date WHERE idMeal = :id")
        void updateMealPlannedStatus(String id, String date);
    }


}
