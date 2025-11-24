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

public class JavaFXView implements View {

    private final Stage stage;
    private final Scene scene;

    // Components
    private final MapCanvas mapCanvas;
    private final ControlPanel controlPanel;
    private final RouteListPanel routeListPanel;
    private final ReportListPanel reportListPanel;
    private final POIPanel poiPanel;

    // Menu
    private final MenuBar menuBar;

    // Status bar
    private final Label statusLabel;
    private final ProgressIndicator loadingIndicator;

    // Configuration
    private final UniversityBounds campusBounds;
    private String currentUser = "Anonymous";

    // ===== CALLBACKS (for controller wiring) =====
    private Consumer<RouteRequest> onFindRoutes;
    private Consumer<Report> onReportSubmit;
    private Consumer<Report> onReportUpvote;
    private Consumer<Report> onReportDownvote;
    private Consumer<PointOfInterest> onPOIAdd;
    private Consumer<PointOfInterest> onPOIDelete;
    private Runnable onRefreshData;

    public static class RouteRequest {
        public final Location origin;
        public final Location destination;
        public final TransportMode mode;
        public final RoutePreference preference;

        public RouteRequest(Location origin, Location destination,
                            TransportMode mode, RoutePreference preference) {
            this.origin = origin;
            this.destination = destination;
            this.mode = mode;
            this.preference = preference;
        }
    }

    public JavaFXView(Stage stage, UniversityBounds campusBounds) {
        this.stage = stage;
        this.campusBounds = campusBounds;
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

        this.statusLabel = new Label("Ready");
        this.loadingIndicator = new ProgressIndicator();
        this.loadingIndicator.setMaxSize(16, 16);
        this.loadingIndicator.setVisible(false);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        statusBar.getChildren().addAll(statusLabel, spacer, loadingIndicator);

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
        stage.setTitle("UNAM Campus Router");
        stage.setScene(scene);
        stage.setMinWidth(1200);
        stage.setMinHeight(700);
    }

    /**
     * Wire internal component events (not controller callbacks).
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

            RouteRequest request = new RouteRequest(
                    origin,
                    dest,
                    controlPanel.getSelectedMode(),
                    controlPanel.getPreference()
            );

            if (onFindRoutes != null) {
                onFindRoutes.accept(request);
            }
        });

        controlPanel.setOnClear(() -> {
            controlPanel.clearSelections();
            clearMap();
            statusLabel.setText("Ready");
        });

        // Route list
        routeListPanel.setOnRouteSelected(route -> {
            mapCanvas.highlightRoute(route);
        });

        // Report list
        reportListPanel.setOnUpvote(report -> {
            if (onReportUpvote != null) {
                onReportUpvote.accept(report);
            }
        });

        reportListPanel.setOnDownvote(report -> {
            if (onReportDownvote != null) {
                onReportDownvote.accept(report);
            }
        });

        // POI panel
        poiPanel.setOnPOISelected(poi -> {
            mapCanvas.centerOn(poi.getLocation());
        });

        poiPanel.setOnPOIDelete(poi -> {
            if (confirm("Delete location: " + poi.getName() + "?")) {
                if (onPOIDelete != null) {
                    onPOIDelete.accept(poi);
                }
            }
        });

        poiPanel.setOnAddPOI(this::handleAddPOI);

        // Location selectors - map click mode
        controlPanel.getOriginSelector().setOnMapClickRequested(v -> {
            enableMapClickMode(location -> {
                controlPanel.getOriginSelector().setSelectedLocation(location);
                disableMapClickMode();
            });
        });

        controlPanel.getDestinationSelector().setOnMapClickRequested(v -> {
            enableMapClickMode(location -> {
                controlPanel.getDestinationSelector().setSelectedLocation(location);
                disableMapClickMode();
            });
        });
    }

    /**
     * Create menu bar.
     */
    private MenuBar createMenuBar() {
        MenuBar bar = new MenuBar();

        // File menu
        Menu fileMenu = new Menu("File");

        MenuItem newReportItem = new MenuItem("New Report...");
        newReportItem.setOnAction(e -> handleNewReport());

        MenuItem newPOIItem = new MenuItem("Add Location...");
        newPOIItem.setOnAction(e -> handleAddPOI());

        MenuItem refreshItem = new MenuItem("Refresh Data");
        refreshItem.setOnAction(e -> {
            if (onRefreshData != null) {
                onRefreshData.run();
            }
        });

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> Platform.exit());

        fileMenu.getItems().addAll(newReportItem, newPOIItem, new SeparatorMenuItem(),
                refreshItem, new SeparatorMenuItem(), exitItem);

        // View menu
        Menu viewMenu = new Menu("View");

        MenuItem clearMapItem = new MenuItem("Clear Map");
        clearMapItem.setOnAction(e -> clearMap());

        viewMenu.getItems().add(clearMapItem);

        bar.getMenus().addAll(fileMenu, viewMenu);

