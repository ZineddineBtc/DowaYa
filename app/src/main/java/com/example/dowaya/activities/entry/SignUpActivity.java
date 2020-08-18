package com.example.dowaya.activities.entry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.activities.TermsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    EditText emailET, passwordET;
    TextView errorTV;
    ProgressDialog progressDialog;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Objects.requireNonNull(getSupportActionBar()).hide();

        firebaseAuth = FirebaseAuth.getInstance();

        emailET = findViewById(R.id.emailET);
        emailET.requestFocus();
        passwordET = findViewById(R.id.passwordET);
        errorTV = findViewById(R.id.errorTV);

        progressDialog = new ProgressDialog(this);
    }

    public void signUp(View view){
        email = emailET.getText().toString().trim();
        password = passwordET.getText().toString().trim();

        if(!StaticClass.isValidEmail(email)) {
            displayErrorTV(R.string.invalid_email);
            return;
        }
        if(password.length()<8) {
            displayErrorTV(R.string.insufficient_password);
            return;
        }

        progressDialog.setMessage("Signing up...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "User created",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),
                                    FinishSignUpActivity.class));
                        } else {
                            displayErrorTV(R.string.authentication_failed);

                        }
                        progressDialog.dismiss();
                    }
                });
    }

    public void toLogin(View view){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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
