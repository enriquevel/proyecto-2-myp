package myp.proyecto2.model.domain.builder;

import java.util.List;
import java.util.Set;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.RouteSegment;
import myp.proyecto2.model.domain.TransportMode;

/**
 * Clase que define a las rutas.
 */
public class Route {

    /** Identificador unico de la ruta. */
    private final String id;

    /** Lugar donde empieza la ruta. */
    private final Location origin;

    /** Lugar donde termina la ruta. */
    private final Location destination;

    /** Distancia total que se recorre al usar la ruta. */
    private final double totalDistance;

    /** Tiempo estimado (en segundos) que toma recorrer la ruta. */
    private final int totalDurationSeconds;

    /** Lista de segmentos de ruta de los que se compone la ruta. */
    private final List<RouteSegment> segments;

    /** Lista de localizaciones de las que se compone la ruta. */
    private final List<Location> pathPoints;

    /** Conjunto de medios de transporte que se emplean durante el recorrido de la ruta.*/
    private final Set<TransportMode> transportModes;

    /**
     * Constructor principal de la clase{@link Route}. Permite construir una ruta.
     * @param id identificador de la ruta.
     * @param origin lugar donde comienza la ruta.
     * @param destination lugar donde termina la ruta.
     * @param totalDistance distancia total que se recorre al usar la ruta.
     * @param totalDurationSeconds tiempo (en segundos) que toma recorrer la ruta.
     * @param segments lista de segmentos de los que se compone la ruta.
     * @param pathPoints lista de localizaciones de las que se compone la ruta.
     * @param transportModes conjunto de medios de transporte que se emplean durante el recorrido de la ruta.
     * @throws NullPointerException si el ID, el punto de origen, el punto destino, la lista de segmentos, 
     * la lista de puntos o la lista de medios de trasnporte dadas es <code>null</code>.
     * @throws IllegalArgumentException si la distancia total o la duracion total es <code>null</code>.
     */
    Route(String id, Location origin, Location destination, double totalDistance, int totalDurationSeconds, List<RouteSegment> segments,
            List<Location> pathPoints, Set<TransportMode> transportModes) throws NullPointerException, IllegalArgumentException{
        if (id == null) 
            throw new NullPointerException("Route's ID cannot be null.");

        if (origin == null) 
            throw new NullPointerException("Route's origin cannot be null.");
        
        if (destination == null)
            throw new NullPointerException("Route's destination cannot be null.");

        if (totalDistance <= 0)
            throw new IllegalArgumentException("Route's total distance needs to be a positive value.");

        if (totalDurationSeconds <= 0)
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
     * Regresa el identificador de la ruta.
     * @return el identificador de la ruta.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Regresa el punto donde inicia la ruta.
     * @return el punto donde inicia la ruta.
     */
    public Location getOrigin() {
        return this.origin;
    }

    /**
     * Regresa el punto donde termina la ruta.
     * @return el punto donde termina la ruta.
     */
    public Location getDestination() {
        return this.destination;
    }

    /**
     * Regresa la distancia que se recorre (en metros) al usar la ruta.
     * @return la distancia que se recorre (en metros) al usar la ruta.
     */
    public double getTotalDistance() {
        return this.totalDistance;
    }

    /**
     * Regresa el tiempo estimado (en segundos) que toma recorrer la ruta.
     * @return el tiempo estimado (en segundos) que toma recorrer la ruta.
     */
    public int getTotalDurationSeconds() {
        return this.totalDurationSeconds;
    }

    /**
     * Regresa la lista de segmentos de ruta que componen la ruta.
     * @return la lista de segmentos de ruta que componen la ruta.
     */
    public List<RouteSegment> getSegments() {
        return this.segments;
    }

    /**
     * Regresa la lista de localizaciones de las que se compone la ruta.
     * @return la lista de localizaciones de las que se compone la ruta.
     */
    public List<Location> getPathPoints() {
        return this.pathPoints;
    }

    /**
     * Regresa el conjunto de medios de trasnporte que se utilizan al recorrer la ruta.
     * @return el conjunto de medios de trasnporte que se utilizan al recorrer la ruta.
     */
    public Set<TransportMode> getTransportModes() {
        return this.transportModes;
    }

    /**
     * Regresa una representacion en cadena de la ruta.
     * @return una representacion en cadena de la ruta.
     */
    public String displayRoute() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\nDisplaying route's details:\n")
            .append("\n\nOrigin:\n").append(this.origin)
            .append("\n\nDestination:\n").append(this.destination)
            .append("\n\nTotal distance:\n").append(this.totalDistance).append(" meters.")
            .append("\n\nTotal duration:\n").append(this.totalDurationSeconds).append(" seconds");
        return sb.toString();
    }

    /**
     * Regresa el numero de segmentos de ruta que componen la ruta.
     * @return el numero de segmentos de ruta que componen la ruta.
     */
    public int getSegmentCount() {
        return this.segments.size();
    }

    /**
     * Regresa el tiempo estimado (en minutos) que toma recorrer la ruta.
     * @return el tiempo estimado (en minutos) que toma recorrer la ruta.
     */
    public int getTotalDurationMinutes() {
        return this.totalDurationSeconds / 60;
    }

    /**
     * Regresa la distancia que se recorre (en kilometros) al usar la ruta.
     * @return la distancia que se recorre (en kilometros) al usar la ruta.
     */
    public double getTotalDistanceKilometers() {
        return this.totalDistance / 1000.0;
    }
}