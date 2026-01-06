package studentrentals.domain;

import studentrentals.util.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Property {
    private final String property_id;
    private final String homeowner_ID;
    private String address;
    private String description;
    private List<String> house_amenities = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private boolean is_active;
    private int review_count = 0;
    private int review_total = 0;

    // Constructor
    public Property(String property_id, String homeowner_ID, String address, String description, List<String> house_amenities) {
        Validate.notBlank(property_id, "Property ID");
        Validate.notBlank(homeowner_ID, "Homeowner ID");
        Validate.notBlank(address, "Address");
        
        this.property_id = property_id;
        this.homeowner_ID = homeowner_ID;
        this.address = address.trim();
        this.description = description.trim();
        this.house_amenities = house_amenities;
        this.is_active = true;
    }

    // Getters
    public String getPropertyId() {
        return property_id;
    }
    public String getHomeownerID() {
        return homeowner_ID;
    }
    public String getAddress() {
        return address;
    }
    public String getDescription() {
        return description;
    }
    public List<String> getHouseAmenities() {
        return house_amenities;
    }
    public List<Room> getRooms() {
        return rooms;
    }
    public boolean isActive() {
        return is_active;
    }

    // Setters / Updaters   
    public void setAddress(String address) { // Update address if any changes
        Validate.notBlank(address, "Address");
        this.address = address.trim();
    }
    public void setDescription(String description) { // Update description for property
        Validate.notBlank(description, "Description");
        this.description = description.trim();
    }
    public void setHouseAmenities(List<String> house_amenities) { //Allow changes for adding/removing amenities
        this.house_amenities = house_amenities;
    }
    public void addRoom(Room room) {
        this.rooms.add(Objects.requireNonNull(room, "Room cannot be null"));
    }

    // Review Methods
    public double getAverageRating() {
        if (review_count == 0) {
            System.out.println("No reviews yet.");
            return 0.0;
        } 
        else {
            double average = (double) review_total / review_count;
            return average;
        }
    }
    public void addReview(int rating) {
        review_total += rating;
        review_count += 1;
    }


    public void deactivate() {
        this.is_active = false;
    }
}




