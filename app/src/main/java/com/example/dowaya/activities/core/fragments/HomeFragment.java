package com.example.dowaya.activities.core.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    Context context;
    SharedPreferences sharedPreferences;
    TextView nameTV, emailTV, phoneTV;
    View fragmentView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        context = fragmentView.getContext();
        sharedPreferences = context.getSharedPreferences(StaticClass.SHARED_PREFERENCES, MODE_PRIVATE);
        findViewsByIds();
        setUserData();

        return fragmentView;
    }
    private void findViewsByIds(){
        nameTV = fragmentView.findViewById(R.id.nameTV);
        emailTV = fragmentView.findViewById(R.id.emailTV);
        phoneTV = fragmentView.findViewById(R.id.phoneTV);
    }

    private void setUserData(){
        nameTV.setText(sharedPreferences.getString(StaticClass.USERNAME, "no username"));
        emailTV.setText(sharedPreferences.getString(StaticClass.EMAIL, "no email"));
        phoneTV.setText(sharedPreferences.getString(StaticClass.PHONE, "no phone number"));
    }
}