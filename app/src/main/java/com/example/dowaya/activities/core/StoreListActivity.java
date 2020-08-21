package com.example.dowaya.activities.core;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
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

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.models.Store;

import java.util.ArrayList;
import java.util.Objects;

public class StoreListActivity extends AppCompatActivity {

    SearchView storeSearchView;
    TextView nameTV, phoneTV, addressTV;
    int storeId, medicineId;
    Store store;
    //RecyclerView storesRV;
    //StoreAdapter adapter;
    ListView storeLV;
    ArrayAdapter arrayAdapter;
    ArrayList<String> storeList, copyList=new ArrayList<>();
    LinearLayout shadeLL, descriptionLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        setActionBarTitle("Stores");
        medicineId = getIntent().getIntExtra(StaticClass.MEDICINE_ID, -1);
        findViewsByIds();
        storeList = setStoreList();
        copyList.addAll(storeList);
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
        //setRecyclerView();
    }
    private void findViewsByIds(){
        storeSearchView = findViewById(R.id.storeSearchView);
        storeLV = findViewById(R.id.storeLV);
        //storesRV = findViewById(R.id.storeRV);
        nameTV = findViewById(R.id.nameTV);
        phoneTV = findViewById(R.id.phoneTV);
        addressTV = findViewById(R.id.addressTV);
        shadeLL = findViewById(R.id.shadeLL);
        descriptionLL = findViewById(R.id.descriptionLL);
    }
    /*private void setRecyclerView(){
        adapter = new StoreAdapter(this, StaticClass.storeList, medicineId);
        storesRV.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        storesRV.setAdapter(adapter);
    }*/
    public ArrayList<String> setStoreList(){
        ArrayList<String> list = new ArrayList<>();
        for(Store store: StaticClass.storeList){
            list.add(store.getName() + " ("+store.getCity()+")");
        }
        return list;
    }
    public void setListView(){
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, storeList);
        storeLV.setAdapter(arrayAdapter);
        storeLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setStoreData(position);
                showDescription();
            }
        });
    }
    public void filter(String queryText){
        storeList.clear();
        if(queryText.isEmpty()) {
            storeList.addAll(copyList);
        }else{
            for(String s: copyList) {
                if(s.toLowerCase().contains(queryText.toLowerCase())) {
                    storeList.add(s);
                }
            }
        }
        arrayAdapter.notifyDataSetChanged();
    }
    public void showDescription(){
        shadeLL.setVisibility(View.VISIBLE);
        descriptionLL.setVisibility(View.VISIBLE);
    }
    public void hideDescription(View view){
        shadeLL.setVisibility(View.GONE);
        descriptionLL.setVisibility(View.GONE);
    }
    public void setStoreData(int position){
        store = StaticClass.storeList.get(position);
        nameTV.setText(store.getName());
        phoneTV.setText(store.getPhone());
        addressTV.setText(store.getAddress());
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
