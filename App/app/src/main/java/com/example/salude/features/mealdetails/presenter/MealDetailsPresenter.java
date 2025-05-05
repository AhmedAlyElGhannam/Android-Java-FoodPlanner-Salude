package com.example.salude.features.mealdetails.presenter;

import com.example.salude.contracts.MealDetailsContract;
import com.example.salude.model.local.datasource.LocalDataSource;
import com.example.salude.model.pojo.Meal;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {
    private final MealDetailsContract.View view;
    private final LocalDataSource.RoomLocalFavouriteRepository favRepo;
    private final LocalDataSource.RoomLocalPlannedRepository planRepo;

    public MealDetailsPresenter(MealDetailsContract.View view,
                                LocalDataSource.RoomLocalFavouriteRepository favRepo,
                                LocalDataSource.RoomLocalPlannedRepository planRepo) {
        this.view = view;
        this.favRepo = favRepo;
        this.planRepo = planRepo;
    }

    @Override
    public void checkFavoriteStatus(Meal meal) {
        favRepo.getListOfFavouriteMeals().observe(view.getViewLifecycleOwner(), meals -> {
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
        planRepo.getListOfPlannedMeals().observe(view.getViewLifecycleOwner(), meals -> {
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
            favRepo.removeMealFromFavourites(meal);
        } else {
            favRepo.addMealToFavourites(meal);
        }
        view.updateFavoriteButton(meal.getIsFavouriteMeal());
    }

    @Override
    public void togglePlanned(Meal meal, String selectedDate) {
        if (selectedDate != null) {
            meal.setPlannedMealDate(selectedDate);
            planRepo.addToPlannedMeals(meal, selectedDate);
            view.updateCalendarButton(true);
        } else {
            meal.setPlannedMealDate(null);
            planRepo.removeFromPlannedMeals(meal);
            view.updateCalendarButton(false);
        }
    }
}