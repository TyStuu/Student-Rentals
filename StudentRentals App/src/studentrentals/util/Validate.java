package studentrentals.util;

public final class Validate {
    private Validate(){}

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


}
