package studentrentals.util;

public class Passwords {
    private Passwords(){}

    public static String hashPassword(String password){ // Fowler-Noll-Vo-1a hash function for password hashing
        Validate.notBlank(password, password);

        long hash = 0xcbf29ce484222325L; // FNV-1a offset (Hex)

        for (int i=0;i<password.length(); i++){
            hash ^= password.charAt(i);
            hash *= 0x100000001b3L; // FNV-1a prime (Hex)
        }

        return Long.toHexString(hash);
    }
}
