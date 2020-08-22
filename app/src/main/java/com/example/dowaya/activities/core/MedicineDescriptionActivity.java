package com.example.dowaya.activities.core;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.activities.FullScreenActivity;
import com.example.dowaya.adapters.ClickableViewPager;
import com.example.dowaya.adapters.CustomPagerAdapter;
import com.example.dowaya.daos.BookmarkDAO;
import com.example.dowaya.models.Medicine;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

public class MedicineDescriptionActivity extends AppCompatActivity {

    TextView nameTV, priceRange, descriptionTV, linksTV;
    ImageView medicineIV;
    Medicine medicine;
    BookmarkDAO bookmarkDAO;
    int medicineId;
    boolean isBookmarked;
    String photoUri=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_description);
        setActionBarTitle("Medicine Description");
        findViewsByIds();
        bookmarkDAO = new BookmarkDAO(this);
        medicineId = getIntent().getIntExtra(StaticClass.MEDICINE_ID, -1);
        if(medicineId != -1){
            medicine = StaticClass.medicineList.get(medicineId-1);
            setMedicineData();
        }

    }
    public void findViewsByIds(){
        nameTV = findViewById(R.id.nameTV);
        priceRange = findViewById(R.id.priceRangeTV);
        descriptionTV = findViewById(R.id.descriptionTV);
        linksTV = findViewById(R.id.linksTV);
        medicineIV = findViewById(R.id.medicineIV);
    }
    public void setMedicineData(){
        nameTV.setText(medicine.getName());
        priceRange.setText(medicine.getPriceRange());
        descriptionTV.setText(medicine.getDescription());
        medicineIV.setImageDrawable(getDrawable(R.drawable.img0));
    }
    public void viewFullScreen(View view){
        startActivity(new Intent(getApplicationContext(), FullScreenActivity.class)
        .putExtra(StaticClass.FULL_SCREEN, photoUri)
        .putExtra(StaticClass.MEDICINE_ID, medicineId)
        .putExtra(StaticClass.FROM, StaticClass.MEDICINE_DESCRIPTION));
    }
    public void searchStore(View view){
        startActivity(new Intent(getApplicationContext(), StoreListActivity.class)
                        .putExtra(StaticClass.MEDICINE_ID, medicineId));
    }
    public void bookmark(boolean isBookmarked){
        if(isBookmarked){
            bookmarkDAO.deleteMedicine(medicineId);
            Snackbar.make(findViewById(R.id.medicine_description_parent_view),
                    "Medicine removed from bookmark", 1000)
                    .setAction("Action", null).show();
        }else{
            bookmarkDAO.insertMedicine(medicine);
            Snackbar.make(findViewById(R.id.medicine_description_parent_view),
                    "Medicine added to bookmark", 1000)
                    .setAction("Action", null).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        isBookmarked = bookmarkDAO.contains(medicine.getName());
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bookmark, menu);
        menu.getItem(0).setIcon(isBookmarked ?
                R.drawable.ic_bookmark_black : R.drawable.ic_bookmark_grey);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.bookmark){
            item.setIcon(isBookmarked ?
                    R.drawable.ic_bookmark_grey : R.drawable.ic_bookmark_black);
            bookmark(isBookmarked);
            isBookmarked = !isBookmarked;
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), CoreActivity.class));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
    public void setActionBarTitle(String title){
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
        Objects.requireNonNull(getSupportActionBar()).setTitle(
                Html.fromHtml("<font color=\"#ffffff\"> "+title+" </font>")
        );
    }

}
