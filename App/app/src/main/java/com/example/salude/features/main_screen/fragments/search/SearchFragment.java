package com.example.salude.features.main_screen.fragments.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salude.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

public class SearchFragment extends Fragment {
    public SearchFragment() { }

    TextInputEditText searchTxt;
    Chip ingredientChip;
    Chip areaChip;
    Chip categoryChip;
    TextView searchResLabel; // hide it by default
    RecyclerView searchResRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchTxt = view.findViewById(R.id.edtSearch);
        ingredientChip = view.findViewById(R.id.chipIngredient);
        areaChip = view.findViewById(R.id.chipArea);
        categoryChip = view.findViewById(R.id.chipCategory);
        searchResLabel = view.findViewById(R.id.txtSearchResultsLabel);
        searchResRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewSearchResults);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}