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
import com.example.dowaya.daos.PostHistoryDAO;
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

public class PostFragment extends Fragment {

    private View fragmentView;
    private Context context;
    private SharedPreferences sharedPreferences;
    private ImageView photoIV, medicineIV;
    private TextView usernameTV, emailTV, phoneTV, clearTV, errorTV;
    private EditText medicineNameET, medicineDescriptionET, medicineDoseET,
            medicinePriceET, medicinePostAddressTV;
    private String medicinePhotoString=null;
    private PostHistoryDAO postHistoryDAO;
    private FirebaseFirestore database;
    private boolean medicineExists, storeExists;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_post, container, false);
        context = fragmentView.getContext();
        database = FirebaseFirestore.getInstance();
        setHasOptionsMenu(true);
        findViewsByIds();
        setUserData();
        postHistoryDAO = new PostHistoryDAO(context);

        return fragmentView;
    }
    private void findViewsByIds(){
        photoIV = fragmentView.findViewById(R.id.photoIV);
        usernameTV = fragmentView.findViewById(R.id.usernameTV);
        emailTV = fragmentView.findViewById(R.id.emailTV);
        phoneTV = fragmentView.findViewById(R.id.phoneTV);
        medicineNameET = fragmentView.findViewById(R.id.medicineNameET);
        medicineDescriptionET = fragmentView.findViewById(R.id.medicineDescriptionET);
        medicinePriceET = fragmentView.findViewById(R.id.medicinePriceET);
        medicinePostAddressTV = fragmentView.findViewById(R.id.medicineAddressET);
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
        sharedPreferences = context.getSharedPreferences(StaticClass.SHARED_PREFERENCES, MODE_PRIVATE);
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
            post();
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
    private void insertPostHistory(){
        Medicine medicine = new Medicine();
        medicine.setName(medicineNameET.getText().toString());
        medicine.setDescription(medicineDescriptionET.getText().toString());
        medicine.setPriceRange(medicinePriceET.getText().toString());
        medicine.setPostAddress(medicinePostAddressTV.getText().toString());
        medicine.setPhoto(medicinePhotoString);
        medicine.setRequestTime(
                new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
                        .format(Calendar.getInstance().getTime()));
        postHistoryDAO.insertPostHistory(medicine);
    }
    private boolean doesMedicineExist(){
        medicineExists = false;
        DocumentReference documentReference =
                database.collection("medicines-names")
                        .document(medicineNameET.getText().toString());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        medicineExists = true;
                    } else {
                        Toast.makeText(context,
                                "No such document",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context,
                            "get failed with " + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        return medicineExists;
    }
    private boolean doesMedicineStoreExist(){
        return medicineExists;
    }
    private void writeDescription(){
        Map<String, Object> medicineDescription = new HashMap<>();
        medicineDescription.put("name", medicineNameET.getText().toString());
        medicineDescription.put("description", medicineDescriptionET.getText().toString());
        medicineDescription.put("price", medicinePriceET.getText().toString());
        medicineDescription.put("dose", medicineDoseET.getText().toString());

        database.collection("medicines-descriptions")
                .document(medicineNameET.getText().toString())
                .set(medicineDescription)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,
                                "description successfully written!",
                                Toast.LENGTH_SHORT).show();
                        writeName();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,
                                "Error writing document",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void writeName(){
        Map<String, Object> medicineName = new HashMap<>();
        medicineName.put("name", medicineNameET.getText().toString());

        database.collection("medicines-names")
                .document(medicineNameET.getText().toString())
                .set(medicineName)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,
                                "name successfully written!",
                                Toast.LENGTH_SHORT).show();
                        writeStore();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,
                                "Error writing document",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void writeStore(){
        Map<String, Object> store = new HashMap<>();
        store.put("name", usernameTV.getText().toString());
        store.put("city", medicinePostAddressTV.getText().toString());
        store.put("address", medicinePostAddressTV.getText().toString());
        store.put("phone", sharedPreferences.getString(StaticClass.PHONE, ""));

        database.collection("stores")
                .document(usernameTV.getText().toString())
                .set(store)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,
                                "store successfully written!",
                                Toast.LENGTH_SHORT).show();
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,
                                "Error writing document",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void writeMedicineStore(){
        DocumentReference documentReference = database.collection("stores")
                .document(usernameTV.getText().toString());
        if(doesMedicineStoreExist()){
            DocumentReference medicinesStores =
                    database.collection("medicines-stores")
                    .document(medicineNameET.getText().toString());
            medicinesStores.update("stores",
                    FieldValue.arrayUnion(documentReference));
        }else{
            ArrayList<DocumentReference> stores = new ArrayList<>();
            stores.add(documentReference);
            Map<String, Object> store = new HashMap<>();
            store.put("stores", stores);

            database.collection("medicines-stores")
                    .document(medicineNameET.getText().toString())
                    .set(store)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context,
                                    "medicine store successfully written!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context,
                                    "Error writing document",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void uploadPost(){
        if(!doesMedicineExist()){
            writeDescription();
        }
        writeMedicineStore();
    }
    private void post(){
        if(!medicineNameET.getText().toString().isEmpty()
                && !medicineDescriptionET.getText().toString().isEmpty()
                && !medicinePostAddressTV.getText().toString().isEmpty()){
            insertPostHistory();
            uploadPost();
            Toast.makeText(context, "posted", Toast.LENGTH_SHORT).show();
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









