package com.example.salude.features.list_Fav.presenter;

import android.content.Context;

import com.example.salude.contracts.ListOfFavouriteMealsContract;
import com.example.salude.model.local.datasource.LocalDataSource;
import com.example.salude.model.pojo.Meal;
import com.example.salude.model.repository.SaludRepository;

public class ListOfFavouriteMealsPresenter implements ListOfFavouriteMealsContract.Presenter {
    private final ListOfFavouriteMealsContract.View view;
    private final ListOfFavouriteMealsContract.Model repo;

    public ListOfFavouriteMealsPresenter(ListOfFavouriteMealsContract.View _view, ListOfFavouriteMealsContract.Model _repo) {
        view = _view;
        repo = _repo;
    }

    @Override
    public void getFavouriteMeals() {
        repo.getListOfFavouriteMeals().observe(view.getViewLifecycleOwner(), meals -> {
            if (meals != null) {
                if (!meals.isEmpty()) {
                    view.showFavouriteMeals(meals);
                } else {
                    view.showEmptyState();
                }
            }
        });
    }

    @Override
    public void removeMealFromFavourites(Meal meal) {
        repo.removeMealFromFavourites(meal);
    }
}
