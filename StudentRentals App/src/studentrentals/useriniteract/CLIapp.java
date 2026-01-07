package studentrentals.useriniteract;

import studentrentals.util.Validate;
import studentrentals.util.Passwords;
import studentrentals.domain.*;
import studentrentals.repository.*;
import studentrentals.util.IndexUtil;
import java.util.Scanner;


public final class CLIapp {

    private final UserRepo user_repo;
    private final PropertyRoomRepo property_room_repo;
    private final BookingRepo booking_repo;

    private final Scanner scanner = new Scanner(System.in);

    public CLIapp(UserRepo user_repo, PropertyRoomRepo property_room_repo, BookingRepo booking_repo) {
        this.user_repo = user_repo;
        this.property_room_repo = property_room_repo;
        this.booking_repo = booking_repo;
    }

    public void run() {
        // need login authentication + admin changes
    }

    private void loginMenu() {
        System.out.println("Welcome to the Student Rentals Management App");
        System.out.println("Please log in to continue.");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try{
            //authentication and session login
        }
        catch (Exception e){
            System.out.println("Login failed: " + e.getMessage());
        }

    }


    private void homeownerMenu(User homeowner) {
        System.out.println("\\n -------- Homeowner Menu --------");
                System.out.println("Welcome, " + homeowner.getName() + " (Homeowner)");

        System.out.println("1) View user profile.");
        System.out.println("2) Edit user profile.");
        //create property, add rooms, dashboard, manage bookings.
        String choice = scanner.nextLine().trim();

    }

    private void studentMenu(User student) {
        
        System.out.println("\\n -------- Student Menu --------");
        System.out.println("Welcome, " + student.getName() + " (Student)");

        System.out.println("1) View user profile.");
        System.out.println("2) Edit user profile.");
        //search booking reviews
        // messages (notification?)
        System.out.println("0) Logout. ");

        String choice = scanner.nextLine().trim();

    }

    private void adminMenu(User admin) {
        System.out.println("\\n -------- Admin Menu --------");

        System.out.println("Welcome, " + admin.getName() + " (Admin)");

        // Admin view data, deativate users, deactivate rooms/properties
        System.out.println("0) Logout. ");

        String choice = scanner.nextLine().trim();
    }

    private void loggedIn() {
        User user = //find current user using session

        if (user instanceof Student student) {
            studentMenu(student);
        }
        else if (user instanceof Homeowner homeowner) {
            homeownerMenu(homeowner);
        }
        else if (user instanceof Admin admin) {
            adminMenu(admin);
        }
        else {
            System.out.println("Unknown user type.");
            //logout
        }
    }

    private void viewProfile(User user) {
        System.out.println("---- User Profile ----");
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Account Active: " + user.isActive());
    }

    private void editProfile(User user) {
        System.out.println("---- Edit Profile ----");
        String new_name = scanner.nextLine().trim();
        user.updateProfileName(new_name);
        System.out.println("Profile updated successfully.");
    }






}
