package bg.mentormate.academy.reservations.models;

import java.util.Calendar;
import java.util.TimeZone;

import bg.mentormate.academy.reservations.common.Validator;

/**
 * Created by Student16 on 2/5/2015.
 */
public class Reservation {
    private int id;
    private int user_id;
    private String user_first_name;
    private String user_last_name;
    private String user_phone;
    private int venue_id;
    private String venue_name;
    private String venue_phone;
    private String venue_city;
    private String venue_address;
    private String venue_image;
    private int date_created;
    private int date_booked;
    private int people_count;
    private String comment;
    private int accepted;

    public Reservation(int id, int user_id, String user_first_name, String user_last_name, String user_phone, int venue_id, String venue_name, String venue_phone, String venue_city, String venue_address, String venue_image, int date_created, int date_booked, int people_count, String comment, int accepted) {
        this.id = id;
        this.user_id = user_id;
        this.user_first_name = user_first_name;
        this.user_last_name = user_last_name;
        this.user_phone = user_phone;
        this.venue_id = venue_id;
        this.venue_name = venue_name;
        this.venue_phone = venue_phone;
        this.venue_city = venue_city;
        this.venue_address = venue_address;
        this.venue_image = venue_image;
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

    public String getUser_first_name() {
        return user_first_name;
    }

    public void setUser_first_name(String user_first_name) {
        this.user_first_name = user_first_name;
    }

    public String getUser_last_name() {
        return user_last_name;
    }

    public void setUser_last_name(String user_last_name) {
        this.user_last_name = user_last_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getVenue_image() {
        return venue_image;
    }

    public void setVenue_image(String venue_image) {
        this.venue_image = venue_image;
    }

    public String getDateBookedString() {
        return Validator.GetDatetimeAsString(getAccepted());
    }

    public String getStatusString() {
        switch(getAccepted()) {
            case -1: return "Rejected";
            case 1: return "Accepted";
            case 0:
            default: return "Pending";
        }
    }
}
