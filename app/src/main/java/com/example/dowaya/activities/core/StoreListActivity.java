package com.example.dowaya.activities.core;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.SearchView;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.adapters.StoreAdapter;

import java.util.Objects;

public class StoreListActivity extends AppCompatActivity {

    SearchView storeSearchView;
    RecyclerView storesRV;
    StoreAdapter adapter;
    int medicineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        setActionBarTitle("Stores");
        medicineId = getIntent().getIntExtra(StaticClass.MEDICINE_ID, -1);

        findViewsByIds();
        storeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return false;}

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
        setRecyclerView();

    }
    private void findViewsByIds(){
        storeSearchView = findViewById(R.id.storeSearchView);
        storesRV = findViewById(R.id.storeRV);
    }
    private void setRecyclerView(){
        adapter = new StoreAdapter(this, StaticClass.storeList);
        storesRV.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        storesRV.setAdapter(adapter);
    }
    public void setActionBarTitle(String title){
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
        Objects.requireNonNull(getSupportActionBar()).setTitle(
                Html.fromHtml("<font color=\"#ffffff\"> "+title+" </font>")
        );
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MedicineDescriptionActivity.class)
        .putExtra(StaticClass.MEDICINE_ID, medicineId));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
