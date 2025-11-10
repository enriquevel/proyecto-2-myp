package myp.proyecto2.model.domain;

public class Location {
    
    private double latitude;
    private double longitude;
    private String address;

    public Location(double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public double getLatitude() {
        return this.latitude
    }

    public double getLongitude() {
        return this.longitude;
    }

    public String getAddress() {
        return this.address;
    }

    public double distanceTo(Location other) {
        return 0;
    }

    @Override 
    public String toString() {
        return null;
    }
}