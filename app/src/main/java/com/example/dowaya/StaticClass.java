package com.example.dowaya;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.dowaya.models.Medicine;
import com.example.dowaya.models.Store;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StaticClass {

    public static int PICK_SINGLE_IMAGE = 1;
    public static String SHARED_PREFERENCES = "shared_preferences";
    public static String USERNAME = "username";
    public static String EMAIL = "email";
    public static String PHONE = "phone";
    public static String ADDRESS = "address";
    public static String CITY = "city";
    public static String PHOTO = "photo";
    public static String MEDICINE_ID = "medicine_id";
    public static String STORE_ID = "store_id";
    public static String FULL_SCREEN = "full_screen";
    public static String FROM = "from";
    public static String MEDICINE_DESCRIPTION = "medicine_description";
    public static String POST_HISTORY = "post_history";
    public static String REQUEST_HISTORY = "request_history";


    public static boolean isValidEmail(String email) {
        if(email.length()>4){
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }else{
            return false;
        }

    }
    public static boolean containsDigit(String s) {
        if(s.length()>2){
            boolean containsDigit = false;
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
            return containsDigit;
        }else{
            return false;
        }
    }
    /*
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
        });*/
}
