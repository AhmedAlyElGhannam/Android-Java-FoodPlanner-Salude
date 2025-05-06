package com.example.salude.features.main_screen.view;

import com.example.salude.R;

import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;

import com.example.salude.contracts.MainScreenContract;
import com.example.salude.features.main_screen.fragments.connection.ConnectionLostFragment;
import com.example.salude.features.main_screen.fragments.connection.ConnectionRestoredFragment;
import com.example.salude.features.main_screen.fragments.home.view.HomeScreenFragment;
import com.example.salude.features.main_screen.fragments.profile.view.ProfileScreenFragment;
import com.example.salude.features.main_screen.fragments.search.view.SearchScreenFragment;
import com.example.salude.utils.clicklistener.OnConnectionRestoredListener;
import com.example.salude.utils.network.NetworkChangeReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainScreenActivity extends AppCompatActivity
        implements MainScreenContract.View, NetworkChangeReceiver.NetworkChangeListener,
        OnConnectionRestoredListener {

    BottomNavigationView bottomNav;
    private NetworkChangeReceiver networkChangeReceiver;
    private boolean isConnected = true;
    private boolean isShowingConnectionFragment = false;
    private boolean isOfflineMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        // to change status bar color
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#1E88E5")); // same blue hue as nav bar

        // runtime permission to use access system calendar (it is a MUST)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 1);

        }


        // network listener shenanigans
        networkChangeReceiver = new NetworkChangeReceiver(this);
        registerReceiver(networkChangeReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            if (isShowingConnectionFragment) {
                return false; // prevent navigation when showing connection fragments
            }

            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment = new HomeScreenFragment();
            } else if (id == R.id.nav_search) {
                if (isOfflineMode) {
                    Toast.makeText(this, "Search requires internet connection", Toast.LENGTH_SHORT).show();
                    return false;
                }
                selectedFragment = new SearchScreenFragment();
            } else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileScreenFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out) // enter, exit
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }

            return false;
        });

        // load the default fragment (home) when activity starts
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }

    @Override
    public void onBackPressed() {
        if (isShowingConnectionFragment) {
            return; // prevent back navigation when showing connection fragments
        }
        // get the current fragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        // if current fragment is Profile or Search, navigate to Home
        if (currentFragment instanceof ProfileScreenFragment || currentFragment instanceof SearchScreenFragment) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        } else {
            // otherwise, perform default back behavior
            super.onBackPressed();
        }
    }

    @Override
    public void onNetworkChanged(boolean isConnected) {
        if (this.isConnected != isConnected) {
            this.isConnected = isConnected;
            runOnUiThread(() -> {
                if (isConnected) {
                    isOfflineMode = false; // reset offline mode when connection is restored
                    showConnectionRestoredAnimation();
                } else {
                    showConnectionLostFragment();
                }
            });
        }
    }

    private void showConnectionLostFragment() {
        isShowingConnectionFragment = true;
        bottomNav.setVisibility(View.GONE);

        ConnectionLostFragment connectionLostFragment = new ConnectionLostFragment();

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out) // enter, exit
                .replace(R.id.fragment_container, connectionLostFragment)
                .commit();
    }

    private void showConnectionRestoredAnimation() {
        isShowingConnectionFragment = true;
        bottomNav.setVisibility(View.GONE);

        // create an instance of this fragment and pass the cbf for action on connection restoration
        ConnectionRestoredFragment connectionRestoredFragment = new ConnectionRestoredFragment(this);

        // switch to fragment
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out) // enter, exit
                .replace(R.id.fragment_container, connectionRestoredFragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregister the receiver when fragment is destroyed
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
        }
    }

    @Override
    public void onConnectionRestored() {
        isShowingConnectionFragment = false;
        bottomNav.setVisibility(View.VISIBLE);

        // refresh data and return to HomeFragment
        bottomNav.setSelectedItemId(R.id.nav_home);

        // notify current fragment to refresh data
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof HomeScreenFragment) {
            ((HomeScreenFragment) currentFragment).onNetworkConnectionSuccess();
        }
    }

    public void continueOffline() {
        isShowingConnectionFragment = false;
        bottomNav.setVisibility(View.VISIBLE);

        // in offline mode
        isOfflineMode = true;

        // return to home fragment
        bottomNav.setSelectedItemId(R.id.nav_home);
    }
}

