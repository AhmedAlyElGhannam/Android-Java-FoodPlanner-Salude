package com.example.salude.contracts;

import androidx.lifecycle.LifecycleOwner;

import com.example.salude.model.pojo.Meal;

import java.util.List;

public interface ListOfPlannedMealsContract {
    interface View {
        void showPlannedMeals(List<Meal> meals);
        void showEmptyState();
        public LifecycleOwner getViewLifecycleOwner();
        void showMealDetails(Meal meal);
    }

    interface Presenter {
        void getPlannedMeals();
        void removeMealFromPlanned(Meal meal);
    }
}
