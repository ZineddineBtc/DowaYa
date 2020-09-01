package com.example.dowaya.activities.core.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.dowaya.R;

public class OrientationFragment extends Fragment {

    private View fragmentView;
    private TextView medicineSearchTV, medicinePostTV, medicineRequestTV, deleteActionTV;
    private ImageView medicineSearchIV, medicinePostIV, medicineRequestIV, deleteActionIV;
    private boolean toggleSearch, togglePost, toggleRequest, toggleDelete;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_orientation, container, false);
        findViewsByIds();
        setClickListeners();

        return fragmentView;
    }
    private void findViewsByIds(){
        medicineSearchIV = fragmentView.findViewById(R.id.toggle_medicine_search);
        medicinePostIV = fragmentView.findViewById(R.id.toggle_medicine_post);
        medicineRequestIV = fragmentView.findViewById(R.id.toggle_medicine_request);
        deleteActionIV = fragmentView.findViewById(R.id.toggle_delete_action);
        medicineSearchTV = fragmentView.findViewById(R.id.medicine_search);
        medicinePostTV = fragmentView.findViewById(R.id.medicine_post);
        medicineRequestTV = fragmentView.findViewById(R.id.medicine_request);
        deleteActionTV = fragmentView.findViewById(R.id.delete_action);
    }
    private void toggleSearch(){
        medicineSearchTV.setVisibility(toggleSearch ? View.GONE : View.VISIBLE);
        medicineSearchIV.setImageResource(toggleSearch ?
                R.drawable.ic_drop_down_black : R.drawable.ic_drop_up_black);
        toggleSearch = !toggleSearch;
    }
    private void togglePost(){
        medicinePostTV.setVisibility(togglePost ? View.GONE : View.VISIBLE);
        medicinePostIV.setImageResource(togglePost ?
                R.drawable.ic_drop_down_black : R.drawable.ic_drop_up_black);
        togglePost = !togglePost;
    }
    private void toggleRequest(){
        medicineRequestTV.setVisibility(toggleRequest ? View.GONE : View.VISIBLE);
        medicineRequestIV.setImageResource(toggleRequest ?
                R.drawable.ic_drop_down_black : R.drawable.ic_drop_up_black);
        toggleRequest = !toggleRequest;
    }
    private void toggleDelete(){
        deleteActionTV.setVisibility(toggleDelete ? View.GONE : View.VISIBLE);
        deleteActionIV.setImageResource(toggleDelete ?
                R.drawable.ic_drop_down_black : R.drawable.ic_drop_up_black);
        toggleDelete = !toggleDelete;
    }
    private void setClickListeners(){
        medicineSearchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSearch();
            }
        });
        medicinePostIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePost();
            }
        });
        medicineRequestIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRequest();
            }
        });
        deleteActionIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDelete();
            }
        });
    }
}