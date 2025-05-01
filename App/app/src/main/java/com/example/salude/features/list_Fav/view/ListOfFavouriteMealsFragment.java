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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salude.R;
import com.example.salude.contracts.HomeScreenContract;
import com.example.salude.features.list_Fav.presenter.ListOfFavouriteMealsPresenter;
import com.example.salude.features.main_screen.view.home.HomeFragmentMealAdapter;

import java.util.ArrayList;

public class ListOfFavouriteMealsFragment extends Fragment {


    ListOfFavouriteMealsPresenter presenter;
    ListOfFavouriteMealsAdapter adapter;
    RecyclerView mealsRecyclerView;
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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mealsRecyclerView.setLayoutManager(layoutManager);

//        adapter = new HomeFragmentMealAdapter(getContext(), new ArrayList<>(), null, null, null);
//        mealCategoriesRecyclerView.setAdapter(adapter);
//        presenter.getAllCategories();
//        presenter.getMealOfTheDay();

    }
}
