package studentrentals.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import studentrentals.util.Validate;
import java.util.List;
import java.util.Objects;


public class Room {
    private String room_ID;
    private String property_ID;
    private RoomType room_type;
    private double monthly_rent;
    private List<String> room_amenities = new ArrayList<>();
    private LocalDate available_from;
    private LocalDate available_to;
    private boolean is_active;

    // Constructor
    public Room(String room_ID, String property_ID, RoomType room_type, double monthly_rent, List<String> room_amenities, LocalDate available_from, LocalDate available_to) {
        Validate.notBlank(room_ID, "Room ID");
        Validate.notBlank(property_ID, "Property ID");
        Validate.positiveInt((int)monthly_rent, "Monthly Rent");
        Objects.requireNonNull(room_type, "Room Type cannot be null");
        Objects.requireNonNull(available_from, "Available From date cannot be null");
        Objects.requireNonNull(available_to, "Available To date cannot be null");
        
        this.room_ID = room_ID;
        this.property_ID = property_ID;
        this.room_type = room_type;
        this.monthly_rent = monthly_rent;
        this.room_amenities = room_amenities;
        this.available_from = available_from;
        this.available_to = available_to;
        this.is_active = true;
    }

    // Getters
    public String getRoomID() {
        return room_ID;
    }
    public String getPropertyID() {
        return property_ID;
    }
    public RoomType getRoomType() {
        return room_type;
    }
    public double getMonthlyRent() {
        return monthly_rent;
    }
    public List<String> getRoomAmenities() {
        return room_amenities;
    }
    public LocalDate getAvailableFrom() {
        return available_from;
    }
    public LocalDate getAvailableTo() {
        return available_to;
    }
    public boolean isActive() {
        return is_active;
    }

    // Setters / Updaters
    public void setMonthlyRent(double monthly_rent) { //Allow changes for rent adjustments
        Validate.positiveInt((int)monthly_rent, "Monthly Rent");
        this.monthly_rent = monthly_rent;
    }
    public void setRoomAmenities(List<String> room_amenities) { //Allow changes for adding/removing amenities
        this.room_amenities = room_amenities;
    }
    public void setAvailableFrom(LocalDate available_from) { //Allow changes for availability adjustments
        this.available_from = available_from;
    }
    public void setAvailableTo(LocalDate available_to) { //^^
        this.available_to = available_to;
    }
    public boolean isAvailable(LocalDate beginning, LocalDate end) {
        return (beginning.isEqual(available_from) || beginning.isAfter(available_from)) &&
               (end.isEqual(available_to) || end.isBefore(available_to));
    }

    
    public void deactivate() {
        this.is_active = false;
    }

}
