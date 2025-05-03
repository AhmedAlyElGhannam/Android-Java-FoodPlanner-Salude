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
import com.example.salude.utils.clicklistener.OnFavouriteClickListener;
import com.example.salude.utils.clicklistener.OnMealItemClickListener;
import com.example.salude.utils.clicklistener.OnPlannedClickListener;

import java.util.ArrayList;
import java.util.List;

public class MealCategoryAdapter extends RecyclerView.Adapter<MealCategoryAdapter.ViewHolder> {

    private final Context context;
    private List<Category> categories;
//    private final OnCategoryClickListener category_listener;

    public MealCategoryAdapter(Context _context) {
        context = _context;
        categories = new ArrayList<>();
    }

    public void setCategories(List<Category> _categories) {
        this.categories = _categories;
    }

    @NonNull
    @Override
    public MealCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* creating a view from the recycler view item */
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealCategoryAdapter.ViewHolder holder, int position) {
        Category category = categories.get(position);

        holder.categoryNameTxt.setText(category.getStrCategory());
        Glide.with(context).load(category.getStrCategoryThumb()).into(holder.categoryThumbnailImg);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, category.getStrCategory(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryThumbnailImg;
        TextView categoryNameTxt;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryThumbnailImg = itemView.findViewById(R.id.imgCategory);
            categoryNameTxt = itemView.findViewById(R.id.txtCategoryName);
            layout = itemView.findViewById(R.id.categoryItemLayout);
        }
    }
}