        return bar;
    }

    /**
     * Handle new report dialog.
     */
    private void handleNewReport() {
        System.out.println("DEBUG: handleNewReport() called");

        // Create dialog
        ReportDialog dialog = new ReportDialog(null, null);
        System.out.println("DEBUG: ReportDialog instance created");

        // Set up map click callback FIRST
        dialog.setOnRequestMapClick(locationCallback -> {
            System.out.println("DEBUG: Map click requested for report dialog");

            // Hide dialog first
            dialog.hideDialog();

            // Enable map click mode
            enableMapClickMode(location -> {
                System.out.println("DEBUG: Location selected from map: " + location);

                // Pass location to dialog
                locationCallback.accept(location);

                // Disable map click mode
                disableMapClickMode();

                // Show dialog again
                dialog.showDialog();
            });
        });
        System.out.println("DEBUG: Map click callback set");

        // Set result callback SECOND
        dialog.setOnResult(report -> {
            System.out.println("DEBUG: Dialog result received: " + report);
            if (report != null && onReportSubmit != null) {
                System.out.println("DEBUG: Firing onReportSubmit callback");
                onReportSubmit.accept(report);
            }
        });
        System.out.println("DEBUG: Result callback set");

        // Show dialog LAST
        System.out.println("DEBUG: About to show dialog");
        dialog.showDialog();
        System.out.println("DEBUG: Dialog shown");

    }

    /**
     * Handle add POI dialog.
     */
    private void handleAddPOI() {
        System.out.println("DEBUG: handleAddPOI() called");

        // Force everything on JavaFX thread
        javafx.application.Platform.runLater(() -> {
            System.out.println("DEBUG: Creating POIDialog on JavaFX thread");

            // Create dialog
            POIDialog dialog = new POIDialog(null, null );
            System.out.println("DEBUG: POIDialog instance created");

            // Set up map click callback
            dialog.setOnRequestMapClick(locationCallback -> {
                System.out.println("DEBUG: Map click requested");
                dialog.hideDialog();

                enableMapClickMode(location -> {
                    System.out.println("DEBUG: Location selected: " + location);
                    locationCallback.accept(location);
                    disableMapClickMode();

                    javafx.application.Platform.runLater(() -> {
                        dialog.showDialog();
                    });
                });
            });

            // Set result callback
            dialog.setOnResult(poi -> {
                System.out.println("DEBUG: Dialog result: " + poi);
                if (poi != null && onPOIAdd != null) {
                    onPOIAdd.accept(poi);
                }
            });

            // Show dialog
            System.out.println("DEBUG: Showing dialog");
            dialog.showDialog();
        });

    }


    /**
     *
     */
    @Override
    public void show() {
        this.stage.show();
    }

    /**
     *
     */
    @Override
    public void close() {
        this.stage.close();
    }

    /**
     * @param message
     */
    @Override
    public void displayMessage(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * @param error
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
     * @param warning
     */
    @Override
    public void displayWarning(String warning) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText(warning);
            alert.showAndWait();
            statusLabel.setText("Warning: " + warning);
        });

    }

    /**
     * @param message
     */
    @Override
    public void displaySuccess(String message) {
        Platform.runLater(() -> {
            statusLabel.setText(message);
        });
    }

    /**
     * @param routes
     */
    @Override
    public void displayRoutes(List<ScoredRoute> routes) {
        Platform.runLater(() -> {
            mapCanvas.setRoutes(routes);
            routeListPanel.setRoutes(routes);
        });
    }

    /**
     * @param route
     */
    @Override
    public void highlightRoute(Route route) {
        Platform.runLater(() -> {
            mapCanvas.highlightRoute(route);
        });
    }

    /**
     *
     */
    @Override
    public void clearRoutes() {
        Platform.runLater(() -> {
            mapCanvas.clearRoutes();
            routeListPanel.clearRoutes();
        });
    }

    /**
     * @param reports
     */
    @Override
    public void displayReports(List<Report> reports) {
        Platform.runLater(() -> {
            mapCanvas.setReports(reports);
            reportListPanel.setReports(reports);
        });
    }

    /**
     *
     */
    @Override
    public void clearReports() {
        Platform.runLater(() -> {
            mapCanvas.clearReports();
            reportListPanel.clearReports();
        });
    }

    /**
     * @param pois
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
     *
     */
    @Override
    public void clearPOIs() {
        Platform.runLater(() -> {
            mapCanvas.clearPOIs();
            poiPanel.clearPOIs();
        });
    }

    /**
     *
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
     * @param location
     */
    @Override
    public void centerMap(Location location) {
        Platform.runLater(() -> {
            mapCanvas.centerOn(location);
        });
    }

    /**
     * @param callback
     */
    @Override
    public void enableMapClickMode(Consumer<Location> callback) {
        mapCanvas.enableMapClickMode(callback);
    }

    /**
     *
     */
    @Override
    public void disableMapClickMode() {
        mapCanvas.disableMapClickMode();
    }

    /**
     * @param question
     * @return
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

    public void setOnFindRoutes(Consumer<RouteRequest> callback) {
        this.onFindRoutes = callback;
    }

    public void setOnReportSubmit(Consumer<Report> callback) {
        this.onReportSubmit = callback;
    }

    public void setOnReportUpvote(Consumer<Report> callback) {
        this.onReportUpvote = callback;
    }

    public void setOnReportDownvote(Consumer<Report> callback) {
        this.onReportDownvote = callback;
    }

    public void setOnPOIAdd(Consumer<PointOfInterest> callback) {
        this.onPOIAdd = callback;
    }

    public void setOnPOIDelete(Consumer<PointOfInterest> callback) {
        this.onPOIDelete = callback;
    }

    public void setOnRefreshData(Runnable callback) {
        this.onRefreshData = callback;
    }

    public Stage getStage() {
        return stage;
    }
}
