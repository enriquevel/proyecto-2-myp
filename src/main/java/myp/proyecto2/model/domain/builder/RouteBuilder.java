package myp.proyecto2.model.domain.builder;

import java.util.List;
import java.util.Set;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.RouteSegment;
import myp.proyecto2.model.domain.TransportMode;

/**
 * Interfaz que define los distintos metodos para construir una instancia de {@link Route}.
 */
public interface RouteBuilder {

    /**
     * Establece el identificador unico de un objeto RouteBuilder que invoca al metodo.
     * @param id el identificador unico de un objeto RouteBuilder.
     * @return el objeto RouteBuilder ahora con el identificador.
     */
    RouteBuilder setId(String id);
    
    /**
     * Establece el punto de inicio del objeto RouteBuilder que invoca al metodo.
     * @param origin el punto de inicio del objeto RouteBuilder.
     * @return el objeto RouteBuilder ahora con dicho punto de origen.
     */
    RouteBuilder setOrigin(Location origin);

    /**
     * Establece el punto de destino del objeto RouteBuilder que invoca al metodo. 
     * @param destination el punto de destino del objeto RouteBuilder.
     * @return el objeto RouteBuilder ahora con dicho punto de destino.
     */
    RouteBuilder setDestination(Location destination);

    /**
     * Establece una distancia en el objeto RouteBuilder que invoca al metodo.
     * @param distance la distancia.
     * @return el objeto RouteBuilder ahora con esa distancia.
     */
    RouteBuilder setDistance(double distance);

    /**
     * Establece una tiempo (en segundos) en el objeto RouteBuilder que invoca al metodo.
     * @param seconds  el tiempo.
     * @return el objeto RouteBuilder ahora con dicho tiempo.
     */
    RouteBuilder setDuration(int seconds);

    /**
     * Establece una lista de segmentos de ruta en el objeto RouteBuilder que invoca al metodo.
     * @param segment lista de segmentos de ruta.
     * @return el objeto RouteBuilder ahora con la lista de segmentos de ruta.
     */
    RouteBuilder setSegments(List<RouteSegment> segment);

    /**
     * Establece una lista de localizaciones en el objeto RouteBuilder que invoca al metodo.
     * @param points la lista de localizaciones.
     * @return el objeto RouteBuilder ahora con la lista de localizaciones.
     */
    RouteBuilder setPathPoints(List<Location> points);

    /**
     * Establece un conjunto de medios de transporte en el objeto RouteBuilder que invoca al metodo.
     * @param transportModes conjunto de medios de transporte.
     * @return el objeto RouteBuilder ahora con el conjunto de medios de transporte.
     */
    RouteBuilder setTransportModes(Set<TransportMode> transportModes);

    /**
     * Regresa una instancia de {@link Route} a partir de un objeto {@link RouteBuilder}.
     * @return una instancia de {@link Route} a partir de un objeto {@link RouteBuilder}.
     */
    Route build();
}