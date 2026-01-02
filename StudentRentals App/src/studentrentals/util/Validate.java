package studentrentals.util;

public final class Validate {
    private Validate(){}

    public static void notBlank(String value, String fieldName){
        if(value == null || value.trim().isEmpty()){
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
    }

    public static void positiveInt(int value, String fieldName){
        if(value <= 0){
            throw new IllegalArgumentException(fieldName + " must be a positive integer");
        }
    }


}
