package myp.proyecto2.model.domain;

public enum TransportMode {

    BICYCLING,
    BUS, 
    DRIVING,
    WALKING;

    public String getGoogleMapsMode() {
        return null;
    }

    public String getDisplayName() {
        return switch(this) {
            case BICYCLING -> "Bicycling";
            case BUS -> "Bus";
            case DRIVING -> "Driving";
            case WALKING -> "Walking";
            default -> throw new IllegalArgumentException("The transport mode provided is not valid.");
        };
    }
}