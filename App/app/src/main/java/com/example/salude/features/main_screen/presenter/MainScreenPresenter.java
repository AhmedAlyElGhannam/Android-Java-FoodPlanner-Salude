package com.example.salude.features.main_screen.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.util.Log;

import com.example.salude.contracts.MainScreenContract;
import com.example.salude.model.local.datasource.LocalDataSource;
import com.example.salude.model.pojo.Area;
import com.example.salude.model.pojo.Category;
import com.example.salude.model.pojo.FilteredMeal;
import com.example.salude.model.pojo.Ingredient;
import com.example.salude.model.pojo.Meal;
import com.example.salude.model.remote.retrofit.callback.RemoteRetrofitCallback;
import com.example.salude.model.remote.retrofit.datasource.RemoteDataSource;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainScreenPresenter implements MainScreenContract.Presenter {
    private final MainScreenContract.View view;
    private final MainScreenContract.Model repo;

    private final SharedPreferences sharedPreferences;
    private Context context;

    public MainScreenPresenter(MainScreenContract.View _view, MainScreenContract.Model _repo, Context _context) {
        context = _context;
        view = _view;
        repo = _repo;
        sharedPreferences = _context.getSharedPreferences("Meal_Preferences", Context.MODE_PRIVATE);
    }
}
