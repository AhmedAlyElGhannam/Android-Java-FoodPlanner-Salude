package com.example.salude.contracts;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.salude.model.pojo.Area;
import com.example.salude.model.pojo.Category;
import com.example.salude.model.pojo.FilteredMeal;
import com.example.salude.model.pojo.Ingredient;
import com.example.salude.model.pojo.Meal;
import com.example.salude.model.remote.retrofit.callback.RemoteRetrofitCallback;

import java.util.List;

public interface HomeScreenContract {
    public interface View {
        public void showMealOfTheDay(Meal meal);
        public LifecycleOwner getViewLifecycleOwner();
        public void addMealToCalendar(Meal meal);
        void updateFavoriteButton(boolean isFavorite);
        void updateCalendarButton(boolean isPlanned);
        void removeMealFromCalendar(Meal meal);
        public void performCalendarInsertion(Meal meal, long startMillis, long endMillis);
        public void onNetworkConnectionFailure();
        public void onNetworkConnectionSuccess();
    }

    public interface Presenter {
        public void getMealOfTheDay();
        public void addMealToFavourites(Meal meal);
        public void removeMealFromFavourites(Meal meal);
        public void addMealToPlanned(Meal meal);
        public void removeMealFromPlanned(Meal meal);
        public void checkFavoriteStatus(Meal meal);
        public void checkPlannedStatus(Meal meal);
        public void toggleFavorite(Meal meal);
        public void togglePlanned(Meal meal, String selectedDate);

        public void onAddMealToCalendarRequested(Meal meal);
        public long getPrimaryCalendarId(Context context);
    }

    public interface Model {
        public void getMealOfTheDay(RemoteRetrofitCallback.RemoteRetrofitMealCallback cbf);
        public void addMealToFavourites(Meal meal);
        public void removeMealFromFavourites(Meal meal);
        public void addToPlannedMeals(Meal meal, String date);
        public void removeFromPlannedMeals(Meal meal);
        public LiveData<List<Meal>> getListOfFavouriteMeals();
        public LiveData<List<Meal>> getListOfPlannedMeals();

        }
}
