package bg.mentormate.academy.reservations.models;

/**
 * Created by Student16 on 2/5/2015.
 */
public class Reservation {
    private int id;
    private int user_id;
    private int venue_id;
    private String venue_name;
    private String venue_phone;
    private String venue_city;
    private String venue_address;
    private int date_created;
    private int date_booked;
    private int people_count;
    private String comment;
    private int accepted;

    public Reservation(int id, int user_id, int venue_id, String venue_name, String venue_phone, String venue_city, String venue_address, int date_created, int date_booked, int people_count, String comment, int accepted) {
        this.id = id;
        this.user_id = user_id;
        this.venue_id = venue_id;
        this.venue_name = venue_name;
        this.venue_phone = venue_phone;
        this.venue_city = venue_city;
        this.venue_address = venue_address;
        this.date_created = date_created;
        this.date_booked = date_booked;
        this.people_count = people_count;
        this.comment = comment;
        this.accepted = accepted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public int getVenueId() {
        return venue_id;
    }

    public void setVenueId(int venue_id) {
        this.venue_id = venue_id;
    }

    public int getDateCreated() {
        return date_created;
    }

    public void setDateCreated(int date_created) {
        this.date_created = date_created;
    }

    public int getDateBooked() {
        return date_booked;
    }

    public void setDateBooked(int date_booked) {
        this.date_booked = date_booked;
    }

    public int getPeopleCount() {
        return people_count;
    }

    public void setPeopleCount(int people_count) {
        this.people_count = people_count;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getAccepted() {
        return accepted;
    }

    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }

    public String getVenue_name() {
        return venue_name;
    }

    public void setVenue_name(String venue_name) {
        this.venue_name = venue_name;
    }

    public String getVenue_phone() {
        return venue_phone;
    }

    public void setVenue_phone(String venue_phone) {
        this.venue_phone = venue_phone;
    }

    public String getVenue_city() {
        return venue_city;
    }

    public void setVenue_city(String venue_city) {
        this.venue_city = venue_city;
    }

    public String getVenue_address() {
        return venue_address;
    }

    public void setVenue_address(String venue_address) {
        this.venue_address = venue_address;
    }
}
