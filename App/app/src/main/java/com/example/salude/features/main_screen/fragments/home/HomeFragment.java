package com.example.salude.features.main_screen.fragments.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salude.R;
import com.example.salude.contracts.HomeScreenContract;
import com.example.salude.features.main_screen.presenter.HomeScreenPresenter;
import com.example.salude.features.main_screen.view.MealAreaAdapter;
import com.example.salude.features.main_screen.view.MealCategoryAdapter;
import com.example.salude.features.main_screen.view.MealIngredientsAdapter;
import com.example.salude.features.mealdetails.view.MealDetailsFragment;
import com.example.salude.model.pojo.Area;
import com.example.salude.model.pojo.FilteredMeal;
import com.example.salude.model.pojo.Ingredient;
import com.example.salude.utils.clicklistener.OnAreaClickListener;
import com.example.salude.utils.clicklistener.OnCategoryClickListener;
import com.example.salude.utils.clicklistener.OnFilteredMealItemClickListener;
import com.example.salude.utils.clicklistener.OnIngredientClickListener;
import com.example.salude.utils.plannedmeal.DatePickerDialogManager;
import com.example.salude.model.local.dao.RoomLocalDB;
import com.example.salude.model.local.repo.RoomLocalRepository;
import com.example.salude.model.pojo.Category;
import com.example.salude.model.pojo.Meal;
import com.example.salude.model.remote.retrofit.client.RemoteRetrofitClient;
import com.example.salude.model.remote.retrofit.repository.RemoteRetrofitRepository;

import java.util.List;
import com.bumptech.glide.Glide;
import com.example.salude.utils.clicklistener.OnFavouriteClickListener;
import com.example.salude.utils.clicklistener.OnMealItemClickListener;
import com.example.salude.utils.clicklistener.OnPlannedClickListener;

public class HomeFragment extends Fragment implements HomeScreenContract.View, OnFavouriteClickListener, OnPlannedClickListener, OnMealItemClickListener, OnAreaClickListener, OnCategoryClickListener, OnIngredientClickListener {
    HomeScreenContract.Presenter presenter;
    MealCategoryAdapter categoryAdapter;
    MealAreaAdapter areaAdapter;
    MealIngredientsAdapter ingredientsAdapter;
//    RecyclerView mealOfTheDayRecyclerView;
    RecyclerView mealCategoriesRecyclerView;
    RecyclerView mealAreasRecyclerView;
    RecyclerView mealIngredientsRecyclerView;

    ImageButton addToFavBtn;
    ImageButton addToCalBtn;
    ImageView mealThumbnailImg;
    TextView mealNameTxt;
    TextView mealCategoryTxt;
    TextView mealCountryTxt;
    TextView mealOfTheDayTxt;
    TextView txtMealCategoriesLabel;
    TextView txtMealAreasLabel;
    TextView txtMealIngredientsLabel;
    ConstraintLayout mealItemLayout;
    Meal mealOfTheDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        presenter = new HomeScreenPresenter(this, RemoteRetrofitRepository.getInstance(RemoteRetrofitClient.getInstance(getContext())),
                RoomLocalRepository.RoomLocalFavouriteRepository.getInstance(RoomLocalDB.getInstance(getContext()).getFavouriteMealDAO()),
                RoomLocalRepository.RoomLocalPlannedRepository.getInstance(RoomLocalDB.getInstance(getContext()).getPlannedMealDAO()),
                getContext());

        categoryAdapter = new MealCategoryAdapter(getContext(), this);
        areaAdapter = new MealAreaAdapter(getContext(), this);
        ingredientsAdapter = new MealIngredientsAdapter(getContext(), this);

