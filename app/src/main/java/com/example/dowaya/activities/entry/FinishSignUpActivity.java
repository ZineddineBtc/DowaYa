package com.example.dowaya.activities.entry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.activities.TermsActivity;
import com.example.dowaya.activities.core.CoreActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FinishSignUpActivity extends AppCompatActivity {

    EditText nameET, phoneET, addressET, cityET;
    TextView errorTV;
    SharedPreferences sharedPreferences;
    String name, phone, email, address, city;
    FirebaseFirestore database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_sign_up);

        Objects.requireNonNull(getSupportActionBar()).hide();
        sharedPreferences = getSharedPreferences(StaticClass.SHARED_PREFERENCES, MODE_PRIVATE);
        database = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        nameET = findViewById(R.id.nameET);
        nameET.requestFocus();
        phoneET = findViewById(R.id.phoneET);
        addressET =findViewById(R.id.addressET);
        cityET =findViewById(R.id.cityET);
        errorTV = findViewById(R.id.errorTV);
        checkBuildVersion();

    }
    public void checkBuildVersion(){
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyHavePermission()) {
                requestForSpecificPermission();
            }
        }
    }
    private boolean checkIfAlreadyHavePermission() {
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.INTERNET},
                101);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // not granted
                moveTaskToBack(true);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public void editSharedPreferences(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(StaticClass.USERNAME, name);
        editor.putString(StaticClass.PHONE, phone);
        editor.putString(StaticClass.EMAIL, email);
        editor.putString(StaticClass.ADDRESS, address);
        editor.putString(StaticClass.CITY, city);
        editor.apply();
    }
    public void writeDatabase(){
        Map<String, Object> userReference = new HashMap<>();
        userReference.put("username", name);
        userReference.put("phone", phone);
        userReference.put("email", email);
        userReference.put("address", address);
        userReference.put("city", city);
        database.collection("users")
                .document(email)
                .set(userReference)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), CoreActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Error writing user",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void finishSignUp(View view){
        name = nameET.getText().toString().trim();
        phone = phoneET.getText().toString().trim();
        email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser())
                .getEmail();
        address = addressET.getText().toString().trim();
        city = cityET.getText().toString().trim();
        if(name.isEmpty()){
            displayErrorTV(R.string.unspecified_name);
            return;
        }
        if(StaticClass.containsDigit(name)){
            displayErrorTV(R.string.name_not_number);
            return;
        }
        if(phone.length()<10){
            displayErrorTV(R.string.insufficient_phone_number);
            return;
        }
        if(address.isEmpty()){
            displayErrorTV(R.string.unspecified_address);
            return;
        }
        if(city.isEmpty()){
            displayErrorTV(R.string.unspecified_city);
            return;
        }
        progressDialog.setMessage("Finish sign-up...");
        progressDialog.show();
        editSharedPreferences();
        writeDatabase();
    }
    public void toTermsAndConditions(View view){
        startActivity(new Intent(getApplicationContext(), TermsActivity.class));
    }
    public void displayErrorTV(int resourceID){
        errorTV.setText(resourceID);
        errorTV.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                errorTV.setVisibility(View.GONE);
            }
        }, 1500);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
