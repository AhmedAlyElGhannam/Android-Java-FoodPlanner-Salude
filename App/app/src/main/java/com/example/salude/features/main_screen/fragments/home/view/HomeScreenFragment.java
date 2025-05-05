package com.example.salude.features.main_screen.fragments.home.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import com.example.salude.R;
import com.example.salude.contracts.HomeScreenContract;
import com.example.salude.features.main_screen.fragments.home.presenter.HomeScreenPresenter;
import com.example.salude.features.mealdetails.view.MealDetailsFragment;
import com.example.salude.model.repository.SaludRepository;
import com.example.salude.utils.plannedmeal.DatePickerDialogManager;
import com.example.salude.model.pojo.Meal;

import java.util.TimeZone;

import android.Manifest;


import com.bumptech.glide.Glide;

public class HomeScreenFragment extends Fragment
        implements HomeScreenContract.View {
    HomeScreenPresenter presenter;
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
    // calendar permission request code (needed by requestPermissions)
    private static final int CALENDAR_PERMISSION_REQUEST_CODE = 101;

    // string array for required permissions (needed by requestPermissions)
    private static final String[] CALENDAR_PERMISSIONS = {
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        presenter = new HomeScreenPresenter(this, SaludRepository.getInstance(requireContext()), requireContext());

        addToFavBtn = view.findViewById(R.id.btnAddToFavourites);
        addToCalBtn = view.findViewById(R.id.btnAddToCalendar);
        mealThumbnailImg = view.findViewById(R.id.imgMeal);
        mealNameTxt = view.findViewById(R.id.txtMealName);
        mealCategoryTxt = view.findViewById(R.id.txtCategory);
        mealCountryTxt = view.findViewById(R.id.txtCountry);
        mealItemLayout = view.findViewById(R.id.mealItemLayout);
        mealOfTheDayTxt = view.findViewById(R.id.textViewMealOfTheDay);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // fetch meal of the day
        presenter.getMealOfTheDay();

        // meal of the day label
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

        // calender button click listener
        addToCalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if app does not have permissions request it before clicking again
                if (!(
                        ContextCompat.checkSelfPermission(requireContext(), CALENDAR_PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED
                                &&
                                ContextCompat.checkSelfPermission(requireContext(), CALENDAR_PERMISSIONS[1]) == PackageManager.PERMISSION_GRANTED
                )) {
                    requestPermissions(CALENDAR_PERMISSIONS, CALENDAR_PERMISSION_REQUEST_CODE);
                    return;
                }

                // if previous planned status is empty
                if (mealOfTheDay.getPlannedMealDate() == null) {
                    // show date picker dialog
                    DatePickerDialogManager.showDatePickerDialog(getContext(), selectedDate -> {
                        // pass meal && selected date to presenter
                        presenter.togglePlanned(mealOfTheDay, selectedDate);
                        // add meal to phone calendar
                        addMealToCalendar(mealOfTheDay);
                        // toast message describing operation
                        Toast.makeText(getContext(), "Meal Scheduled for " + selectedDate, Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // show alert dialog before unscheduling
                    new AlertDialog.Builder(getContext())
                            .setTitle("Unscheduled Meal")
                            .setMessage("Are you sure you want to unschedule this meal?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // nullify date field in meal
                                    presenter.togglePlanned(mealOfTheDay, null);
                                    // remove meal from calendar
                                    removeMealFromCalendar(mealOfTheDay);
                                    // toast action describing action
                                    Toast.makeText(getContext(), "Meal Unscheduled", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            }
        });

        // fav button click listener
        addToFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // toast message depending on the NEXT fav state of meal
                if (!mealOfTheDay.getIsFavouriteMeal()) {
                    Toast.makeText(getContext(), "Meal Added to Favourites", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Meal Removed from Favourites", Toast.LENGTH_SHORT).show();
                }

                // toggle meal state
                presenter.toggleFavorite(mealOfTheDay);
            }
        });
    }

    @Override
    public void updateFavoriteButton(boolean isFavorite) {
        addToFavBtn.setImageResource(
                isFavorite ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border
        );
    }

    @Override
    public void updateCalendarButton(boolean isPlanned) {
        addToCalBtn.setImageResource(
                isPlanned ? R.drawable.ic_calendar_filled : R.drawable.ic_calendar_border
        );
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showMealOfTheDay(Meal meal) {
        mealOfTheDay = meal;
        mealNameTxt.setText(meal.getStrMeal());
        mealCategoryTxt.setText(meal.getStrCategory());
        mealCountryTxt.setText(meal.getStrArea());
        Glide.with(getContext()).load(meal.getStrMealThumb()).into(mealThumbnailImg);
        presenter.checkFavoriteStatus(mealOfTheDay);
        presenter.checkPlannedStatus(mealOfTheDay);
    }

    @Override
    public void addMealToCalendar(Meal meal) {
        presenter.onAddMealToCalendarRequested(meal);
    }

    @Override
    public void performCalendarInsertion(Meal meal, long startMillis, long endMillis) {
        long calendarId = presenter.getPrimaryCalendarId(requireContext());
        if (calendarId == -1) {
            return;
        }

        try {
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

                SharedPreferences prefs = requireContext().getSharedPreferences("MealCalendarPrefs", Context.MODE_PRIVATE);
                prefs.edit().putLong("event_" + meal.getIdMeal(), eventId).apply();
            }
        } catch (Exception e) {
            Log.i("TAG", "MealDetailsFragment - performDetailsPresenter: caught an exception " + e.getMessage());

        }
    }

    @Override
    public void removeMealFromCalendar(Meal meal) {
        SharedPreferences prefs = requireContext().getSharedPreferences("MealCalendarPrefs", Context.MODE_PRIVATE);
        long eventId = prefs.getLong("event_" + meal.getIdMeal(), -1);
        if (eventId != -1) {
            Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
            requireContext().getContentResolver().delete(deleteUri, null, null);

            prefs.edit().remove("event_" + meal.getIdMeal()).apply();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // calendar access request answer (will be granted cuz app has permissions)
        if (requestCode == CALENDAR_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Calendar permissions granted. Tap again to schedule.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Calendar permissions are required to schedule meals.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @NonNull
    @Override
    public LifecycleOwner getViewLifecycleOwner() {
        return this;
    }

    @Override
    public void onNetworkConnectionSuccess() {
        if (presenter != null) {
            presenter.getMealOfTheDay();
        }
    }

    @Override
    public void onNetworkConnectionFailure() {
        // do nothing :)
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

}
