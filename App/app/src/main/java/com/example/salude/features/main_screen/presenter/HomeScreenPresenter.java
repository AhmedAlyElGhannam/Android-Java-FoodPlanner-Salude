package com.example.salude.features.main_screen.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.salude.contracts.HomeScreenContract;
import com.example.salude.features.plannedmeal.DatePickerDialogManager;
import com.example.salude.model.local.repo.RoomLocalRepository;
import com.example.salude.model.pojo.Category;
import com.example.salude.model.pojo.Meal;
import com.example.salude.model.remote.retrofit.callback.RemoteRetrofitCallback;
import com.example.salude.model.remote.retrofit.repository.RemoteRetrofitRepository;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeScreenPresenter implements HomeScreenContract.Presenter {
    private final HomeScreenContract.View view;
    private final RemoteRetrofitRepository remoteRepo;
    private final RoomLocalRepository.RoomLocalFavouriteRepository localFavRepo;
    private final RoomLocalRepository.RoomLocalPlannedRepository localPlanRepo;

    private final SharedPreferences sharedPreferences;
    private Context context;

    public HomeScreenPresenter(HomeScreenContract.View _view, RemoteRetrofitRepository _repo, RoomLocalRepository.RoomLocalFavouriteRepository _localRepo, RoomLocalRepository.RoomLocalPlannedRepository _localPlanRepo, Context _context) {
        context = _context;
        view = _view;
        remoteRepo = _repo;
        localFavRepo = _localRepo;
        localPlanRepo = _localPlanRepo;
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

        // If we get here, either date doesn't match or no saved meal exists
        remoteRepo.getMealOfTheDay(new RemoteRetrofitCallback.RemoteRetrofitMealCallback() {
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
    public void getAllCategories() {
        remoteRepo.getMealsCategories(new RemoteRetrofitCallback.RemoteRetrofitCategoryCallback() {
            @Override
            public void onSuccess(List<Category> categories) {
                if (categories != null && !categories.isEmpty()) {
                    view.showMealCategories(categories);
                } else {
                    Log.i("TAG", "Categories list is null or empty");
                    // Optionally, show an error message in the UI
                }
            }

            @Override
            public void onFailure(String err) {
                Log.i("TAG", "on failure");
            }
        });
    }

    @Override
    public void getFavouriteMealBtnStatus(Meal meal) {
        localFavRepo.getListOfFavouriteMeals().observe(view.getViewLifecycleOwner(), meals -> {
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
            view.updateFavouriteMealBtn(isFavorite);
        });
    }

    @Override
    public void addMealToFavourites(Meal meal) {
        localFavRepo.addMealToFavourites(meal);
    }

    @Override
    public void removeMealFromFavourites(Meal meal) {
        localFavRepo.removeMealFromFavourites(meal);
    }

    @Override
    public void getPlannedMealBtnStatus(Meal meal) {
        localPlanRepo.getListOfPlannedMeals().observe(view.getViewLifecycleOwner(), meals -> {
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
            view.updatePlannedMealBtn(isPlanned);
        });
    }

    @Override
    public void addMealToPlanned(Meal meal) {
        localPlanRepo.addToPlannedMeals(meal, meal.getPlannedMealDate());
        view.updatePlannedMealBtn(meal.getPlannedMealDate() != null);
    }

    @Override
    public void removeMealFromPlanned(Meal meal) {
        localPlanRepo.removeFromPlannedMeals(meal);
        view.updatePlannedMealBtn(false);
    }
}
