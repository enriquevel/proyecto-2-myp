package myp.proyecto2.view.javafx;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import myp.proyecto2.model.domain.*;
import myp.proyecto2.model.domain.builder.Route;
import myp.proyecto2.view.View;

/**
 * Implementacion de la interfaz View usando JavaFX como framework de interfaz grafica.
 * Actua como la capa de presentacion principal del patron MVC.
 */
public class JavaFXView implements View {

    /** El escenario principal de JavaFX. */
    private final Stage stage;

    /** Escena que contiene todos los elementos de la interfaz grafica. */
    private final Scene scene;

    /** Lienzo interactivo donde se dibuja el mapa y las rutas. */
    private final MapCanvas mapCanvas;

    /** Panel de control para seleccionar origen, destino y preferencias de ruta. */
    private final ControlPanel controlPanel;

    /** Panel que muestra la lista de rutas calculadas con sus puntuaciones. */
    private final RouteListPanel routeListPanel;

    /** Panel que muestra la lista de reportes activos en el sistema. */
    private final ReportListPanel reportListPanel;

    /** Panel para administrar puntos de interes (POIs). */
    private final POIPanel poiPanel;

    /** Barra de menu superior con opciones de archivo y vista. */
    private final MenuBar menuBar;

    /** Etiqueta en la barra de estado que muestra mensajes al usuario. */
    private final Label statusLabel;

    /** Callback ejecutado cuando se solicita buscar rutas. */
    private Consumer<RouteRequest> onFindRoutes;

    /** Callback ejecutado cuando se envia un nuevo reporte. */
    private Consumer<Report> onReportSubmit;

    /** Callback ejecutado cuando se da voto positivo a un reporte. */
    private Consumer<Report> onReportUpvote;

    /** Callback ejecutado cuando se da voto negativo a un reporte. */
    private Consumer<Report> onReportDownvote;

    /** Callback ejecutado cuando se agrega un nuevo POI. */
    private Consumer<PointOfInterest> onPOIAdd;

    /** Callback ejecutado cuando se elimina un POI. */
    private Consumer<PointOfInterest> onPOIDelete;

    /** Callback ejecutado cuando se solicita refrescar los datos. */
    private Runnable onRefreshData;

    /** Callback ejecutado cuando se solicita cambiar la informacion. */
    private Runnable onSettingsRequested;

    /**
     * Construye una nueva vista JavaFX con todos sus componentes visuales.
     * Inicializa el escenario, la escena, y todos los paneles de la interfaz.
     * Configura el layout principal y conecta los eventos internos de los componentes.
     *
     * @param stage el escenario principal de JavaFX
     * @param campusBounds los limites geograficos del campus
     */
    public JavaFXView(Stage stage, UniversityBounds campusBounds) {
        this.stage = stage;
        this.mapCanvas = new MapCanvas(823, 900, campusBounds);
        this.controlPanel = new ControlPanel();
        this.routeListPanel = new RouteListPanel();
        this.reportListPanel = new ReportListPanel();
        this.poiPanel = new POIPanel();

        // Menu bar
        this.menuBar = createMenuBar();

        // Status bar
        HBox statusBar = new HBox(10);
        statusBar.setPadding(new Insets(5));
        statusBar.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #e0e0e0; -fx-border-width: 1 0 0 0;");

        this.statusLabel = new Label("Listo");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        statusBar.getChildren().addAll(statusLabel, spacer);

        // Layout
        BorderPane root = new BorderPane();

        root.setTop(menuBar);

        StackPane mapContainer = new StackPane(mapCanvas);
        mapContainer.setStyle("-fx-background-color: #f5f5f5;");
        root.setCenter(mapContainer);

        // Left panel
        VBox leftPanel = new VBox();
        leftPanel.getChildren().addAll(controlPanel, new Separator(), poiPanel);
        VBox.setVgrow(poiPanel, Priority.ALWAYS);
        root.setLeft(leftPanel);

        // Right panel
        VBox rightPanel = new VBox();
        rightPanel.getChildren().addAll(routeListPanel, new Separator(), reportListPanel);
        VBox.setVgrow(reportListPanel, Priority.ALWAYS);
        root.setRight(rightPanel);

        root.setBottom(statusBar);

        // Scene
        this.scene = new Scene(root, 1500, 800);

        // Wire internal events
        wireEvents();

        // Configure stage
        stage.setTitle("PumaMaps");
        stage.setScene(scene);
        stage.setMinWidth(1200);
        stage.setMinHeight(1000);
    }

