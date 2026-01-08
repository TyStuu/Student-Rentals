package studentrentals.repository;

import studentrentals.domain.Booking;
import studentrentals.domain.BookingStatus;
import studentrentals.util.IndexUtil;

import java.util.*;


public class BookingRepo {


    // In-memory storage for bookings   
    private final Map<String, Booking> booking_by_ID = new HashMap<>();
    private final Map<String, List<String>> booking_IDs_by_student_ID = new HashMap<>();
    private final Map<String, List<String>> booking_IDs_by_homeowner_ID = new HashMap<>();

    public void saveBooking (Booking b) {
        Objects.requireNonNull(b);
        String booking_id = b.getBookingID();

        if (booking_by_ID.containsKey(booking_id)){
            throw new IllegalArgumentException("Booking with given ID already exists");
        }

        booking_by_ID.put(booking_id, b);

        IndexUtil.addToIndex(booking_IDs_by_homeowner_ID, b.getHomeownerID(), booking_id); // Index booking by homeowner
        IndexUtil.addToIndex(booking_IDs_by_student_ID, b.getStudentID(), booking_id); // Index booking by student
    }

    public Optional<Booking> findBookingByID (String id) {
        Optional<Booking> booking = Optional.ofNullable(booking_by_ID.get(id));
        return booking;
    }

    public List<Booking> findBookingByHomeownerID (String homeownerID) {
        return listBookingsFromIDs(booking_IDs_by_homeowner_ID.get(homeownerID));
    }

    public List<Booking> findBookingByStudent (String studentID) {
        return listBookingsFromIDs(booking_IDs_by_student_ID.get(studentID));
    }

    public Collection<Booking> getAllBookings() {
        Collection<Booking> all_bookings = Collections.unmodifiableCollection(booking_by_ID.values());
        return all_bookings;
    }

    private List<Booking> listBookingsFromIDs (List<String> bookingIDs) {
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

    public List<Booking> listBookinigsByRoomID (String roomID) {
        if (roomID == null) {
            return Collections.emptyList();
        }

        List<Booking> bookings = new ArrayList<>();
        for (Booking booking : booking_by_ID.values()) {
            if (roomID.equals(booking.getRoomID())) {
                bookings.add(booking);
            }
        }
        return bookings;
    }

    public boolean hasApprovedBookingForRoom(String roomID) {
        List<Booking> bookings = listBookinigsByRoomID(roomID);
        for (Booking b : bookings) {
            if (b.getBookingStatus() == BookingStatus.APPROVED) {
                return true;
            }
        }
        return false;
    }

    public boolean hasApprovedBookingForProperty(String propertyID) {
        for (Booking b : getAllBookings()) {
            if (b.getPropertyID().equals(propertyID) && b.getBookingStatus() == BookingStatus.APPROVED) {
                return true;
            }
        }
        return false;
    }

    public BookingStatus geBookingStatus( String bookingID) {
        Booking booking = booking_by_ID.get(bookingID);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found for given ID");
        }
        return booking.getBookingStatus();
    }

}
