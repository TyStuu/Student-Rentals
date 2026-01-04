package studentrentals.domain;

public final class Student extends User{
    private final String university;
    private final String student_ID;

    // Constructor using User superclass
    public Student(String id, String name, String email, String password_hash, String university, String student_ID) {
        super(id, name, email, password_hash);
        this.university = university;
        this.student_ID = student_ID;
    }

    // Getters
    public String getUniversity() {
        return university;
    }

    public String getStudentID() {
        return student_ID;
    }
}
