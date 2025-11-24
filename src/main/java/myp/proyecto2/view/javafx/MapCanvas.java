package myp.proyecto2.view.javafx;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.PointOfInterest;
import myp.proyecto2.model.domain.Report;
import myp.proyecto2.model.domain.ScoredRoute;
import myp.proyecto2.model.domain.builder.Route;

public class MapCanvas extends Canvas {

    private final UniversityBounds bounds;
    private final CoordinateProjection projection;

    private Image campusImage;
    private final List<ScoredRoute> routes;
    private final List<Report> reports;
    private final List<PointOfInterest> pois;

    private Route highlightedRoute;
    private Consumer<Location> mapClickCallback;

    public MapCanvas(double width, double height, UniversityBounds bounds) {
        super(width, height);

        this.bounds = bounds;
        this.projection = new CoordinateProjection(bounds, width, height);

        this.routes = new ArrayList<>();
        this.reports = new ArrayList<>();
        this.pois = new ArrayList<>();

        // Load campus image (if exists)
        try {
            this.campusImage = new Image("file:data/ciudad_universitaria.png");
            System.out.println("Campus map image loaded successfully");
        } catch (Exception e) {
            System.out.println("No campus image found, using blank background");
            this.campusImage = null;
        }

        setOnMouseClicked(this::handleClick);
        redraw();
    }

    private void handleClick(MouseEvent event) {
        if (this.mapClickCallback != null) {
            Location location = this.projection.unprojectToGeo(event.getX(), event.getY());
            this.mapClickCallback.accept(location);
        }
    }

    public void setRoutes(List<ScoredRoute> routes) {
        this.routes.clear();
        this.routes.addAll(routes);
        redraw();
    }

    public void highlightRoute(Route route) {
        this.highlightedRoute = route;
        redraw();
    }

    public void clearRoutes() {
        this.routes.clear();
        this.highlightedRoute = null;
        redraw();
    }

    public void setReports(List<Report> reports) {
        this.reports.clear();
        this.reports.addAll(reports);
        redraw();
    }

    public void clearReports() {
        this.reports.clear();
        redraw();
    }

    public void setPOIs(List<PointOfInterest> pois) {
        this.pois.clear();
        this.pois.addAll(pois);
        redraw();
    }

    public void clearPOIs() {
        this.pois.clear();
        redraw();
    }

    public void clearAll() {
        clearRoutes();
        clearReports();
        clearPOIs();
    }

    public void enableMapClickMode(Consumer<Location> callback) {
        this.mapClickCallback = callback;
        setCursor(javafx.scene.Cursor.CROSSHAIR);
    }

    public void disableMapClickMode() {
        this.mapClickCallback = null;
        setCursor(javafx.scene.Cursor.DEFAULT);
    }

    public void centerOn(Location location) {
        // For now just redraw (future: pan/zoom support)
        redraw();
    }

    private void redraw() {
        GraphicsContext gc = getGraphicsContext2D();

        // Clear
        gc.clearRect(0, 0, getWidth(), getHeight());

        // Background
        if (this.campusImage != null)
            gc.drawImage(this.campusImage, 0, 0, getWidth(), getHeight());
        else {
            gc.setFill(Color.rgb(245, 245, 245));
            gc.fillRect(0, 0, getWidth(), getHeight());
        }

        // Draw routes
        for (int i = 0; i < this.routes.size(); i++) {
            ScoredRoute scoredRoute = this.routes.get(i);
            Route route = scoredRoute.getRoute();

            boolean highlighted = (route == this.highlightedRoute);
            boolean primary = (i == 0 && this.highlightedRoute == null);

            drawRoute(gc, route, highlighted, primary);
        }

        // Draw reports
        for (Report report : this.reports) {
            Point2D point = this.projection.projectToScreen(report.getLocation());
            IconRenderer.drawReportIcon(gc, point, report.getType(), 8);
        }

        // Draw POIs
        for (PointOfInterest poi : this.pois) {
            Point2D point = this.projection.projectToScreen(poi.getLocation());
            IconRenderer.drawLocationPin(gc, point, Color.rgb(76, 175, 80), 10);
        }
    }

    private void drawRoute(GraphicsContext gc, Route route, boolean highlighted, boolean primary) {
        List<Location> polyline = route.getPathPoints();

        if (polyline.isEmpty())
            return;

        // Colors (inlined)
        Color color;
        double lineWidth;

        if (highlighted) {
            color = Color.rgb(255, 87, 34);  // Orange
            lineWidth = 5;
        } else if (primary) {
            color = Color.rgb(33, 150, 243);  // Blue
            lineWidth = 4;
        } else {
            color = Color.rgb(158, 158, 158);  // Gray
            lineWidth = 3;
        }

        gc.setStroke(color);
        gc.setLineWidth(lineWidth);

        // Draw polyline
        for (int i = 0; i < polyline.size() - 1; i++) {
            Point2D p1 = this.projection.projectToScreen(polyline.get(i));
            Point2D p2 = this.projection.projectToScreen(polyline.get(i + 1));
            gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }

        // Start marker (green)
        Point2D start = this.projection.projectToScreen(route.getOrigin());
        IconRenderer.drawLocationPin(gc, start, Color.rgb(76, 175, 80), 12);

        // End marker (red)
        Point2D end = this.projection.projectToScreen(route.getDestination());
        IconRenderer.drawLocationPin(gc, end, Color.rgb(244, 67, 54), 12);
    }

    public CoordinateProjection getProjection() {
        return this.projection;
    }

}
