package com.example.salude.contracts;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.example.salude.model.pojo.Area;
import com.example.salude.model.pojo.Category;
import com.example.salude.model.pojo.FilteredMeal;
import com.example.salude.model.pojo.Ingredient;
import com.example.salude.model.pojo.Meal;

import java.util.List;

public interface MainScreenContract {
    public interface View {
        public void showMealOfTheDay(Meal meal);
        public void showMealCategories(List<Category> categories);
        public void showMealIngredients(List<Ingredient> ingredients);
        public void showMealAreas(List<Area> areas);
        public void updateFavouriteMealBtn(boolean state);
        public LifecycleOwner getViewLifecycleOwner();
        public void updatePlannedMealBtn(boolean state);
        public void showFilteredMeals(List<FilteredMeal> filteredMeals);
        public void showMealDetails(Meal meal);
        public void showMealSearchFailure(String err);
        public void showMealWithName(List<Meal> meals);
        public void addMealToCalendar(Meal meal);
        void updateFavoriteButton(boolean isFavorite);
        void updateCalendarButton(boolean isPlanned);
        void removeMealFromCalendar(Meal meal);
        public void performCalendarInsertion(Meal meal, long startMillis, long endMillis);
        public void showConnectionRestoredAnimation();
        public void showConnectionLostFragment();
    }

    public interface Presenter {
        public void getMealOfTheDay();
        public void getAllCategories();
        public void getAllAreas();
        public void getAllIngredients();
        public void getFavouriteMealBtnStatus(Meal meal);
        public void addMealToFavourites(Meal meal);
        public void removeMealFromFavourites(Meal meal);
        public void getPlannedMealBtnStatus(Meal meal);
        public void addMealToPlanned(Meal meal);
        public void removeMealFromPlanned(Meal meal);
        public void getMealsFilteredByIngredient(String ingredient);
        public void getMealsFilteredByArea(String area);
        public void getMealsFilteredByCategory(String category);
        public void getMealsFilteredByFirstLetter(String str);
        public void getMealByName(String str);








        public void onAddMealToCalendarRequested(Meal meal);
        public long getPrimaryCalendarId(Context context);
    }

}
