package com.example.dowaya.activities.core;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.models.Store;

import java.util.Objects;

public class StoreDescriptionActivity extends AppCompatActivity {

    TextView nameTV, phoneTV, addressTV;
    int storeId;
    Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_description);

        setActionBarTitle("Store Description");
        findViewsByIds();
        storeId = getIntent().getIntExtra(StaticClass.STORE_ID, -1);
        if(storeId != -1){
            setStoreData();
        }
    }

    public void findViewsByIds(){
        nameTV = findViewById(R.id.nameTV);
        phoneTV = findViewById(R.id.phoneTV);
        addressTV = findViewById(R.id.addressTV);
    }
    public void setStoreData(){
        store = StaticClass.storeList.get(storeId);
        nameTV.setText(store.getName());
        phoneTV.setText(store.getPhone());
        addressTV.setText(store.getAddress());
    }
    public void dialPhone(View view){
        String phone_no = phoneTV.getText().toString()
                .replaceAll("-", "");
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone_no));
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
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), StoreListActivity.class));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
