package com.example.dowaya.activities.core.fragments;

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
    private ImageView photoIV;
    private TextView usernameTV, emailTV, phoneTV, errorTV;
    private EditText medicineNameET, medicineDescriptionET,
            medicineDoseET, minPriceET, maxPriceET;
    private String medicinePhotoString=null, medicineName, email, priceRange;
    private PostHistoryDAO postHistoryDAO;
    private FirebaseFirestore database;

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
        minPriceET = fragmentView.findViewById(R.id.medicineMinPriceET);
        maxPriceET = fragmentView.findViewById(R.id.medicineMaxPriceET);
        medicineDoseET = fragmentView.findViewById(R.id.medicineDoseET);
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
    private void insertPostHistory(){
        Medicine medicine = new Medicine();
        medicine.setId(medicineName);
        medicine.setName(medicineName);
        medicine.setDescription(medicineDescriptionET.getText().toString());
        medicine.setPrice(priceRange);
        medicine.setPostAddress(sharedPreferences.getString(StaticClass.ADDRESS, ""));
        medicine.setPhoto(medicinePhotoString);
        medicine.setRequestTime(
                new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
                        .format(Calendar.getInstance().getTime()));
        postHistoryDAO.insertPostHistory(medicine);
    }
    private void writeDescription() {
        Map<String, Object> medicineDescription = new HashMap<>();
        medicineDescription.put("name", medicineName);
        medicineDescription.put("description", medicineDescriptionET.getText().toString());
        medicineDescription.put("price", priceRange);
        medicineDescription.put("dose", medicineDoseET.getText().toString());

        database.collection("medicines-descriptions")
                .document(medicineName)
                .set(medicineDescription)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        writeName();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,
                                "Error writing description",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void writeName(){
        Map<String, Object> medicineNameMap = new HashMap<>();
        medicineNameMap.put("name", medicineName);
        database.collection("medicines-names")
                .document(medicineName)
                .set(medicineNameMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,
                                "name successfully written!",
                                Toast.LENGTH_SHORT).show();
                        writeStore(false);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,
                                "Error writing name",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void writeStore(final boolean medicineExists){
        Map<String, Object> store = new HashMap<>();
        store.put("name", usernameTV.getText().toString());
        store.put("city", sharedPreferences.getString(StaticClass.CITY, ""));
        store.put("address", sharedPreferences.getString(StaticClass.ADDRESS, ""));
        store.put("phone", sharedPreferences.getString(StaticClass.PHONE, ""));

        database.collection("stores")
                .document(email)
                .set(store)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,
                                "store successfully written!",
                                Toast.LENGTH_SHORT).show();
                        writeMedicineStore(medicineExists);
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,
                                "Error writing store",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void writeMedicineStore(boolean medicineExists){
        DocumentReference storeReference = database.collection("stores")
                .document(email);
        DocumentReference medicinesStores =
                database.collection("medicines-stores")
                        .document(medicineName);
        if (medicineExists){
            medicinesStores.update("stores",
                    FieldValue.arrayUnion(storeReference))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context,
                                    "medicine store successfully written!",
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
        }else {
            ArrayList<DocumentReference> referenceList = new ArrayList<>();
            referenceList.add(storeReference);
            Map<String, ArrayList> referenceMap = new HashMap<>();
            referenceMap.put("stores", referenceList);
            medicinesStores.set(referenceMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context,
                                    "medicine store successfully written!",
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
    }
    private void uploadPost(){
        DocumentReference documentReference =
                database.collection("medicines-names")
                        .document(medicineName);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        writeStore(true);
                    } else {
                        writeDescription();
                    }
                } else {
                    Toast.makeText(context,
                            "get failed with " + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void post(){
        email = sharedPreferences.getString(StaticClass.EMAIL, "");
        String temp = medicineNameET.getText().toString().trim().toLowerCase();
        if(temp.length()>2){
            medicineName = temp.substring(0, 1).toUpperCase() + temp.substring(1);
        }else{
            medicineName = "";
        }
        priceRange = minPriceET.getText().toString()+"-"+maxPriceET.getText().toString()+" DA";
        if(!medicineNameET.getText().toString().isEmpty()
                && !medicineDescriptionET.getText().toString().isEmpty()){
            uploadPost();
            insertPostHistory();
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









