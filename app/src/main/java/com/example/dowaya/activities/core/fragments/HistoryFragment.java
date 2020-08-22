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
import com.example.dowaya.adapters.MedicineStoreHistoryAdapter;
import com.example.dowaya.adapters.PostHistoryAdapter;
import com.example.dowaya.adapters.RequestHistoryAdapter;
import com.example.dowaya.daos.MedicineHistoryDAO;
import com.example.dowaya.daos.PostHistoryDAO;
import com.example.dowaya.daos.RequestHistoryDAO;
import com.example.dowaya.daos.StoreHistoryDAO;
import com.example.dowaya.models.Medicine;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private View fragmentView;
    private Context context;
    private TextView storeTV, medicineTV, requestTV, postTV, emptyListTV;
    private RecyclerView medicinesStoresHistoryRV, requestHistoryRV, postHistoryRV;
    private MedicineStoreHistoryAdapter medicinesStoresAdapter;
    private RequestHistoryAdapter requestHistoryAdapter;
    private PostHistoryAdapter postHistoryAdapter;
    private ArrayList<String[]> medicineList, storeList, medicineStoreList=new ArrayList<>();
    private ArrayList<Medicine> requestList, postList;
    private StoreHistoryDAO storeHistoryDAO;
    private MedicineHistoryDAO medicineHistoryDAO;
    private RequestHistoryDAO requestHistoryDAO;
    private PostHistoryDAO postHistoryDAO;
    private short currentTab; // 0->Medicines  1->Stores  2->Requests  3->Posts

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_history, container, false);
        context = fragmentView.getContext();
        initializeLists();
        findViewsByIds();
        // as a start
        medicinesStoresAdapter = new MedicineStoreHistoryAdapter(context, medicineStoreList);
        medicineTab();
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
        postTV = fragmentView.findViewById(R.id.postTV);
        medicinesStoresHistoryRV = fragmentView.findViewById(R.id.medicines_stores_historyRV);
        initializeRequestRV();
        initializePostRV();
        emptyListTV = fragmentView.findViewById(R.id.emptyListTV);
    }
    private void initializeRequestRV(){
        requestHistoryAdapter = new RequestHistoryAdapter(context, requestList);
        requestHistoryRV = fragmentView.findViewById(R.id.requestHistoryRV);
        requestHistoryRV.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        requestHistoryRV.setAdapter(requestHistoryAdapter);
    }
    private void initializePostRV(){
        postHistoryAdapter = new PostHistoryAdapter(context, postList);
        postHistoryRV = fragmentView.findViewById(R.id.postHistoryRV);
        postHistoryRV.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        postHistoryRV.setAdapter(postHistoryAdapter);
    }
    private void medicineTab(){
        if(currentTab != 0){
            medicineTV.setTextColor(context.getColor(R.color.green));
            storeTV.setTextColor(context.getColor(R.color.grey));
            requestTV.setTextColor(context.getColor(R.color.grey));
            postTV.setTextColor(context.getColor(R.color.grey));
            medicineStoreList.clear();
            medicineStoreList.addAll(medicineList);
            adaptMedicineStore();
            currentTab = 0;
        }
    }
    private void storeTab(){
        if(currentTab != 1) {
            medicineTV.setTextColor(context.getColor(R.color.grey));
            storeTV.setTextColor(context.getColor(R.color.green));
            requestTV.setTextColor(context.getColor(R.color.grey));
            postTV.setTextColor(context.getColor(R.color.grey));
            medicineStoreList.clear();
            medicineStoreList.addAll(storeList);
            adaptMedicineStore();
            currentTab = 1;
        }
    }
    private void adaptMedicineStore(){
        if(medicineStoreList.isEmpty()){
            emptyListTV.setVisibility(View.VISIBLE);
            medicinesStoresHistoryRV.setVisibility(View.GONE);
        }else{
            emptyListTV.setVisibility(View.GONE);
            medicinesStoresHistoryRV.setVisibility(View.VISIBLE);
            medicinesStoresAdapter.notifyDataSetChanged();
        }
        requestHistoryRV.setVisibility(View.GONE);
        postHistoryRV.setVisibility(View.GONE);
    }
    private void requestTab(){
        if(currentTab != 2) {
            medicineTV.setTextColor(context.getColor(R.color.grey));
            storeTV.setTextColor(context.getColor(R.color.grey));
            requestTV.setTextColor(context.getColor(R.color.green));
            postTV.setTextColor(context.getColor(R.color.grey));
            if(requestList.isEmpty()){
                emptyListTV.setVisibility(View.VISIBLE);
                requestHistoryRV.setVisibility(View.GONE);
            }else{
                emptyListTV.setVisibility(View.GONE);
                requestHistoryRV.setVisibility(View.VISIBLE);
            }
            medicinesStoresHistoryRV.setVisibility(View.GONE);
            postHistoryRV.setVisibility(View.GONE);
            currentTab = 2;
        }
    }
    private void postTab(){
        if(currentTab != 3) {
            medicineTV.setTextColor(context.getColor(R.color.grey));
            storeTV.setTextColor(context.getColor(R.color.grey));
            requestTV.setTextColor(context.getColor(R.color.grey));
            postTV.setTextColor(context.getColor(R.color.green));
            if(postList.isEmpty()){
                emptyListTV.setVisibility(View.VISIBLE);
                postHistoryRV.setVisibility(View.GONE);
            }else{
                emptyListTV.setVisibility(View.GONE);
                postHistoryRV.setVisibility(View.VISIBLE);
            }
            medicinesStoresHistoryRV.setVisibility(View.GONE);
            requestHistoryRV.setVisibility(View.GONE);
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
        medicineHistoryDAO = new MedicineHistoryDAO(context);
        medicineList = medicineHistoryDAO.getAllMedicineHistory();
        medicineStoreList.addAll(medicineList);
        storeHistoryDAO = new StoreHistoryDAO(context);
        storeList = storeHistoryDAO.getAllStoreHistory();
        requestHistoryDAO = new RequestHistoryDAO(context);
        requestList = requestHistoryDAO.getAllRequestHistory();
        postHistoryDAO = new PostHistoryDAO(context);
        postList = postHistoryDAO.getAllPostHistory();
    }
}