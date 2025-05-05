package com.example.salude.features.main_screen.fragments.search.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.salude.R;
import com.example.salude.model.pojo.Area;
import com.example.salude.utils.clicklistener.OnAreaClickListener;
import com.example.salude.utils.mealarea.CountryFlagsUtil;

import java.util.ArrayList;
import java.util.List;

public class MealAreaAdapter extends RecyclerView.Adapter<MealAreaAdapter.ViewHolder> {

    private final Context context;
    private List<Area> areas;
    private OnAreaClickListener area_listener;

    public MealAreaAdapter(Context _context, OnAreaClickListener _area_listener) {
        context = _context;
        areas = new ArrayList<>();
        area_listener = _area_listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAreas(List<Area> _areas) {
        areas = _areas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealAreaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* creating a view from the recycler view item */
        View view = LayoutInflater.from(context).inflate(R.layout.area_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealAreaAdapter.ViewHolder holder, int position) {
        Area area = areas.get(position);

        holder.areaNameTxt.setText(area.getStrArea());
        Log.i("TAG", "onBindViewHolder: " + area.getStrArea());
        Glide.with(context).load(CountryFlagsUtil.getFlagUrl(area.getStrArea())).into(holder.areaFlagImg);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call item click listener
                area_listener.onAreaClickListener(area.getStrArea());
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
            areaFlagImg = itemView.findViewById(R.id.imgArea);
            areaNameTxt = itemView.findViewById(R.id.txtAreaName);
            layout = itemView.findViewById(R.id.categoryItemLayout);
        }
    }
}
