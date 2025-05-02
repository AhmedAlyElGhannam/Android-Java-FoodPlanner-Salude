package com.example.salude.contracts;

import androidx.lifecycle.LifecycleOwner;

import com.example.salude.model.pojo.Category;
import com.example.salude.model.pojo.Meal;

import java.util.List;

public interface ListOfFavouriteMealsContract {
    interface View {
        void showFavouriteMeals(List<Meal> meals);
        void showEmptyState();
        public LifecycleOwner getViewLifecycleOwner();
        void showMealDetails(Meal meal);
    }

    interface Presenter {
        void getFavouriteMeals();
        void removeMealFromFavourites(Meal meal);
    }
}