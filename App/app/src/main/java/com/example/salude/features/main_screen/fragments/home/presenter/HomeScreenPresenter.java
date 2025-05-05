package com.example.salude.features.main_screen.fragments.home.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.util.Log;

import com.example.salude.contracts.HomeScreenContract;
import com.example.salude.model.pojo.Meal;
import com.example.salude.model.remote.retrofit.callback.RemoteRetrofitCallback;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeScreenPresenter implements HomeScreenContract.Presenter {
    private final HomeScreenContract.View view;
    private final HomeScreenContract.Model repo;

    private final SharedPreferences sharedPreferences;
    private Context context;

    public HomeScreenPresenter(HomeScreenContract.View _view, HomeScreenContract.Model _repo, Context _context) {
        context = _context;
        view = _view;
        repo = _repo;
        sharedPreferences = _context.getSharedPreferences("Meal_Preferences", Context.MODE_PRIVATE);
    }

    @Override
    public void getMealOfTheDay() {
        String savedDate = sharedPreferences.getString("mealDate", "");
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (savedDate.equals(today)) {
            // get meal from shared preferences instead of fetching a new one
            String mealJson = sharedPreferences.getString("mealOfTheDay", null);
            if (mealJson != null) {
                try {
                    Meal savedMeal = new Gson().fromJson(mealJson, Meal.class);
                    if (savedMeal != null) {
                        view.showMealOfTheDay(savedMeal);
                        return;
                    }
                } catch (Exception e) {
                    Log.e("HomeScreenPresenter", "Error parsing saved meal", e);
                }
            }
        }

        repo.getMealOfTheDay(new RemoteRetrofitCallback.RemoteRetrofitMealCallback() {
            @Override
            public void onSuccess(List<Meal> meals) {
                if (meals != null && !meals.isEmpty()) {
                    Meal todaysMeal = meals.get(0);
                    view.showMealOfTheDay(todaysMeal);

                    // save meal and current date
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("mealOfTheDay", new Gson().toJson(todaysMeal));
                    editor.putString("mealDate", today); // Store today's date
                    editor.apply();
                }
            }

            @Override
            public void onFailure(String err) {
                Log.e("HomeScreenPresenter", "Failed to get meal of the day: " + err);
            }
        });
    }

    @Override
    public void addMealToFavourites(Meal meal) {
        repo.addMealToFavourites(meal);
    }

    @Override
    public void removeMealFromFavourites(Meal meal) {
        repo.removeMealFromFavourites(meal);
    }

    @Override
    public void addMealToPlanned(Meal meal) {
        repo.addToPlannedMeals(meal, meal.getPlannedMealDate());
        view.updateFavoriteButton(true);
    }

    @Override
    public void removeMealFromPlanned(Meal meal) {
        repo.removeFromPlannedMeals(meal);
        view.updateCalendarButton(false);
    }

    @Override
    public void checkFavoriteStatus(Meal meal) {
        repo.getListOfFavouriteMeals().observe(view.getViewLifecycleOwner(), meals -> {
            boolean isFavorite = false;
            if (meals != null) {
                for (Meal m : meals) {
                    if (m.getIdMeal().equals(meal.getIdMeal())) {
                        isFavorite = true;
                        break;
                    }
                }
            }
            meal.setIsFavouriteMeal(isFavorite);
            view.updateFavoriteButton(isFavorite);
        });
    }

    @Override
    public void checkPlannedStatus(Meal meal) {
        repo.getListOfPlannedMeals().observe(view.getViewLifecycleOwner(), meals -> {
            boolean isPlanned = false;
            String plannedDate = null;
            if (meals != null) {
                for (Meal m : meals) {
                    if (m.getIdMeal().equals(meal.getIdMeal())) {
                        isPlanned = true;
                        plannedDate = m.getPlannedMealDate();
                        break;
                    }
                }
            }
            meal.setPlannedMealDate(plannedDate);
            view.updateCalendarButton(isPlanned);
        });
    }

    @Override
    public void toggleFavorite(Meal meal) {
        if (meal.getIsFavouriteMeal()) {
            repo.removeMealFromFavourites(meal);
        } else {
            repo.addMealToFavourites(meal);
        }
        view.updateFavoriteButton(meal.getIsFavouriteMeal());
    }

    @Override
    public void togglePlanned(Meal meal, String selectedDate) {
        if (selectedDate != null) {
            meal.setPlannedMealDate(selectedDate);
            repo.addToPlannedMeals(meal, selectedDate);
            view.updateCalendarButton(true);
        } else {
            meal.setPlannedMealDate(null);
            repo.removeFromPlannedMeals(meal);
            view.updateCalendarButton(false);
        }
    }

    @Override
    public void onAddMealToCalendarRequested(Meal meal) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = sdf.parse(meal.getPlannedMealDate());
            if (date == null) return;

            long startMillis = date.getTime();
            long endMillis = startMillis + 60 * 60 * 1000;

            view.performCalendarInsertion(meal, startMillis, endMillis);
        } catch (Exception e) {
            Log.i("TAG", "MealDetailsPresenter - onAddMealToCalendarRequested: caught an exception " + e.getMessage());
        }
    }

    @Override
    public long getPrimaryCalendarId(Context context) {
        Cursor cursor = context.getContentResolver().query(
                CalendarContract.Calendars.CONTENT_URI,
                new String[]{CalendarContract.Calendars._ID},
                CalendarContract.Calendars.IS_PRIMARY + "=1",
                null, null
        );

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getLong(0);
                }
            } finally {
                cursor.close();
            }
        }
        return -1;
    }
}
