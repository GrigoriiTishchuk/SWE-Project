package com.edujournal.backend.utils;

public class InputValidator {
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (email == null) return false;

        email = email.trim();
        if (email.isEmpty() || email.contains(" ")) return false;

        // just one @
        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex != email.lastIndexOf('@')) return false;

        String local = email.substring(0, atIndex);
        String domain = email.substring(atIndex + 1);

        if (local.startsWith(".") || !domain.contains(".")) return false;

        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 9;
    }
}
