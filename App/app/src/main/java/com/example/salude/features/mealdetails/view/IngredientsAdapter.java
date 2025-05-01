package com.example.salude.features.mealdetails.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.salude.R;
import com.example.salude.model.pojo.Ingredient;
import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    private Context context;
    private List<Ingredient> ingredientEntries;

    public IngredientsAdapter(Context context) {
        this.context = context;
        this.ingredientEntries = new ArrayList<>();
    }

    public void setIngredientList(List<Ingredient> _ingredients) {
        ingredientEntries = _ingredients;
    }

    @NonNull
    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredientEntries.get(position);


        holder.textViewIngredient.setText(ingredient.getStrIngredient());
        holder.textViewMeasure.setText(ingredient.getStrMeasure());

        // Load ingredient image
        Glide.with(context).load(ingredient.getStrImageUrl()).into(holder.imageViewThumb);
    }

    @Override
    public int getItemCount() {
        return ingredientEntries != null ? ingredientEntries.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewIngredient;
        TextView textViewMeasure;
        ImageView imageViewThumb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewIngredient = itemView.findViewById(R.id.txtIngredientName);
            textViewMeasure = itemView.findViewById(R.id.txtIngredientPortion);
            imageViewThumb = itemView.findViewById(R.id.imgIngredient);
        }
    }
}