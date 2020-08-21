package com.example.dowaya.activities.core;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;

import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class CoreActivity extends AppCompatActivity {

    AppBarConfiguration appBarConfiguration;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    ImageView userPhotoIV;
    TextView userNameTV, userEmailTV, userPhoneTV;
    SharedPreferences sharedPreferences;
    String photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);
        findViewsByIds();
        setSupportActionBar(toolbar);
        setUpNavigation();
        setUserData();
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
    public void findViewsByIds(){
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
    }
    public void setUserData(){
        userPhotoIV = navigationView.getHeaderView(0).findViewById(R.id.userPhotoIV);
        userNameTV = navigationView.getHeaderView(0).findViewById(R.id.userNameTV);
        userEmailTV = navigationView.getHeaderView(0).findViewById(R.id.userEmailTV);
        userPhoneTV = navigationView.getHeaderView(0).findViewById(R.id.userPhoneTV);
        sharedPreferences = getSharedPreferences(StaticClass.SHARED_PREFERENCES, MODE_PRIVATE);
        photoUri = sharedPreferences.getString(StaticClass.PHOTO, "");
        if(!photoUri.isEmpty()){
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(
                        this.getContentResolver(), Uri.parse(photoUri));
            } catch (IOException e) {
                Toast.makeText(this, "IO Exception",
                        Toast.LENGTH_LONG).show();
            }
            userPhotoIV.setImageBitmap(imageBitmap);
        }
        userNameTV.setText(sharedPreferences.getString(StaticClass.USERNAME, "no username"));
        userEmailTV.setText(sharedPreferences.getString(StaticClass.EMAIL, "no email"));
        userPhoneTV.setText(sharedPreferences.getString(StaticClass.PHONE, "no phone number"));
    }
    public void setUpNavigation(){
        navigationView = findViewById(R.id.nav_view);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_medicine_list, R.id.nav_bookmark, R.id.nav_history,
                R.id.nav_tools, R.id.nav_share, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController,
                appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void importImage(View view){
        Intent intent;
        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Select Images"),
                StaticClass.PICK_SINGLE_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StaticClass.PICK_SINGLE_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri uri = data.getData();
            if(uri != null){
                final int takeFlags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
                ContentResolver resolver = this.getContentResolver();
                resolver.takePersistableUriPermission(uri, takeFlags);

                Bitmap imageBitmap = null;
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(), uri);
                } catch (IOException e) {
                    Toast.makeText(this, "IO Exception",
                            Toast.LENGTH_LONG).show();
                }
                userPhotoIV.setImageBitmap(imageBitmap);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(StaticClass.PHOTO, String.valueOf(uri));
                editor.apply();
            }
        }
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
