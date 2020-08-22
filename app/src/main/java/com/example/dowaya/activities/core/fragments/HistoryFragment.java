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
import com.example.dowaya.adapters.MedicinesStoresHistoryAdapter;
import com.example.dowaya.adapters.RequestsHistoryAdapter;
import com.example.dowaya.daos.MedicineHistoryDAO;
import com.example.dowaya.daos.RequestHistoryDAO;
import com.example.dowaya.daos.StoreHistoryDAO;
import com.example.dowaya.models.Medicine;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private View fragmentView;
    private Context context;
    private TextView storeTV, medicineTV, requestTV, emptyListTV;
    private RecyclerView medicinesStoresHistoryRV, requestsHistoryRV;
    private MedicinesStoresHistoryAdapter medicinesStoresAdapter;
    private RequestsHistoryAdapter requestsHistoryAdapter;
    private ArrayList<String[]> medicineList, storeList, list=new ArrayList<>();
    private ArrayList<Medicine> requestList;
    private StoreHistoryDAO storeHistoryDAO;
    private MedicineHistoryDAO medicineHistoryDAO;
    private RequestHistoryDAO requestHistoryDAO;
    private short currentTab; // 0->Medicines  1->Stores  2->Requests

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_history, container, false);
        context = fragmentView.getContext();
        initializeLists();
        findViewsByIds();
        // as a start
        medicinesStoresAdapter = new MedicinesStoresHistoryAdapter(context, list);
        medicinesTab();
        medicinesStoresHistoryRV.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        medicinesStoresHistoryRV.setAdapter(medicinesStoresAdapter);
        tabListeners();
        return fragmentView;
    }
    private void findViewsByIds(){
        medicineTV = fragmentView.findViewById(R.id.medicineTV);
        storeTV = fragmentView.findViewById(R.id.storeTV);
        requestTV = fragmentView.findViewById(R.id.requestTV);
        medicinesStoresHistoryRV = fragmentView.findViewById(R.id.medicines_stores_historyRV);
        initializeRequestsRV();
        emptyListTV = fragmentView.findViewById(R.id.emptyListTV);
    }
    private void initializeRequestsRV(){
        requestsHistoryAdapter = new RequestsHistoryAdapter(context, requestList);
        requestsHistoryRV = fragmentView.findViewById(R.id.requests_historyRV);
        requestsHistoryRV.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        requestsHistoryRV.setAdapter(requestsHistoryAdapter);
    }
    private void medicinesTab(){
        if(currentTab != 0){
            medicineTV.setTextColor(context.getColor(R.color.green));
            storeTV.setTextColor(context.getColor(R.color.grey));
            requestTV.setTextColor(context.getColor(R.color.grey));
            list.clear();
            list.addAll(medicineList);
            adaptMedicinesStores();
            currentTab = 0;
        }
    }
    private void storesTab(){
        if(currentTab != 1) {
            medicineTV.setTextColor(context.getColor(R.color.grey));
            storeTV.setTextColor(context.getColor(R.color.green));
            requestTV.setTextColor(context.getColor(R.color.grey));
            list.clear();
            list.addAll(storeList);
            adaptMedicinesStores();
            currentTab = 1;
        }
    }
    private void adaptMedicinesStores(){
        if(list.isEmpty()){
            emptyListTV.setVisibility(View.VISIBLE);
            medicinesStoresHistoryRV.setVisibility(View.GONE);
        }else{
            emptyListTV.setVisibility(View.GONE);
            medicinesStoresHistoryRV.setVisibility(View.VISIBLE);
            medicinesStoresAdapter.notifyDataSetChanged();
        }
        requestsHistoryRV.setVisibility(View.GONE);
    }
    private void requestsTab(){
        if(currentTab != 2) {
            medicineTV.setTextColor(context.getColor(R.color.grey));
            storeTV.setTextColor(context.getColor(R.color.grey));
            requestTV.setTextColor(context.getColor(R.color.green));
            if(requestList.isEmpty()){
                emptyListTV.setVisibility(View.VISIBLE);
                requestsHistoryRV.setVisibility(View.GONE);
            }else{
                emptyListTV.setVisibility(View.GONE);
                requestsHistoryRV.setVisibility(View.VISIBLE);
            }
            medicinesStoresHistoryRV.setVisibility(View.GONE);
            currentTab = 2;
        }
    }
    private void tabListeners(){
        medicineTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medicinesTab();
            }
        });
        storeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storesTab();
            }
        });
        requestTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestsTab();
            }
        });
    }
    private void initializeLists(){
        medicineHistoryDAO = new MedicineHistoryDAO(context);
        medicineList = medicineHistoryDAO.getAllMedicineHistory();
        list.addAll(medicineList);
        storeHistoryDAO = new StoreHistoryDAO(context);
        storeList = storeHistoryDAO.getAllStoreHistory();
        requestHistoryDAO = new RequestHistoryDAO(context);
        requestList = requestHistoryDAO.getAllRequestHistory();
    }
}