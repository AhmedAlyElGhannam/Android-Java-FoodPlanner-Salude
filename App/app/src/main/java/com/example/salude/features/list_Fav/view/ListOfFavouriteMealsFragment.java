package com.example.salude.features.list_Fav.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salude.R;
import com.example.salude.contracts.ListOfFavouriteMealsContract;
import com.example.salude.features.list_Fav.presenter.ListOfFavouriteMealsPresenter;
import com.example.salude.model.repository.SaludRepository;
import com.example.salude.utils.clicklistener.OnFavouriteClickListener;
import com.example.salude.utils.clicklistener.OnMealItemClickListener;
import com.example.salude.features.mealdetails.view.MealDetailsFragment;
import com.example.salude.model.local.dao.RoomLocalDB;
import com.example.salude.model.local.datasource.LocalDataSource;
import com.example.salude.model.pojo.Meal;

import java.util.ArrayList;
import java.util.List;

public class ListOfFavouriteMealsFragment extends Fragment implements ListOfFavouriteMealsContract.View, OnFavouriteClickListener, OnMealItemClickListener {



    private ListOfFavouriteMealsContract.Presenter presenter;
    private ListOfFavouriteMealsAdapter adapter;
    private RecyclerView mealsRecyclerView;
    TextView favMealsid;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.list_of_fav_meals, container, false);

        mealsRecyclerView = view.findViewById(R.id.listOfFavMealsRecyclerView);
        favMealsid = view.findViewById(R.id.favMealsid);
        presenter = new ListOfFavouriteMealsPresenter(this, SaludRepository.getInstance(requireContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mealsRecyclerView.setLayoutManager(layoutManager);

        favMealsid.setText("Favourite Meals");

        adapter = new ListOfFavouriteMealsAdapter(getContext(), this, this);
        mealsRecyclerView.setAdapter(adapter);
        presenter.getFavouriteMeals();

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showFavouriteMeals(List<Meal> meals) {
        adapter.setMeals(meals);
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showEmptyState() {
        adapter.setMeals(new ArrayList<>());
        adapter.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LifecycleOwner getViewLifecycleOwner() {
        return this;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onFavouriteClickListener(Meal meal) {
        presenter.removeMealFromFavourites(meal);
        Toast.makeText(getContext(), "Meal Removed from Favourites", Toast.LENGTH_SHORT).show();
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
