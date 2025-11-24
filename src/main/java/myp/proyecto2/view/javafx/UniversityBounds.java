package myp.proyecto2.view.javafx;

import myp.proyecto2.model.domain.Location;

/**
 * Representa los limites geograficos rectangulares de un area universitaria.
 * Define una region mediante dos esquinas opuestas: noroeste y sureste.
 */
public class UniversityBounds {

    /** Esquina noroeste del area universitaria (maxima latitud, minima longitud). */
    private final Location northwest;

    /** Esquina sureste del area universitaria (minima latitud, maxima longitud. */
    private final Location southeast;

    /**
     * Construye un nuevo rectangulo de limites geograficos con las esquinas especificadas.
     * Las esquinas noroeste y sureste definen el area rectangular completa.
     *
     * @param northwest ubicacion de la esquina noroeste (maxima latitud, minima longitud)
     * @param southeast ubicacion de la esquina sureste (minima latitud, maxima longitud)
     */
    public UniversityBounds(Location northwest, Location southeast) {
        this.northwest = northwest;
        this.southeast = southeast;
    }

    /**
     * Obtiene la ubicacion de la esquina noroeste del area.
     *
     * @return la esquina noroeste
     */
    public Location getNorthwest() {
        return this.northwest;
    }

    /**
     * Obtiene la ubicacion de la esquina sureste del area.
     *
     * @return la esquina sureste
     */
    public Location getSoutheast() {
        return this.southeast;
    }

    /**
     * Obtiene la latitud minima del area (borde sur).
     * Corresponde a la latitud de la esquina sureste.
     *
     * @return la latitud minima en grados decimales
     */
    public double getMinLatitude() {
        return this.southeast.getLatitude();
    }

    /**
     * Obtiene la latitud maxima del area (borde norte).
     * Corresponde a la latitud de la esquina noroeste.
     *
     * @return la latitud maxima en grados decimales
     */
    public double getMaxLatitude() {
        return this.northwest.getLatitude();
    }

    /**
     * Obtiene la longitud minima del area (borde oeste).
     * Corresponde a la longitud de la esquina noroeste.
     *
     * @return la longitud minima en grados decimales
     */
    public double getMinLongitude() {
        return this.northwest.getLongitude();
    }

    /**
     * Obtiene la longitud maxima del area (borde este).
     * Corresponde a la longitud de la esquina sureste.
     *
     * @return la longitud maxima en grados decimales
     */
    public double getMaxLongitude() {
        return this.southeast.getLongitude();
    }

    /**
     * Verifica si una ubicacion geografica esta dentro de los limites del area.
     * Una ubicacion esta contenida si su latitud esta entre la minima y maxima,
     * y su longitud esta entre la minima y maxima.
     *
     * @param location la ubicacion a verificar
     * @return true si la ubicacion esta dentro de los limites, false en caso contrario
     */
    public boolean contains(Location location) {
        return location.getLatitude() >= getMinLatitude() &&
                location.getLatitude() <= getMaxLatitude() &&
                location.getLongitude() >= getMinLongitude() &&
                location.getLongitude() <= getMaxLongitude();
    }

    /**
     * Crea y retorna los limites geograficos predefinidos de Ciudad Universitaria.
     *
     * @return instancia de UniversityBounds configurada para Ciudad Universitaria
     */
    public static UniversityBounds ciudadUniversitaria() {
        Location nw = new Location(19.3410, -99.2015);
        Location se = new Location(19.3060, -99.1695);
        return new UniversityBounds(nw, se);
    }
}
