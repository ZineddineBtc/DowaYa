package com.example.dowaya.activities.core;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.activities.entry.LoginActivity;
import com.example.dowaya.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class CoreActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private FirebaseAuth firebaseAuth;
    TextView nameTV, emailTV, phoneTV;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);

        firebaseAuth = FirebaseAuth.getInstance();
        id = firebaseAuth.getCurrentUser().getUid();

        nameTV = findViewById(R.id.nameTV);
        emailTV = findViewById(R.id.emailTV);
        phoneTV = findViewById(R.id.phoneTV);

        sharedPreferences = getSharedPreferences(StaticClass.SHARED_PREFERENCES, MODE_PRIVATE);
        updateUserData();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Users").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(StaticClass.USERNAME,
                        Objects.requireNonNull(user).getUsername());
                editor.putString(StaticClass.EMAIL, user.getEmail());
                editor.putString(StaticClass.PHONE, user.getPhone());
                editor.apply();
                Toast.makeText(getApplicationContext(), "onDataChange",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateUserData(){
        nameTV.setText(sharedPreferences.getString(StaticClass.USERNAME, "no username"));
        emailTV.setText(sharedPreferences.getString(StaticClass.EMAIL, "no email"));
        phoneTV.setText(sharedPreferences.getString(StaticClass.PHONE, "no phone number"));
    }

    public void signOut(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}