        addToFavBtn = view.findViewById(R.id.btnAddToFavourites);
        addToCalBtn = view.findViewById(R.id.btnAddToCalendar);
        mealThumbnailImg = view.findViewById(R.id.imgMeal);
        mealNameTxt = view.findViewById(R.id.txtMealName);
        mealCategoryTxt = view.findViewById(R.id.txtCategory);
        mealCountryTxt = view.findViewById(R.id.txtCountry);
        mealCategoriesRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewCategories);
        mealAreasRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewAreas);
        mealIngredientsRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewIngredients);
        mealItemLayout = view.findViewById(R.id.mealItemLayout);
        mealOfTheDayTxt = view.findViewById(R.id.textViewMealOfTheDay);
        txtMealCategoriesLabel = view.findViewById(R.id.txtMealCategoriesLabel);
        txtMealAreasLabel = view.findViewById(R.id.txtMealAreasLabel);
        txtMealIngredientsLabel = view.findViewById(R.id.txtMealIngredientsLabel);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealCategoriesRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        layoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        mealCategoriesRecyclerView.setLayoutManager(layoutManager1);

        mealAreasRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        mealAreasRecyclerView.setLayoutManager(layoutManager2);

        mealIngredientsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext());
        layoutManager3.setOrientation(RecyclerView.HORIZONTAL);
        mealIngredientsRecyclerView.setLayoutManager(layoutManager3);

        mealCategoriesRecyclerView.setAdapter(categoryAdapter);
        mealAreasRecyclerView.setAdapter(areaAdapter);
        mealIngredientsRecyclerView.setAdapter(ingredientsAdapter);

        presenter.getAllCategories();
        presenter.getMealOfTheDay();
        presenter.getAllIngredients();
        presenter.getAllAreas();

        txtMealAreasLabel.setText("Meal Areas");
        txtMealIngredientsLabel.setText("Meal Ingredients");
        txtMealCategoriesLabel.setText("Meal Categories");
        mealOfTheDayTxt.setText("Meal of The Day");

        mealItemLayout.setOnClickListener(v -> {
            // create the destination fragment object
            MealDetailsFragment fragment = new MealDetailsFragment();

            // create a bundle and put meal into it
            Bundle args = new Bundle();
            args.putParcelable("meal", mealOfTheDay);

            // set the arguments
            fragment.setArguments(args);

            // perform fragment transaction
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Calendar button click
        addToCalBtn.setOnClickListener(v -> {
            // save fav in db (toggle to undo it)
            if (mealOfTheDay.getPlannedMealDate() == null) {
                // Open date picker dialog to set a new date
                DatePickerDialogManager.showDatePickerDialog(getContext(), selectedDate -> {
                    mealOfTheDay.setPlannedMealDate(selectedDate);
                    presenter.addMealToPlanned(mealOfTheDay);
                    Toast.makeText(getContext(), "Meal Scheduled for " + selectedDate, Toast.LENGTH_SHORT).show();
                });
            }
            else {
                // Clear the date
                mealOfTheDay.setPlannedMealDate(null);
                presenter.removeMealFromPlanned(mealOfTheDay);
                Toast.makeText(getContext(), "Meal Unscheduled", Toast.LENGTH_SHORT).show();
            }
        });

        // Favourites button click
        addToFavBtn.setOnClickListener(v -> {
            // save fav in db (toggle to undo it)
            if (mealOfTheDay.getIsFavouriteMeal()) {
                // set it to false and remove its flag + make button hollow
                presenter.removeMealFromFavourites(mealOfTheDay);
                Toast.makeText(getContext(), "Meal Removed from Favourites", Toast.LENGTH_SHORT).show();
            }
            else {
                // set it to true and set its flag + make button filled
                presenter.addMealToFavourites(mealOfTheDay);
                Toast.makeText(getContext(), "Meal Added to Favourites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showMealOfTheDay(Meal meal) {
        mealOfTheDay = meal;
        mealNameTxt.setText(meal.getStrMeal());
        mealCategoryTxt.setText(meal.getStrCategory());
        mealCountryTxt.setText(meal.getStrArea());
        Glide.with(getContext()).load(meal.getStrMealThumb()).into(mealThumbnailImg);
        presenter.getFavouriteMealBtnStatus(mealOfTheDay);
        presenter.getPlannedMealBtnStatus(mealOfTheDay);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showMealCategories(List<Category> categories) {
        categoryAdapter.setCategories(categories);
        categoryAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showMealIngredients(List<Ingredient> ingredients) {
        ingredientsAdapter.setIngredients(ingredients);
        ingredientsAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showMealAreas(List<Area> areas) {
        Log.i("TAG", "showMealAreas: " + areas);
        areaAdapter.setAreas(areas);
        areaAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateFavouriteMealBtn(boolean state) {
        addToFavBtn.setImageResource(
                mealOfTheDay.getIsFavouriteMeal() ?
                        R.drawable.ic_favorite_filled :
                        R.drawable.ic_favorite_border
        );
    }


    @Override
    public void onFavouriteClickListener(Meal meal) {

    }

    @Override
    public void onMealItemClickListener(Meal meal) {

    }

    @Override
    public void onPlannedClickListener(Meal meal) {

    }

    @NonNull
    @Override
    public LifecycleOwner getViewLifecycleOwner() {
        return this;
    }

    @Override
    public void updatePlannedMealBtn(boolean state) {
        addToCalBtn.setImageResource(
                state ?
                        R.drawable.ic_calendar_filled :
                        R.drawable.ic_calendar_border
        );
    }

    @Override
    public void showFilteredMeals(List<FilteredMeal> filteredMeals) {

    }

    @Override
    public void showMealDetails(Meal meal) {

    }

    @Override
    public void showMealSearchFailure(String err) {

    }

    @Override
    public void showMealWithName(List<Meal> meals) {

    }

    @Override
    public void showMealsWithFirstLetter(List<FilteredMeal> meals) {

    }

    @Override
    public void onAreaClickListener(String area) {

    }

    @Override
    public void onCategoryClickListener(String category) {

    }

    @Override
    public void onIngredientClickListener(String ingredient) {

    }

}
