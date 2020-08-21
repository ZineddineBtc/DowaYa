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
import com.example.dowaya.adapters.HistoryAdapter;
import com.example.dowaya.daos.MedicineHistoryDAO;
import com.example.dowaya.daos.StoreHistoryDAO;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private View fragmentView;
    private Context context;
    private TextView storeTV, medicineTV, emptyListTV;
    private RecyclerView historyRV;
    private HistoryAdapter adapter;
    private ArrayList<String[]> medicineList, storeList, list=new ArrayList<>();
    private StoreHistoryDAO storeHistoryDAO;
    private MedicineHistoryDAO medicineHistoryDAO;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_history, container, false);
        context = fragmentView.getContext();
        findViewsByIds();
        initializeLists();
        // as a start
        adapter = new HistoryAdapter(context, list);
        tabbing(medicineTV);
        historyRV.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        historyRV.setAdapter(adapter);
        tabListeners();
        return fragmentView;
    }
    private void findViewsByIds(){
        medicineTV = fragmentView.findViewById(R.id.medicineTV);
        storeTV = fragmentView.findViewById(R.id.storeTV);
        historyRV = fragmentView.findViewById(R.id.historyRV);
        emptyListTV = fragmentView.findViewById(R.id.emptyListTV);
    }
    private void tabbing(View view){
        list.clear();
        if(view.getId() == R.id.medicineTV){
            medicineTV.setTextColor(context.getColor(R.color.green));
            storeTV.setTextColor(context.getColor(R.color.grey));
            list.addAll(medicineList);
        }else if(view.getId() == R.id.storeTV){
            medicineTV.setTextColor(context.getColor(R.color.grey));
            storeTV.setTextColor(context.getColor(R.color.green));
            list.addAll(storeList);
        }
        adapt();
    }
    private void adapt(){
        if(list.isEmpty()){
            emptyListTV.setVisibility(View.VISIBLE);
            historyRV.setVisibility(View.GONE);
        }else{
            emptyListTV.setVisibility(View.GONE);
            historyRV.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }
    private void tabListeners(){
        medicineTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabbing(v);
            }
        });
        storeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabbing(v);
            }
        });
    }
    private void initializeLists(){
        medicineHistoryDAO = new MedicineHistoryDAO(context);
        medicineList = medicineHistoryDAO.getAllMedicineHistory();
        list.addAll(medicineList);
        storeHistoryDAO = new StoreHistoryDAO(context);
        storeList = storeHistoryDAO.getAllStoreHistory();
    }
}