    /**
     * Conecta los eventos internos de los componentes visuales con sus handlers.
     * Configura las acciones de los botones, selectores y listas. No incluye
     * los callbacks del controlador, que se configuran por separado.
     */
    private void wireEvents() {
        // Control panel
        controlPanel.setOnFindRoutes(() -> {
            Location origin = controlPanel.getOriginSelector().getSelectedLocation();
            Location dest = controlPanel.getDestinationSelector().getSelectedLocation();

            if (origin == null || dest == null) {
                displayError("Please select both origin and destination");
                return;
            }

            RouteRequest request = new RouteRequest(origin, dest,
                    controlPanel.getSelectedMode(), controlPanel.getPreference());

            if (onFindRoutes != null)
                onFindRoutes.accept(request);
        });

        controlPanel.setOnClear(() -> {
            controlPanel.clearSelections();
            clearMap();
            statusLabel.setText("Ready");
        });

        // Route list
        routeListPanel.setOnRouteSelected(mapCanvas::highlightRoute);

        // Report list
        reportListPanel.setOnUpvote(report -> {
            if (onReportUpvote != null)
                onReportUpvote.accept(report);
        });

        reportListPanel.setOnDownvote(report -> {
            if (onReportDownvote != null)
                onReportDownvote.accept(report);
        });

        // POI panel
        poiPanel.setOnPOISelected(poi -> mapCanvas.centerOn(poi.getLocation()));

        poiPanel.setOnPOIDelete(poi -> {
            if (confirm("Delete location: " + poi.getName() + "?")) {
                if (onPOIDelete != null)
                    onPOIDelete.accept(poi);
            }
        });

        poiPanel.setOnAddPOI(this::handleAddPOI);

        // Location selectors - map click mode
        controlPanel.getOriginSelector().setOnMapClickRequested(v -> enableMapClickMode(location -> {
            controlPanel.getOriginSelector().setSelectedLocation(location);
            disableMapClickMode();
        }));

        controlPanel.getDestinationSelector().setOnMapClickRequested(v -> enableMapClickMode(location -> {
            controlPanel.getDestinationSelector().setSelectedLocation(location);
            disableMapClickMode();
        }));
    }

    /**
     * Crea y configura la barra de menu superior con todas sus opciones.
     * Incluye menus de archivo (nuevo reporte, agregar POI, refrescar, salir)
     * y vista (limpiar mapa).
     *
     * @return la barra de menu configurada
     */
    private MenuBar createMenuBar() {
        MenuBar bar = new MenuBar();

        Menu fileMenu = new Menu("Archivo");

        MenuItem newReportItem = new MenuItem("Agregar Reporte...");
        newReportItem.setOnAction(e -> handleNewReport());

        MenuItem newPOIItem = new MenuItem("Agregar Punto de Interes...");
        newPOIItem.setOnAction(e -> handleAddPOI());

        MenuItem refreshItem = new MenuItem("Recargar informacion");
        refreshItem.setOnAction(e -> {
            if (onRefreshData != null) {
                onRefreshData.run();
            }
        });

        MenuItem settingsItem = new MenuItem("Configuracion...");
        settingsItem.setOnAction(e -> handleSettings());

        MenuItem exitItem = new MenuItem("Salir");
        exitItem.setOnAction(e -> Platform.exit());

        fileMenu.getItems().addAll(newReportItem, newPOIItem, new SeparatorMenuItem(),
                refreshItem, settingsItem, new SeparatorMenuItem(), exitItem);

        Menu viewMenu = new Menu("Vista");

        MenuItem clearMapItem = new MenuItem("Reiniciar mapa");
        clearMapItem.setOnAction(e -> clearMap());

        viewMenu.getItems().add(clearMapItem);

        bar.getMenus().addAll(fileMenu, viewMenu);

        return bar;
    }

    /**
     * Maneja la apertura del dialogo para crear un nuevo reporte. Configura los
     * callbacks del dialogo incluyendo la seleccion de ubicacion en el mapa y
     * el envio del reporte completado.
     */
    private void handleNewReport() {
        ReportDialog dialog = new ReportDialog(null, null);

        dialog.setOnRequestMapClick(locationCallback -> {
            dialog.hideDialog();

            enableMapClickMode(location -> {
                locationCallback.accept(location);
                disableMapClickMode();
                dialog.showDialog();
            });
        });

        dialog.setOnResult(report -> {
            if (report != null && onReportSubmit != null)
                onReportSubmit.accept(report);
        });

        dialog.showDialog();
    }

