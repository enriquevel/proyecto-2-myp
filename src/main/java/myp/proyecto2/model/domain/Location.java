package myp.proyecto2.model.domain;

/** Clase que define a las localizaciones. Nuestras localizaciones se componen
 * de coordenadas geograficas (latitud y longitud) y una direccion.
*/
public class Location {

    /** Latitud. */
    private final double latitude;

    /** Longitud. */
    private final double longitude;
    
    /** Direccion de la localizacion. Por ejemplo:
     *  Avenida Universidad 3000, Colonia Universidad Nacional Autonoma de Mexico. 
    */
    private String address;

    /**
     * Constructor principal de la clase {@link Location}.
     *
     * @param latitude latitud de la nueva localizacion
     * @param longitude longitud de la nueva localizacion.
     * @param address direccion de la nueva localizacion.
     * @throws IllegalArgumentException si la latitud es mayor a 90 o menor a -90, o si la longitud
     *          es mayor a 180 o menor a -180.
     * @throws NullPointerException si la direccion es null.
     */
    public Location(double latitude, double longitude, String address) {
        if (latitude < -90.0 || latitude > 90.0)
            throw new IllegalArgumentException("Invalid latitude: " + latitude);

        if (longitude < -180.0 || longitude > 180.0)
            throw new IllegalArgumentException("Invalid longitude: " + longitude);

        if (address == null)
            throw new NullPointerException("Address cannot be null");

        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    /**
     * Construye una localizacion sin direccion.
     *
     * @param latitude latitud de la nueva localization.
     * @param longitude longitud de la nueva localizacion.
     * @throws IllegalArgumentException si la latitud es mayor a 90 o menor a -90, o si la longitud
     *          es mayor a 180 o menor a -180.
     */
    public Location(double latitude, double longitude) {
        this(latitude, longitude, "");
    }

    /**
     * Regresa la latitud de la localizacion.
     *
     * @return la latitud de la localizacion.
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * Regresa la longitud de la localizacion.
     *
     * @return regresa la longitud de la localizacion.
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * Regresa la direccion de la localizacion.
     *
     * @return la direccion de la localizacion.
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Establece la direccion de la localizacion
     *
     * @param address la nueva direccion de la localizacion
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Calcula la distancia a otra localizacion, utilizando las coordenadas
     * geograficas de ambas localizaciones.
     *
     * @param other localizacion respecto a la que se quiere obtener la distancia.
     * @return la distancia (en metros) a otra localizacion.
     * @throws NullPointerException si la localizacion dada, respecto a la que se quiere calcular
     *          la distancia es <code>null</code>.
     */
    public double distanceTo(Location other) {
        if (other == null)
            throw new NullPointerException("A location needs to be provided in order to calculate the distance.");

        double lat1Rad = Math.toRadians(this.latitude);
        double lat2Rad = Math.toRadians(other.getLatitude());
        double deltaLatRad = Math.toRadians(other.getLatitude() - this.latitude);
        double deltaLonRad = Math.toRadians(other.getLongitude() - this.longitude);

        // Formula de Haversine.
        double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLonRad / 2) * Math.sin(deltaLonRad / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return 6371000.0 * c; // Radio de la Tierra en metros
    }

    /**
     * Regresa una representacion en cadena de una localizacion.
     *
     * @return una representacion en cadena de una localizacion.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Latitude:\n").append(this.latitude)
                .append("\nLongitude:\n").append(this.longitude)
                .append("\nAddress:\n").append(this.address);
        return sb.toString();
    }
}