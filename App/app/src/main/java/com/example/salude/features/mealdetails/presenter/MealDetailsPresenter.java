package com.example.salude.features.mealdetails.presenter;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.util.Log;

import com.example.salude.contracts.MealDetailsContract;
import com.example.salude.model.local.datasource.LocalDataSource;
import com.example.salude.model.pojo.Meal;
import com.example.salude.model.repository.SaludRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {
    private final MealDetailsContract.View view;

    private final MealDetailsContract.Model repo;

    public MealDetailsPresenter(MealDetailsContract.View _view, MealDetailsContract.Model _repo) {
        view = _view;
        repo = _repo;
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
    public String extractYouTubeId(String url) {
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\\?video_id=)([^#\\&\\?\\n]*)";
        Matcher matcher = Pattern.compile(pattern).matcher(url);
        return matcher.find() ? matcher.group() : null;
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