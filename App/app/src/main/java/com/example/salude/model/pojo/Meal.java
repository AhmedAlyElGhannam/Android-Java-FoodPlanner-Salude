package com.example.salude.model.pojo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "meals")
public class Meal {
    @PrimaryKey
    @NonNull
    private String idMeal;

    // for marking favourite meals
    private boolean isFavouriteMeal;

    // for marking planned meals
    private String plannedMealDate;
}
