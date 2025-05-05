package com.example.salude.features.list_Fav.presenter;

import android.content.Context;

import com.example.salude.contracts.ListOfFavouriteMealsContract;
import com.example.salude.model.local.datasource.LocalDataSource;
import com.example.salude.model.pojo.Meal;

public class ListOfFavouriteMealsPresenter implements ListOfFavouriteMealsContract.Presenter {
    private final ListOfFavouriteMealsContract.View view;
    private final LocalDataSource.RoomLocalFavouriteRepository localFavRepo;
    private final LocalDataSource.RoomLocalPlannedRepository localPlanRepo;
    private final Context context;

    public ListOfFavouriteMealsPresenter(ListOfFavouriteMealsContract.View _view,
                                         LocalDataSource.RoomLocalFavouriteRepository _localRepo, LocalDataSource.RoomLocalPlannedRepository _localPlanRepo, Context _context) {
        view = _view;
        localFavRepo = _localRepo;
        localPlanRepo = _localPlanRepo;
        context = _context;
    }

    @Override
    public void getFavouriteMeals() {
        localFavRepo.getListOfFavouriteMeals().observe(view.getViewLifecycleOwner(), meals -> {
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
        localFavRepo.removeMealFromFavourites(meal);
    }
}
