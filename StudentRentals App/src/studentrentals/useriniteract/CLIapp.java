package studentrentals.useriniteract;

import studentrentals.util.Validate;
import studentrentals.util.Passwords;
import studentrentals.domain.*;
import studentrentals.repository.*;
import studentrentals.util.IDManage;
import studentrentals.util.IndexUtil;
import java.util.Scanner;
import java.util.jar.Attributes.Name;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.time.LocalDate;

import studentrentals.authentication.Authentication;


public final class CLIapp {

    private final UserRepo user_repo;
    private final PropertyRoomRepo property_room_repo;
    private final BookingRepo booking_repo;
    private final Authentication auth;

    private final Scanner scanner = new Scanner(System.in);
    private final Session session = new Session();

    public CLIapp(UserRepo user_repo, PropertyRoomRepo property_room_repo, BookingRepo booking_repo, Authentication auth) {
        this.user_repo = user_repo;
        this.property_room_repo = property_room_repo;
        this.booking_repo = booking_repo;
        this.auth = auth;
    }

    public void run() {
        while (true) {
            if (session.isLoggedIn()) {
                loggedIn();
            } else {
                authenticationMenu();
            }
        }
    }

    private void authenticationMenu() {
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

// Login and Registration
    private void loginMenu() {
        System.out.println("Please log in to continue.");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        try{
            User user = auth.loginAndAuth(email, password);
            System.out.println("Login successful. Welcome, " + user.getName());
            session.loginSetCurrentUser(user);
        }
        catch (Exception e){
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private void registerMenu() {
        System.out.println("\n -------- Registration --------");
        System.out.println("Please select what type of account to register:");
        System.out.println("1) Student");
        System.out.println("2) Homeowner");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();

        System.out.println("Enter new account Name: ");
        String name = scanner.nextLine().trim();
        Validate.validateName(name);
        System.out.println("Enter new account Email: ");
        String email = scanner.nextLine().trim();
        Validate.validateEmail(email);
        System.out.println("Enter new account Password: ");
        String password = scanner.nextLine().trim();
        Validate.validatePassword(password);

        switch (choice) {
            case "1":
                System.out.println("University: ");
                String university = scanner.nextLine().trim();
                System.out.println("Your University student ID: ");
                String student_ID = scanner.nextLine().trim();

                Student student = auth.registerStudent( IDManage.generateUserID(), name, email, password, university, student_ID);
                System.out.println("Student registration successful. Please log in again to continue, " + student.getName());
                break;

            case "2":
                Homeowner homeowner = auth.registerHomeowner(name, email, password);
                System.out.println("Homeowner registration successful. Please log in again to continue, " + homeowner.getName());
        }
    }

// Logged in routing
    private void loggedIn() {
        User user = session.getCurrentUser();

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
            session.logoutClearCurrentUser();
        }
    }

// Useer type specific menus
    private void homeownerMenu(User homeowner) {
        System.out.println("\n -------- Homeowner Menu --------");
                System.out.println("Welcome, " + homeowner.getName() + " (Homeowner)");

        System.out.println("1) View user profile.");
        System.out.println("2) Edit user profile.");
        System.out.println("3) Create a Property listing.");
        System.out.println("4) Add a Room to a Property.");
        System.out.println("5) View Dashboard.");
        String choice = scanner.nextLine().trim();

        try {
            switch (choice) {
                case "1":
                    viewProfile(homeowner);
                    break;
                case "2":
                    editProfile(homeowner);
                    break;

                case "3":
                    createPropertyMenu(homeowner);
                    break;
                case "4":
                    addRoomMenu(homeowner);
                    break;
                case "5":
                    dashboardMenu(homeowner);
                    break;  

                case "0":
                    System.out.println("Logging out.");
                    session.logoutClearCurrentUser();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void studentMenu(User student) {
        
        System.out.println("\n -------- Student Menu --------");
        System.out.println("Welcome, " + student.getName() + " (Student)");

        System.out.println("1) View user profile.");
        System.out.println("2) Edit user profile.");
        //search booking reviews
        System.out.println("0) Logout. ");

        String choice = scanner.nextLine().trim();

        try {
            switch (choice) {
                case "1":
                    viewProfile(student);
                    break;
                case "2":
                    editProfile(student);
                    break;
                case "0":
                    System.out.println("Logging out.");
                    session.logoutClearCurrentUser();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void adminMenu(User admin) {
        System.out.println("\n -------- Admin Menu --------");

        System.out.println("Welcome, " + admin.getName() + " (Admin)");

        // Admin view data, deativate users, deactivate rooms/properties
        System.out.println("0) Logout. ");

        String choice = scanner.nextLine().trim();

        try { 
            switch (choice) {
                case "0":
                    System.out.println("Logging out.");
                    session.logoutClearCurrentUser();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

//Homeowner Functions (Create Property, Add Room and Dashboard)
    private void createPropertyMenu(User homeowner) {
        System.out.println("\n ---- Create Property ----");
        System.out.println("Enter property address (address also used as title):");
        String address = scanner.nextLine().trim();
        Validate.notBlank(address, "Address");

        System.out.println("Enter property description:");
        String description = scanner.nextLine().trim();
        Validate.notBlank(description, "Description");

        System.out.println("Enter property amenities (comma separated or blank):");
        String amenities_input = scanner.nextLine().trim();
        List<String> amenities = new ArrayList<>();
        if (!amenities_input.isEmpty()){
            String[] amenities_array = amenities_input.split(","); //split amenities by comma
            for (String amenity : amenities_array){
                amenities.add(amenity.trim());
            }
        }

        Property property = new Property(IDManage.generatePropertyID(), homeowner.getId(), address, description, amenities);
        property_room_repo.saveProperty(property);

        System.out.println("Property created successfully with ID: " + property.getPropertyId());
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private void addRoomMenu(User homeowner){
        System.out.println("\n ---- Add Room to Property ----");

        List<Property> properties = property_room_repo.findPropertyByOwnerID(homeowner.getId());
        if (properties.isEmpty()) {
            System.out.println("No properties found. Please create a property first.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.println("Select a property to add a room to:");
        for (int i = 0; i < properties.size(); i++){ //List all propertiues owned by homeowner
            System.out.println("   " + (i +1) + ") "+ properties.get(i).getAddress()+ " (ID: "+ properties.get(i).getPropertyId()+ ")");
        }
        System.out.println("Enter your choicie (Property ID): ");
        String choice = scanner.nextLine().trim();
        Validate.notBlank(choice, "Property ID");

        Optional<Property> selected_property_opttional = property_room_repo.findPropertyByID(choice);
        if (selected_property_opttional.isEmpty()) {
            System.out.println("Property not found. Please try again.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        Property selected_property = selected_property_opttional.get();

        System.out.println("Enter Room Type (Single, Double, Ensuite): ");
        System.out.println("Room Type: ");
        String room_type_input = scanner.nextLine().trim();
        RoomType room_type = RoomType.valueOf(room_type_input);

        System.out.println("Enter Monthly Rent: ");
        String rent_input = scanner.nextLine().trim();
        double monthly_rent = Double.parseDouble(rent_input);
        Validate.positiveDecimal(monthly_rent, "Monthly Rent");

        System.out.println("Enter Room Amenities (comma separated or blank): ");
        String amenities_input = scanner.nextLine().trim();
        List<String> room_amenities = new ArrayList<>();
        if (!amenities_input.isEmpty()){
            String[] amenities_array = amenities_input.split(","); //split amenities by comma
            for (String amenity : amenities_array){
                room_amenities.add(amenity.trim());
            }
        }

        System.out.println("Enter Available From date (YYYY-MM-DD): ");
        String from_date_input = scanner.nextLine().trim();
        LocalDate available_from = LocalDate.parse(from_date_input);

        System.out.println("Enter Available To date (YYYY-MM-DD): ");
        String to_date_input = scanner.nextLine().trim();
        LocalDate available_to = LocalDate.parse(to_date_input);

        Validate.validateDateOrder(available_from, available_to, "Available From", "Available To");

        Room room = new Room(IDManage.generateRoomID(), selected_property.getPropertyId(), room_type, monthly_rent, room_amenities, available_from, available_to); //Create new room and add it to corersponding property
        property_room_repo.addRoomToProperty(room);

        System.out.println("Room added successfully with ID: " + room.getRoomID()+ " to Property ID: " + selected_property.getPropertyId());
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private void dashboardMenu(User homeowner){
        System.out.println("\n ---- Homeowner Dashboard ----");
        
        List<Property> properties = property_room_repo.findPropertyByOwnerID(homeowner.getId());
        if (properties.isEmpty()) {
            System.out.println("No properties found. Please create a property first.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.println("Your Properties:");
        for (Property property : properties){
            System.out.println(" - " + property.getAddress() + " (ID: "+ property.getPropertyId()+ ")");
            System.out.println("Active?: " + property.isActive());
            System.out.println("Rooms:");
            if (property.getRooms().isEmpty()){
                System.out.println("No rooms added in " + property.getAddress()+ "yet.");
            }
            else {
                for (Room room : property.getRooms()){
                    System.out.println("   - " + room.getRoomID()+ "\n     Type: " + room.getRoomType()+ "\n     Rent: " + room.getMonthlyRent()+ 
                    "\n     Availibility: " + room.getAvailableFrom() + " to " + room.getAvailableTo() + "\n     Active?: " + room.isActive());
                }
            }
        }
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

// Menus for all user types.
    private void viewProfile(User user) {
        System.out.println("\n ---- User Profile ----");
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Account Active: " + user.isActive());
        System.out.println("----------------------");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private void editProfile(User user) {
        System.out.println("\n ---- Edit Profile ----");
        System.out.print("Enter new name (leave blank to keep current): ");
        String new_name = scanner.nextLine().trim();
        if (!new_name.isEmpty()) {
            Validate.validateName(new_name);
            user.updateProfileName(new_name);
        }
        System.out.println("Enter new password (Leave blank to keep current): ");
        String new_password = scanner.nextLine().trim();
        if (!new_password.isEmpty()) {
            Validate.validatePassword(new_password);
            String hashed_password = Passwords.hashPassword(new_password);
            user.updatePasswordHash(hashed_password);
        }
        System.out.println("Profile updated successfully.");
    }
}
