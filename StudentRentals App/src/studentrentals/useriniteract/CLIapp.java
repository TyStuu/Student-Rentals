package studentrentals.useriniteract;

import studentrentals.util.Validate;
import studentrentals.util.Passwords;
import studentrentals.domain.*;
import studentrentals.repository.*;
import studentrentals.util.IDManage;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Instant;

import studentrentals.authentication.Authentication;


public final class CLIapp {

    private final UserRepo user_repo;
    private final PropertyRoomRepo property_room_repo;
    private final BookingRepo booking_repo;
    private final Authentication auth;

    private final Scanner scanner = new Scanner(System.in);
    private final Session session =new Session();

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
                    System.exit(0);
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
        System.out.println("6) View my bookings");
        System.out.println("7) Cancel a booking.");
        System.out.println("0) Logout. ");
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
                case "6":
                    viewBookingHistory(homeowner);
                    break;
                case "7":
                    cancelBookingMenu(homeowner);
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
        System.out.println("3) Search Rooms.");
        System.out.println("4) Create a booking request.");
        System.out.println("5) View my bookings.");
        System.out.println("6) Cancel a booking.");
        System.out.println("7) Leave a review.");
        // booking reviews
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
                case "3":
                    studentSearchRoomsMenu((Student) student);
                    break;
                case "4":
                    studentCreateBookingMenu((Student) student);
                    break;
                case "5":
                    viewBookingHistory(student);
                    break;
                case "6":
                    cancelBookingMenu((Student) student);
                    break;
                case "7":
                    leaveReviewMenu((Student) student);
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

        System.out.println("1) List data type (Users, Properties, Rooms, Bookings).");
        System.out.println("2) Delete data type (User, Property, Room, Booking).");
        System.out.println("0) Logout. ");

        String choice = scanner.nextLine().trim();

