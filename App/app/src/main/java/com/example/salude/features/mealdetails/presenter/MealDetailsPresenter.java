package com.example.salude.features.mealdetails.presenter;

import com.example.salude.contracts.MealDetailsContract;
import com.example.salude.model.local.datasource.LocalDataSource;
import com.example.salude.model.pojo.Meal;
import com.example.salude.model.repository.SaludRepository;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {
    private final MealDetailsContract.View view;

    private final MealDetailsContract.Model repo;

    public MealDetailsPresenter(MealDetailsContract.View view, MealDetailsContract.Model _repo) {
        this.view = view;
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
}