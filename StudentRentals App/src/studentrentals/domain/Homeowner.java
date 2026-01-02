package studentrentals.domain;

public final class Homeowner extends User{

    // Constructor using User superclass
    public Homeowner(String id, String name, String email, String password_hash) {
        super(id, name, email, password_hash);
    }

}
