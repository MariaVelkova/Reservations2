package bg.mentormate.academy.reservations.common;

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
    long userUpdatedAt = 0;
    long venuesUpdatedAt = 0;
    long reservationsUpdatedAt = 0;
    long citiesUpdatedAt = 0;
    int curerntVenueId = 0;

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
        setUserUpdatedAt();
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
        setCitiesUpdatedAt();
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
        setReservationsUpdatedAt();
    }

    public ArrayList<Venue> getVenues() {
        return venues;
    }

    public void setVenues(ArrayList<Venue> venues) {
        this.venues = venues;
        setVenuesUpdatedAt();
    }

    public long getUserUpdatedAt() {
        return userUpdatedAt;
    }

    public void setUserUpdatedAt() {
        this.userUpdatedAt = Validator.getCurrentGMTTime();
    }

    public long getVenuesUpdatedAt() {
        return venuesUpdatedAt;
    }

    public void setVenuesUpdatedAt() {
        this.venuesUpdatedAt = Validator.getCurrentGMTTime();
    }

    public long getReservationsUpdatedAt() {
        return reservationsUpdatedAt;
    }

    public void setReservationsUpdatedAt() {
        this.reservationsUpdatedAt = Validator.getCurrentGMTTime();
    }

    public long getCitiesUpdatedAt() {
        return citiesUpdatedAt;
    }

    public void setCitiesUpdatedAt() {
        this.citiesUpdatedAt = Validator.getCurrentGMTTime();
    }

    public int getCurerntVenueId() {
        return curerntVenueId;
    }

    public void setCurerntVenueId(int curerntVenueId) {
        this.curerntVenueId = curerntVenueId;
    }
}
