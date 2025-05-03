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
import com.example.salude.model.pojo.Area;
import com.example.salude.model.pojo.Category;
import com.example.salude.utils.clicklistener.OnFavouriteClickListener;
import com.example.salude.utils.clicklistener.OnMealItemClickListener;
import com.example.salude.utils.clicklistener.OnPlannedClickListener;
import com.example.salude.utils.mealarea.CountryFlagsUtil;

import java.util.List;

public class MealAreaAdapter extends RecyclerView.Adapter<MealAreaAdapter.ViewHolder> {

    private final Context context;
    private List<Area> areas;
    private final OnFavouriteClickListener fav_listener;
    private final OnPlannedClickListener plan_listener;
    private final OnMealItemClickListener meal_listener;

    public MealAreaAdapter(Context _context, List<Area> _areas, OnFavouriteClickListener _fav_listener, OnPlannedClickListener _plan_listener, OnMealItemClickListener _meal_listener) {
        context = _context;
        areas = _areas;
        fav_listener = _fav_listener;
        plan_listener = _plan_listener;
        meal_listener = _meal_listener;
    }

    public void setAreas(List<Area> _areas) {
        areas = _areas;
    }

    @NonNull
    @Override
    public MealAreaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* creating a view from the recycler view item */
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealAreaAdapter.ViewHolder holder, int position) {
        Area area = areas.get(position);

        holder.areaNameTxt.setText(area.getStrArea());
        Glide.with(context).load(CountryFlagsUtil.getFlagUrl(area.getStrArea())).into(holder.areaFlagImg);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, area.getStrArea(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return areas != null ? areas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView areaFlagImg;
        TextView areaNameTxt;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            areaFlagImg = itemView.findViewById(R.id.imgCategory);
            areaNameTxt = itemView.findViewById(R.id.txtCategoryName);
            layout = itemView.findViewById(R.id.categoryItemLayout);
        }
    }
}
