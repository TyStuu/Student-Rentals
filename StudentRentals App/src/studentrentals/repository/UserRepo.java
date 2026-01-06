package studentrentals.repository;

import studentrentals.domain.User;
import java.util.*;


public final class UserRepo {

    // In-memory storage for users
    private final Map<String, User> user_by_ID = new HashMap<>();
    private final Map<String, User> user_by_email = new HashMap<>();
    
    public Optional<User> findUserByID (String id) {
        Optional<User> user = Optional.ofNullable(user_by_ID.get(id));
        return user;
    }

    public Optional<User> findUserByEmail (String email) {
        Optional<User> user = Optional.ofNullable(user_by_email.get(emailNormalise(email)));
        return user;
    }

    public void saveUser(User user) {
        Objects.requireNonNull(user);
        String email = emailNormalise(user.getEmail());

        if (user_by_ID.containsKey(user.getId()) || user_by_email.containsKey(email)) {
            throw new IllegalArgumentException("User with given ID or email already exists");
        }
        user_by_ID.put(user.getId(), user);
        user_by_email.put(email, user);
    }

    public Collection<User> getAllUsers() {
        Collection<User> all_users = Collections.unmodifiableCollection(user_by_ID.values());
        return all_users;
    }

    private static String emailNormalise (String email) { // Normalize email for consistent storage and lookup
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        return email.trim().toLowerCase();
    }
}
