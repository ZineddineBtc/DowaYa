package com.example.dowaya.activities.core.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dowaya.R;
import com.example.dowaya.adapters.MedicineHistoryAdapter;
import com.example.dowaya.adapters.PostHistoryAdapter;
import com.example.dowaya.adapters.RequestHistoryAdapter;
import com.example.dowaya.adapters.StoreHistoryAdapter;
import com.example.dowaya.daos.MedicineHistoryDAO;
import com.example.dowaya.daos.PostHistoryDAO;
import com.example.dowaya.daos.RequestHistoryDAO;
import com.example.dowaya.daos.StoreHistoryDAO;
import com.example.dowaya.models.Medicine;
import com.example.dowaya.models.Store;

import java.util.ArrayList;

public class MyActivitiesFragment extends Fragment {

    private View fragmentView;
    private Context context;
    private TextView storeTV, medicineTV, requestTV, postTV, emptyListTV;
    private RecyclerView medicineRV, storeRV, requestRV, postRV;
    private ArrayList<Store> storeList;
    private ArrayList<Medicine> medicineList, requestList, postList;
    private short currentTab; // 0->Medicines  1->Stores  2->Requests  3->Posts

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_my_activities, container, false);
        context = fragmentView.getContext();
        initializeLists();
        findViewsByIds();
        tabListeners();
        return fragmentView;
    }
    private void findViewsByIds(){
        medicineTV = fragmentView.findViewById(R.id.medicineTV);
        storeTV = fragmentView.findViewById(R.id.storeTV);
        requestTV = fragmentView.findViewById(R.id.requestTV);
        postTV = fragmentView.findViewById(R.id.postTV);
        initializeMedicineRV();
        initializeStoreRV();
        initializeRequestRV();
        initializePostRV();
        emptyListTV = fragmentView.findViewById(R.id.emptyListTV);
    }
    private void initializeMedicineRV(){
        MedicineHistoryAdapter medicineAdapter = new MedicineHistoryAdapter(context, medicineList);
        medicineRV = fragmentView.findViewById(R.id.medicineHistoryRV);
        medicineRV.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        medicineRV.setAdapter(medicineAdapter);
    }
    private void initializeStoreRV(){
        StoreHistoryAdapter storeAdapter = new StoreHistoryAdapter(context, storeList);
        storeRV = fragmentView.findViewById(R.id.storeHistoryRV);
        storeRV.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        storeRV.setAdapter(storeAdapter);
    }
    private void initializeRequestRV(){
        RequestHistoryAdapter requestAdapter = new RequestHistoryAdapter(context, requestList);
        requestRV = fragmentView.findViewById(R.id.requestHistoryRV);
        requestRV.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        requestRV.setAdapter(requestAdapter);
    }
    private void initializePostRV(){
        PostHistoryAdapter postAdapter = new PostHistoryAdapter(context, postList);
        postRV = fragmentView.findViewById(R.id.postHistoryRV);
        postRV.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        postRV.setAdapter(postAdapter);
    }
    private void medicineTab(){
        if(currentTab != 0) {
            medicineTV.setTextColor(context.getColor(R.color.white));
            medicineTV.setBackground(context.getDrawable(R.drawable.green_background_rounded_border));
            storeTV.setTextColor(context.getColor(R.color.grey));
            storeTV.setBackgroundColor(context.getColor(R.color.white));
            requestTV.setTextColor(context.getColor(R.color.grey));
            requestTV.setBackgroundColor(context.getColor(R.color.white));
            postTV.setTextColor(context.getColor(R.color.grey));
            postTV.setBackgroundColor(context.getColor(R.color.white));
            if(medicineList.isEmpty()){
                emptyListTV.setVisibility(View.VISIBLE);
                medicineRV.setVisibility(View.GONE);
            }else{
                emptyListTV.setVisibility(View.GONE);
                medicineRV.setVisibility(View.VISIBLE);
            }
            storeRV.setVisibility(View.GONE);
            requestRV.setVisibility(View.GONE);
            postRV.setVisibility(View.GONE);
            currentTab = 0;
        }
    }
    private void storeTab(){
        if(currentTab != 1) {
            medicineTV.setTextColor(context.getColor(R.color.grey));
            medicineTV.setBackgroundColor(context.getColor(R.color.white));
            storeTV.setTextColor(context.getColor(R.color.white));
            storeTV.setBackground(context.getDrawable(R.drawable.green_background_rounded_border));
            requestTV.setTextColor(context.getColor(R.color.grey));
            requestTV.setBackgroundColor(context.getColor(R.color.white));
            postTV.setTextColor(context.getColor(R.color.grey));
            postTV.setBackgroundColor(context.getColor(R.color.white));
            if(storeList.isEmpty()){
                emptyListTV.setVisibility(View.VISIBLE);
                storeRV.setVisibility(View.GONE);
            }else{
                emptyListTV.setVisibility(View.GONE);
                storeRV.setVisibility(View.VISIBLE);
            }
            medicineRV.setVisibility(View.GONE);
            requestRV.setVisibility(View.GONE);
            postRV.setVisibility(View.GONE);
            currentTab = 1;
        }
    }
    private void requestTab(){
        if(currentTab != 2) {
            medicineTV.setTextColor(context.getColor(R.color.grey));
            medicineTV.setBackgroundColor(context.getColor(R.color.white));
            storeTV.setTextColor(context.getColor(R.color.grey));
            storeTV.setBackgroundColor(context.getColor(R.color.white));
            requestTV.setTextColor(context.getColor(R.color.white));
            requestTV.setBackground(context.getDrawable(R.drawable.green_background_rounded_border));
            postTV.setTextColor(context.getColor(R.color.grey));
            postTV.setBackgroundColor(context.getColor(R.color.white));
            if(requestList.isEmpty()){
                emptyListTV.setVisibility(View.VISIBLE);
                requestRV.setVisibility(View.GONE);
            }else{
                emptyListTV.setVisibility(View.GONE);
                requestRV.setVisibility(View.VISIBLE);
            }
            medicineRV.setVisibility(View.GONE);
            storeRV.setVisibility(View.GONE);
            postRV.setVisibility(View.GONE);
            currentTab = 2;
        }
    }
    private void postTab(){
        if(currentTab != 3) {
            medicineTV.setTextColor(context.getColor(R.color.grey));
            medicineTV.setBackgroundColor(context.getColor(R.color.white));
            storeTV.setTextColor(context.getColor(R.color.grey));
            storeTV.setBackgroundColor(context.getColor(R.color.white));
            requestTV.setTextColor(context.getColor(R.color.grey));
            requestTV.setBackgroundColor(context.getColor(R.color.white));
            postTV.setTextColor(context.getColor(R.color.white));
            postTV.setBackground(context.getDrawable(R.drawable.green_background_rounded_border));
            if(postList.isEmpty()){
                emptyListTV.setVisibility(View.VISIBLE);
                postRV.setVisibility(View.GONE);
            }else{
                emptyListTV.setVisibility(View.GONE);
                postRV.setVisibility(View.VISIBLE);
            }
            medicineRV.setVisibility(View.GONE);
            storeRV.setVisibility(View.GONE);
            requestRV.setVisibility(View.GONE);
            currentTab = 3;
        }
    }
    private void tabListeners(){
        medicineTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medicineTab();
            }
        });
        storeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeTab();
            }
        });
        requestTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestTab();
            }
        });
        postTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postTab();
            }
        });
    }
    private void initializeLists(){
        MedicineHistoryDAO medicineDAO = new MedicineHistoryDAO(context);
        medicineList = medicineDAO.getAllMedicineHistory();

        StoreHistoryDAO storeDAO = new StoreHistoryDAO(context);
        storeList = storeDAO.getAllStoreHistory();

        RequestHistoryDAO requestDAO = new RequestHistoryDAO(context);
        requestList = requestDAO.getAllRequestHistory();

        PostHistoryDAO postDAO = new PostHistoryDAO(context);
        postList = postDAO.getAllPostHistory();
    }

}