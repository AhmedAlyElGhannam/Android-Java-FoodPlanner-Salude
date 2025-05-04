package com.example.salude.contracts;

import androidx.lifecycle.LifecycleOwner;

import com.example.salude.model.pojo.Meal;

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
}