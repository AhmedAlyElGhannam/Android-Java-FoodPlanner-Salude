package com.example.salude.features.main_screen.fragments.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
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
//    private final OnAreaClickListener  area_listener;

    public MealAreaAdapter(Context _context, List<Area> _areas) {
        context = _context;
        areas = _areas;
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
            areaFlagImg = itemView.findViewById(R.id.imgArea);
            areaNameTxt = itemView.findViewById(R.id.txtAreaName);
            layout = itemView.findViewById(R.id.categoryItemLayout);
        }
    }
}
