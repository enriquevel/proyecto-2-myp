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

/**
 * Lienzo interactivo que muestra el mapa del campus universitario con rutas,
 * reportes y puntos de interes. Maneja la proyeccion de coordenadas geograficas
 * a coordenadas de pantalla y permite la interaccion del usuario mediante clicks.
 */
public class MapCanvas extends Canvas {

    /** Sistema de proyeccion para convertir coordenadas geograficas a pantalla. */
    private final CoordinateProjection projection;

    /** Imagen de fondo del mapa. */
    private Image backgroundImage;

    /** Lista de rutas puntuadas a mostrar en el mapa. */
    private final List<ScoredRoute> routes;

    /** Lista de reportes activos a mostrar en el mapa. */
    private final List<Report> reports;

    /** Lista de puntos de interes a mostrar en el mapa. */
    private final List<PointOfInterest> pois;

    /** Ruta actualmente resaltada con color diferente. */
    private Route highlightedRoute;

    /** Callback ejecutado cuando el usuario hace clic en el mapa en modo seleccion. */
    private Consumer<Location> mapClickCallback;

    /**
     * Construye un nuevo lienzo de mapa con las dimensiones y limites especificados.
     * Inicializa el sistema de proyeccion, carga la imagen del campus si esta disponible
     * y configura el handler de eventos de clic.
     *
     * @param width ancho del lienzo en pixeles
     * @param height alto del lienzo en pixeles
     * @param bounds limites geograficos del area a mostrar
     */
    public MapCanvas(double width, double height, UniversityBounds bounds) {
        super(width, height);

        this.projection = new CoordinateProjection(bounds, width, height);

        this.routes = new ArrayList<>();
        this.reports = new ArrayList<>();
        this.pois = new ArrayList<>();

        try {
            this.backgroundImage = new Image("file:data/ciudad_universitaria.png");
            System.out.println("Imagen del mapa cargada exitosamente");
        } catch (Exception e) {
            System.out.println("No se encontro imagen para fondo del mapa");
            this.backgroundImage = null;
        }

        setOnMouseClicked(this::handleClick);
        redraw();
    }

    /**
     * Maneja los eventos de clic del mouse en el lienzo. Si el modo de seleccion
     * en mapa esta activo, convierte las coordenadas de pantalla a coordenadas
     * geograficas y ejecuta el callback correspondiente.
     *
     * @param event evento del mouse con informacion de la posicion del clic
     */
    private void handleClick(MouseEvent event) {
        if (this.mapClickCallback != null) {
            Location location = this.projection.unprojectToGeo(event.getX(), event.getY());
            this.mapClickCallback.accept(location);
        }
    }

    /**
     * Establece la lista de rutas a mostrar en el mapa y redibuja el lienzo.
     *
     * @param routes lista de rutas puntuadas a mostrar
     */
    public void setRoutes(List<ScoredRoute> routes) {
        this.routes.clear();
        this.routes.addAll(routes);
        redraw();
    }

    /**
     * Resalta una ruta especifica dibujandola con color y grosor diferente.
     * Actualiza el lienzo inmediatamente para reflejar el cambio.
     *
     * @param route la ruta a resaltar
     */
    public void highlightRoute(Route route) {
        this.highlightedRoute = route;
        redraw();
    }

    /**
     * Limpia todas las rutas del mapa incluyendo la ruta resaltada.
     * Redibuja el lienzo sin rutas.
     */
    public void clearRoutes() {
        this.routes.clear();
        this.highlightedRoute = null;
        redraw();
    }

    /**
     * Establece la lista de reportes a mostrar en el mapa y redibuja el lienzo.
     *
     * @param reports lista de reportes activos a mostrar
     */
    public void setReports(List<Report> reports) {
        this.reports.clear();
        this.reports.addAll(reports);
        redraw();
    }

    /**
     * Limpia todos los reportes del mapa y redibuja el lienzo.
     */
    public void clearReports() {
        this.reports.clear();
        redraw();
    }

    /**
     * Establece la lista de puntos de interes a mostrar en el mapa y redibuja el lienzo.
     *
     * @param pois lista de puntos de interes a mostrar
     */
    public void setPOIs(List<PointOfInterest> pois) {
        this.pois.clear();
        this.pois.addAll(pois);
        redraw();
    }

