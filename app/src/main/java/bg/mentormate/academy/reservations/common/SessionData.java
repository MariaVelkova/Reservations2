package bg.mentormate.academy.reservations.common;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import bg.mentormate.academy.reservations.models.City;
import bg.mentormate.academy.reservations.models.Reservation;
import bg.mentormate.academy.reservations.models.User;
import bg.mentormate.academy.reservations.models.Venue;

/**
 * Created by Maria on 2/17/2015.
 */
public class SessionData {
    private static SessionData instance;

    private User user = null;
    private ArrayList<City> cities;
    private ArrayList<Reservation> reservations;
    private ArrayList<Venue> venues;

    private SessionData() {
    }

    public static SessionData getInstance() {
        if (instance == null) {
            instance = new SessionData();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }

    public ArrayList<Venue> getVenues() {
        return venues;
    }

    public void setVenues(ArrayList<Venue> venues) {
        this.venues = venues;
    }
}
