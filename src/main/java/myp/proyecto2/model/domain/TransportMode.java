package myp.proyecto2.model.domain;

public enum TransportMode {

    BICYCLING,
    BUS,
    DRIVING,
    WALKING;

    public String getGoogleMapsMode() {
        return switch (this) {
            case BICYCLING -> "bicycling";
            case BUS -> "transit";
            case DRIVING -> "driving";
            case WALKING -> "walking";
        };
    }

    public String getDisplayName() {
        return switch (this) {
            case BICYCLING -> "Bicycling";
            case BUS -> "Bus";
            case DRIVING -> "Driving";
            case WALKING -> "Walking";
        };
    }

    public static TransportMode fromString(String mode) {
        return switch (mode.toLowerCase()) {
            case "bicycling" -> BICYCLING;
            case "transit" -> BUS;
            case "driving" -> DRIVING;
            case "walking" -> WALKING;
            default -> throw new IllegalArgumentException("Unknown mode: " + mode);
        };
    }
}