package com.example.salude.contracts;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.salude.model.pojo.Category;
import com.example.salude.model.pojo.Meal;

import java.util.List;

public interface ListOfFavouriteMealsContract {
    interface View {
        public void showFavouriteMeals(List<Meal> meals);
        public void showEmptyState();
        public LifecycleOwner getViewLifecycleOwner();
    }

    interface Presenter {
        public void getFavouriteMeals();
        public void removeMealFromFavourites(Meal meal);
    }

    interface Model {
        public LiveData<List<Meal>> getListOfFavouriteMeals();
        public void removeMealFromFavourites(Meal meal);
    }
}