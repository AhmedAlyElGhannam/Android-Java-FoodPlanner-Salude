package com.example.salude.features.main_screen.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.salude.contracts.HomeScreenContract;
import com.example.salude.model.local.repo.RoomLocalRepository;
import com.example.salude.model.pojo.Area;
import com.example.salude.model.pojo.Category;
import com.example.salude.model.pojo.FilteredMeal;
import com.example.salude.model.pojo.Ingredient;
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
                }
                else {
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
    public void getAllAreas() {
        remoteRepo.getMealAreas(new RemoteRetrofitCallback.RemoteRetrofitAreaCallback() {
            @Override
            public void onSuccess(List<Area> areas) {
                if (areas != null && !areas.isEmpty()) {
                    view.showMealAreas(areas);
                    Log.i("TAG", "onSuccess: " + areas);
                }
                else {
                    Log.i("TAG", "Areas list is null or empty");
                }
            }

            @Override
            public void onFailure(String err) {
                Log.i("TAG", "on failure");
            }
        });
    }

    @Override
    public void getAllIngredients() {
        remoteRepo.getMealsIngredients(new RemoteRetrofitCallback.RemoteRetrofitIngredientCallback() {
            @Override
            public void onSuccess(List<Ingredient> ingredients) {
                if (ingredients != null && !ingredients.isEmpty()) {
                    view.showMealIngredients(ingredients);
                }
                else {
                    Log.i("TAG", "Categories list is null or empty");
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

    @Override
    public void getMealsFilteredByIngredient(String ingredient) {
        remoteRepo.getMealsFilteredByIngredient(new RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback(){
            @Override
            public void onSuccess(List<FilteredMeal> filteredMeals) {
                if (filteredMeals != null && !filteredMeals.isEmpty()) {
                    view.showFilteredMeals(filteredMeals);
                }
                else {
                    Log.i("TAG", "Meals filtered by ingredient list is null or empty");
                }
            }

            @Override
            public void onFailure(String err) {
                Log.i("TAG", "on failure");
            }
        }, ingredient);
    }

    @Override
    public void getMealsFilteredByArea(String area) {
        remoteRepo.getMealsFilteredByArea(new RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback() {
            @Override
            public void onSuccess(List<FilteredMeal> filteredMeals) {
                if (filteredMeals != null && !filteredMeals.isEmpty()) {
                    view.showFilteredMeals(filteredMeals);
                }
                else {
                    Log.i("TAG", "Meals filtered by area list is null or empty");
                }
            }

            @Override
            public void onFailure(String err) {
                Log.i("TAG", "on failure");
            }
        }, area);
    }

    @Override
    public void getMealsFilteredByCategory(String category) {
        remoteRepo.getMealsFilteredByCategory(new RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback() {
            @Override
            public void onSuccess(List<FilteredMeal> filteredMeals) {
                if (filteredMeals != null && !filteredMeals.isEmpty()) {
                    view.showFilteredMeals(filteredMeals);
                }
                else {
                    Log.i("TAG", "Meals filtered by category list is null or empty");
                }
            }

            @Override
            public void onFailure(String err) {
                Log.i("TAG", "on failure");
            }
        }, category);
    }

    @Override
    public void getMealsFilteredByFirstLetter(String str) {
        remoteRepo.getMealsFilteredByFirstLetter(new RemoteRetrofitCallback.RemoteRetrofitFilteredMealCallback() {
            @Override
            public void onSuccess(List<FilteredMeal> filteredMeals) {
                view.showFilteredMeals(filteredMeals);
            }

            @Override
            public void onFailure(String err) {
                view.showMealSearchFailure(err);
            }
        }, str);
    }

    @Override
    public void getMealByName(String str) {
        remoteRepo.getMealByName(new RemoteRetrofitCallback.RemoteRetrofitMealCallback() {
            @Override
            public void onSuccess(List<Meal> meals) {
                view.showMealWithName(meals);
            }

            @Override
            public void onFailure(String err) {
                view.showMealSearchFailure(err);
            }
        }, str);
    }

    public void getMealByID(String id) {
        remoteRepo.getMealByID(new RemoteRetrofitCallback.RemoteRetrofitMealCallback() {
            @Override
            public void onSuccess(List<Meal> meals) {
                // it returned a list so I will assume the first is the one I want
                view.showMealDetails(meals.get(0));
            }

            @Override
            public void onFailure(String err) {
                view.showMealSearchFailure(err);
            }
        }, id);
    }


}
