package com.example.salude.features.list_Fav.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.salude.R;
import com.example.salude.model.pojo.Meal;

import java.util.List;

public class ListOfFavouriteMealsAdapter extends RecyclerView.Adapter<ListOfFavouriteMealsAdapter.ViewHolder> {

    private final Context context;
    private List<Meal> meals;
    private final OnFavouriteClickListener favListener;

    public ListOfFavouriteMealsAdapter(Context _context, List<Meal> _meals, OnFavouriteClickListener _favListener) {
        context = _context;
        meals = _meals;
        favListener = _favListener;
    }

    public void setMeals(List<Meal> _meals) {
        this.meals = _meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.meal_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.mealName.setText(meal.getStrMeal());
        holder.mealCategory.setText(meal.getStrCategory());
        Glide.with(context).load(meal.getStrMealThumb()).into(holder.mealThumbnail);

        holder.favButton.setImageResource(
                meal.getIsFavouriteMeal() ?
                        R.drawable.ic_favorite_filled :
                        R.drawable.ic_favorite_border
        );

        holder.favButton.setOnClickListener(v -> {
            if (favListener != null) {
                favListener.onFavouriteClick(meal);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            // Handle meal item click if needed
            Toast.makeText(context, meal.getStrMeal(), Toast.LENGTH_SHORT).show();
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
        ImageButton favButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mealThumbnail = itemView.findViewById(R.id.imgMeal);
            mealName = itemView.findViewById(R.id.txtMealName);
            mealCategory = itemView.findViewById(R.id.txtCategory);
            favButton = itemView.findViewById(R.id.btnAddToFavourites);
        }
    }

    public interface OnFavouriteClickListener {
        void onFavouriteClick(Meal meal);
    }
}