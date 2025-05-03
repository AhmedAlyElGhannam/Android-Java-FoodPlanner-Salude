package com.example.salude.model.remote.retrofit.response;

import android.util.Log;

import com.example.salude.model.pojo.Area;
import com.example.salude.model.pojo.FilteredMeal;

import java.util.List;

public class AreaResponse {
    private List<Area> meals;

    public List<Area> getAreas() {
        Log.i("TAG", "getAreas: " + meals);
        return meals;
    }
}
