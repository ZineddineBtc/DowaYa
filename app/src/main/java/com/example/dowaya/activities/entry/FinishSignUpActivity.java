package com.example.dowaya.activities.entry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.activities.TermsActivity;
import com.example.dowaya.activities.core.CoreActivity;
import com.example.dowaya.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class FinishSignUpActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    EditText nameET, phoneET;
    TextView errorTV;
    SharedPreferences sharedPreferences;
    String name, phone, id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_sign_up);

        Objects.requireNonNull(getSupportActionBar()).hide();
        sharedPreferences = getSharedPreferences(StaticClass.SHARED_PREFERENCES, MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();
        id = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        nameET = findViewById(R.id.nameET);
        nameET.requestFocus();
        phoneET = findViewById(R.id.phoneET);
        errorTV = findViewById(R.id.errorTV);
    }

    public void finishSignUp(View view){
        name = nameET.getText().toString();
        phone = phoneET.getText().toString().trim();

        if(StaticClass.containsDigit(name)){
            displayErrorTV(R.string.name_not_number);
            return;
        }
        if(phone.length()<10){
            displayErrorTV(R.string.insufficient_phone_number);
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Users").child(id);
        databaseReference.setValue(
                new User(
                    name,
                    Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail(),
                    phone
                )
        );

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(StaticClass.USERNAME, name);
        editor.putString(StaticClass.PHONE, phone);
        editor.putString(StaticClass.EMAIL,
                Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser())
                        .getEmail());
        editor.apply();

        startActivity(new Intent(getApplicationContext(), CoreActivity.class));
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
