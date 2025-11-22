package myp.proyecto2.view.javafx;

import javafx.geometry.Point2D;
import myp.proyecto2.model.domain.Location;

public class CoordinateProjection {

    private final UniversityBounds bounds;
    private final double canvasWidth;
    private final double canvasHeight;

    private final double minLat;
    private final double maxLat;
    private final double minLng;
    private final double maxLng;

    private final double xScale;
    private final double yScale;

    public CoordinateProjection(UniversityBounds bounds, double canvasWidth, double canvasHeight) {
        this.bounds = bounds;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;

        this.minLat = bounds.getMinLatitude();
        this.maxLat = bounds.getMaxLatitude();
        this.minLng = bounds.getMinLongitude();
        this.maxLng = bounds.getMaxLongitude();

        this.xScale = canvasWidth / (this.maxLng - this.minLng);
        this.yScale = canvasHeight / (this.maxLat - this.minLat);
    }

    /**
     * Convert geographic location to screen coordinates.
     */
    public Point2D projectToScreen(Location location) {
        double x = (location.getLongitude() - this.minLng) * this.xScale;
        double y = (this.maxLat - location.getLatitude()) * this.yScale;
        return new Point2D(x, y);
    }

    /**
     * Convert screen coordinates to geographic location.
     */
    public Location unprojectToGeo(double screenX, double screenY) {
        double lng = this.minLng + (screenX / this.xScale);
        double lat = this.maxLat - (screenY / this.yScale);
        return new Location(lat, lng);
    }

    public UniversityBounds getBounds() {
        return this.bounds;
    }

}
