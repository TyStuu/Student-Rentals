package studentrentals.util;

public class IDManage {

    private static int user_count = 0;
    private static int property_count = 0;
    private static int booking_count = 0;
    private static int room_count = 0;
    private static int review_count = 0;


    private IDManage(){}

    public static String generateUserID() {
        user_count++;
        return "U" + String.format("%05d", user_count);
    }
    public static String generatePropertyID() {
        property_count++;
        return "P" + String.format("%05d", property_count);
    }
    public static String generateBookingID() {
        booking_count++;
        return "B" + String.format("%05d", booking_count);
    }
    public static String generateRoomID() {
        room_count++;
        return "R" + String.format("%05d", room_count);
    }
    public static String generateReviewID() {
        review_count++;
        return "V" + String.format("%05d", review_count);
    }
}
