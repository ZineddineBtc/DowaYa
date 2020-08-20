package com.example.dowaya.activities.core.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.adapters.MedicineAdapter;

public class MedicineListFragment extends Fragment {

    Context context;
    View fragmentView;
    SearchView medicineSearchView;
    RecyclerView medicineRV;
    MedicineAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_medicine_list, container, false);
        context = fragmentView.getContext();
        findViewsByIds();
        medicineSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return false;}

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
        setRecyclerView();

        return fragmentView;
    }
    private void findViewsByIds(){
        medicineSearchView = fragmentView.findViewById(R.id.medicineSearchView);
        medicineRV = fragmentView.findViewById(R.id.medicineRV);
    }
    private void setRecyclerView(){
        adapter = new MedicineAdapter(context, StaticClass.medicineList);
        medicineRV.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        medicineRV.setAdapter(adapter);
    }

}