package com.example.salude.features.list_Fav.presenter;

import android.content.Context;
import android.util.Log;

import com.example.salude.contracts.ListOfFavouriteMealsContract;
import com.example.salude.model.local.repo.RoomLocalRepository;
import com.example.salude.model.pojo.Meal;
import com.example.salude.utils.sessionmanager.UserSessionManager;

public class ListOfFavouriteMealsPresenter implements ListOfFavouriteMealsContract.Presenter {
    private final ListOfFavouriteMealsContract.View view;
    private final RoomLocalRepository.RoomLocalFavouriteRepository localFavRepo;
    private final RoomLocalRepository.RoomLocalPlannedRepository localPlanRepo;
    private final Context context;
    private UserSessionManager sessionManager;


    public ListOfFavouriteMealsPresenter(ListOfFavouriteMealsContract.View _view,
                                         RoomLocalRepository.RoomLocalFavouriteRepository _localRepo, RoomLocalRepository.RoomLocalPlannedRepository _localPlanRepo, Context _context) {
        view = _view;
        localFavRepo = _localRepo;
        localPlanRepo = _localPlanRepo;
        context = _context;
        sessionManager = new UserSessionManager(context);
    }

    @Override
    public void getFavouriteMeals() {
        String userID = sessionManager.getUserId();
        localFavRepo.getListOfFavouriteMeals(userID).observe(view.getViewLifecycleOwner(), meals -> {
            if (meals != null) {
                if (!meals.isEmpty()) {
                    meals.get(0).setUserID(userID);
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
