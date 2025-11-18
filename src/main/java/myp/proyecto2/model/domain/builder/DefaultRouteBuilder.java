package myp.proyecto2.model.domain.builder;

import java.util.List;
import java.util.Set;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.RouteSegment;
import myp.proyecto2.model.domain.TransportMode;

/**
 * Implementacion concreta del patron Builder para consturir rutas para
 * usuarios.
 * Esta clase proporciona una implementacion por defecto de la interfaz
 * RouteBuilder,
 * permitiendo la construccion fluida de objetos de tipo Route mediante el
 * encadenamiento de metodos.
 */
public class DefaultRouteBuilder implements RouteBuilder {

    /** Una identificacion para la ruta. */
    private String id;

    /** El lugar origen de la ruta. */
    private Location origin;

    /** El lugar destino de la ruta. */
    private Location destination;

    /** La distancia total de la ruta. */
    private double totalDistance;

    /** La duracion total en segundos de la ruta. */
    private int totalDurationSeconds;

    /** Lista con los segmentos de la ruta. */
    private List<RouteSegment> segments;

    /** Lista con los puntos de ruta de la ruta. */
    private List<Location> pathPoints;

    /** Lista con los modos de transporte disponibles de la ruta. */
    private Set<TransportMode> transportModes;

    /**
     * Constructor que inicializa un nuevo builder de rutas.
     */
    public DefaultRouteBuilder() {
    }

    /**
     * Establece el ID de la ruta a construir.
     * 
     * @param id el ID de la ruta a construir.
     * @return esta instancia del builder para permitir encadenar metodos.
     * @throws NullPointerException si el ID de la ruta es <code>null</code>.
     */
    @Override
    public RouteBuilder setId(String id) {
        if (id == null)
            throw new NullPointerException("Route's ID cannot be null.");

        this.id = id;
        return this;
    }

    /**
     * Establece el origen de la ruta a construir.
     * 
     * @param origin el origen de la ruta a construir.
     * @return esta instancia del builder para permitir encadenar metodos.
     * @throws NullPointerException si el origen de la ruta es <code>null</code>.
     */
    @Override
    public RouteBuilder setOrigin(Location origin) {
        if (origin == null)
            throw new NullPointerException("Route's origin cannot be null.");

        this.origin = origin;
        return this;
    }

    /**
     * Establece el destino de la ruta a construir.
     * 
     * @param destination el destino de la ruta a construir.
     * @return esta instancia del builder para permitir encadenar metodos.
     * @throws NullPointerException si el destino de la ruta es <code>null</code>.
     */
    @Override
    public RouteBuilder setDestination(Location destination) {
        if (destination == null)
            throw new NullPointerException("Route's destination cannot be null.");

        this.destination = destination;
        return this;
    }

    /**
     * Establece la distancia de la ruta a construir.
     * 
     * @param distance la distancia de la ruta a construir.
     * @return esta instancia del builder para permitir encadenar metodos.
     * @throws IllegalArgumentException si la distancia de la ruta no es un valor positivo.
     */
    @Override
    public RouteBuilder setDistance(double distance) {
        if (distance < 0)
            throw new IllegalArgumentException("Route's distance has to be positive.");

        this.totalDistance = distance;
        return this;
    }

    /**
     * Establece la duracion en segundos de la ruta a construir.
     * 
     * @param seconds la duracion en segundos de la ruta a construir.
     * @return esta instancia del builder para permitir encadenar metodos.
     * @throws IllegalArgumentException si la duracion en segundos de la ruta no es un valor positivo.
     */
    @Override
    public RouteBuilder setDuration(int seconds) {
        if (seconds < 0)
            throw new IllegalArgumentException("Route's duration has to be positive.");

        this.totalDurationSeconds = seconds;
        return this;
    }

    /**
     * Establece los segmentos de la ruta a construir.
     * 
     * @param segments lista con los segmentos de la ruta a construir.
     * @return esta instancia del builder para permitir encadenar metodos.
     * @throws NullPointerException si la lista con los segmentos de la ruta es <code>null</code>.
     */
    @Override
    public RouteBuilder setSegments(List<RouteSegment> segments) {
        if (segments == null)
            throw new NullPointerException("Route segment cannot be null.");

        this.segments = segments;
        return this;
    }

    /**
     * Establece los puntos de ruta de la ruta a construir.
     * 
     * @param pathPoints los puntos de ruta de la ruta a construir.
     * @return esta instancia del builder para permitir encadenar metodos.
     * @throws NullPointerException si la lista con puntos de ruta es <code>null</code>.
     */
    @Override
    public RouteBuilder setPathPoints(List<Location> pathPoints) {
        if (pathPoints == null)
            throw new NullPointerException("Path point cannot be null.");

        this.pathPoints = pathPoints;
        return this;
    }

    /**
     * Establece los modos de transporte de la ruta a construir.
     * 
     * @param modes los modos de transporte de la ruta a construir.
     * @return esta instancia del builder para permitir encadenar metodos.
     * @throws NullPointerException si la lista con los modos de transporte de la ruta es <code>null</code>.
     */
    @Override
    public RouteBuilder setTransportModes(Set<TransportMode> modes) {
        if (modes == null)
            throw new NullPointerException("Transport modes cannot be null.");

        this.transportModes = modes;
        return this;
    }

    /**
	 * Construye y devuelve la configuracion final de la ruta con todos los parametros agregados.
	 *
	 * @return un nuevo objeto Route con todas los parametros configurados.
	 */
    @Override
    public Route build() {
        validateCompleteBuild();
        return new Route(this.id, this.origin, this.destination, this.totalDistance,
                this.totalDurationSeconds, this.segments, this.pathPoints, this.transportModes);
    }

    /**
	 * Metodo auxiliar que se encarga de verificar que la ruta no tenga ningun parametro faltante.
	 * 
	 * @throws IllegalStateException si alguno de los parametros no existe en la configuracion de la ruta.
	 */
    private void validateCompleteBuild() {
        if (this.id == null)
            throw new IllegalStateException("Cannot build route: ID is required");

        if (this.origin == null)
            throw new IllegalStateException("Cannot build route: An origin is required");

        if (this.destination == null)
            throw new IllegalStateException("Cannot build route: A destination is required");

        if (this.totalDistance == 0)
            throw new IllegalStateException("Cannot build route: A route distance is required");

        if (this.totalDurationSeconds == 0.0)
            throw new IllegalStateException("Cannot build route: A route duration is required");

        if (this.segments == null)
            throw new IllegalStateException("Cannot build route: No route segments have been added.");

        if (this.pathPoints == null)
            throw new IllegalStateException("Cannot build route: No path points have been added.");

        if (this.transportModes == null)
            throw new IllegalStateException("Cannot build route: No transport modes have been added.");
    }
}