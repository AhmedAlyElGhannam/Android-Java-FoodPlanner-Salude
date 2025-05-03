package com.example.salude.features.main_screen.fragments.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.salude.R;
import com.example.salude.model.pojo.Category;
import com.example.salude.model.pojo.Ingredient;
import com.example.salude.utils.clicklistener.OnFavouriteClickListener;
import com.example.salude.utils.clicklistener.OnMealItemClickListener;
import com.example.salude.utils.clicklistener.OnPlannedClickListener;

import java.util.List;

public class MealIngredientsAdapter extends RecyclerView.Adapter<MealIngredientsAdapter.ViewHolder> {

    private final Context context;
    private List<Ingredient> ingredients;
//    private final OnIngredientClickListener ingredient_listener;

    public MealIngredientsAdapter(Context _context, List<Ingredient> _ingredients) {
        context = _context;
        ingredients = _ingredients;
    }

    public void setIngredients(List<Ingredient> _ingredients) {
        ingredients = _ingredients;
    }

    @NonNull
    @Override
    public MealIngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* creating a view from the recycler view item */
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealIngredientsAdapter.ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.ingredientNameTxt.setText(ingredient.getStrIngredient());
        Glide.with(context).load(ingredient.getStrImageUrl()).into(holder.ingredientImg);

        holder.ingredientPortion.setVisibility(View.INVISIBLE);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ingredient.getStrIngredient(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients != null ? ingredients.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ingredientImg;
        TextView ingredientNameTxt;
        TextView ingredientPortion;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImg = itemView.findViewById(R.id.imgIngredient);
            ingredientNameTxt = itemView.findViewById(R.id.txtIngredientName);
            ingredientPortion = itemView.findViewById(R.id.txtIngredientPortion);
            layout = itemView.findViewById(R.id.ingredientLayout);
        }
    }
}
