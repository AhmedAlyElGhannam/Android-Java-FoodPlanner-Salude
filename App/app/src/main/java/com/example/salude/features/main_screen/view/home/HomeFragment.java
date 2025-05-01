package com.example.salude.features.main_screen.view.home;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salude.R;
import com.example.salude.contracts.HomeScreenContract;
import com.example.salude.features.main_screen.presenter.HomeScreenPresenter;
import com.example.salude.features.mealdetails.view.MealDetailsFragment;
import com.example.salude.model.pojo.Category;
import com.example.salude.model.pojo.Meal;
import com.example.salude.model.remote.retrofit.client.RemoteRetrofitClient;
import com.example.salude.model.remote.retrofit.repository.RemoteRetrofitRepository;

import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.Glide;

public class HomeFragment extends Fragment implements HomeScreenContract.View, OnFavouriteClickListener, OnPlannedClickListener, OnMealItemClickListener{
    HomeScreenContract.Presenter presenter;
    HomeFragmentMealAdapter adapter;
//    RecyclerView mealOfTheDayRecyclerView;
    RecyclerView mealCategoriesRecyclerView;

    ImageButton addToFavBtn;
    ImageButton addToCalBtn;
    ImageView mealThumbnailImg;
    TextView mealNameTxt;
    TextView mealCategoryTxt;
    TextView mealCountryTxt;
    TextView mealOfTheDayTxt;
    TextView txtMealCategoriesLabel;
    ConstraintLayout mealItemLayout;
    Meal mealOfTheDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        addToFavBtn = view.findViewById(R.id.btnAddToFavourites);
        addToCalBtn = view.findViewById(R.id.btnAddToCalendar);
        mealThumbnailImg = view.findViewById(R.id.imgMeal);
        mealNameTxt = view.findViewById(R.id.txtMealName);
        mealCategoryTxt = view.findViewById(R.id.txtCategory);
        mealCountryTxt = view.findViewById(R.id.txtCountry);
        mealCategoriesRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView2);
        mealItemLayout = view.findViewById(R.id.mealItemLayout);
        mealOfTheDayTxt = view.findViewById(R.id.textViewMealOfTheDay);
        txtMealCategoriesLabel = view.findViewById(R.id.txtMealCategoriesLabel);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealCategoriesRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        mealCategoriesRecyclerView.setLayoutManager(layoutManager2);


        presenter = new HomeScreenPresenter(this, RemoteRetrofitRepository.getInstance(RemoteRetrofitClient.getInstance()), getContext());
        adapter = new HomeFragmentMealAdapter(getContext(), new ArrayList<>(), null, null, null);
        mealCategoriesRecyclerView.setAdapter(adapter);
        presenter.getAllCategories();
        presenter.getMealOfTheDay();

        txtMealCategoriesLabel.setText("Meal Categories");
        mealOfTheDayTxt.setText("Meal of The Day");

        mealItemLayout.setOnClickListener(v -> {
            // create the destination fragment object
            MealDetailsFragment fragment = new MealDetailsFragment();

            // create a bundle and put meal into it
            Bundle args = new Bundle();
            args.putParcelable("meal", mealOfTheDay); // For Parcelable

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
            Toast.makeText(getContext(), "Add to calendar", Toast.LENGTH_SHORT).show();

        });

        // Favourites button click
        addToFavBtn.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Added to favourites", Toast.LENGTH_SHORT).show();

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
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showMealCategories(List<Category> categories) {
        adapter.setCategories(categories);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onFavouriteClickListener() {

    }

    @Override
    public void onMealItemClickListener() {

    }

    @Override
    public void onPlannedClickListener() {

    }
}
