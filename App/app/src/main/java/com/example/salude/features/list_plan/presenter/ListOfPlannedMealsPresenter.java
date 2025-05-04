package com.example.salude.features.list_plan.presenter;

import android.content.Context;

import com.example.salude.contracts.ListOfFavouriteMealsContract;
import com.example.salude.contracts.ListOfPlannedMealsContract;
import com.example.salude.model.local.repo.RoomLocalRepository;
import com.example.salude.model.pojo.Meal;

public class ListOfPlannedMealsPresenter implements ListOfPlannedMealsContract.Presenter {
    private final ListOfPlannedMealsContract.View view;
    private final RoomLocalRepository.RoomLocalFavouriteRepository localFavRepo;
    private final RoomLocalRepository.RoomLocalPlannedRepository localPlanRepo;
    private final Context context;

    public ListOfPlannedMealsPresenter(ListOfPlannedMealsContract.View _view,
                                         RoomLocalRepository.RoomLocalFavouriteRepository _localRepo, RoomLocalRepository.RoomLocalPlannedRepository _localPlanRepo, Context _context) {
        view = _view;
        localFavRepo = _localRepo;
        localPlanRepo = _localPlanRepo;
        context = _context;
    }

    @Override
    public void getPlannedMeals() {
        localPlanRepo.getListOfPlannedMeals().observe(view.getViewLifecycleOwner(), meals -> {
            if (meals != null) {
                if (!meals.isEmpty()) {
                    view.showPlannedMeals(meals);
                } else {
                    view.showEmptyState();
                }
            }
        });
    }

    @Override
    public void removeMealFromPlanned(Meal meal) {
        localPlanRepo.removeFromPlannedMeals(meal);
    }

//    @Override
//    public void togglePlanned(Meal meal, String selectedDate) {
//        if (selectedDate != null) {
//            meal.setPlannedMealDate(selectedDate);
//            localPlanRepo.addToPlannedMeals(meal, selectedDate);
//            view.updateCalendarButton(true);
//        } else {
//            meal.setPlannedMealDate(null);
//            localPlanRepo.removeFromPlannedMeals(meal);
//            view.updateCalendarButton(false);
//        }
//    }
}

