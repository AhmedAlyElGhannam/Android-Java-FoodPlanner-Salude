package com.example.salude.features.main_screen.fragments.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
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
import androidx.core.content.ContextCompat;
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
import com.example.salude.utils.clicklistener.OnIngredientClickListener;
import com.example.salude.utils.network.OnNetworkConnectionListener;
import com.example.salude.utils.plannedmeal.DatePickerDialogManager;
import com.example.salude.model.local.dao.RoomLocalDB;
import com.example.salude.model.local.datasource.LocalDataSource;
import com.example.salude.model.pojo.Category;
import com.example.salude.model.pojo.Meal;
import com.example.salude.model.remote.retrofit.client.RemoteRetrofitClient;
import com.example.salude.model.remote.retrofit.datasource.RemoteDataSource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.Manifest;


import com.bumptech.glide.Glide;
import com.example.salude.utils.clicklistener.OnFavouriteClickListener;
import com.example.salude.utils.clicklistener.OnMealItemClickListener;
import com.example.salude.utils.clicklistener.OnPlannedClickListener;

public class HomeFragment extends Fragment
        implements HomeScreenContract.View,
        OnFavouriteClickListener, OnPlannedClickListener,
        OnMealItemClickListener, OnAreaClickListener,
        OnCategoryClickListener, OnIngredientClickListener,
        OnNetworkConnectionListener {
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
    private static final int CALENDAR_PERMISSION_REQUEST_CODE = 101;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        presenter = new HomeScreenPresenter(this, RemoteDataSource.getInstance(RemoteRetrofitClient.getInstance(getContext())),
                LocalDataSource.RoomLocalFavouriteRepository.getInstance(RoomLocalDB.getInstance(getContext()).getFavouriteMealDAO()),
                LocalDataSource.RoomLocalPlannedRepository.getInstance(RoomLocalDB.getInstance(getContext()).getPlannedMealDAO()),
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
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out) // enter, exit
                    .addToBackStack(null)
                    .commit();
        });

        // Calendar button click
        addToCalBtn.setOnClickListener(v -> {
            if (!hasCalendarPermissions()) {
                requestCalendarPermissions();
                return;
            }

            if (mealOfTheDay.getPlannedMealDate() == null) {
                DatePickerDialogManager.showDatePickerDialog(getContext(), selectedDate -> {
                    mealOfTheDay.setPlannedMealDate(selectedDate);
                    presenter.addMealToPlanned(mealOfTheDay);
                    addMealToCalendar(mealOfTheDay);
                    Toast.makeText(getContext(), "Meal Scheduled for " + selectedDate, Toast.LENGTH_SHORT).show();
                });
            } else {
                // Add confirmation dialog before unscheduling
                new AlertDialog.Builder(getContext())
                        .setTitle("Unscheduled Meal")
                        .setMessage("Are you sure you want to unschedule this meal?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            mealOfTheDay.setPlannedMealDate(null);
                            presenter.removeMealFromPlanned(mealOfTheDay);
                            removeMealFromCalendar(mealOfTheDay);
                            Toast.makeText(getContext(), "Meal Unscheduled", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No", null)
                        .show();
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

    @Override
    public void onNetworkConnectionSuccess() {
        if (presenter != null) {
            presenter.getAllCategories();
            presenter.getMealOfTheDay();
            presenter.getAllIngredients();
            presenter.getAllAreas();
        }
    }

    @Override
    public void onNetworkConnectionFailure() {

    }

    @Override
    public void addMealToCalendar(Meal meal) {
        // Make sure the app has calendar permissions before this
        long calendarId = getPrimaryCalendarId();
        if (calendarId == -1) return;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = sdf.parse(meal.getPlannedMealDate());
            if (date == null) return;

            long startMillis = date.getTime();
            long endMillis = startMillis + 60 * 60 * 1000;

            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, endMillis);
            values.put(CalendarContract.Events.TITLE, meal.getStrMeal());
            values.put(CalendarContract.Events.DESCRIPTION, "Planned meal: " + meal.getStrMeal());
            values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

            Uri uri = requireContext().getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
            if (uri != null) {
                long eventId = Long.parseLong(uri.getLastPathSegment());

                // Save eventId tied to the meal ID
                SharedPreferences prefs = requireContext().getSharedPreferences("MealCalendarPrefs", Context.MODE_PRIVATE);
                prefs.edit().putLong("event_" + meal.getIdMeal(), eventId).apply();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getPrimaryCalendarId() {
        Cursor cursor = requireContext().getContentResolver().query(
                CalendarContract.Calendars.CONTENT_URI,
                new String[]{CalendarContract.Calendars._ID},
                CalendarContract.Calendars.IS_PRIMARY + "=1",
                null, null
        );

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getLong(0);
                }
            } finally {
                cursor.close();
            }
        }
        return -1;
    }

    private void removeMealFromCalendar(Meal meal) {
        SharedPreferences prefs = requireContext().getSharedPreferences("MealCalendarPrefs", Context.MODE_PRIVATE);
        long eventId = prefs.getLong("event_" + meal.getIdMeal(), -1);
        if (eventId != -1) {
            Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
            requireContext().getContentResolver().delete(deleteUri, null, null);

            // Remove saved ID
            prefs.edit().remove("event_" + meal.getIdMeal()).apply();
        }
    }

    private boolean hasCalendarPermissions() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCalendarPermissions() {
        requestPermissions(
                new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
                CALENDAR_PERMISSION_REQUEST_CODE
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALENDAR_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, but you might need to retry the calendar action here manually if needed
                Toast.makeText(getContext(), "Calendar permissions granted. Tap again to schedule.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Calendar permissions are required to schedule meals.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
