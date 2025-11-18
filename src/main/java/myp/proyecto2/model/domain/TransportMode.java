package myp.proyecto2.model.domain;

/** Enumeracion que define los distintos medios de transporte. */
public enum TransportMode {

    /** Bicicleta. */
    BICYCLING,

    /** Camion. */
    BUS,

    /** Automovil. */
    DRIVING,

    /** Caminando. */
    WALKING;

    /**
     * Regresa la cadena asociada a algun elemento de la enumeracion.
     * @return la cadena asociada a algun elemento de la enumeracion.
     */
    public String getGoogleMapsMode() {
        return switch (this) {
            case BICYCLING -> "bicycling";
            case BUS -> "transit";
            case DRIVING -> "driving";
            case WALKING -> "walking";
        };
    }

     /**
     * Regresa el nombre asociado a algun elemento de la enumeracion y que sera el texo
     * visible por el usuario.
     * @return el nombre asociado a algun elemento de la enumeracion.
     */
    public String getDisplayName() {
        return switch (this) {
            case BICYCLING -> "Bicycling";
            case BUS -> "Bus";
            case DRIVING -> "Driving";
            case WALKING -> "Walking";
        };
    }

    /**
     * Regresa el elemento de la enumeracion asociado a una cadena.
     * @param mode cadena que se quiere saber cual es su elemento asociado.
     * @return el elemento de la enumeracion asociado a la cadena.
     */
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