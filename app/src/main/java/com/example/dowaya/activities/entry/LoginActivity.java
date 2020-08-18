package com.example.dowaya.activities.entry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dowaya.R;
import com.example.dowaya.activities.TermsActivity;
import com.example.dowaya.activities.core.CoreActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    EditText emailET, passwordET;
    TextView errorTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        emailET = findViewById(R.id.emailET);
        emailET.requestFocus();
        passwordET = findViewById(R.id.passwordET);
        errorTV = findViewById(R.id.errorTV);
    }

    public void login(View view){
        String email = emailET.getText().toString().isEmpty() ? " ":emailET.getText().toString();
        String password = passwordET.getText().toString().isEmpty() ? " ":passwordET.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Sign in with success",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(
                                    getApplicationContext(), CoreActivity.class
                            ));
                        } else {
                            displayErrorTV(R.string.authentication_failed);
                        }
                    }
                });
    }

    public void toSignUp(View view){
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
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

}
