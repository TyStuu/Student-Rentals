package studentrentals.domain;

import studentrentals.util.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Property {
    private String property_id;
    private String address;
    private String description;
    private List<String> house_amenities = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private boolean is_active;

    // Constructor
    public Property(String property_id, String address, String description, List<String> house_amenities) {
        Validate.notBlank(property_id, "Property ID");
        Validate.notBlank(address, "Address");
        
        this.property_id = property_id;
        this.address = address.trim();
        this.description = description.trim();
        this.house_amenities = house_amenities;
        this.is_active = true;
    }

    public String getPropertyId() {
        return property_id;
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

    public void deactivate() {
        this.is_active = false;
    }

    
    


}




