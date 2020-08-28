package com.example.dowaya.activities.core.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.activities.core.CoreActivity;
import com.example.dowaya.daos.RequestHistoryDAO;
import com.example.dowaya.models.Medicine;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class RequestFragment extends Fragment {

    private View fragmentView;
    private Context context;
    private ImageView photoIV, medicineIV;
    private TextView usernameTV, emailTV, phoneTV, clearTV, errorTV;
    private EditText medicineNameET, medicineDescriptionET, medicineDoseET;
    private String medicinePhotoString=null, medicineName, email;
    private RequestHistoryDAO requestHistoryDAO;
    private FirebaseFirestore database;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_request, container, false);
        context = fragmentView.getContext();
        database = FirebaseFirestore.getInstance();
        sharedPreferences = context.getSharedPreferences(StaticClass.SHARED_PREFERENCES, MODE_PRIVATE);
        setHasOptionsMenu(true);
        findViewsByIds();
        setUserData();
        requestHistoryDAO = new RequestHistoryDAO(context);

        return fragmentView;
    }
    private void findViewsByIds(){
        photoIV = fragmentView.findViewById(R.id.photoIV);
        usernameTV = fragmentView.findViewById(R.id.usernameTV);
        emailTV = fragmentView.findViewById(R.id.emailTV);
        phoneTV = fragmentView.findViewById(R.id.phoneTV);
        medicineNameET = fragmentView.findViewById(R.id.medicineNameET);
        medicineDescriptionET = fragmentView.findViewById(R.id.medicineDescriptionET);
        medicineDoseET = fragmentView.findViewById(R.id.medicineDoseET);
        medicineIV = fragmentView.findViewById(R.id.medicineIV);
        medicineIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importImage();
            }
        });
        clearTV = fragmentView.findViewById(R.id.clearTV);
        clearTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearImage();
            }
        });
        errorTV = fragmentView.findViewById(R.id.errorTV);
    }
    private void setUserData(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(StaticClass.SHARED_PREFERENCES, MODE_PRIVATE);
        String photoUri = sharedPreferences.getString(StaticClass.PHOTO, "");
        if(!photoUri.isEmpty()){
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(
                        context.getContentResolver(), Uri.parse(photoUri));
            } catch (IOException e) {
                Toast.makeText(context, "IO Exception",
                        Toast.LENGTH_LONG).show();
            }
            photoIV.setImageBitmap(imageBitmap);
        }
        usernameTV.setText(sharedPreferences.getString(StaticClass.USERNAME, "no username"));
        emailTV.setText(sharedPreferences.getString(StaticClass.EMAIL, "no email"));
        phoneTV.setText(sharedPreferences.getString(StaticClass.PHONE, "no phone number"));
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_request, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.postRequest){
            request();
        }
        return false;
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
                medicineIV.setImageBitmap(imageBitmap);
                medicineIV.setScaleType(ImageView.ScaleType.FIT_CENTER);
                medicineIV.setAdjustViewBounds(true);
                clearTV.setVisibility(View.VISIBLE);
                medicinePhotoString = String.valueOf(uri);
            }
        }
    }
    private void clearImage(){
        medicineIV.setImageDrawable(context.getDrawable(R.drawable.ic_image_grey));
        medicineIV.setAdjustViewBounds(false);
        medicinePhotoString = null;
        clearTV.setVisibility(View.GONE);
    }
    private void insertRequestHistory(){
        Medicine medicine = new Medicine();
        medicine.setId(medicineName);
        medicine.setName(medicineName);
        medicine.setDescription(medicineDescriptionET.getText().toString());
        medicine.setDose(medicineDoseET.getText().toString());
        medicine.setPhoto(medicinePhotoString);
        medicine.setRequestTime(
                new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
                .format(Calendar.getInstance().getTime()));
        requestHistoryDAO.insertRequestHistory(medicine);
    }
    private void addRequestUser(){
        DocumentReference userReference = database.collection("users")
                .document(email);
        DocumentReference medicinesRequests =
                database.collection("medicines-requests")
                        .document(medicineName);
        medicinesRequests.update("requesters",
                FieldValue.arrayUnion(userReference))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,
                                "medicine requester successfully written!",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(context, CoreActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,
                                "Error writing medicine store",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void writeRequest(){
        DocumentReference userReference = database.collection("users")
                .document(email);
        ArrayList<DocumentReference> requesters = new ArrayList<>();
        requesters.add(userReference);
        Map<String, Object> medicineRequest = new HashMap<>();
        medicineRequest.put("name", medicineName);
        medicineRequest.put("description", medicineDescriptionET.getText().toString());
        medicineRequest.put("dose", medicineDoseET.getText().toString());
        medicineRequest.put("requesters", requesters);
        database.collection("medicines-requests")
                .document(medicineName)
                .set(medicineRequest)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,
                                "request successfully written!",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,
                                "Error writing request",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void uploadRequest(){
        DocumentReference documentReference =
                database.collection("medicines-requests")
                        .document(medicineName);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        addRequestUser();
                        Toast.makeText(context,
                                "request exists",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        writeRequest();
                    }
                } else {
                    Toast.makeText(context,
                            "get failed with " + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void request(){
        email = sharedPreferences.getString(StaticClass.EMAIL, "");
        String temp = medicineNameET.getText().toString().trim().toLowerCase();
        medicineName = temp.substring(0, 1).toUpperCase() + temp.substring(1);
        if(!medicineNameET.getText().toString().isEmpty()){
            Toast.makeText(context, "requested", Toast.LENGTH_SHORT).show();
            insertRequestHistory();
            uploadRequest();
            startActivity(new Intent(context, CoreActivity.class));
        }else{
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
}

















