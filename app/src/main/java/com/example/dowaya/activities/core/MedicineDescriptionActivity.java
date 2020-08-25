package com.example.dowaya.activities.core;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.activities.FullScreenActivity;
import com.example.dowaya.daos.BookmarkDAO;
import com.example.dowaya.models.Medicine;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MedicineDescriptionActivity extends AppCompatActivity {

    TextView nameTV, priceRange, descriptionTV, doseTV;
    ImageView medicineIV;
    Medicine medicine;
    BookmarkDAO bookmarkDAO;
    String medicineId;
    boolean isBookmarked;
    String photoUri=null;
    private FirebaseFirestore database;
    ProgressDialog progressDialog;
    Menu aMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_description);
        setActionBarTitle("Medicine Description");
        database = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        findViewsByIds();
        bookmarkDAO = new BookmarkDAO(this);
        medicineId = getIntent().getStringExtra(StaticClass.MEDICINE_ID);
        getMedicineData();
    }
    public void findViewsByIds(){
        nameTV = findViewById(R.id.nameTV);
        priceRange = findViewById(R.id.priceRangeTV);
        descriptionTV = findViewById(R.id.descriptionTV);
        doseTV = findViewById(R.id.doseTV);
        medicineIV = findViewById(R.id.medicineIV);
    }
    public void getMedicineData(){
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        DocumentReference documentReference =
                database.collection("medicines-descriptions")
                        .document(medicineId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        medicine = new Medicine();
                        medicine.setId(document.getId());
                        medicine.setName(document.get("name").toString());
                        medicine.setDescription(document.get("description").toString());
                        medicine.setPrice(document.get("price").toString());
                        medicine.setDose(document.get("dose").toString());
                        setMedicineData();

                        isBookmarked = bookmarkDAO.contains(medicine.getName());
                        aMenu.getItem(0).setIcon(isBookmarked ?
                                R.drawable.ic_bookmark_black : R.drawable.ic_bookmark_grey);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "No such document",
                                Toast.LENGTH_SHORT).show();
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "get failed with " + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        progressDialog.dismiss();
    }
    public void setMedicineData(){
        nameTV.setText(medicine.getName());
        priceRange.setText(medicine.getPriceRange());
        descriptionTV.setText(medicine.getDescription());
        doseTV.setText(medicine.getDose());
        //medicineIV.setImageDrawable(getDrawable(R.drawable.img0));
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bookmark, menu);
        aMenu = menu;
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
