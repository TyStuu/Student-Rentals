package studentrentals.domain;

public final class Admin extends User{

    // Constructor using User superclass
    public Admin(String id, String name, String email, String password_hash) {
        super(id, name, email, password_hash);
    }

}
