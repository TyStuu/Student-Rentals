package studentrentals.util;

import java.time.LocalDate;
import java.util.Objects;

public final class Validate {
    private Validate(){}


    // Basic Validators
    public static void notBlank(String value, String fieldName){ // Validate that a string is not null or blank
        if(value == null || value.trim().isEmpty()){
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
    }

    public static void positiveInt(int value, String fieldName){ // Validate that an integer is positive
        if(value <= 0){
            throw new IllegalArgumentException(fieldName + " must be a positive integer");
        }
    }

    public static void positiveDecimal(double value, String fieldName){ // Validate that a decimal number is positive
        if(value <= 0){
            throw new IllegalArgumentException(fieldName + " must be a positive decimal number");
        }
    }

    public static void validateDateOrder(LocalDate startDate, LocalDate endDate, String startName, String endName) {
        Objects.requireNonNull(startDate, startName + " cannot be null");
        Objects.requireNonNull(endDate, endName + " cannot be null");

        if (!startDate.isBefore(endDate)) {
            throw new IllegalArgumentException(startName + " must be before " + endName);
        }
    }


    // Format Validators
    public static void validatePassword(String password) { // Validate that password meets minimum complexity
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if ("!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~".indexOf(c) >= 0) hasSpecial = true;
        }
        if (!(hasUpper && hasLower && hasDigit && hasSpecial)) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character");
        }
    }

    public static void validateName(String name) { // Validate that name is between 2 and 50 characters
        if( name == null || name.length() < 2 || name.length() > 50) {
            throw new IllegalArgumentException("Name must be between 2 and 50 characters");
        }
    }

    public static void validateEmail(String email) { // Validate email format
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }




}
