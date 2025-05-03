package com.example.salude.features.main_screen.view;

import com.example.salude.R;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.salude.features.main_screen.fragments.home.HomeFragment;
import com.example.salude.features.main_screen.fragments.profile.ProfileFragment;
import com.example.salude.features.main_screen.fragments.search.SearchFragment;
import com.example.salude.model.pojo.Meal;
import com.example.salude.model.remote.retrofit.callback.RemoteRetrofitCallback;
import com.example.salude.model.remote.retrofit.client.RemoteRetrofitClient;
import com.example.salude.model.remote.retrofit.repository.RemoteRetrofitRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainScreenActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        // to change status bar color
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#1E88E5")); // same blue hue as nav bar

        Meal testMeal = new Meal();

        testMeal.setIdMeal("test_123");
        testMeal.setStrMeal("YasTest Favorite Meal");
        testMeal.setStrCategory("Test Category");
        testMeal.setIdMeal("planned_123");
        testMeal.setStrMeal("Christmas Dinner");
        testMeal.setStrCategory("Holiday");

//        RoomLocalRepository.RoomLocalPlannedRepository repo1 =
//                RoomLocalRepository.RoomLocalPlannedRepository.getInstance(RoomLocalDB.getInstance(this).getPlannedMealDAO());
//
//        repo1.addToPlannedMeals(testMeal, "haha");
//
//        RoomLocalRepository.RoomLocalFavouriteRepository repo2 =
//                RoomLocalRepository.RoomLocalFavouriteRepository.getInstance(RoomLocalDB.getInstance(this).getFavouriteMealDAO());
//
//        repo2.addMealToFavourites(testMeal);
//
//        repo1.removeFromPlannedMeals(testMeal);


        RemoteRetrofitRepository.getInstance(RemoteRetrofitClient.getInstance()).getMealOfTheDay(new RemoteRetrofitCallback.RemoteRetrofitMealCallback() {
            @Override
            public void onSuccess(List<Meal> meals) {
                Log.i("TAG", "onSuccess: " + meals.get(0).getStrMeal());
            }

            @Override
            public void onFailure(String err) {
                Log.i("TAG", "onFailure: failed because no");
            }
        });

        bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.nav_search) {
                selectedFragment = new SearchFragment();
            } else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }

            return false;
        });

        // Load the default fragment when activity starts
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }

    @Override
    public void onBackPressed() {
        // get the current fragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        // if current fragment is Profile or Search, navigate to Home
        if (currentFragment instanceof ProfileFragment || currentFragment instanceof SearchFragment) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        } else {
            // otherwise, perform default back behavior
            super.onBackPressed();
        }
    }
}

