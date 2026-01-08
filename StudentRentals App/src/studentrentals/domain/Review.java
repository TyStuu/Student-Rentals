package studentrentals.domain;

import studentrentals.util.Validate;

import java.time.Instant;
import java.util.Objects;

public class Review {

    private final String review_ID;
    private final String booking_ID;
    private final String student_ID;
    private final String property_ID;

    private final int rating; // 1 to 5
    private final String comment; // optional
    private final Instant created_at;

    public Review(String review_ID, String booking_ID, String student_ID, String property_ID, int rating, String comment) {
        Validate.notBlank(review_ID, "Review ID");
        Validate.notBlank(booking_ID, "Booking ID");
        Validate.notBlank(student_ID, "Student ID");
        Validate.notBlank(property_ID, "Property ID");

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        this.review_ID = review_ID;
        this.booking_ID = booking_ID;
        this.student_ID = student_ID;
        this.property_ID = property_ID;
        this.rating = rating;
        this.comment = (comment == null) ? "" : comment.trim();
        this.created_at = Instant.now();
    }

    public String getReviewID() {
        return review_ID;
    }

    public String getBookingID() {
        return booking_ID;
    }

    public String getStudentID() {
        return student_ID;
    }

    public String getPropertyID() {
        return property_ID;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Instant getCreatedAt() {
        return created_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review review)) return false;
        return Objects.equals(review_ID, review.review_ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(review_ID);
    }
}
