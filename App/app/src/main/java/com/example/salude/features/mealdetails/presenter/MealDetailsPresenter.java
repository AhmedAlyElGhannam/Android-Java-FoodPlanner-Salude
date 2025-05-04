package com.example.salude.features.mealdetails.presenter;

import android.content.Context;

import com.example.salude.contracts.MealDetailsContract;
import com.example.salude.features.mealdetails.view.MealDetailsFragment;
import com.example.salude.model.local.repo.RoomLocalRepository;
import com.example.salude.model.pojo.Meal;
import com.example.salude.utils.sessionmanager.UserSessionManager;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {
    private final MealDetailsContract.View view;
    private final RoomLocalRepository.RoomLocalFavouriteRepository favRepo;
    private final RoomLocalRepository.RoomLocalPlannedRepository planRepo;
    private UserSessionManager sessionManager;


    public MealDetailsPresenter(MealDetailsContract.View view,
                                RoomLocalRepository.RoomLocalFavouriteRepository favRepo,
                                RoomLocalRepository.RoomLocalPlannedRepository planRepo, Context context) {
        this.view = view;
        this.favRepo = favRepo;
        this.planRepo = planRepo;
        sessionManager = new UserSessionManager(context);
    }

    @Override
    public void checkFavoriteStatus(Meal meal) {
        String userID = sessionManager.getUserId();
        favRepo.getListOfFavouriteMeals(userID).observe(view.getViewLifecycleOwner(), meals -> {
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
        String userID = sessionManager.getUserId();
        planRepo.getListOfPlannedMeals(userID).observe(view.getViewLifecycleOwner(), meals -> {
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
            planRepo.addToPlannedMeals(meal, selectedDate, meal.getUserID());
            view.updateCalendarButton(true);
        } else {
            meal.setPlannedMealDate(null);
            planRepo.removeFromPlannedMeals(meal);
            view.updateCalendarButton(false);
        }
    }
}