package bg.mentormate.academy.reservations.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Student16 on 2/5/2015.
 */
public class Venue {
    private int id;
    private String name;
    private String type;
    private String phone;
    private String address;
    private String city;
    private Double lat;
    private Double lon;
    private String worktime;
    private int capacity;
    private int owner_id;
    private String image;

    public Venue(int id, String name, String type, String phone, String address, String city, Double lat, Double lon, String worktime, int capacity, int owner_id, String image) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.lat = lat;
        this.lon = lon;
        this.worktime = worktime;
        this.capacity = capacity;
        this.owner_id = owner_id;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getWorktime() {
        return worktime;
    }

    public void setWorktime(String worktime) {
        this.worktime = worktime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    @Override
    public String toString()
    {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", Integer.toString(getId()));
            obj.put("name", getName());
            obj.put("type", getType());
            obj.put("phone", getPhone());
            obj.put("address", getAddress());
            obj.put("city", getCity());
            obj.put("lat", Double.toString(getLat()));
            obj.put("lon", Double.toString(getLon()));
            obj.put("worktime", getWorktime());
            obj.put("capacity", Integer.toString(getCapacity()));
            obj.put("owner_id", Integer.toString(getOwner_id()));
            obj.put("image", getImage());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj.toString();
    }
}
