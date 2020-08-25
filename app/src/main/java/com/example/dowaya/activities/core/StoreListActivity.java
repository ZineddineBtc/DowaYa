package com.example.dowaya.activities.core;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.daos.StoreHistoryDAO;
import com.example.dowaya.models.Store;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class StoreListActivity extends AppCompatActivity {

    SearchView storeSearchView;
    TextView nameTV, phoneTV, addressTV;
    String medicineId;
    ListView storeLV;
    ArrayAdapter adapter;
    ArrayList<String> nameCityList = new ArrayList<>(), copyList = new ArrayList<>();
    ArrayList<Store> storeList = new ArrayList<>();
    LinearLayout shadeLL, descriptionLL;
    FirebaseFirestore database;
    ProgressDialog progressDialog;
    StoreHistoryDAO storeHistoryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        setActionBarTitle("Stores");
        storeHistoryDAO = new StoreHistoryDAO(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        database = FirebaseFirestore.getInstance();
        medicineId = getIntent().getStringExtra(StaticClass.MEDICINE_ID);
        findViewsByIds();
        getMedicineStores();
        setListView();
        storeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return false;}

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }
    private void findViewsByIds(){
        storeSearchView = findViewById(R.id.storeSearchView);
        storeLV = findViewById(R.id.storeLV);
        nameTV = findViewById(R.id.nameTV);
        phoneTV = findViewById(R.id.phoneTV);
        addressTV = findViewById(R.id.addressTV);
        shadeLL = findViewById(R.id.shadeLL);
        descriptionLL = findViewById(R.id.descriptionLL);
    }
    public void getMedicineStores(){
        DocumentReference documentReference =
                database.collection("medicines-stores").document(medicineId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<DocumentReference> referenceList =
                                (ArrayList<DocumentReference>) document.get("stores");
                        for(DocumentReference ref: referenceList){
                            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Store store = new Store();
                                            store.setName((String)document.get("name"));
                                            store.setCity((String)document.get("city"));
                                            store.setPhone((String)document.get("phone"));
                                            store.setAddress((String)document.get("address"));
                                            storeList.add(store);
                                            String nameCity = store.getName()+", "+store.getCity();
                                            nameCityList.add(nameCity);
                                            copyList.add(nameCity);
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(getApplicationContext(),
                                                    "No such document",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "get failed with " + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "No such document",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "get failed with " + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }
    public void setListView(){
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nameCityList);
        storeLV.setAdapter(adapter);
        storeLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = nameCityList.get(position).split(",")[0].trim();
                String clickedID = "";
                for(Store store: storeList){
                    if(store.getName().equals(name)){
                        clickedID = store.getId();
                        name = store.getName();
                        setStoreData(store);
                        break;
                    }
                }
                showDescription();
                insertHistory(clickedID, name);
            }
        });
    }
    public void filter(String queryText){
        nameCityList.clear();
        if(queryText.isEmpty()) {
            nameCityList.addAll(copyList);
        }else{
            for(String s: copyList) {
                if(s.toLowerCase().contains(queryText.toLowerCase())) {
                    nameCityList.add(s);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
    public void showDescription(){
        shadeLL.setVisibility(View.VISIBLE);
        descriptionLL.setVisibility(View.VISIBLE);
    }
    public void hideDescription(View view){
        shadeLL.setVisibility(View.GONE);
        descriptionLL.setVisibility(View.GONE);
    }
    public void setStoreData(Store store){
        nameTV.setText(store.getName());
        phoneTV.setText(store.getPhone());
        addressTV.setText(store.getAddress());
    }
    public void insertHistory(String id, String name){
        Store store = new Store();
        store.setId(id);
        store.setName(name);
        store.setHistoryTime(
                new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).
                        format(Calendar.getInstance().getTime())
        );
        storeHistoryDAO.insertStoreHistory(store);
    }
    public void dialPhone(View view){
        String phoneNumber = phoneTV.getText().toString()
                .replaceAll("-", "");
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (checkSelfPermission(Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }
    public void locatePharmacy(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(
                Uri.parse("https://www.google.com/maps/search/" +
                        addressTV.getText().toString()));
        startActivity(browserIntent);
    }
    public void setActionBarTitle(String title){
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
        Objects.requireNonNull(getSupportActionBar()).setTitle(
                Html.fromHtml("<font color=\"#ffffff\"> "+title+" </font>")
        );
    }
    @Override
    public void onBackPressed(){
        if(shadeLL.getVisibility()==View.VISIBLE){
            hideDescription(shadeLL);
        }else{
            startActivity(new Intent(getApplicationContext(), MedicineDescriptionActivity.class)
                    .putExtra(StaticClass.MEDICINE_ID, medicineId));
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}

