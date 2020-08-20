package com.example.dowaya.activities.core;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.activities.FullScreenActivity;
import com.example.dowaya.adapters.ClickableViewPager;
import com.example.dowaya.adapters.CustomPagerAdapter;
import com.example.dowaya.models.Medicine;

import java.util.ArrayList;
import java.util.Objects;

public class MedicineDescriptionActivity extends AppCompatActivity {

    TextView nameTV, priceRange, descriptionTV, linksTV;
    ImageView bookmarkIV;
    ClickableViewPager imagesVP;
    CustomPagerAdapter adapter;
    ArrayList<Bitmap> imagesList;
    LinearLayout dotLayout;
    TextView[] dot;
    Medicine medicine;
    int medicineId;
    boolean isBookmarked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_description);
        setActionBarTitle("Medicine Description");
        findViewsByIds();
        medicineId = getIntent().getIntExtra(StaticClass.MEDICINE_ID, -1);
        if(medicineId != -1){
            medicine = StaticClass.medicineList.get(medicineId);
            setMedicineData();
        }
        setImagesLocally();
        if(!imagesList.isEmpty()){
            setViewPager();
        }
    }
    public void findViewsByIds(){
        nameTV = findViewById(R.id.nameTV);
        bookmarkIV = findViewById(R.id.bookmarkIV);
        priceRange = findViewById(R.id.priceRangeTV);
        descriptionTV = findViewById(R.id.descriptionTV);
        linksTV = findViewById(R.id.linksTV);
        imagesVP = findViewById(R.id.imagesVP);
        dotLayout = findViewById(R.id.dotLayout);
    }
    public void setMedicineData(){
        nameTV.setText(medicine.getName());
        priceRange.setText(medicine.getPriceRange());
        descriptionTV.setText(medicine.getDescription());
    }
    public void setImagesLocally(){
        imagesList = new ArrayList<Bitmap>(){{
                    add(BitmapFactory.decodeResource(getResources(), R.drawable.img0));
                    add(BitmapFactory.decodeResource(getResources(), R.drawable.img1));
                    add(BitmapFactory.decodeResource(getResources(), R.drawable.img2));
                    add(BitmapFactory.decodeResource(getResources(), R.drawable.img3));
                    }};
    }
    public void setViewPager(){
        adapter = new CustomPagerAdapter(this, imagesList);
        imagesVP.setAdapter(adapter);
        imagesVP.setPageMargin(20);
        addDot(0);
        imagesVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int i) {addDot(i);}
            @Override
            public void onPageScrolled(int i, float v, int i1) {}
            @Override
            public void onPageScrollStateChanged(int i) {}
        });
        imagesVP.setOnViewPagerClickListener(new ClickableViewPager.OnClickListener() {
            @Override
            public void onViewPagerClick(ViewPager viewPager) {
                startActivity(new Intent(getApplicationContext(), FullScreenActivity.class)
                .putExtra(StaticClass.MEDICINE_ID, medicineId));
            }
        });
    }
    public void addDot(int pagePosition) {
        dot = new TextView[imagesList.size()];
        dotLayout.removeAllViews();
        for (int i = 0; i < dot.length; i++) {
            dot[i] = new TextView(this);
            dot[i].setText(Html.fromHtml("&#9673;"));
            dot[i].setTextSize(14);
            dot[i].setTextColor(getColor(R.color.dark_grey));
            dotLayout.addView(dot[i]);
        }
        dot[pagePosition].setTextColor(getColor(R.color.blue));
    }
    public void searchStore(View view){
        startActivity(new Intent(getApplicationContext(), StoreListActivity.class)
                        .putExtra(StaticClass.MEDICINE_ID, medicineId));
    }
    public void bookmark(View view){
        bookmarkIV.setImageResource(isBookmarked ?
                R.drawable.ic_bookmark_grey : R.drawable.ic_bookmark_green);
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
        startActivity(new Intent(getApplicationContext(), CoreActivity.class));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
