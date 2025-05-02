package com.example.salude.features.list_plan.view;

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
import com.example.salude.contracts.ListOfPlannedMealsContract;
import com.example.salude.features.list_Fav.presenter.ListOfFavouriteMealsPresenter;
import com.example.salude.features.list_Fav.view.ListOfFavouriteMealsAdapter;
import com.example.salude.features.list_plan.presenter.ListOfPlannedMealsPresenter;
import com.example.salude.features.main_screen.view.home.OnFavouriteClickListener;
import com.example.salude.features.main_screen.view.home.OnMealItemClickListener;
import com.example.salude.features.main_screen.view.home.OnPlannedClickListener;
import com.example.salude.features.mealdetails.view.MealDetailsFragment;
import com.example.salude.model.local.dao.RoomLocalDB;
import com.example.salude.model.local.repo.RoomLocalRepository;
import com.example.salude.model.pojo.Meal;

import java.util.ArrayList;
import java.util.List;

public class ListOfPlannedMealsFragment extends Fragment implements ListOfPlannedMealsContract.View, OnPlannedClickListener, OnMealItemClickListener {



    private ListOfPlannedMealsContract.Presenter presenter;
    private ListOfPlannedMealsAdapter adapter;
    private RecyclerView mealsRecyclerView;
    TextView favMealsid;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.list_of_planned_meals, container, false);

        mealsRecyclerView = view.findViewById(R.id.listOfPlannedMealsRecyclerView);
        favMealsid = view.findViewById(R.id.plannedMealsid);
        presenter = new ListOfPlannedMealsPresenter(this,
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

        favMealsid.setText("Planned Meals");

        adapter = new ListOfPlannedMealsAdapter(getContext(), null, this, this);
        mealsRecyclerView.setAdapter(adapter);
        presenter.getPlannedMeals();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showPlannedMeals(List<Meal> meals) {
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

    @Override
    public void showMealDetails(Meal meal) {

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

    @Override
    public void onPlannedClickListener(Meal meal) {
        presenter.removeMealFromPlanned(meal);
        Toast.makeText(getContext(), "Meal Unscheduled", Toast.LENGTH_SHORT).show();
    }
}

