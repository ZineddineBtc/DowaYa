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
    private ImageView photoIV;
    private TextView usernameTV, emailTV, phoneTV, errorTV;
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
        if(temp.length()>2){
            medicineName = temp.substring(0, 1).toUpperCase() + temp.substring(1);
        }else{
            medicineName = "";
        }
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

















