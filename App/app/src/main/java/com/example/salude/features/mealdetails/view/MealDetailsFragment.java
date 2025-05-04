package com.example.salude.features.mealdetails.view;

import android.Manifest;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.salude.R;
import com.example.salude.contracts.MealDetailsContract;
import com.example.salude.features.mealdetails.presenter.MealDetailsPresenter;
import com.example.salude.utils.plannedmeal.DatePickerDialogManager;
import com.example.salude.model.local.dao.RoomLocalDB;
import com.example.salude.model.local.repo.RoomLocalRepository;
import com.example.salude.model.pojo.Meal;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MealDetailsFragment extends Fragment implements MealDetailsContract.View {

    Meal meal;
    private ImageView ivMeal;
    private ImageButton btnFavorite;
    private ImageButton btnAddToCalendar;
    private TextView tvMealName;
    private TextView tvCategory;
    private TextView tvArea;
    private TextView tvInstructions;
    private YouTubePlayerView youtubePlayerView;
    private RecyclerView rvIngredients;
    private IngredientsAdapter adapter;
    private MealDetailsPresenter presenter;
    // Add these constants at the top of the class
    private static final int CALENDAR_PERMISSION_REQUEST_CODE = 101;
    private static final String[] CALENDAR_PERMISSIONS = {
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
    };

    // Add this method to check permissions
    private boolean hasCalendarPermissions() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED;
    }

    // Add this method to request permissions
    private void requestCalendarPermissions() {
        requestPermissions(CALENDAR_PERMISSIONS, CALENDAR_PERMISSION_REQUEST_CODE);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.full_meal_details, container, false);

        presenter = new MealDetailsPresenter(this,
                RoomLocalRepository.RoomLocalFavouriteRepository.getInstance(RoomLocalDB.getInstance(getContext()).getFavouriteMealDAO()),
                RoomLocalRepository.RoomLocalPlannedRepository.getInstance(RoomLocalDB.getInstance(getContext()).getPlannedMealDAO()));

        // Retrieve the Meal object from the Bundle
        if (getArguments() != null) {
            meal = getArguments().getParcelable("meal");
            if (meal == null) {
                Toast.makeText(getContext(), "Meal is NULL?!!", Toast.LENGTH_SHORT).show();
                Log.i("TAG", "onCreateView: meal is NULL?!!");
            }
            else {
                ivMeal = view.findViewById(R.id.ivMeal);
                btnFavorite = view.findViewById(R.id.btnFavorite);
                btnAddToCalendar = view.findViewById(R.id.btnAddToCalendar);
                tvMealName = view.findViewById(R.id.tvMealName);
                tvCategory = view.findViewById(R.id.tvCategory);
                tvArea = view.findViewById(R.id.tvArea);
                tvInstructions = view.findViewById(R.id.tvInstructions);
                youtubePlayerView = view.findViewById(R.id.youtubePlayerView);
                rvIngredients = view.findViewById(R.id.rvIngredients);
            }
        }

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.checkFavoriteStatus(meal);
        presenter.checkPlannedStatus(meal);

        rvIngredients.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvIngredients.setLayoutManager(layoutManager);

        adapter = new IngredientsAdapter(getContext());
        rvIngredients.setAdapter(adapter);
        adapter.setIngredientList(meal.getIngredientsList());
        adapter.notifyDataSetChanged();


        // Set meal image
        Glide.with(requireContext())
                .load(meal.getStrMealThumb())
                .into(ivMeal);

        // Set basic info
        tvMealName.setText(meal.getStrMeal());
        tvCategory.setText(meal.getStrCategory());
        tvArea.setText(meal.getStrArea());
        tvInstructions.setText(meal.getStrInstructions());

        // youtube
        setupYouTubePlayer();

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save fav in db (toggle to undo it)
                if (!meal.getIsFavouriteMeal()) {
                    Toast.makeText(getContext(), "Meal Added to Favourites", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Meal Removed from Favourites", Toast.LENGTH_SHORT).show();
                }
                presenter.toggleFavorite(meal);
            }
        });

        btnAddToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasCalendarPermissions()) {
                    requestCalendarPermissions();
                    return;
                }

                if (meal.getPlannedMealDate() == null) {
                    DatePickerDialogManager.showDatePickerDialog(getContext(), selectedDate -> {
                        presenter.togglePlanned(meal, selectedDate);
                        addMealToCalendar(meal);
                        Toast.makeText(getContext(), "Meal Scheduled for " + selectedDate, Toast.LENGTH_SHORT).show();
                    });
                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Unscheduled Meal")
                            .setMessage("Are you sure you want to unschedule this meal?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                presenter.togglePlanned(meal, null);
                                removeMealFromCalendar(meal);
                                Toast.makeText(getContext(), "Meal Unscheduled", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            }
        });
    }

    private void setupYouTubePlayer() {
        String videoUrl = meal.getStrYoutube();

        if (videoUrl == null || videoUrl.isEmpty()) {
            youtubePlayerView.setVisibility(View.GONE);
            return;
        }

        String videoId = extractYouTubeId(videoUrl);
        if (videoId == null) {
            youtubePlayerView.setVisibility(View.GONE);
            return;
        }

        // Add lifecycle observer to properly handle YouTubePlayerView
        getLifecycle().addObserver(youtubePlayerView);

        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                // Cue the video (doesn't autoplay)
                youTubePlayer.cueVideo(videoId, 0);
            }
        });
    }

    private String extractYouTubeId(String url) {
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\\?video_id=)([^#\\&\\?\\n]*)";
        Matcher matcher = Pattern.compile(pattern).matcher(url);
        return matcher.find() ? matcher.group() : null;
    }

    @Override
    public void updateFavoriteButton(boolean isFavorite) {
        btnFavorite.setImageResource(
                isFavorite ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border
        );
    }

    @Override
    public void updateCalendarButton(boolean isPlanned) {
        btnAddToCalendar.setImageResource(
                isPlanned ? R.drawable.ic_calendar_filled : R.drawable.ic_calendar_border
        );
    }

    @NonNull
    @Override
    public LifecycleOwner getViewLifecycleOwner() {
        return this;
    }

    @Override
    public void addMealToCalendar(Meal meal) {
        // Implement the same calendar addition logic as in HomeFragment
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

                SharedPreferences prefs = requireContext().getSharedPreferences("MealCalendarPrefs", Context.MODE_PRIVATE);
                prefs.edit().putLong("event_" + meal.getIdMeal(), eventId).apply();
            }

        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALENDAR_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, button will work on next click
                Toast.makeText(getContext(), "Calendar permissions granted. Tap again to schedule.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Calendar permissions are required to schedule meals.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}