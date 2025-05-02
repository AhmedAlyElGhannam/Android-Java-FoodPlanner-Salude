package com.example.salude.contracts;

import androidx.lifecycle.LifecycleOwner;

import com.example.salude.model.pojo.Category;
import com.example.salude.model.pojo.Meal;

import java.util.List;

public interface HomeScreenContract {
    public interface View {
        public void showMealOfTheDay(Meal meal);

        public void showMealCategories(List<Category> categories);

        public void updateFavouriteMealBtn(boolean state);

        public LifecycleOwner getViewLifecycleOwner();
        public void updatePlannedMealBtn(boolean state);
    }

    public interface Presenter {
        public void getMealOfTheDay();
        public void getAllCategories();
        public void getFavouriteMealBtnStatus(Meal meal);
        public void addMealToFavourites(Meal meal);
        public void removeMealFromFavourites(Meal meal);
        public void getPlannedMealBtnStatus(Meal meal);
        public void addMealToPlanned(Meal meal);
        public void removeMealFromPlanned(Meal meal);
    }

}
