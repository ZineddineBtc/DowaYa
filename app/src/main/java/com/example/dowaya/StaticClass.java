package com.example.dowaya;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StaticClass {

    public static int PICK_SINGLE_IMAGE = 1;
    public static String SHARED_PREFERENCES = "shared_preferences";
    public static String USERNAME = "username";
    public static String EMAIL = "email";
    public static String PHONE = "phone";
    public static String PHOTO = "photo";

    public static boolean isValidEmail(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean containsDigit(String s) {
        boolean containsDigit = false;
        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
        }
        return containsDigit;
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