        try { 
            switch (choice) {
                case "1":
                    adminListDataMenu((Admin) admin);
                    break;
                case "2":
                    adminDeleteDataMenu((Admin) admin);
                    break;
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
        if (!selected_property.getHomeownerID().equals(homeowner.getId())) { //Make sure homeowner actually owns property to add room to.
            System.out.println("You do not own this property.");
            System.out.println("Press Enter to continue..");
            scanner.nextLine();
            return;
        }

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

        System.out.println("Would you like to activate/deactivate any of your properties / rooms? (Yes/No): ");
        String choice = scanner.nextLine().trim().toLowerCase();
        if (choice.equals("yes")) {
            activateDeactivateMenu(homeowner);
        }
        else {
            System.out.println("Returning to menu.");
            return;
        }
    }

    private void activateDeactivateMenu(User homeowner) {
        System.out.println("\n ---- Activate / Deactivate Menu ----");

        List<Property> properties = property_room_repo.findPropertyByOwnerID(homeowner.getId());
        if (properties.isEmpty()) {
            System.out.println("No properties found. Please create a property first.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.println("Your Properties:");
        for (int i = 0; i < properties.size(); i++){
            Property property = properties.get(i);
            System.out.println((i + 1) + ") " + property.getAddress() + " (ID: "+ property.getPropertyId()+ ") - Active?: " + property.isActive());
        }
        System.out.println("Enter the number of the property you wish to activate/deactivate: ");
        String choice_input = scanner.nextLine().trim();
        int choice = Integer.parseInt(choice_input);
        if (choice < 1 || choice > properties.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Property selected_property = properties.get(choice - 1);
        if (selected_property.isActive()) {
            for (Booking booking : booking_repo.findBookingByHomeownerID(selected_property.getHomeownerID())) {
                if (booking.getBookingStatus() == BookingStatus.APPROVED) {
                    System.out.println("Cannot deactivate property with active bookings.");
                    return;
                }
            }

            selected_property.deactivate();
            System.out.println("Property deactivated.");
        } else {
            selected_property.activate();
            System.out.println("Property activated.");
        }
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

// Search functions
    private void studentSearchRoomsMenu(Student student) {
        System.out.println("\n ---- Search Rooms ----");
        
        
        System.out.println("Enter Keyword to seach for (address / description) or leave blank to view all results:");
        String keyword = scanner.nextLine().trim();

        //Base linear search
        List<RoomSearch> results = property_room_repo.linearRoomSearch(keyword);
        if (results.isEmpty()) {
            throw new IllegalArgumentException("No rooms found matching your criteria.");
        }

        //Apply filters
        results = applySearchFilters(results);
        if (results.isEmpty()) {
            throw new IllegalArgumentException("No rooms found matching your criteria after applying filters.");
        }

        //sort results
        sortSearchResults(results);

        results = applyTopN(results);

        displaySearchResults(results);

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private List<RoomSearch> applySearchFilters(List<RoomSearch> rooms) {
        System.out.println("Apply Filters? (Yes/No): ");
        String yes_no = scanner.nextLine().trim().toLowerCase();
        if (!yes_no.equals("yes")) {
            return rooms; //No filters applied
        }

        System.out.println("Minimum Rent (blank = none): "); //Get min rent filter
        String min_rent_input = scanner.nextLine().trim();
        Double min_rent = null;
        if (!min_rent_input.isEmpty()) {
            min_rent = Double.parseDouble(min_rent_input);
            Validate.positiveDecimal(min_rent, "Minimum Rent");
        }

        System.out.println("Maximum Rent (blank = none): "); //Get max rent filter
        String max_rent_input = scanner.nextLine().trim();
        Double max_rent = null;
        if (!max_rent_input.isEmpty()) {
            max_rent = Double.parseDouble(max_rent_input);
            Validate.positiveDecimal(max_rent, "Maximum Rent");
        }

        System.out.println("Room Type (Single, Double, Ensuite) (blank = none): "); //  Get room type filter
        String room_type_input = scanner.nextLine().trim();
        RoomType room_type = null;
        if (!room_type_input.isEmpty()) {
            room_type = RoomType.valueOf(room_type_input);
        }

        System.out.println("Filter by availability dates? (Yes/No): "); //Get availability date filter
        String date_filter_input = scanner.nextLine().trim().toLowerCase();
        LocalDate available_from = null;
        LocalDate available_to = null;
        if (date_filter_input.equals("yes")) {
            System.out.println("Available From date (YYYY-MM-DD): ");
            String from_date_input = scanner.nextLine().trim();
            available_from = LocalDate.parse(from_date_input);

            System.out.println("Available To date (YYYY-MM-DD): ");
            String to_date_input = scanner.nextLine().trim();
            available_to = LocalDate.parse(to_date_input);

            Validate.validateDateOrder(available_from, available_to, "Available From", "Available To");
        }

        //Apply above filtes
        List<RoomSearch> filtered_rooms = new ArrayList<>();
        for (RoomSearch room_search : rooms) {
            Room room = room_search.getRoom();
            boolean matches = true;

            if (min_rent !=null && room.getMonthlyRent() < min_rent) {
                matches = false;
            }
            if (max_rent !=null && room.getMonthlyRent() > max_rent){
                matches = false;
            }
            if (room_type !=null && room.getRoomType() !=room_type){
                matches = false;
            }
            if (available_from !=null && available_to !=null) {
                if (room.getAvailableFrom().isAfter(available_from) || room.getAvailableTo().isBefore(available_to)){
                    matches =false;
                }
            }

            if (matches) { //Room meets all criteria
                filtered_rooms.add(room_search);
            }
        }

        return filtered_rooms;
    }

    private void sortSearchResults(List<RoomSearch> rooms) {
        System.out.println("\nSort by:");

        System.out.println("1) Price: Low to High");
        System.out.println("2) Price: High to Low");
        System.out.println("3) Address: A to Z");
        System.out.println("4) Address: Z to A");
        System.out.println("5) Listing Date: Newest to Oldest");
        System.out.println("6) Listing Date: Oldest to Newest");
        String choice = scanner.nextLine().trim();

        Comparator<RoomSearch> comparator;

        switch (choice) {
            case "1":
                comparator = Comparator.comparingDouble(rs-> rs.getRoom().getMonthlyRent());
                break;

            case "2":
                comparator = Comparator.comparingDouble((RoomSearch rs)-> rs.getRoom().getMonthlyRent()).reversed();
                break;
            case "3":
                comparator = Comparator.comparing(rs ->rs.getProperty().getAddress().toLowerCase());
                break;
            case "4":
                comparator = Comparator.comparing((RoomSearch rs)-> rs.getProperty().getAddress().toLowerCase()).reversed();
                break;
            case "5":
                comparator = Comparator.comparing((RoomSearch rs) ->rs.getRoom().getCreatedAt()).reversed();
                break;
            case "6":
                comparator = Comparator.comparing(rs ->rs.getRoom().getCreatedAt());
                break;

            default:
                System.out.println("Invalid choice. No sorting applied.");
                return;
        }

        Comparator<RoomSearch> finalComparator = comparator.thenComparing(rs-> rs.getRoom().getRoomID()); //final tiebreaker case of exact matches are sorted by room id
        rooms.sort(finalComparator);
    }

    private List<RoomSearch> applyTopN(List<RoomSearch> rooms) {
        System.out.println("Limit amount of results? (Yes/No): ");
        String yes_no = scanner.nextLine().trim().toLowerCase();
        if (!yes_no.equals("yes")) {
            return rooms;
        }

        System.out.println("Enter number of results to show:");
        int n = Integer.parseInt(scanner.nextLine().trim());
        if (n <=0 || n > rooms.size()) {
            System.out.println("Invalid number of results. Showing all results.");
            return rooms;
        }
        return rooms.subList(0, n);
    }

    private void displaySearchResults(List<RoomSearch> rooms) {
        System.out.println("\n ---- Search Results ----");
        
        for (RoomSearch room_search : rooms){
            Property property =  room_search.getProperty();
            Room room  = room_search.getRoom();
            if (property.isActive() && room.isActive()) {
                System.out.println("Property ID: " + property.getPropertyId()
                        + "\n    Address: " + property.getAddress()
                        + "\n    Description: " + property.getDescription()
                        + "\n    Room ID: " + room.getRoomID()
                        + "\n    Room Type: " + room.getRoomType()
                        + "\n    Monthly Rent: " + room.getMonthlyRent()
                        + "\n    Available From: " + room.getAvailableFrom() + " to " + room.getAvailableTo());
            }
        }
    }

//Booking functions
    private void studentCreateBookingMenu(Student student) {
        System.out.println("\n ---- Create Booking Request ----");
        System.out.println("Enter Room ID to book: ");
        String room_id = scanner.nextLine().trim();
        Validate.notBlank(room_id, "Room ID");

        Optional<Room> room = property_room_repo.findRoomByID(room_id);
        if (room.isEmpty()) {
            throw new IllegalArgumentException("Room not found. Please try again.");
        }

        Optional<Property> property = property_room_repo.findPropertyByRoomID(room_id);
        if (property.isEmpty()) {
            throw new IllegalArgumentException("Property for the selected room not found. Please try again.");
        }

        if (!room.get().isActive() || !property.get().isActive()) {
            throw new IllegalArgumentException("Selected room or its property is not active. Cannot proceed with booking.");
        }
        // Check that booking dates are within room availability and that start date is before end date and that end date is after start date
        System.out.println("Enter booking srart date (YYYY-MM-DD): "); 
        String start_date_input = scanner.nextLine().trim();

        LocalDate start_date = LocalDate.parse(start_date_input);
        System.out.println("Enter booking end date (YYYY-MM-DD): ");
        String end_date_input = scanner.nextLine().trim();
        LocalDate end_date = LocalDate.parse(end_date_input);
        Validate.validateDateOrder(start_date, end_date, "Start Date", "End Date");

        if (start_date.isBefore(room.get().getAvailableFrom())|| end_date.isAfter(room.get().getAvailableTo())) {
            throw new IllegalArgumentException("Booking dates must be within the room's available dates: " + room.get().getAvailableFrom() + " to " + room.get().getAvailableTo());
        }

        // check overlap against approved bookings for the same room
        for (Booking existing_booking : booking_repo.listBookinigsByRoomID(room_id)) {
            if (existing_booking.getBookingStatus() == BookingStatus.APPROVED) {
                if (overlaps(start_date, end_date, existing_booking.getBookingStartDate(), existing_booking.getBookingEndDate())) {
                    throw new IllegalArgumentException("Booking dates overlap with an existing approved booking from " + existing_booking.getBookingStartDate() + " to " + existing_booking.getBookingEndDate());
                }
            }
        }

        Instant expiry = Instant.now().plus(Duration.ofHours(24)); //Booking request expires in 24 hours if not approved/rejected by homeowner

        Booking booking = new Booking(
            IDManage.generateBookingID(),
            room_id,
            student.getId(),
            property.get().getPropertyId(),
            property.get().getHomeownerID(),
            start_date,
            end_date,
            expiry
        );

        booking_repo.saveBooking(booking);

        System.out.println("Booking request created successfully with ID: " + booking.getBookingID());
        System.out.println("Status: " + booking.getBookingStatus());
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private void viewBookingHistory( User currentUser) {
        System.out.println("\n---- My Booking History ----");

        List<Booking> bookings;

        if (currentUser instanceof Student) {
            bookings = booking_repo.findBookingByStudent(currentUser.getId());
        } else if (currentUser instanceof Homeowner) {
            bookings = booking_repo.findBookingByHomeownerID(currentUser.getId());
        } else {
            System.out.println("(Admins do not have booking history)");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        bookings.sort(Comparator.comparing(Booking::getBookingStartDate));

        if (bookings.isEmpty()) {
            System.out.println("(No bookings)");
        } 
        else {
            for (Booking b : bookings) {
                System.out.println("Booking ID: " + b.getBookingID()
                        + "\n    Property: " + b.getPropertyID()
                        + "\n    Room: " + b.getRoomID()
                        + "\n    Student: " + b.getStudentID()
                        + "\n    Start Date: " + b.getBookingStartDate() + " to " + b.getBookingEndDate()
                        + "\n    Status: " + b.getBookingStatus());
            }
        }

        if (currentUser instanceof Homeowner){
            System.out.println("Would you like to aprrove/decline any pending bookings? (Yes/No): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("yes")) {
                approveDeclineBookingMenu(currentUser);
            }
            else {
                System.out.println("Returning to menu.");
                return;
            }
        }
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
        }

    private void cancelBookingMenu(User user) {
        System.out.println("\n---- Cancel Booking ----");

        System.out.print("Enter Booking ID: ");
        String bookingID = scanner.nextLine().trim();
        Validate.notBlank(bookingID, "Booking ID");

        Booking booking = booking_repo.findBookingByID(bookingID)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // Only the booking's student OR homeowner can cancel
        boolean isStudentParty = booking.getStudentID().equals(user.getId());
        boolean isHomeownerParty = booking.getHomeownerID().equals(user.getId());

        if (!isStudentParty && !isHomeownerParty) {
            throw new IllegalArgumentException("You are not allowed to cancel this booking.");
        }

        // Only pending or approved can be cancelled (your rule)
        if (booking.getBookingStatus() != BookingStatus.PENDING &&
            booking.getBookingStatus() != BookingStatus.APPROVED) {
            throw new IllegalArgumentException("Only PENDING or APPROVED bookings can be cancelled.");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        System.out.println("Booking cancelled.");

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    private void approveDeclineBookingMenu(User homeowner) {
        System.out.println("\n---- Approve / Decline Booking ----");

        System.out.print("Enter Booking ID: ");
        String bookingID = scanner.nextLine().trim();
        Validate.notBlank(bookingID, "Booking ID");

        Booking booking = booking_repo.findBookingByID(bookingID)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!booking.getHomeownerID().equals(homeowner.getId())) {
            throw new IllegalArgumentException("You do not own this booking request.");
        }

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new IllegalArgumentException("Only PENDING bookings can be approved/declined.");
        }

        System.out.println("1) Approve");
        System.out.println("2) Decline");
        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            // Re-check overlap against existing APPROVED bookings before approving
            for (Booking b : booking_repo.listBookinigsByRoomID(booking.getRoomID())) {
                if (b.getBookingStatus() == BookingStatus.APPROVED) {
                    if (overlaps(booking.getBookingStartDate(), booking.getBookingEndDate(),
                                b.getBookingStartDate(), b.getBookingEndDate())) {
                        throw new IllegalArgumentException("Cannot approve: overlaps an approved booking.");
                    }
                }
            }

            booking.setBookingStatus(BookingStatus.APPROVED);
            System.out.println("Booking approved.");

        } else if (choice.equals("2")) {
            booking.setBookingStatus(BookingStatus.DECLINED);
            System.out.println("Booking declined.");
        } else {
            System.out.println("Invalid choice.");
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

// review functions 
private void leaveReviewMenu(Student student) {
    System.out.println("\n---- Leave a Review ----");

    System.out.print("Enter Booking ID: ");
    String bookingID = scanner.nextLine().trim();
    Validate.notBlank(bookingID, "Booking ID");

    Booking booking = booking_repo.findBookingByID(bookingID)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

    // must be the student who made it
    if (!booking.getStudentID().equals(student.getId())) {
        throw new IllegalArgumentException("You can only review your own bookings.");
    }

    // must be approved
    if (booking.getBookingStatus() != BookingStatus.APPROVED) {
        throw new IllegalArgumentException("Only APPROVED bookings can be reviewed.");
    }

    // must be after booking end date
    if (!LocalDate.now().isAfter(booking.getBookingEndDate())) {
        throw new IllegalArgumentException("You can only review after the booking end date: " + booking.getBookingEndDate());
    }

    // prevent duplicate review
    if (booking.getReviewID() != null && !booking.getReviewID().isBlank()) {
        throw new IllegalArgumentException("This booking has already been reviewed.");
    }

    Property property = property_room_repo.findPropertyByID(booking.getPropertyID())
            .orElseThrow(() -> new IllegalArgumentException("Property not found"));

    System.out.print("Rating (1-5): ");
    int rating = Integer.parseInt(scanner.nextLine().trim());
    if (rating < 1 || rating > 5) {
        throw new IllegalArgumentException("Rating must be between 1 and 5.");
    }

    System.out.print("Optional comment (press Enter to skip): ");
    String comment = scanner.nextLine();

    Review review = new Review(
            IDManage.generateReviewID(),
            booking.getBookingID(),
            student.getId(),
            property.getPropertyId(),
            rating,
            comment
    );

    property.addReview(review);
    booking.setReviewID(review.getReviewID());

    System.out.println("Review submitted. Thank you!");
    System.out.println("Press Enter to continue...");
    scanner.nextLine();
}

//admin finctions
    private void adminListDataMenu(Admin admin) {
        System.out.println("\n---- List Data ----");
        System.out.println("Select data type to list:");
        System.out.println("1) Users");
        System.out.println("2) Properties");
        System.out.println("3) Rooms");
        System.out.println("4) Bookings");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                for (User user : user_repo.getAllUsers()) {
                    System.out.println(user);
                }
                break;
            case "2":
                for (Property property : property_room_repo.getAllProperties()) {
                    System.out.println(property);
                }
                break;
            case "3":
                for (RoomSearch room : property_room_repo.getAllActiveRooms()) {
                    System.out.println(room);
                }
                break;
            case "4":
                for (Booking booking : booking_repo.getAllBookings()) {
                    System.out.println(booking);
                }
                break;
            default:
                System.out.println("Invalid choice.");
                return;
            }
        }

    private void adminDeleteDataMenu(Admin admin) {
        System.out.println("\n---- Delete Data ----");
        System.out.println("Select data type to delete:");
        System.out.println("1) User");
        System.out.println("2) Property");
        System.out.println("3) Room");
        System.out.println("4) Booking");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                System.out.print("Enter User ID to delete: ");
                String userID = scanner.nextLine().trim();
                user_repo.findUserByID(userID).get().deactivateAccount();
                System.out.println("User deactivated.");
                break;
            case "2":
                System.out.print("Enter Property ID to delete: ");
                String propertyID = scanner.nextLine().trim();
                property_room_repo.findPropertyByID(propertyID).get().deactivate();
                System.out.println("Property deactivated.");
                break;
            case "3":
                System.out.print("Enter Room ID to delete: ");
                String roomID = scanner.nextLine().trim();
                property_room_repo.findRoomByID(roomID).get().deactivate();
                System.out.println("Room deactivated.");
                break;
            case "4":
                System.out.print("Enter Booking ID to delete: ");
                String bookingID = scanner.nextLine().trim();
                booking_repo.findBookingByID(bookingID).get().setBookingStatus(BookingStatus.CANCELLED);
                System.out.println("Booking cancelled.");
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
    }




    private boolean overlaps(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

}

