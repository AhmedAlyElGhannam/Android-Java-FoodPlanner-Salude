package com.example.salude.features.mealdetails.view;

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
                presenter.toggleFavorite(meal);
            }
        });

        btnAddToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meal.getPlannedMealDate() == null) {
                    DatePickerDialogManager.showDatePickerDialog(getContext(), selectedDate
                            -> presenter.togglePlanned(meal, selectedDate));
                }
                else {
                    presenter.togglePlanned(meal, null);
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

        if (isFavorite) {
            Toast.makeText(getContext(), "Meal Added to Favourites", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(), "Meal Removed from Favourites", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateCalendarButton(boolean isPlanned) {
        btnAddToCalendar.setImageResource(
                isPlanned ? R.drawable.ic_calendar_filled : R.drawable.ic_calendar_border
        );
        if (isPlanned) {
            Toast.makeText(getContext(), "Meal Scheduled for " + meal.getPlannedMealDate(), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(), "Meal Unscheduled", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public LifecycleOwner getViewLifecycleOwner() {
        return this;
    }

}