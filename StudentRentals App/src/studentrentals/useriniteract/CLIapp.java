package studentrentals.useriniteract;

import studentrentals.util.Validate;
import studentrentals.util.Passwords;
import studentrentals.domain.*;
import studentrentals.repository.*;
import studentrentals.util.IDManage;
import studentrentals.util.IndexUtil;
import java.util.Scanner;
import java.util.jar.Attributes.Name;

import studentrentals.authentication.Authentication;


public final class CLIapp {

    private final UserRepo user_repo;
    private final PropertyRoomRepo property_room_repo;
    private final BookingRepo booking_repo;
    private final Authentication auth;

    private final Scanner scanner = new Scanner(System.in);

    public CLIapp(UserRepo user_repo, PropertyRoomRepo property_room_repo, BookingRepo booking_repo, Authentication auth) {
        this.user_repo = user_repo;
        this.property_room_repo = property_room_repo;
        this.booking_repo = booking_repo;
        this.auth = auth;
    }

    public void run() {
        while (true) {
            System.out.println("Welcome to the Student Rentals Management App");
            System.out.println("1) Login");
            System.out.println("2) Register");
            System.out.println("0) Exit");
            String choice = scanner.nextLine().trim();

            try{
                switch (choice) {
                    case "1" :
                        loginMenu();
                        break;
                    case "2":
                        registerMenu();
                        break;
                    case "0":
                        System.out.println("Exiting the application.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
            catch (Exception e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void loginMenu() {
        System.out.println("Please log in to continue.");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        try{
            User user = auth.loginAndAuth(email, password);
            System.out.println("Login successful. Welcome, " + user.getName());
            // Proceed to user-specific menu;
        }
        catch (Exception e){
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private void registerMenu() {
        System.out.println("Please select what type of account to register:");
        System.out.println("1) Student");
        System.out.println("2) Homeowner");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();

        System.out.println("Name: ");
        String name = scanner.nextLine().trim();
        System.out.println("Email: ");
        String email = scanner.nextLine().trim();
        System.out.println("Password: ");
        String password = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                System.out.println("University: ");
                String university = scanner.nextLine().trim();
                System.out.println("Your University student ID: ");
                String student_ID = scanner.nextLine().trim();

                Student student = auth.registerStudent( IDManage.generateUserID(), name, email, password, university, student_ID);
                System.out.println("Student registration successful. Welcome, " + student.getName());
                break;

            case "2":
                Homeowner homeowner = auth.registerHomeowner(name, email, password);
                System.out.println("Homeowner registration successful. Welcome, " + homeowner.getName());
        }
    }


    private void homeownerMenu(User homeowner) {
        System.out.println("\n -------- Homeowner Menu --------");
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
