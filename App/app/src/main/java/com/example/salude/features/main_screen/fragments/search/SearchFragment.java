package com.example.salude.features.main_screen.fragments.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salude.R;
import com.example.salude.contracts.HomeScreenContract;
import com.example.salude.features.main_screen.fragments.home.MealAreaAdapter;
import com.example.salude.features.main_screen.fragments.home.MealCategoryAdapter;
import com.example.salude.features.main_screen.fragments.home.MealIngredientsAdapter;
import com.example.salude.features.main_screen.presenter.HomeScreenPresenter;
import com.example.salude.model.local.dao.RoomLocalDB;
import com.example.salude.model.local.repo.RoomLocalRepository;
import com.example.salude.model.pojo.Area;
import com.example.salude.model.pojo.Category;
import com.example.salude.model.pojo.FilteredMeal;
import com.example.salude.model.pojo.Ingredient;
import com.example.salude.model.pojo.Meal;
import com.example.salude.model.remote.retrofit.client.RemoteRetrofitClient;
import com.example.salude.model.remote.retrofit.repository.RemoteRetrofitRepository;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements HomeScreenContract.View {
    public SearchFragment() { }

    TextInputEditText searchTxt;
    Chip ingredientChip;
    Chip areaChip;
    Chip categoryChip;
    TextView searchResLabel; // hide it by default
    RecyclerView searchResRecyclerView;
    MealAreaAdapter areaAdapter;
    MealIngredientsAdapter ingredientsAdapter;
    MealCategoryAdapter categoryAdapter;
    HomeScreenPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        presenter = new HomeScreenPresenter(this, RemoteRetrofitRepository.getInstance(RemoteRetrofitClient.getInstance()),
                RoomLocalRepository.RoomLocalFavouriteRepository.getInstance(RoomLocalDB.getInstance(getContext()).getFavouriteMealDAO()),
                RoomLocalRepository.RoomLocalPlannedRepository.getInstance(RoomLocalDB.getInstance(getContext()).getPlannedMealDAO()),
                getContext());

        areaAdapter = new MealAreaAdapter(getContext());
        categoryAdapter = new MealCategoryAdapter(getContext());
        ingredientsAdapter = new MealIngredientsAdapter(getContext());

        searchTxt = view.findViewById(R.id.edtSearch);
        ingredientChip = view.findViewById(R.id.chipIngredient);
        areaChip = view.findViewById(R.id.chipArea);
        categoryChip = view.findViewById(R.id.chipCategory);
        searchResLabel = view.findViewById(R.id.txtSearchResultsLabel);
        searchResRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewSearchResults);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void showMealOfTheDay(Meal meal) {

    }

    @Override
    public void showMealCategories(List<Category> categories) {

    }

    @Override
    public void showMealIngredients(List<Ingredient> ingredients) {

    }

    @Override
    public void showMealAreas(List<Area> areas) {

    }

    @Override
    public void updateFavouriteMealBtn(boolean state) {

    }

    @Override
    public void updatePlannedMealBtn(boolean state) {

    }

    @Override
    public void showFilteredMeals(List<FilteredMeal> filteredMeals) {

    }
}