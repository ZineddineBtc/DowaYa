package com.example.dowaya.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.activities.core.CoreActivity;
import com.example.dowaya.activities.core.MedicineDescriptionActivity;
import com.example.dowaya.adapters.CustomPagerAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class FullScreenActivity extends AppCompatActivity {

    ArrayList<Bitmap> imagesList;
    int medicineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        Objects.requireNonNull(getSupportActionBar()).hide();
        medicineId = getIntent().getIntExtra(StaticClass.MEDICINE_ID, -1);
        setImagesLocally();
        setViewPager();

    }
    public void setImagesLocally() {
        imagesList = new ArrayList<Bitmap>(){{
            add(BitmapFactory.decodeResource(getResources(), R.drawable.img0));
            add(BitmapFactory.decodeResource(getResources(), R.drawable.img1));
            add(BitmapFactory.decodeResource(getResources(), R.drawable.img2));
            add(BitmapFactory.decodeResource(getResources(), R.drawable.img3));
        }};
    }
    public void setViewPager(){
        CustomPagerAdapter adapter = new CustomPagerAdapter(this, imagesList);
        ViewPager viewPager = findViewById(R.id.fullScreenVP);
        viewPager.setAdapter(adapter);
        viewPager.setPageMargin(20);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MedicineDescriptionActivity.class)
        .putExtra(StaticClass.MEDICINE_ID, medicineId));
    }
}
