package myp.proyecto2.model.domain;

public class Location {

    private final double latitude;
    private final double longitude;
    private String address;

    public Location(double latitude, double longitude, String address) {
        if (latitude < -90.0 || latitude > 90.0)
            throw new IllegalArgumentException("Invalid latitude: " + latitude);

        if (longitude < -180.0 || longitude > 180.0)
            throw new IllegalArgumentException("Invalid longitude: " + longitude);

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
        if (other == null)
            throw new NullPointerException("A location needs to be provided in order to calculate the distance.");

        double lat1Rad = Math.toRadians(this.latitude);
        double lat2Rad = Math.toRadians(other.getLatitude());
        double deltaLatRad = Math.toRadians(other.getLatitude() - this.latitude);
        double deltaLonRad = Math.toRadians(other.getLongitude() - this.longitude);

        double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLonRad / 2) * Math.sin(deltaLonRad / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return 6371000.0 * c; // Radio de la Tierra en metros
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Latitude:\n").append(this.latitude)
                .append("\nLongitude:\n").append(this.longitude)
                .append("\nAddress:\n").append(this.address);
        return sb.toString();
    }
}