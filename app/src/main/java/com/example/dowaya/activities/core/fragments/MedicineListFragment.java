package com.example.dowaya.activities.core.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.activities.core.MedicineDescriptionActivity;
import com.example.dowaya.daos.MedicineHistoryDAO;
import com.example.dowaya.models.Medicine;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class MedicineListFragment extends Fragment {

    private View fragmentView;
    private Context context;
    private SearchView medicineSearchView;
    private ListView medicineLV;
    private ArrayAdapter adapter;
    private FirebaseFirestore database;
    private ArrayList<String> nameList = new ArrayList<>(),
            copyList = new ArrayList<>();
    private ArrayList<Medicine> medicineList = new ArrayList<>();
    private MedicineHistoryDAO medicineHistoryDAO;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_medicine_list, container, false);
        context = fragmentView.getContext();
        medicineList.clear();
        copyList.clear();
        nameList.clear();
        medicineHistoryDAO = new MedicineHistoryDAO(context);
        database = FirebaseFirestore.getInstance();
        findViewsByIds();
        getMedicineList();
        medicineSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return false;}
            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
        a();
        return fragmentView;
    }
    private void findViewsByIds(){
        medicineSearchView = fragmentView.findViewById(R.id.medicineSearchView);
        medicineLV = fragmentView.findViewById(R.id.medicineLV);
    }
    private void setListView(){
        adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, nameList);
        medicineLV.setAdapter(adapter);
        medicineLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = nameList.get(position);
                String clickedId=null;
                for(Medicine medicine: medicineList){
                    if(medicine.getName().equals(name)){
                        clickedId = medicine.getId();
                        break;
                    }
                }
                insertMedicineHistory(clickedId, name);
                startActivity(
                        new Intent(context, MedicineDescriptionActivity.class)
                                .putExtra(StaticClass.MEDICINE_ID, clickedId)
                );
            }
        });
    }
    private void getMedicineList(){
        database.collection("medicines-names")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document :
                                    Objects.requireNonNull(task.getResult())) {
                                Medicine medicine = new Medicine();
                                medicine.setId(document.getId());
                                medicine.setName(document.getData().get("name").toString());
                                medicineList.add(medicine);
                                nameList.add(medicine.getName());
                                copyList.add(medicine.getName());
                                setListView();
                            }
                        } else {
                            Toast.makeText(context,
                                    "Error getting documents." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void insertMedicineHistory(String id, String name){
        Medicine medicine = new Medicine();
        medicine.setId(id);
        medicine.setName(name);
        medicine.setSearchHistoryTime(
                new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).
                format(Calendar.getInstance().getTime()));
        medicineHistoryDAO.insertMedicineHistory(medicine);
    }
    private void filter(String queryText){
        nameList.clear();
        if(queryText.isEmpty()) {
            nameList.addAll(copyList);
        }else{
            for(String s: copyList) {
                if(s.toLowerCase().contains(queryText.toLowerCase())) {
                    nameList.add(s);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void a(){
        DocumentReference documentReference =
                database.collection("medicines-descriptions")
                        .document("Augmentine");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(context,
                                "there is such document",
                                Toast.LENGTH_SHORT).show();
                        } else {
                        Toast.makeText(context,
                                "No such document",
                                Toast.LENGTH_SHORT).show();
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    Toast.makeText(context,
                            "get failed with " + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}