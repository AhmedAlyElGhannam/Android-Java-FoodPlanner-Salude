package com.example.salude.contracts;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

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

    interface Model {
        LiveData<List<Meal>> getListOfPlannedMeals();
        void removeFromPlannedMeals(Meal meal);
    }
}
