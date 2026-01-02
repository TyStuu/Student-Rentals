package studentrentals.domain;

import studentrentals.util.Validate;

import java.util.ArrayList;
import java.util.List;

public class Property {
    private String property_id;
    private String address;
    private String description;
    private List<String> house_amenities = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private boolean is_active;

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

    public void setAddress(String address) {
        Validate.notBlank(address, "Address");
        this.address = address.trim();
    }

    public void setDescription(String description) {
        Validate.notBlank(description, "Description");
        this.description = description.trim();
    }
    public void setHouseAmenities(List<String> house_amenities) {
        this.house_amenities = house_amenities;
    }

    public void deactivate() {
        this.is_active = false;
    }

    
    


}




