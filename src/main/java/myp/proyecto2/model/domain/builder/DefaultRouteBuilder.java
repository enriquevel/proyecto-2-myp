package myp.proyecto2.model.domain.builder;

import java.util.List;
import java.util.Set;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.RouteSegment;
import myp.proyecto2.model.domain.TransportMode;

/**
 * Clase que define al constructor predeterminado de rutas.
 */
public class DefaultRouteBuilder implements RouteBuilder {

    /** Identificador unico. */
    private String id;

    /** Lugar de origen. */
    private Location origin;

    /** Lugar de destino. */
    private Location destination;

    /** Distancia que se recorre al usar la ruta. */
    private double totalDistance;

    /** Duracion esperada (en segundos) del recorrido.  */
    private int totalDurationSeconds;

    /** Lista de segmentos de ruta. */
    private List<RouteSegment> segments;

    /** Lista de localizaciones. */
    private List<Location> pathPoints;

    /** Conjunto de medios de transporte. */
    private Set<TransportMode> transportModes;

    /**
     * Constructor por defecto de {@link DefaultRouteBuilder}
     */
    public DefaultRouteBuilder() {}
    
    /**
     * Establece el identificador el DefaultRouteBuilder que invoca al metodo.
     *
     * @param id identificador.
     * @return el objeto DefaultRouteBuilder ahora con el id dado.
     * @throws NullPointerException si el id dado es null.
     */
    @Override
    public RouteBuilder setId(String id) {
        if (id == null) 
            throw new NullPointerException("Route's ID cannot be null.");

        this.id = id;
        return this;
    }

    /**
     * Establece el punto de origen del DefaultRouteBuilder que invoca al metodo.
     *
     * @param origin el punto de origen.
     * @return el objeto DefaultRouteBuilder ahora con el punto de origen dado.
     * @throws NullPointerException si el punto de origen es <code>null</code>.
     */
    @Override
    public RouteBuilder setOrigin(Location origin) {
        if (origin == null)
            throw new NullPointerException("Route's origin cannot be null.");

        this.origin = origin;
        return this;
    }

    /**
     * Establece el punto de destino del DefaultRouteBuilder que invoca al metodo.
     *
     * @param destination el punto de destino.
     * @return el objeto DefaultRouteBuilder ahora con el punto de destino dado.
     * @throws NullPointerException si el punto de destino es <code>null</code>.
     */
    @Override
    public RouteBuilder setDestination(Location destination) {
        if (destination == null)
            throw new NullPointerException("Route's destination cannot be null.");

        this.destination = destination;
        return this;
    }

    /**
     * Establece la distancia del DefaultRouteBuilder que invoca al metodo.
     *
     * @param distance la distancia.
     * @return el objeto DefaultRouteBuilder ahora con la distancia dada.
     * @throws IllegalArgumentException si la distancia dada es menor igual a cero.
     */
    @Override
    public RouteBuilder setDistance(double distance) {
        if (distance < 0)
            throw new IllegalArgumentException("Route's distance has to be positive.");

        this.totalDistance = distance;
        return this;
    }

    /**
     * Establece la duracion del DefaultRouteBuilder que invoca al metodo.
     *
     * @param seconds duracion en segundos.
     * @return el objeto DefaultRouteBuilder ahora con la duracion dada.
     * @throws IllegalArgumentException si la duracion dada es menor igual a cero.
     */
    @Override
    public RouteBuilder setDuration(int seconds) {
        if (seconds < 0)
            throw new IllegalArgumentException("Route's duration has to be positive.");

        this.totalDurationSeconds = seconds;
        return this;
    }

    /**
     * Establece la lista de segmentos de ruta del DefaultRouteBuilder que invoca al metodo.
     *
     * @param segments una lista de segmentos de ruta.
     * @return el objeto DefaultRouteBuilder ahora con la lista de segmentos de ruta dada.
     * @throws NullPointerException si la lista dada es <code>null</code>.
     */
    @Override
    public RouteBuilder setSegments(List<RouteSegment> segments) {
        if (segments == null)
            throw new NullPointerException("Route segment cannot be null.");

        this.segments = segments;
        return this;
    }
    
    /**
     * Establece la lista de localizaciones del DefaultRouteBuilder que invoca al metodo.
     *
     * @param pathPoints una lista de localizaciones.
     * @return el objeto DefaultRouteBuilder ahora con la lista de localizaciones dada.
     * @throws NullPointerException si la lista dada es <code>null</code>.
     */
    @Override
    public RouteBuilder setPathPoints(List<Location> pathPoints) {
        if (pathPoints == null)
            throw new NullPointerException("Path point cannot be null.");

        this.pathPoints = pathPoints;
        return this;
    }

    /**
     * Establece el conjunto de medios de transporte del DefaultRouteBuilder que invoca al metodo.
     *
     * @param modes una conjunto de medios de transporte.
     * @return el objeto DefaultRouteBuilder ahora con el conjunto de medios de transporte.
     * @throws NullPointerException si el conjunto dado es <code>null</code>.
     */
    @Override
    public RouteBuilder setTransportModes(Set<TransportMode> modes) {
        if (modes == null)
            throw new NullPointerException("Transport modes cannot be null.");

        this.transportModes = modes;
        return this;
    }

    /**
     * Construye una instancia de {@link Route} a partir de un objeto {@link DefaultRouteBuilder}.
     *
     * @return una instancia de {@link Route}.
     */
    @Override
    public Route build() {
        validateCompleteBuild();
        return new Route(this.id, this.origin, this.destination, this.totalDistance,
                this.totalDurationSeconds, this.segments, this.pathPoints, this.transportModes);
    }

    /**
     * Metodo auxiliar que verifica si es posible construir una instancia de {@link Route} con 
     * un objeto {@link RouteBuilder}, en caso contrario lanza una excepcion. 
     * Este metodo es util en caso de que no se utilicen todos lo setters de la clase y aun asi se quiera 
     * utilizar el metodo <code>build()</code>.
     * @throws IllegalStateException si ocurre al menos uno de los siguientes:
     *      El identificador es <code>null</code>.
     *      El punto de origen es <code>null</code>.
     *      El punto de destino es <code>null</code>.
     *      La distancia total es menor a 0.
     *      La duracion total es menor a 0.
     *      La lista de segmentos de ruta es <code>null</code>.
     *      La lista de localizaciones es <code>null</code>.
     *      El conjunto de medios de transporte es <code>null</code>.
     */
    private void validateCompleteBuild() throws IllegalStateException {
        if (this.id == null)  
            throw new IllegalStateException("Cannot build route: ID is required");

        if (this.origin == null)
            throw new IllegalStateException("Cannot build route: An origin is required");

        if (this.destination == null)
            throw new IllegalStateException("Cannot build route: A destination is required");

        if (this.totalDistance < 0)
            throw new IllegalStateException("Cannot build route: A route distance is required");

        if (this.totalDurationSeconds < 0.0)
            throw new IllegalStateException("Cannot build route: A route duration is required");

        if (this.segments == null)
            throw new IllegalStateException("Cannot build route: No route segments have been added.");

        if (this.pathPoints == null)
            throw new IllegalStateException("Cannot build route: No path points have been added.");

        if (this.transportModes == null)
            throw new IllegalStateException("Cannot build route: No transport modes have been added.");
    }
}