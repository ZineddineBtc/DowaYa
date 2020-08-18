package com.example.dowaya.activities.entry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.dowaya.R;
import com.example.dowaya.activities.core.CoreActivity;
import com.example.dowaya.activities.entry.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LogoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        Objects.requireNonNull(getSupportActionBar()).hide();

        mAuth = FirebaseAuth.getInstance();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkEntry();
            }
        }, 1000);
    }
    public void checkEntry(){
        if(currentUser == null){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }else{
            startActivity(new Intent(getApplicationContext(), CoreActivity.class));
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }
}
