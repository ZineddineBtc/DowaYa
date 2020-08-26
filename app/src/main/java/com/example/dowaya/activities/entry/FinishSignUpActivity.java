package com.example.dowaya.activities.entry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.dowaya.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FinishSignUpActivity extends AppCompatActivity {

    EditText nameET, phoneET;
    TextView errorTV;
    SharedPreferences sharedPreferences;
    String name, phone, email;
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
        errorTV = findViewById(R.id.errorTV);
    }

    public void finishSignUp(View view){
        progressDialog.setMessage("Finish sign-up...");
        progressDialog.show();
        name = nameET.getText().toString();
        phone = phoneET.getText().toString().trim();
        email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser())
                        .getEmail();

        if(StaticClass.containsDigit(name)){
            displayErrorTV(R.string.name_not_number);
            return;
        }
        if(phone.length()<10){
            displayErrorTV(R.string.insufficient_phone_number);
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(StaticClass.USERNAME, name);
        editor.putString(StaticClass.PHONE, phone);
        editor.putString(StaticClass.EMAIL, email);
        editor.apply();

        Map<String, Object> userReference = new HashMap<>();
        userReference.put("username", name);
        userReference.put("phone", phone);
        userReference.put("email", email);
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

}
