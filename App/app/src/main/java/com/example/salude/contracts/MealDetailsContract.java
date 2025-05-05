package com.example.salude.contracts;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.salude.model.pojo.Meal;

import java.util.List;

public interface MealDetailsContract {
    interface View {
        public LifecycleOwner getViewLifecycleOwner();
        void updateFavoriteButton(boolean isFavorite);
        void updateCalendarButton(boolean isPlanned);
        void addMealToCalendar(Meal meal);
        void removeMealFromCalendar(Meal meal);
    }

    interface Presenter {
        void checkFavoriteStatus(Meal meal);
        void checkPlannedStatus(Meal meal);
        void toggleFavorite(Meal meal);
        void togglePlanned(Meal meal, String selectedDate);
    }

    interface Model {
        public void addToPlannedMeals(Meal meal, String date);

        public void removeFromPlannedMeals(Meal meal);

        public LiveData<List<Meal>> getListOfPlannedMeals();

        public void addMealToFavourites(Meal meal);

        public void removeMealFromFavourites(Meal meal);

        public LiveData<List<Meal>> getListOfFavouriteMeals();
    }
}