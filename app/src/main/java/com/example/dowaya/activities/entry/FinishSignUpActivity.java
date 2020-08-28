package com.example.dowaya.activities.entry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.activities.TermsActivity;
import com.example.dowaya.activities.core.CoreActivity;
import com.example.dowaya.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class FinishSignUpActivity extends AppCompatActivity {

    EditText nameET, phoneET, addressET;
    TextView errorTV;
    SharedPreferences sharedPreferences;
    String name, phone, email, address, city;
    FirebaseFirestore database;
    ProgressDialog progressDialog;
    private FusedLocationProviderClient fusedLocationClient;

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
        errorTV = findViewById(R.id.errorTV);
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyHavePermission()) {
                requestForSpecificPermission();
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }
    private boolean checkIfAlreadyHavePermission() {
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
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

    public void finishSignUp(View view){
        name = nameET.getText().toString();
        phone = phoneET.getText().toString().trim();
        email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser())
                        .getEmail();
        address = addressET.getText().toString();
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
        progressDialog.setMessage("Finish sign-up...");
        progressDialog.show();

        if(city==null) city = address;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(StaticClass.USERNAME, name);
        editor.putString(StaticClass.PHONE, phone);
        editor.putString(StaticClass.EMAIL, email);
        editor.putString(StaticClass.ADDRESS, address);
        editor.apply();

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
                        Toast.makeText(getApplicationContext(),
                                "user successfully written!",
                                Toast.LENGTH_SHORT).show();
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
    public void getLocation(View view) {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            getFullAddress(location);
                        }
                    }
                });
        Toast.makeText(getApplicationContext(), "getLocation", Toast.LENGTH_SHORT).show();
    }
    @SuppressLint("SetTextI18n")
    public void getFullAddress(Location location){
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        try{
            addresses = geocoder.getFromLocation(
                    location.getLatitude(), location.getLongitude(), 1);
            // Here 1 represent max location result to returned, it's recommended 1 to 5 in the docs
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            addressET.setText(address+" "+city+" "+state);
        }catch(IOException | NullPointerException e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }


    }
}
