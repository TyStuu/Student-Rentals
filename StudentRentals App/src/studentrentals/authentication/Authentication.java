package studentrentals.authentication;

import studentrentals.repository.UserRepo;
import studentrentals.util.IDManage;
import studentrentals.util.Passwords;
import studentrentals.domain.Student;
import studentrentals.domain.User;
import studentrentals.domain.Homeowner;

import java.util.Optional;

import studentrentals.util.Validate;


public final class Authentication {
    private final UserRepo userRepo;

    public Authentication(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Student registerStudent( String id, String name, String email, String password, String university, String student_ID){
        Validate.notBlank(university, "University");
        Validate.notBlank(student_ID, "Student ID");

        Student student = new Student(id, name, email, Passwords.hashPassword(password), university, student_ID);
        userRepo.saveUser(student);
        return student;
    }

    public Homeowner registerHomeowner(String name, String email, String password){
        Homeowner homeowner = new Homeowner(IDManage.generateUserID(), name, email, Passwords.hashPassword(password));
        userRepo.saveUser(homeowner);
        return homeowner;
    }

    public User loginAndAuth (String email, String password) {
        Optional<User> user = userRepo.findUserByEmail(email);
        if (user.isEmpty() || !user.get().isActive()) {
            throw new IllegalArgumentException("Invalid email or account is deactivated");
        }

        String hash = Passwords.hashPassword(password);
        String stored_hash = user.get().getPasswordHash();

        if (!hash.equals(stored_hash)) {
            throw new IllegalArgumentException("Invalid password");
        }

        return user.get();
    }
}
