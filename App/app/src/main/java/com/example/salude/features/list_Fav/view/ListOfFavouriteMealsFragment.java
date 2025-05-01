package com.example.salude.features.list_Fav.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salude.R;
import com.example.salude.contracts.HomeScreenContract;
import com.example.salude.contracts.ListOfFavouriteMealsContract;
import com.example.salude.features.list_Fav.presenter.ListOfFavouriteMealsPresenter;
import com.example.salude.features.main_screen.view.home.HomeFragmentMealAdapter;
import com.example.salude.features.main_screen.view.home.OnFavouriteClickListener;
import com.example.salude.features.main_screen.view.home.OnPlannedClickListener;
import com.example.salude.model.local.dao.RoomLocalDB;
import com.example.salude.model.local.repo.RoomLocalRepository;
import com.example.salude.model.pojo.Meal;

import java.util.ArrayList;
import java.util.List;

public class ListOfFavouriteMealsFragment extends Fragment implements ListOfFavouriteMealsContract.View, OnFavouriteClickListener, OnPlannedClickListener {



    private ListOfFavouriteMealsContract.Presenter presenter;
    private ListOfFavouriteMealsAdapter adapter;
    private RecyclerView mealsRecyclerView;
    ImageButton addToFavBtn;
    ImageButton addToCalBtn;
    ImageView mealThumbnailImg;
    TextView mealNameTxt;
    TextView mealCategoryTxt;
    TextView mealCountryTxt;
    TextView mealOfTheDayTxt;
    TextView txtMealCategoriesLabel;
    ConstraintLayout mealItemLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.list_of_fav_meals, container, false);

        mealsRecyclerView = view.findViewById(R.id.listOfFavMealsRecyclerView);
        presenter = new ListOfFavouriteMealsPresenter(this,
                RoomLocalRepository.RoomLocalFavouriteRepository.getInstance(
                        RoomLocalDB.getInstance(getContext()).getFavouriteMealDAO()
                ));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mealsRecyclerView.setLayoutManager(layoutManager);

        adapter = new ListOfFavouriteMealsAdapter(getContext(), null, this);
        mealsRecyclerView.setAdapter(adapter);
//        presenter.getAllCategories();
//        presenter.getMealOfTheDay();

    }

    @Override
    public void showFavouriteMeals(List<Meal> meals) {

    }

    @Override
    public void showEmptyState() {

    }

    @NonNull
    @Override
    public LifecycleOwner getViewLifecycleOwner() {
        return this;
    }

    @Override
    public void onFavouriteClickListener() {

    }

    @Override
    public void onPlannedClickListener() {

    }
}
