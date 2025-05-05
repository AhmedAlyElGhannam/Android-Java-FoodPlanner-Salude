package com.example.salude.features.list_plan.presenter;

import android.content.Context;

import com.example.salude.contracts.ListOfPlannedMealsContract;
import com.example.salude.model.local.datasource.LocalDataSource;
import com.example.salude.model.pojo.Meal;

public class ListOfPlannedMealsPresenter implements ListOfPlannedMealsContract.Presenter {
    private final ListOfPlannedMealsContract.View view;
    private final ListOfPlannedMealsContract.Model repo;

    public ListOfPlannedMealsPresenter(ListOfPlannedMealsContract.View _view, ListOfPlannedMealsContract.Model _repo) {
        view = _view;
        repo = _repo;
    }

    @Override
    public void getPlannedMeals() {
        repo.getListOfPlannedMeals().observe(view.getViewLifecycleOwner(), meals -> {
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
        repo.removeFromPlannedMeals(meal);
    }
}

