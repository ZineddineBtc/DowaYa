package com.example.dowaya;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StaticClass {

    public static String SHARED_PREFERENCES = "shared_preferences";
    public static String USERNAME = "username";
    public static String EMAIL = "email";
    public static String PHONE = "phone";

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
}
