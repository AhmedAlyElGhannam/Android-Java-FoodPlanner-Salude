package com.example.salude.features.main_screen.fragments.search;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.util.Objects;

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

        searchResRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        layoutManager1.setOrientation(RecyclerView.VERTICAL);
        searchResRecyclerView.setLayoutManager(layoutManager1);

        presenter.getAllCategories();
        presenter.getMealOfTheDay();
        presenter.getAllIngredients();

        // only show it once a search/category is selected
        searchResLabel.setVisibility(View.INVISIBLE);


        areaChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaChip.setChecked(true);
                // set adapter && display search results + label
                Toast.makeText(getContext(), "Area Chip", Toast.LENGTH_SHORT).show();
            }
        });

        categoryChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryChip.setChecked(true);
                // set adapter && display search results + label
                Toast.makeText(getContext(), "Category Chip", Toast.LENGTH_SHORT).show();
            }
        });

        ingredientChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientChip.setChecked(true);
                // set adapter && display search results + label
                Toast.makeText(getContext(), "Ingredient Chip", Toast.LENGTH_SHORT).show();
            }
        });


        searchTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                    String query = Objects.requireNonNull(searchTxt.getText()).toString().trim();
                    if (!query.isEmpty()) {
                        // perform search
                    }
                    else {
                        // toast to tell user to input sth
                        Toast.makeText(getContext(), "Write sth to search for", Toast.LENGTH_SHORT).show();
                    }
                    // close the keyboard after search
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null && getActivity().getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }
                    else {
                        // ?!
                    }
                    return true;
                }
                return false;
            }
        });
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