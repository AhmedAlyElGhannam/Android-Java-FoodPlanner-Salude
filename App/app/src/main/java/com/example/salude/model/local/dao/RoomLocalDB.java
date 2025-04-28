package com.example.salude.model.local.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.salude.model.pojo.Meal;

@Database(entities = {Meal.class}, version = 1)
public abstract class RoomLocalDB extends RoomDatabase {
    public abstract MealDAO.FavouriteMealDAO getFavouriteMealDAO();
    public abstract MealDAO.PlannedMealDAO getPlannedMealDAO();

    private static RoomLocalDB db;

    public static RoomLocalDB getInstance(Context context) {
        // maybe make it synchronized
        if (db == null) {
            db = Room.databaseBuilder(context.getApplicationContext(), RoomLocalDB.class, "meals").build();
        }
        return db;
    }

}
