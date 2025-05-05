package com.example.salude.features.list_plan.view;

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
import com.example.salude.utils.clicklistener.OnMealItemClickListener;
import com.example.salude.utils.clicklistener.OnPlannedClickListener;
import com.example.salude.model.pojo.Meal;

import java.util.List;

import android.Manifest;

public class ListOfPlannedMealsAdapter extends RecyclerView.Adapter<ListOfPlannedMealsAdapter.ViewHolder> {

    private final Context context;
    private List<Meal> meals;
    private OnMealItemClickListener mealListener;
    private OnPlannedClickListener planListener;

    public ListOfPlannedMealsAdapter(Context _context, List<Meal> _meals, OnMealItemClickListener _mealListener, OnPlannedClickListener _planListener) {
        context = _context;
        meals = _meals;
        mealListener = _mealListener;
        planListener = _planListener;
    }

    public void setMeals(List<Meal> _meals) {
        this.meals = _meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListOfPlannedMealsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.meal_item, parent, false);
        return new ListOfPlannedMealsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfPlannedMealsAdapter.ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.mealName.setText(meal.getStrMeal());
        holder.mealCategory.setText(meal.getPlannedMealDate());
        Glide.with(context).load(meal.getStrMealThumb()).into(holder.mealThumbnail);

        holder.favButton.setVisibility(View.INVISIBLE);
        holder.mealArea.setVisibility(View.INVISIBLE);
        holder.planBtn.setImageResource(
                meal.getPlannedMealDate() != null ?
                        R.drawable.ic_calendar_filled :
                        R.drawable.ic_calendar_border
        );


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mealListener.onMealItemClickListener(meal);
            }
        });

        holder.planBtn.setOnClickListener(v -> {
            if (planListener != null) {
                planListener.onPlannedClickListener(meal);
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