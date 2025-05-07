package com.example.salude.features.main_screen.fragments.profile.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.example.salude.R;
import com.example.salude.contracts.ProfileScreenContract;
import com.example.salude.features.auth_firebase.login.view.LoginAuthFirebaseActivity;
import com.example.salude.features.auth_firebase.register.view.RegisterAuthFirebaseActivity;
import com.example.salude.features.list_Fav.view.ListOfFavouriteMealsFragment;
import com.example.salude.features.list_plan.view.ListOfPlannedMealsFragment;
import com.example.salude.features.main_screen.fragments.profile.presenter.ProfileScreenPresenter;
import com.example.salude.model.repository.SaludRepository;
import com.example.salude.utils.guest.GuestMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileScreenFragment extends Fragment implements ProfileScreenContract.View {

    public ProfileScreenFragment() { }

    ProfileScreenPresenter presenter;
    TextView profileTxt;
    TextView usernameTxt;
    Button favMealsBtn;
    Button planMealsBtn;
    Button signOutBtn;
    ImageView userProfileImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        presenter = new ProfileScreenPresenter(this, SaludRepository.getInstance(requireContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileTxt = view.findViewById(R.id.tvProfileTitle);
        usernameTxt = view.findViewById(R.id.tvUserName);
        favMealsBtn = view.findViewById(R.id.btnFavouriteMeals);
        planMealsBtn = view.findViewById(R.id.btnPlannedMeals);
        signOutBtn = view.findViewById(R.id.btnSignOut);
        userProfileImg = view.findViewById(R.id.ivProfile);

        usernameTxt.setText("Salut, " + presenter.getUserName());

        profileTxt.setText("Profile");

        if (GuestMode.getGuestModeState()) {
            signOutBtn.setText("Sign In");
        }

        presenter.showUserProfilePhoto();


        favMealsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GuestMode.getGuestModeState()) {
                    ListOfFavouriteMealsFragment newFragment = new ListOfFavouriteMealsFragment();

                    // perform fragment transaction
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, newFragment)
                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out) // enter, exit
                            .addToBackStack(null)
                            .commit();
                }
                else {
                    Toast.makeText(requireContext(), "Sign in to view favourite meals.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        planMealsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GuestMode.getGuestModeState()) {
                    ListOfPlannedMealsFragment newFragment = new ListOfPlannedMealsFragment();

                    // perform fragment transaction
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, newFragment)
                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out) // enter, exit
                            .addToBackStack(null)
                            .commit();
                }
                else {
                    Toast.makeText(requireContext(), "Sign in to view planned meals.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GuestMode.getGuestModeState()) {
                    presenter.userSignOut();
                    Intent intent = new Intent(getActivity(), LoginAuthFirebaseActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Exit Guest Mode")
                            .setMessage("Want to go to sign in page?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    GuestMode.setGuestModeState(false);
                                    Intent intent = new Intent(getActivity(), LoginAuthFirebaseActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            }
        });
    }

    @Override
    public void showUserProfilePhoto(String uri) {
        if (uri == null) {
            Log.i("TAG", "showUserProfilePhoto: " + uri);
            userProfileImg.setImageResource(R.mipmap.app_icon_prof_foreground);
        }
        else {
            Glide.with(requireContext()).load(uri).into(userProfileImg);
        }
    }
}