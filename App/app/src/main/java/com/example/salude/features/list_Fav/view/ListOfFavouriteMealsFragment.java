package com.example.salude.features.list_Fav.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.example.salude.contracts.ListOfFavouriteMealsContract;
import com.example.salude.features.list_Fav.presenter.ListOfFavouriteMealsPresenter;
import com.example.salude.features.main_screen.view.home.HomeFragmentMealAdapter;
import com.example.salude.features.main_screen.view.home.OnFavouriteClickListener;
import com.example.salude.features.main_screen.view.home.OnMealItemClickListener;
import com.example.salude.features.main_screen.view.home.OnPlannedClickListener;
import com.example.salude.features.mealdetails.view.MealDetailsFragment;
import com.example.salude.model.local.dao.RoomLocalDB;
import com.example.salude.model.local.repo.RoomLocalRepository;
import com.example.salude.model.pojo.Meal;

import java.util.ArrayList;
import java.util.List;

public class ListOfFavouriteMealsFragment extends Fragment implements ListOfFavouriteMealsContract.View, OnFavouriteClickListener, OnMealItemClickListener {



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
                RoomLocalRepository.RoomLocalFavouriteRepository.getInstance(RoomLocalDB.getInstance(getContext()).getFavouriteMealDAO()),
                RoomLocalRepository.RoomLocalPlannedRepository.getInstance(RoomLocalDB.getInstance(getContext()).getPlannedMealDAO()),
                getContext());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mealsRecyclerView.setLayoutManager(layoutManager);

        adapter = new ListOfFavouriteMealsAdapter(getContext(), null, this, this);
        mealsRecyclerView.setAdapter(adapter);
        presenter.getFavouriteMeals();
//        presenter.getAllCategories();
//        presenter.getMealOfTheDay();

    }

    @Override
    public void showFavouriteMeals(List<Meal> meals) {
        adapter.setMeals(meals);
        adapter.notifyDataSetChanged();
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
    public void showMealDetails(Meal meal) {

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onFavouriteClickListener(Meal meal) {

        presenter.removeMealFromFavourites(meal);
        Toast.makeText(getContext(), "Meal Removed from Favourites", Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMealItemClickListener(Meal meal) {
        // create the destination fragment object
        MealDetailsFragment fragment = new MealDetailsFragment();

        // create a bundle and put meal into it
        Bundle args = new Bundle();
        args.putParcelable("meal", meal); // For Parcelable

        // set the arguments
        fragment.setArguments(args);

        // perform fragment transaction
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
