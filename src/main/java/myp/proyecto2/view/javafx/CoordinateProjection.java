package myp.proyecto2.view.javafx;

import javafx.geometry.Point2D;
import myp.proyecto2.model.domain.Location;

/**
 * Clase que define una proyeccion de coordenadas para traducir puntos en el mapa a coordenadas geograficas.
 */
public class CoordinateProjection {

    /** La latitud minima. */
    private final double minLat;

    /** La latitud maxima. */
    private final double maxLat;

    /** La longitud minima. */
    private final double minLng;

    /** La longitud maxima. */
    private final double maxLng;

    /** La escala de pixeles horizontal. */
    private final double xScale;

    /** La escala de pixeles vertical. */
    private final double yScale;

    /**
     * Constructor principal de la clase que inicializa todas las escalas y coordenadas.
     *
     * @param bounds los limites del mapa
     * @param canvasWidth el ancho del mapa
     * @param canvasHeight la altura del mapa
     */
    public CoordinateProjection(UniversityBounds bounds, double canvasWidth, double canvasHeight) {
        this.minLat = bounds.getMinLatitude();
        this.maxLat = bounds.getMaxLatitude();
        this.minLng = bounds.getMinLongitude();
        this.maxLng = bounds.getMaxLongitude();

        this.xScale = canvasWidth / (this.maxLng - this.minLng);
        this.yScale = canvasHeight / (this.maxLat - this.minLat);
    }

    /**
     * Convierte coordenadas geograficas a puntos del mapa.
     *
     * @param location la localizacion geografica a convertir
     * @return un punto del mapa con las coordenadas correspondientes
     */
    public Point2D projectToScreen(Location location) {
        double x = (location.getLongitude() - this.minLng) * this.xScale;
        double y = (this.maxLat - location.getLatitude()) * this.yScale;
        return new Point2D(x, y);
    }

    /**
     * Convierte puntos del mapa a coordenadas geograficas.
     *
     * @param screenX la coordenada horizontal de la pantalla
     * @param screenY la coordenada vertical de la pantalla
     * @return la localizacion con coordenadas geograficas
     */
    public Location unprojectToGeo(double screenX, double screenY) {
        double lng = this.minLng + (screenX / this.xScale);
        double lat = this.maxLat - (screenY / this.yScale);
        return new Location(lat, lng);
    }

}
