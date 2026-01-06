package studentrentals.repository;

import studentrentals.domain.Booking;
import java.util.*;


public class BookingRepo {


    // In-memory storage for bookings   
    private final Map<String, Booking> booking_by_ID = new HashMap<>();
    private final Map<String, List<String>> booking_IDs_by_student_ID = new HashMap<>();
    private final Map<String, List<String>> booking_IDs_by_homeowner_ID = new HashMap<>();

    public void saveBooking (Booking b) {
        Objects.requireNonNull(b);
        String booking_id = b.getBookingID();

        if (booking_by_ID.containsKey(b)){
            throw new IllegalArgumentException("Booking with given ID already exists");
        }

        booking_by_ID.put(booking_id, b);

        String student_id = b.getStudentID(); // Index booking by student
        List<String> student_bookings = booking_IDs_by_student_ID.get(student_id);
        if (student_bookings == null) { // First booking for this student
            student_bookings = new ArrayList<>();
            booking_IDs_by_student_ID.put(student_id, student_bookings);
        }
        student_bookings.add(booking_id);

        String homeowner_id = b.getHomeownerID(); // Index booking by homeowner
        List<String> homeowner_bookings = booking_IDs_by_homeowner_ID.get(homeowner_id);
        if (homeowner_bookings == null) { // First booking for this homeowner
            homeowner_bookings = new ArrayList<>();
            booking_IDs_by_homeowner_ID.put(homeowner_id, homeowner_bookings);
        }
        homeowner_bookings.add(booking_id);
    }

    public Optional<Booking> findBookingByID (String id) {
        Optional<Booking> booking = Optional.ofNullable(booking_by_ID.get(id));
        return booking;
    }

    public List<Booking> findBookingByHomeowner (String homeownerID) {
        List<String> bookingIDs = booking_IDs_by_homeowner_ID.get(homeownerID);
        if (bookingIDs == null) {
            return Collections.emptyList();
        }
        List<Booking> bookings = new ArrayList<>();
        for (String bookingID : bookingIDs) {
            Booking booking = booking_by_ID.get(bookingID);
            if (booking != null) {
                bookings.add(booking);
            }
        }
        return bookings;
    }

    public List<Booking> findBookingByStudent (String studentID) {
        List<String> bookingIDs = booking_IDs_by_student_ID.get(studentID);
        if (bookingIDs == null) {
            return Collections.emptyList();
        }
        List<Booking> bookings = new ArrayList<>();
        for (String bookingID : bookingIDs) {
            Booking booking = booking_by_ID.get(bookingID);
            if (booking != null) {
                bookings.add(booking);
            }
        }
        return bookings;
    }

    


}
