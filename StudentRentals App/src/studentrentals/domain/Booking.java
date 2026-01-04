package studentrentals.domain;

import java.time.LocalDate;
import java.time.Instant;

public class Booking {

    private final String booking_ID;
    private final String room_ID;
    private final String student_ID;
    private final String property_ID;
    private final String homeowner_ID;

    private final LocalDate booking_start_date;
    private final LocalDate booking_end_date;

    private BookingStatus booking_status;
    private final Instant created_at;
    private Instant recent_update;
    private Instant expiry;

    public Booking(String booking_ID, String room_ID, String student_ID, String property_ID, String homeowner_ID,
                   LocalDate booking_start_date, LocalDate booking_end_date, Instant expiry) {
        this.booking_ID = booking_ID;
        this.room_ID = room_ID;
        this.student_ID = student_ID;
        this.property_ID = property_ID;
        this.homeowner_ID = homeowner_ID;
        this.booking_start_date = booking_start_date;
        this.booking_end_date = booking_end_date;
        this.booking_status = BookingStatus.PENDING;
        this.created_at = Instant.now();
        this.recent_update = created_at;
        this.expiry = expiry;
    }

    // Getters
    public String getBookingID() {
        return booking_ID;
    }
    public String getRoomID() {
        return room_ID;
    }
    public String getStudentID() {
        return student_ID;
    }
    public String getPropertyID() {
        return property_ID;
    }
    public String getHomeownerID() {
        return homeowner_ID;
    }
    public LocalDate getBookingStartDate() {
        return booking_start_date;
    }
    public LocalDate getBookingEndDate() {
        return booking_end_date;
    }
    public BookingStatus getBookingStatus() {
        return booking_status;
    }
    public Instant getCreatedAt() {
        return created_at;
    }
    public Instant getRecentUpdate() {
        return recent_update;
    }
    public Instant getExpiry() {
        return expiry;
    }

    // Setters / Updaters
    public void setBookingStatus(BookingStatus booking_status) {
        this.booking_status = booking_status;
        this.recent_update = Instant.now();
    }
    public void setExpiry(Instant expiry) {
        this.expiry = expiry;
        this.recent_update = Instant.now();
    }






}
