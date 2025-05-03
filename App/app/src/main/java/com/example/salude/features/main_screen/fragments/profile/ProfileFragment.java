package com.example.salude.features.main_screen.fragments.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.salude.R;
import com.example.salude.features.auth_firebase.login.view.LoginAuthFirebaseActivity;
import com.example.salude.features.list_Fav.view.ListOfFavouriteMealsFragment;
import com.example.salude.features.list_plan.view.ListOfPlannedMealsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    public ProfileFragment() { }

    TextView profileTxt;
    TextView usernameTxt;
    Button favMealsBtn;
    Button planMealsBtn;
    Button signOutBtn;
    ImageView appIconImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        profileTxt = view.findViewById(R.id.tvProfileTitle);
        usernameTxt = view.findViewById(R.id.tvUserName);
        favMealsBtn = view.findViewById(R.id.btnFavouriteMeals);
        planMealsBtn = view.findViewById(R.id.btnPlannedMeals);
        signOutBtn = view.findViewById(R.id.btnSignOut);
        appIconImg = view.findViewById(R.id.ivProfile);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**************************move to repo***************************************/
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            usernameTxt.setText("Hello, " + name);
        }
        else {
            usernameTxt.setText("Hello, Guest");
        }
        /**************************move to repo***************************************/


        profileTxt.setText("Profile");
        appIconImg.setImageResource(R.mipmap.app_icon_prof_foreground);

        favMealsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "To Favourite Meals List", Toast.LENGTH_SHORT).show();
                ListOfFavouriteMealsFragment newFragment = new ListOfFavouriteMealsFragment();

                // perform fragment transaction
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, newFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        planMealsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListOfPlannedMealsFragment newFragment = new ListOfPlannedMealsFragment();

                // perform fragment transaction
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, newFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getActivity(), LoginAuthFirebaseActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}