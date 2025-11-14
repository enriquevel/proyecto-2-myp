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
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public String getAddress() {
        return this.address;
    }

    public double distanceTo(Location other) {
        if(other == null) 
            throw new NullPointerException("A location needs to be provided in order to calculate the distance.");
        
        return 0;
    }

    @Override 
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nLatitude:\n").append(this.latitude)
            .append("\nLongitude:\n").append(this.longitude)
            .append("\nAddress:\n").append(this.address);
        return sb.toString();
    }
}