    /**
     * Limpia todos los puntos de interes del mapa y redibuja el lienzo.
     */
    public void clearPOIs() {
        this.pois.clear();
        redraw();
    }

    /**
     * Limpia todos los elementos del mapa: rutas, reportes y puntos de interes.
     * Redibuja el lienzo mostrando solo el fondo del campus.
     */
    public void clearAll() {
        clearRoutes();
        clearReports();
        clearPOIs();
    }

    /**
     * Habilita el modo de seleccion de ubicacion en el mapa. Cambia el cursor
     * a una cruz y espera a que el usuario haga clic para seleccionar una ubicacion.
     *
     * @param callback funcion que recibe la ubicacion seleccionada
     */
    public void enableMapClickMode(Consumer<Location> callback) {
        this.mapClickCallback = callback;
        setCursor(javafx.scene.Cursor.CROSSHAIR);
    }

    /**
     * Deshabilita el modo de seleccion de ubicacion en el mapa y restaura
     * el cursor normal.
     */
    public void disableMapClickMode() {
        this.mapClickCallback = null;
        setCursor(javafx.scene.Cursor.DEFAULT);
    }

    /**
     * Centra la vista del mapa en una ubicacion especifica. Actualmente
     * solo redibuja el mapa. Funcionalidad de zoom/pan pendiente de implementar.
     *
     * @param location la ubicacion donde centrar el mapa
     */
    public void centerOn(Location location) {
        redraw();
    }

    /**
     * Redibuja completamente el lienzo del mapa. Limpia el area de dibujo,
     * dibuja el fondo del campus, luego dibuja en orden las rutas, reportes
     * y puntos de interes. Las rutas se dibujan con diferentes colores segun
     * si estan resaltadas o son la ruta principal.
     */
    private void redraw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        if (this.backgroundImage != null)
            gc.drawImage(this.backgroundImage, 0, 0, getWidth(), getHeight());
        else {
            gc.setFill(Color.rgb(245, 245, 245));
            gc.fillRect(0, 0, getWidth(), getHeight());
        }

        for (int i = 0; i < this.routes.size(); i++) {
            ScoredRoute scoredRoute = this.routes.get(i);
            Route route = scoredRoute.getRoute();

            boolean highlighted = (route == this.highlightedRoute);
            boolean primary = (i == 0 && this.highlightedRoute == null);

            drawRoute(gc, route, highlighted, primary);
        }

        for (Report report : this.reports) {
            Point2D point = this.projection.projectToScreen(report.getLocation());
            IconRenderer.drawReportIcon(gc, point, report.getType(), 8);
        }

        for (PointOfInterest poi : this.pois) {
            Point2D point = this.projection.projectToScreen(poi.getLocation());
            IconRenderer.drawLocationPin(gc, point, Color.rgb(76, 175, 80), 10);
        }
    }

    /**
     * Dibuja una ruta individual en el lienzo. La ruta se dibuja con diferentes
     * colores y grosores dependiendo de si esta resaltada o es la ruta principal.
     * Tambien dibuja marcadores para el origen (verde) y destino (rojo).
     *
     * @param gc contexto grafico donde dibujar
     * @param route la ruta a dibujar
     * @param highlighted true si la ruta debe dibujarse resaltada (naranja, grosor 5)
     * @param primary true si es la ruta principal (azul, grosor 4)
     */
    private void drawRoute(GraphicsContext gc, Route route, boolean highlighted, boolean primary) {
        List<Location> polyline = route.getPathPoints();

        if (polyline.isEmpty())
            return;

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

        for (int i = 0; i < polyline.size() - 1; i++) {
            Point2D p1 = this.projection.projectToScreen(polyline.get(i));
            Point2D p2 = this.projection.projectToScreen(polyline.get(i + 1));
            gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }

        Point2D start = this.projection.projectToScreen(route.getOrigin());
        IconRenderer.drawLocationPin(gc, start, Color.rgb(76, 175, 80), 12);

        Point2D end = this.projection.projectToScreen(route.getDestination());
        IconRenderer.drawLocationPin(gc, end, Color.rgb(244, 67, 54), 12);
    }

}
