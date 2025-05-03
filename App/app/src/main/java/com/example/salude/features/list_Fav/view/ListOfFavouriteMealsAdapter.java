package com.example.salude.features.list_Fav.view;

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
import com.example.salude.utils.clicklistener.OnFavouriteClickListener;
import com.example.salude.utils.clicklistener.OnMealItemClickListener;
import com.example.salude.model.pojo.Meal;

import java.util.List;

public class ListOfFavouriteMealsAdapter extends RecyclerView.Adapter<ListOfFavouriteMealsAdapter.ViewHolder> {

    private final Context context;
    private List<Meal> meals;
    private OnMealItemClickListener mealListener;
    private OnFavouriteClickListener favListener;

    public ListOfFavouriteMealsAdapter(Context _context, List<Meal> _meals, OnMealItemClickListener _mealListener, OnFavouriteClickListener _favListener) {
        context = _context;
        meals = _meals;
        mealListener = _mealListener;
        favListener = _favListener;
    }

    public void setMeals(List<Meal> _meals) {
        this.meals = _meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListOfFavouriteMealsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.meal_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.mealName.setText(meal.getStrMeal());
        holder.mealCategory.setText(meal.getStrCategory());
        holder.mealArea.setText(meal.getStrArea());
        Glide.with(context).load(meal.getStrMealThumb()).into(holder.mealThumbnail);

        holder.planBtn.setVisibility(View.INVISIBLE);

        holder.favButton.setImageResource(
                meal.getIsFavouriteMeal() ?
                        R.drawable.ic_favorite_filled :
                        R.drawable.ic_favorite_border
        );


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mealListener.onMealItemClickListener(meal);
            }
        });

        holder.favButton.setOnClickListener(v -> {
            if (favListener != null) {

                favListener.onFavouriteClickListener(meal);
            }
        });

//        holder.itemView.setOnClickListener(v -> {
//            // Handle meal item click if needed
//            view.showMealDetails(meal);
//        });
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