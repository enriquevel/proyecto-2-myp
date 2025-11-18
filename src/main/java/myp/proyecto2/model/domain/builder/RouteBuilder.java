package myp.proyecto2.model.domain.builder;

import java.util.List;
import java.util.Set;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.RouteSegment;
import myp.proyecto2.model.domain.TransportMode;

/**
 * Interfaz que establece el contrato que todos los objetos builder de Route
 * deben seguir.
 * Permite agregar diferentes tipos de parametros de ruta de manera fluida
 * y construir una ruta al final del proceso.
 */
public interface RouteBuilder {

    /**
     * Establece el ID de la ruta a construir.
     * 
     * @param id el ID de la ruta a construir.
     * @return esta instancia del builder para permitir encadenar metodos.
     * @throws NullPointerException si el ID de la ruta es <code>null</code>.
     */
    RouteBuilder setId(String id);
    
    /**
     * Establece el origen de la ruta a construir.
     * 
     * @param origin el origen de la ruta a construir.
     * @return esta instancia del builder para permitir encadenar metodos.
     * @throws NullPointerException si el origen de la ruta es <code>null</code>.
     */
    RouteBuilder setOrigin(Location origin);

    /**
     * Establece el destino de la ruta a construir.
     * 
     * @param destination el destino de la ruta a construir.
     * @return esta instancia del builder para permitir encadenar metodos.
     * @throws NullPointerException si el destino de la ruta es <code>null</code>.
     */
    RouteBuilder setDestination(Location destination);

    /**
     * Establece la distancia de la ruta a construir.
     * 
     * @param distance la distancia de la ruta a construir.
     * @return esta instancia del builder para permitir encadenar metodos.
     * @throws IllegalArgumentException si la distancia de la ruta no es un valor positivo.
     */
    RouteBuilder setDistance(double distance);

    /**
     * Establece la duracion en segundos de la ruta a construir.
     * 
     * @param seconds la duracion en segundos de la ruta a construir.
     * @return esta instancia del builder para permitir encadenar metodos.
     * @throws IllegalArgumentException si la duracion en segundos de la ruta no es un valor positivo.
     */
    RouteBuilder setDuration(int seconds);

    /**
     * Establece los segmentos de la ruta a construir.
     * 
     * @param segments lista con los segmentos de la ruta a construir.
     * @return esta instancia del builder para permitir encadenar metodos.
     * @throws NullPointerException si la lista con los segmentos de la ruta es <code>null</code>.
     */
    RouteBuilder setSegments(List<RouteSegment> segment);

    /**
     * Establece los puntos de ruta de la ruta a construir.
     * 
     * @param pathPoints los puntos de ruta de la ruta a construir.
     * @return esta instancia del builder para permitir encadenar metodos.
     * @throws NullPointerException si la lista con puntos de ruta es <code>null</code>.
     */
    RouteBuilder setPathPoints(List<Location> point);

    /**
     * Establece los modos de transporte de la ruta a construir.
     * 
     * @param modes los modos de transporte de la ruta a construir.
     * @return esta instancia del builder para permitir encadenar metodos.
     * @throws NullPointerException si la lista con los modos de transporte de la ruta es <code>null</code>.
     */
    RouteBuilder setTransportModes(Set<TransportMode> transportModes);

    /**
	 * Construye y devuelve la configuracion final de la ruta con todos los parametros agregados.
	 *
	 * @return un nuevo objeto Route con todas los parametros configurados.
	 */
    Route build();
}