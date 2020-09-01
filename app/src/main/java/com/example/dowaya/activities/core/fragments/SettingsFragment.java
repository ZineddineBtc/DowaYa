package com.example.dowaya.activities.core.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.activities.TermsActivity;
import com.example.dowaya.activities.entry.LoginActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {

    private View fragmentView;
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseFirestore database;
    private LinearLayout addressCityLL;
    private TextView nameTV, emailTV, phoneTV, addressCityTV, signOutTV, termsTV;
    private EditText nameET, phoneET, addressET, cityET;
    private ImageView photoIV, editNameIV, editPhoneIV, editAddressCityIV;
    private boolean isNameEdit, isPhoneEdit, isAddressEdit, imageChanged;
    private String uriString;


    @SuppressLint("CommitPrefEdits")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_settings, container, false);
        context = fragmentView.getContext();
        sharedPreferences = context.getSharedPreferences(StaticClass.SHARED_PREFERENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        database = FirebaseFirestore.getInstance();
        findViewsByIds();
        initializeData();
        setClickListeners();
        return fragmentView;
    }
    private void findViewsByIds(){
        photoIV = fragmentView.findViewById(R.id.photoIV);
        nameTV = fragmentView.findViewById(R.id.nameTV);
        nameET = fragmentView.findViewById(R.id.nameET);
        emailTV = fragmentView.findViewById(R.id.emailTV);
        phoneTV = fragmentView.findViewById(R.id.phoneTV);
        phoneET = fragmentView.findViewById(R.id.phoneET);
        addressCityTV = fragmentView.findViewById(R.id.addressCityTV);
        addressCityLL = fragmentView.findViewById(R.id.addressCityLL);
        addressET = fragmentView.findViewById(R.id.addressET);
        cityET = fragmentView.findViewById(R.id.cityET);
        editNameIV = fragmentView.findViewById(R.id.editNameIV);
        editPhoneIV = fragmentView.findViewById(R.id.editPhoneIV);
        editAddressCityIV = fragmentView.findViewById(R.id.editAddressIV);
        signOutTV = fragmentView.findViewById(R.id.signOutTV);
        termsTV = fragmentView.findViewById(R.id.termsTV);
    }
    private void initializeData(){
        /*
        if(!sharedPreferences.getString(StaticClass.PHOTO, "").isEmpty()){
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(
                        context.getContentResolver(),
                        Uri.parse(sharedPreferences.getString(StaticClass.PHOTO, "")));
            } catch (IOException e) {
                Toast.makeText(context, "IO Exception",
                        Toast.LENGTH_LONG).show();
            }
            photoIV.setImageBitmap(imageBitmap);
        }*/
        nameTV.setText(sharedPreferences.getString(StaticClass.USERNAME, "no username"));
        nameET.setText(sharedPreferences.getString(StaticClass.USERNAME, ""));
        emailTV.setText(sharedPreferences.getString(StaticClass.EMAIL, "no email"));
        phoneTV.setText(sharedPreferences.getString(StaticClass.PHONE, "no phone number"));
        phoneET.setText(sharedPreferences.getString(StaticClass.PHONE, ""));
        String addressCity = sharedPreferences.getString(StaticClass.ADDRESS, "") + ", " +
                sharedPreferences.getString(StaticClass.CITY, "");
        addressCityTV.setText(addressCity);
        addressET.setText(sharedPreferences.getString(StaticClass.ADDRESS, ""));
        cityET.setText(sharedPreferences.getString(StaticClass.CITY, ""));
    }
    private void setClickListeners(){
        photoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importImage();
            }
        });
        editNameIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editName();
            }
        });
        editPhoneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPhone();
            }
        });
        editAddressCityIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAddressCity();
            }
        });
        signOutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        termsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(fragmentView.getContext(), TermsActivity.class));
            }
        });
    }
    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(fragmentView.getContext(), LoginActivity.class));
    }
    private void editName(){
        nameTV.setVisibility(isNameEdit ? View.VISIBLE : View.GONE);
        nameET.setVisibility(isNameEdit ? View.GONE : View.VISIBLE);
        editNameIV.setImageResource(isNameEdit ?
                R.drawable.ic_edit_green_24dp : R.drawable.ic_check_green_24dp);
        if(isNameEdit) updateData();
        isNameEdit = !isNameEdit;
    }
    private void editPhone(){
        phoneTV.setVisibility(isPhoneEdit ? View.VISIBLE : View.GONE);
        phoneET.setVisibility(isPhoneEdit ? View.GONE : View.VISIBLE);
        editPhoneIV.setImageResource(isPhoneEdit ?
                R.drawable.ic_edit_green_24dp : R.drawable.ic_check_green_24dp);
        if(isPhoneEdit) updateData();
        isPhoneEdit = !isPhoneEdit;
    }
    private void editAddressCity(){
        addressCityTV.setVisibility(isAddressEdit ? View.VISIBLE : View.GONE);
        addressCityLL.setVisibility(isAddressEdit ? View.GONE : View.VISIBLE);
        editAddressCityIV.setImageResource(isAddressEdit ?
                R.drawable.ic_edit_green_24dp : R.drawable.ic_check_green_24dp);
        if(isAddressEdit) updateData();
        isAddressEdit = !isAddressEdit;
    }
    private void importImage(){
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
                Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri uri = data.getData();
            if(uri != null){
                final int takeFlags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
                ContentResolver resolver = context.getContentResolver();
                resolver.takePersistableUriPermission(uri, takeFlags);

                Bitmap imageBitmap = null;
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(
                            context.getContentResolver(), uri);
                } catch (IOException e) {
                    Toast.makeText(context, "IO Exception",
                            Toast.LENGTH_LONG).show();
                }
                photoIV.setImageBitmap(imageBitmap);
                imageChanged = true;
                uriString = String.valueOf(uri);
            }
        }
    }
    private void detectChange(){
        Map<String, Object> userReference = new HashMap<>();
        if(!nameET.getText().toString().equals(
                sharedPreferences.getString(StaticClass.USERNAME, ""))){
            editor.putString(StaticClass.USERNAME, nameET.getText().toString());
            userReference.put("username", nameET.getText().toString());
        }
        if(!phoneET.getText().toString().equals(
                sharedPreferences.getString(StaticClass.PHONE, ""))){
            editor.putString(StaticClass.PHONE, phoneET.getText().toString());
            userReference.put("phone", phoneET.getText().toString());
        }
        if(!addressET.getText().toString().equals(
                sharedPreferences.getString(StaticClass.ADDRESS, ""))
        || !cityET.getText().toString().equals(
                sharedPreferences.getString(StaticClass.CITY, ""))){
            editor.putString(StaticClass.ADDRESS, addressET.getText().toString());
            editor.putString(StaticClass.CITY, cityET.getText().toString());
            userReference.put("address", addressET.getText().toString());
            userReference.put("city", cityET.getText().toString());
        }
        if(imageChanged){
            editor.putString(StaticClass.PHOTO, String.valueOf(uriString));
        }
        database.collection("users")
                .document(emailTV.getText().toString())
                .update(userReference);
    }
    private boolean isValidInput(){
        return !StaticClass.containsDigit(nameET.getText().toString())
                && phoneET.getText().toString().length() > 9
                && !addressET.getText().toString().isEmpty();
    }
    private void updateData(){
        detectChange();
        String snackBarString;
        if(isValidInput()){
            editor.apply();
            initializeData();
            snackBarString = "updated";
        }else{
            snackBarString = "invalid input";
        }
        Snackbar.make(fragmentView.findViewById(R.id.parentLayout),
                snackBarString, 1000)
                .setAction("Action", null).show();
    }
}