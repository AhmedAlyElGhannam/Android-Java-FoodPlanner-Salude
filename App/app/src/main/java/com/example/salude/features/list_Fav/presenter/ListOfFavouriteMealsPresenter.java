package com.example.salude.features.list_Fav.presenter;

import android.content.Context;
import android.util.Log;

import com.example.salude.contracts.ListOfFavouriteMealsContract;
import com.example.salude.model.local.repo.RoomLocalRepository;
import com.example.salude.model.pojo.Meal;

public class ListOfFavouriteMealsPresenter implements ListOfFavouriteMealsContract.Presenter {
    private final ListOfFavouriteMealsContract.View view;
    private final RoomLocalRepository.RoomLocalFavouriteRepository localFavRepo;

    public ListOfFavouriteMealsPresenter(ListOfFavouriteMealsContract.View _view,
                                         RoomLocalRepository.RoomLocalFavouriteRepository _localRepo) {
        view = _view;
        localFavRepo = _localRepo;
    }

    @Override
    public void getFavouriteMeals() {
        localFavRepo.getListOfFavouriteMeals().observe(view.getViewLifecycleOwner(), meals -> {
            if (meals != null && !meals.isEmpty()) {
                view.showFavouriteMeals(meals);
            } else {
                view.showEmptyState();
            }
        });
    }

    @Override
    public void removeMealFromFavourites(Meal meal) {
        localFavRepo.removeMealFromFavourites(meal);
    }
}
