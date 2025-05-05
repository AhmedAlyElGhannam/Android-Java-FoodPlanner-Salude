package com.example.salude.features.main_screen.fragments.search.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.salude.R;
import com.example.salude.model.pojo.FilteredMeal;
import com.example.salude.utils.clicklistener.OnFilteredMealItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class ListOfFilteredMealsAdapter extends RecyclerView.Adapter<ListOfFilteredMealsAdapter.ViewHolder> {

    private final Context context;
    private List<FilteredMeal> meals;
    private OnFilteredMealItemClickListener filteredListener;

    public ListOfFilteredMealsAdapter(Context _context, OnFilteredMealItemClickListener _filteredListener) {
        context = _context;
        meals = new ArrayList<>();
        filteredListener = _filteredListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredMeals(List<FilteredMeal> _meals) {
        this.meals = _meals;
    }

    @NonNull
    @Override
    public ListOfFilteredMealsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.meal_item, parent, false);
        return new ListOfFilteredMealsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfFilteredMealsAdapter.ViewHolder holder, int position) {
        FilteredMeal meal = meals.get(position);
        holder.mealName.setText(meal.getStrMeal());
        Glide.with(context).load(meal.getStrMealThumb()).into(holder.mealThumbnail);

        // send all unnecessary UI elements to the shadow realm
        holder.planBtn.setVisibility(View.INVISIBLE);
        holder.favButton.setVisibility(View.INVISIBLE);
        holder.mealArea.setVisibility(View.INVISIBLE);
        holder.mealCategory.setVisibility(View.INVISIBLE);

        // handle item click
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filteredListener.onFilteredMealItemClickListener(meal);
            }
        });

    }

    @Override
    public int getItemCount() {
        return meals != null ? meals.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mealThumbnail;
        TextView mealName;
        TextView mealCategory;
        TextView mealArea;
        ImageButton favButton;
        ImageButton planBtn;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mealThumbnail = itemView.findViewById(R.id.imgMeal);
            mealName = itemView.findViewById(R.id.txtMealName);
            mealCategory = itemView.findViewById(R.id.txtCategory);
            mealArea = itemView.findViewById(R.id.txtCountry);
            favButton = itemView.findViewById(R.id.btnAddToFavourites);
            planBtn = itemView.findViewById(R.id.btnAddToCalendar);
            layout = itemView.findViewById(R.id.mealItemLayout);
        }
    }
}