    /**
     * Maneja la apertura del dialogo para agregar un nuevo punto de interes.
     * Configura los callbacks del dialogo incluyendo la seleccion de ubicacion
     * en el mapa y el registro del POI completado.
     */
    private void handleAddPOI() {
        javafx.application.Platform.runLater(() -> {
            POIDialog dialog = new POIDialog(null, null);

            dialog.setOnRequestMapClick(locationCallback -> {
                dialog.hideDialog();

                enableMapClickMode(location -> {
                    locationCallback.accept(location);
                    disableMapClickMode();

                    javafx.application.Platform.runLater(dialog::showDialog);
                });
            });

            dialog.setOnResult(poi -> {
                if (poi != null && onPOIAdd != null)
                    onPOIAdd.accept(poi);
            });

            dialog.showDialog();
        });

    }

    private void handleSettings() {
        if (onSettingsRequested != null) {
            onSettingsRequested.run();
        }
    }

    /**
     * Muestra la ventana principal de la aplicacion.
     */
    @Override
    public void show() {
        this.stage.show();
    }

    /**
     * Cierra la ventana principal de la aplicacion.
     */
    @Override
    public void close() {
        this.stage.close();
    }

    /**
     * Muestra un mensaje informativo al usuario en un dialogo modal.
     *
     * @param message el mensaje a mostrar
     */
    @Override
    public void displayMessage(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informacion");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * Muestra un mensaje de error al usuario en un dialogo modal y actualiza
     * la barra de estado con el mensaje de error.
     *
     * @param error el mensaje de error a mostrar
     */
    @Override
    public void displayError(String error) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(error);
            alert.showAndWait();
            statusLabel.setText("Error: " + error);
        });
    }

    /**
     * Muestra un mensaje de advertencia al usuario en un dialogo modal y actualiza
     * la barra de estado con el mensaje de advertencia.
     *
     * @param warning el mensaje de advertencia a mostrar
     */
    @Override
    public void displayWarning(String warning) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText(warning);
            alert.showAndWait();
            statusLabel.setText("Advertencia: " + warning);
        });

    }

    /**
     * Muestra un mensaje de exito en la barra de estado sin dialogo modal.
     *
     * @param message el mensaje de exito a mostrar
     */
    @Override
    public void displaySuccess(String message) {
        Platform.runLater(() -> statusLabel.setText(message));
    }

    /**
     * Muestra las rutas calculadas en el mapa y en la lista de rutas.
     *
     * @param routes lista de rutas puntuadas a mostrar
     */
    @Override
    public void displayRoutes(List<ScoredRoute> routes) {
        Platform.runLater(() -> {
            mapCanvas.setRoutes(routes);
            routeListPanel.setRoutes(routes);
        });
    }

    /**
     * Resalta una ruta especifica en el mapa cambiando su color y grosor.
     *
     * @param route la ruta a resaltar
     */
    @Override
    public void highlightRoute(Route route) {
        Platform.runLater(() -> mapCanvas.highlightRoute(route));
    }

    /**
     * Limpia todas las rutas del mapa y de la lista de rutas.
     */
    @Override
    public void clearRoutes() {
        Platform.runLater(() -> {
            mapCanvas.clearRoutes();
            routeListPanel.clearRoutes();
        });
    }

    /**
     * Muestra los reportes en el mapa y en la lista de reportes.
     *
     * @param reports lista de reportes a mostrar
     */
    @Override
    public void displayReports(List<Report> reports) {
        Platform.runLater(() -> {
            mapCanvas.setReports(reports);
            reportListPanel.setReports(reports);
        });
    }

    /**
     * Limpia todos los reportes del mapa y de la lista de reportes.
     */
    @Override
    public void clearReports() {
        Platform.runLater(() -> {
            mapCanvas.clearReports();
            reportListPanel.clearReports();
        });
    }

    /**
     * Muestra los puntos de interes en el mapa, en el panel de POIs y en
     * los selectores de ubicacion.
     *
     * @param pois lista de puntos de interes a mostrar
     */
    @Override
    public void displayPOIs(List<PointOfInterest> pois) {
        Platform.runLater(() -> {
            mapCanvas.setPOIs(pois);
            poiPanel.setPOIs(pois);
            controlPanel.getOriginSelector().setPOIs(pois);
            controlPanel.getDestinationSelector().setPOIs(pois);
        });
    }

    /**
     * Limpia todos los puntos de interes del mapa y del panel de POIs.
     */
    @Override
    public void clearPOIs() {
        Platform.runLater(() -> {
            mapCanvas.clearPOIs();
            poiPanel.clearPOIs();
        });
    }

    /**
     * Limpia todo el contenido del mapa incluyendo rutas, reportes y POIs.
     * Tambien limpia las listas de rutas y reportes.
     */
    @Override
    public void clearMap() {
        Platform.runLater(() -> {
            mapCanvas.clearAll();
            routeListPanel.clearRoutes();
            reportListPanel.clearReports();
        });
    }

    /**
     * Centra el mapa en una ubicacion especifica.
     *
     * @param location la ubicacion donde centrar el mapa
     */
    @Override
    public void centerMap(Location location) {
        Platform.runLater(() -> mapCanvas.centerOn(location));
    }

    /**
     * Habilita el modo de seleccion de ubicacion en el mapa. Cambia el cursor
     * a una cruz y espera a que el usuario haga clic en el mapa para seleccionar
     * una ubicacion.
     *
     * @param callback funcion que se ejecuta cuando se selecciona una ubicacion
     */
    @Override
    public void enableMapClickMode(Consumer<Location> callback) {
        mapCanvas.enableMapClickMode(callback);
    }

    /**
     * Deshabilita el modo de seleccion de ubicacion en el mapa y restaura
     * el cursor normal.
     */
    @Override
    public void disableMapClickMode() {
        mapCanvas.disableMapClickMode();
    }

    /**
     * Muestra un dialogo de confirmacion con un mensaje y botones Si/No.
     *
     * @param question la pregunta a mostrar al usuario
     * @return true si el usuario confirma, false en caso contrario
     */
    @Override
    public boolean confirm(String question) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText(null);
        alert.setContentText(question);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;

    }

    /**
     * Configura el callback que se ejecuta cuando se solicita buscar rutas.
     *
     * @param callback funcion que recibe una solicitud de ruta
     */
    @Override
    public void setOnFindRoutes(Consumer<RouteRequest> callback) {
        this.onFindRoutes = callback;
    }

    /**
     * Configura el callback que se ejecuta cuando se envia un nuevo reporte.
     *
     * @param callback funcion que recibe un reporte
     */
    @Override
    public void setOnReportSubmit(Consumer<Report> callback) {
        this.onReportSubmit = callback;
    }

    /**
     * Configura el callback que se ejecuta cuando se da voto positivo a un reporte.
     *
     * @param callback funcion que recibe un reporte
     */
    @Override
    public void setOnReportUpvote(Consumer<Report> callback) {
        this.onReportUpvote = callback;
    }

    /**
     * Configura el callback que se ejecuta cuando se da voto negativo a un reporte.
     *
     * @param callback funcion que recibe un reporte
     */
    @Override
    public void setOnReportDownvote(Consumer<Report> callback) {
        this.onReportDownvote = callback;
    }

    /**
     * Configura el callback que se ejecuta cuando se agrega un nuevo POI.
     *
     * @param callback funcion que recibe un punto de interes
     */
    @Override
    public void setOnPOIAdd(Consumer<PointOfInterest> callback) {
        this.onPOIAdd = callback;
    }

    /**
     * Configura el callback que se ejecuta cuando se elimina un POI.
     *
     * @param callback funcion que recibe un punto de interes
     */
    @Override
    public void setOnPOIDelete(Consumer<PointOfInterest> callback) {
        this.onPOIDelete = callback;
    }

    /**
     * Configura el callback que se ejecuta cuando se solicita refrescar los datos.
     *
     * @param callback funcion sin parametros
     */
    @Override
    public void setOnRefreshData(Runnable callback) {
        this.onRefreshData = callback;
    }

    /**
     * Configura el callback que se ejecuta cuando se solicita cambiar la configuracion.
     *
     * @param callback funcion sin parametros
     */
    @Override
    public void setOnSettingsRequested(Runnable callback) {
        this.onSettingsRequested = callback;
    }
}
