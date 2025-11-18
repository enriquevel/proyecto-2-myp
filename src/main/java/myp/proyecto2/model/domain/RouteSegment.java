package myp.proyecto2.model.domain;

/**
 * Esta clase representa a los segmentos de ruta. Cada instancia de {@link Route}
 * tiene una lista de segmentos de ruta. Los segmentos de ruta nos ayudan a guardar las
 * distintas indicaciones de cada segmento de la ruta y a calcular la duracion 
 * del viaje en dicho segmento. Un segmento de ruta es una linea que une dos localizaciones (puntos).
 */
public class RouteSegment {

    /** Instruccion que debe seguir el usuario para llegar al final del segmento. */
    private String instruction;

    /** Distancia en metros del punto inicial al punto final del segmento. */
    private double distanceInMeters;

    /** Duracion esperada (en segundos) de ir desde el punto inicial al punto final del segmento.  */
    private double durationInSeconds;

    /** Lugar de inicio del segmento. Instancia de {@link Location}. */
    private Location startPoint;

    /** Lugar donde termina el segmento. Instancia de {@link Location}. */
    private Location endPoint;

    /** Medio de trasnporte asociado al segmento. Un elemento de {@link TransportMode}. */
    private TransportMode mode;

    /**
     * Constructor principal de la clase {@link RouteSegment}. Construye un segmento
     * de ruta con los parametros dados.
     * @param instruction instruccion que debe seguirse en el segmento.
     * @param distanceInMeters distancia en metros dese el punto inicial hast el punto final del segmento.
     * @param durationInSeconds duracion esperada (en segundos) de ir desde el punto inicial al punto final del segmento.
     * @param startPoint lugar de inicio del segmento. Instancia de {@link Location}.
     * @param endPoint lugar donde termina el segmento. Instancia de {@link Location}. 
     * @param mode medio de trasnporte que se asociara al segmento.
     * @throws NullPointerException si la instruccion, el punto de incio, el punto final 
     * o el medio de trasnporte dados son <code>null</code>.
     * @throws IllegalArgumentException si la distancia o el tiempo que toma recorrer el segmento 
     * dados son menores que 0.
     */
    public RouteSegment(String instruction, double distanceInMeters, double durationInSeconds,
                        Location startPoint, Location endPoint, TransportMode mode) throws NullPointerException, IllegalArgumentException {
        if (instruction == null)
            throw new NullPointerException("Instruction cannot be null");

        if (distanceInMeters < 0)
            throw new IllegalArgumentException("Distance cannot be negative");

        if (durationInSeconds < 0)
            throw new IllegalArgumentException("Duration cannot be negative");

        if (startPoint == null)
            throw new NullPointerException("Start point cannot be null");

        if (endPoint == null)
            throw new NullPointerException("End point cannot be null");
            
        if (mode == null)
            throw new NullPointerException("Transport mode cannot be null");

        this.instruction = instruction;
        this.distanceInMeters = distanceInMeters;
        this.durationInSeconds = durationInSeconds;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.mode = mode;
    }

    /**
     * Regresa la instruccion del segmento.
     * @return la instruccion del segmento.
     */
    public String getInstruction() {
        return this.instruction;
    }

    /**
     * Regresa la distancia (en metros) del punto inicial al punto final del segmento.
     * @return la distancia (en metros) del punto inicial al punto final del segmento.
     */
    public double getDistanceInMeters() {
        return this.distanceInMeters;
    }

    /**
     * Regresa el tiempo (en segundos) que toma recorrer el segmento.
     * @return el tiempo (en segundos) que toma recorrer el segmento.
     */
    public double getDurationInSeconds() {
        return this.durationInSeconds;
    }

    /**
     * Regresa el punto de inicio del segmento.
     * @return el punto de inicio del segmento.
     */
    public Location getStartPoint() {
        return this.startPoint;
    }

    /**
     * Regresa el punto donde termina el segmento.
     * @return el punto donde termina el segmento.
     */
    public Location getEndPoint() {
        return this.endPoint;
    }

    /**
     * Regresa el medio de trasnporte asociado al segmento.
     * @return el medio de trasnporte asociado al segmento.
     */
    public TransportMode getMode() {
        return this.mode;
    }

    /**
     * Regresa una representacion en cadena del segmento.Por defecto esta representacion 
     * siempre es <code>null</code>.
     * @return una representacion en cadena del segmento.
     */
    @Override
    public String toString() {
        return null;
    }
}