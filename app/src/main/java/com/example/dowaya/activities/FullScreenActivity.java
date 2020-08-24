package com.example.dowaya.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.activities.core.MedicineDescriptionActivity;

import java.util.Objects;

public class FullScreenActivity extends AppCompatActivity {

    ImageView photoIV;
    String photoUri;
    int medicineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        Objects.requireNonNull(getSupportActionBar()).hide();
        photoIV = findViewById(R.id.fullScreenIV);
        photoUri = getIntent().getStringExtra(StaticClass.FULL_SCREEN);
        medicineId = getIntent().getIntExtra(StaticClass.MEDICINE_ID, -1);

        /*if(photoUri!=null) { for testing
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), Uri.parse(photoUri));
            } catch (IOException e) {
                Toast.makeText(this, "IO Exception",
                        Toast.LENGTH_LONG).show();
            }
            photoIV.setImageBitmap(imageBitmap);
        }*/
        photoIV.setImageDrawable(getDrawable(R.drawable.img0));
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MedicineDescriptionActivity.class)
                .putExtra(StaticClass.MEDICINE_ID, medicineId));
    }
}
