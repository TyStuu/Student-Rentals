package studentrentals.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import studentrentals.util.Validate;
import java.util.List;


public class Room {
    private String room_ID;
    private String property_ID;
    private RoomType room_type;
    private double monthly_rent;
    private List<String> room_amenities = new ArrayList<>();
    private LocalDate available_from;
    private LocalDate available_to;
    private boolean is_active;

    public Room(String room_ID, String property_ID, RoomType room_type, double monthly_rent, List<String> room_amenities, LocalDate available_from, LocalDate available_to) {
        Validate.notBlank(room_ID, "Room ID");
        Validate.notBlank(property_ID, "Property ID");
        Validate.positiveInt((int)monthly_rent, "Monthly Rent");
        this.room_ID = room_ID;
        this.property_ID = property_ID;
        this.room_type = room_type;
        this.monthly_rent = monthly_rent;
        this.room_amenities = room_amenities;
        this.available_from = available_from;
        this.available_to = available_to;
        this.is_active = true;
    }
}
