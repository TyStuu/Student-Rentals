package studentrentals.domain;

public abstract class User {
    private final String id;
    private String name;
    private final String email;
    private String password_hash;
    private boolean is_active;

    //Create util.validate for not blank ------------------------------------------------------

    // Constructor
    protected User(String id, String name, String email, String password_hash){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password_hash = password_hash;
        this.is_active = true;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return password_hash;
    }

    public boolean isActive() {
        return is_active;
    }

    public void updateUserName(String name) {
        this.name = name;
    }

    public void updatePasswordHash(String password_hash) {
        this.password_hash = password_hash;
    }

    public void updateProfileName(String name) {
        this.name = name;
    }

    public void deactivateAccount() {
        this.is_active = false;
    }
}
