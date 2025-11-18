package myp.proyecto2.model.domain.builder;

import java.util.List;
import java.util.Set;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.RouteSegment;
import myp.proyecto2.model.domain.TransportMode;

/**
 * Clase que representa una configuracion de ruta.
 * Esta clase es el producto final del patron builder implementado
 * por DefaultRouteBuilder.
 */
public class Route {

    /** Una identificacion para la ruta. */
    private final String id;

    /** El lugar origen de la ruta. */
    private final Location origin;

    /** El lugar destino de la ruta. */
    private final Location destination;

    /** La distancia total de la ruta. */
    private final double totalDistance;

    /** La duracion total en segundos de la ruta. */
    private final int totalDurationSeconds;

    /** Lista con los segmentos de la ruta. */
    private final List<RouteSegment> segments;

    /** Lista con los puntos de ruta de la ruta. */
    private final List<Location> pathPoints;

    /** Lista con los modos de transporte disponibles de la ruta. */
    private final Set<TransportMode> transportModes;

    /**
     * Constructor de una ruta con los parametros especificados.
     * 
     * @param id una identificacion para la ruta.
     * @param origin el lugar origen de la ruta.
     * @param destination el lugar destino de la ruta.
     * @param totalDistance la distancia total de la ruta.
     * @param totalDurationSeconds la duracion total en segundos de la ruta.
     * @param segments lista con los segmentos de la ruta.
     * @param pathPoints lista con los puntos de ruta de la ruta.
     * @param transportModes lista con los modos de transporte disponibles de la ruta.
     * @throws NullPointerException si alguno de los parametros no numericos es <code>null</code>.
     * @throws IllegalArgumentException si alguno de los parametros numericos no es positivo.
     */
    Route(String id, Location origin, Location destination, double totalDistance, int totalDurationSeconds, List<RouteSegment> segments,
            List<Location> pathPoints, Set<TransportMode> transportModes) {
        if (id == null) 
            throw new NullPointerException("Route's ID cannot be null.");

        if (origin == null) 
            throw new NullPointerException("Route's origin cannot be null.");
        
        if (destination == null)
            throw new NullPointerException("Route's destination cannot be null.");

        if (totalDistance < 0)
            throw new IllegalArgumentException("Route's total distance needs to be a positive value.");

        if (totalDurationSeconds < 0)
            throw new IllegalArgumentException("Route's total duration needs to be a positive value.");

        if (segments == null) 
            throw new NullPointerException("Route's segments cannot be null.");

        if (pathPoints == null)
            throw new NullPointerException("Route's path points cannot be null.");

        if (transportModes == null)
            throw new NullPointerException("Route's transport mode cannot be null.");
    
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.totalDistance = totalDistance;
        this.totalDurationSeconds = totalDurationSeconds;
        this.segments = segments;
        this.pathPoints = pathPoints;
        this.transportModes = transportModes;
    }

    /**
     * Devuelve el ID de la ruta.
     * 
     * @return el ID de la ruta.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Devuelve el lugar origen de la ruta.
     * 
     * @return el lugar origen de la ruta.
     */
    public Location getOrigin() {
        return this.origin;
    }

    /**
     * Devuelve el lugar destino de la ruta.
     * 
     * @return el lugar destino de la ruta.
     */
    public Location getDestination() {
        return this.destination;
    }

    /**
     * Devuelve la distancia total de la ruta.
     * 
     * @return la distancia total de la ruta.
     */
    public double getTotalDistance() {
        return this.totalDistance;
    }

    /**
     * Devuelve la duracion total en segundos de la ruta.
     * 
     * @return la duracion total en segundos de la ruta.
     */
    public int getTotalDurationSeconds() {
        return this.totalDurationSeconds;
    }

    /**
     * Devuelve una lista con los segmentos de la ruta.
     * 
     * @return una lista con los segmentos de la ruta.
     */
    public List<RouteSegment> getSegments() {
        return this.segments;
    }

    /**
     * Devuelve una lista con los puntos de ruta de la ruta.
     * 
     * @return una lista con los puntos de ruta de la ruta.
     */
    public List<Location> getPathPoints() {
        return this.pathPoints;
    }

    /**
     * Devuelve una lista con los modos de transporte disponibles de la ruta.
     * 
     * @return una lista con los modos de transporte disponibles de la ruta.
     */
    public Set<TransportMode> getTransportModes() {
        return this.transportModes;
    }

    /**
     * Muestra los detalles e informacion de la ruta. 
     */
    public void displayRoute() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\nDisplaying route's details:\n")
            .append("\n\nOrigin:\n").append(this.origin)
            .append("\n\nDestination:\n").append(this.destination)
            .append("\n\nTotal distance:\n").append(this.totalDistance).append(" meters.")
            .append("\n\nTotal duration:\n").append(this.totalDurationSeconds).append(" seconds");
    }

    /**
     * Devuelve el contador de segmentos de la ruta.
     * 
     * @return el contador de segmentos de la ruta.
     */
    public int getSegmentCount() {
        return this.segments.size();
    }

    /**
     * Devuelve la duracion total de la ruta en minutos.
     * 
     * @return la duracion total de la ruta en minutos.
     */
    public int getTotalDurationMinutes() {
        return this.totalDurationSeconds / 60;
    }

    /**
     * Devuelve la distancia total de la ruta en kilometros.
     * 
     * @return la distancia total de la ruta en kilometros.
     */
    public double getTotalDistanceKilometers() {
        return this.totalDistance / 1000.0;
    }
}