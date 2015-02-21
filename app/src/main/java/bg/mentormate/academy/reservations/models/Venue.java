package bg.mentormate.academy.reservations.models;

/**
 * Created by Student16 on 2/5/2015.
 */
public class Venue {
    private int id;
    private String name;
    private int type;
    private String phone;
    private String address;
    private int city_id;
    private Double lat;
    private Double lon;
    private String worktime;
    private int capacity;
    private int owner_id;
    private int created;
    private int last_updated;

    public Venue(int id, String name, int type, String phone, String address, int city_id, Double lat, Double lon, String worktime, int capacity, int owner_id, int created, int last_updated) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.phone = phone;
        this.address = address;
        this.city_id = city_id;
        this.lat = lat;
        this.lon = lon;
        this.worktime = worktime;
        this.capacity = capacity;
        this.owner_id = owner_id;
        this.created = created;
        this.last_updated = last_updated;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getCityId() {
        return city_id;
    }

    public void setCityId(int city_id) {
        this.city_id = city_id;
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

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getLastUpdated() {
        return last_updated;
    }

    public void setLastUpdated(int last_updated) {
        this.last_updated = last_updated;
    }
}
