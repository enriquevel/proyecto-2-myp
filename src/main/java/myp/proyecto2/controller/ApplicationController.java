package myp.proyecto2.controller;

import java.util.List;
import myp.proyecto2.model.catalog.POICatalog;
import myp.proyecto2.model.catalog.ReportCatalog;
import myp.proyecto2.model.domain.*;
import myp.proyecto2.model.provider.RouteProvider;
import myp.proyecto2.model.provider.RouteProviderFactory;
import myp.proyecto2.view.View;

/**
 * Clase que representa el controlador principal de la aplicacion.
 */
public class ApplicationController {

    /** La vista de la aplicacion. */
    private final View view;

    /** El controlador de rutas. */
    private final RouteController routeController;

    /** El controlador de reportes. */
    private final ReportController reportController;

    /** El controlador de puntos de interes. */
    private final POIController poiController;

    /**
     * Constructor principal del controlador principal.
     *
     * @param view la vista del controlador
     * @param provider el proveedor de rutas preferido
     * @param apiKey la llave API del usuario
     */
    public ApplicationController(View view, String provider, String apiKey) {
        this.view = view;
        System.out.println("Inicializando aplicacion.");

        RouteProvider routeProvider = RouteProviderFactory.createRouteProvider(provider, apiKey);
        this.routeController = new RouteController(routeProvider);

        ReportCatalog reportCatalog = new ReportCatalog("data/reports.csv");
        this.reportController = new ReportController(reportCatalog);

        POICatalog poiCatalog = new POICatalog("data/points_of_interest.csv");
        this.poiController = new POIController(poiCatalog);

        wireViewCallbacks();
        initializeApplication();
        System.out.println("Inicializacion completa.");
    }

    /**
     * Asocia los callback de la vista a metodos de esta clase.
     */
    private void wireViewCallbacks() {
        this.view.setOnFindRoutes(request -> handleFindRoutes(
                request.origin,
                request.destination,
                request.mode,
                request.preference
        ));

        this.view.setOnReportSubmit(this::handleReportSubmit);
        this.view.setOnReportUpvote(this::handleReportUpvote);
        this.view.setOnReportDownvote(this::handleReportDownvote);
        this.view.setOnPOIAdd(this::handlePOIAdd);
        this.view.setOnPOIDelete(this::handlePOIDelete);
        this.view.setOnRefreshData(this::refreshAllData);
    }

    /**
     * Inicializa las bases de datos requeridas para la aplicacion
     */
    private void initializeApplication() {
        try {
            List<PointOfInterest> pois = this.poiController.getAllPOIs();
            this.view.displayPOIs(pois);
            System.out.println("Loaded " + pois.size() + " POIs");

            List<Report> reports = this.reportController.getActiveReports();
            this.view.displayReports(reports);
            System.out.println("Loaded " + reports.size() + " reports");
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    /**
     * Maneja las peticiones de rutas del usuario.
     *
     * @param origin el origen de la ruta
     * @param destination el destino de la ruta
     * @param mode el modo elegido de la ruta
     * @param preference la preferencia de rutas
     */
    private void handleFindRoutes(
            Location origin,
            Location destination,
            TransportMode mode,
            RoutePreference preference) {

        new Thread(() -> {
            try {
                List<Report> reports = reportController.getActiveReports();

                List<ScoredRoute> routes = routeController.findAndScoreRoutes(
                        origin, destination, mode, reports, preference
                );

                javafx.application.Platform.runLater(() -> {
                    view.displayRoutes(routes);
                    view.displaySuccess("Found " + routes.size() + " routes");
                });

            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    view.displayError("Failed to find routes: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Maneja las creaciones de reportes del usuario.
     *
     * @param report el reporte creado.
     */
    private void handleReportSubmit(Report report) {
        try {
            reportController.submitReport(report);
            view.displaySuccess("Report submitted");
            refreshReports();
        } catch (Exception e) {
            view.displayError("Failed to submit: " + e.getMessage());
        }
    }

    /**
     * Maneja los votos a favor de un reporte.
     *
     * @param report el reporte votado
     */
    private void handleReportUpvote(Report report) {
        try {
            reportController.upvoteReport(report);
            refreshReports();
        } catch (Exception e) {
            view.displayError("Failed to upvote: " + e.getMessage());
        }
    }

    /**
     * Maneja los votos en contra de un reporte.
     *
     * @param report el reporte votado
     */
    private void handleReportDownvote(Report report) {
        try {
            reportController.downvoteReport(report);
            refreshReports();
        } catch (Exception e) {
            view.displayError("Failed to downvote: " + e.getMessage());
        }
    }

    /**
     * Actualiza los reportes mostrados.
     */
    private void refreshReports() {
        List<Report> reports = reportController.getActiveReports();
        view.displayReports(reports);
    }

    /**
     * Maneja las creaciones de puntos de interes del usuario.
     *
     * @param poi el punto de interes creado
     */
    private void handlePOIAdd(PointOfInterest poi) {
        try {
            poiController.addPOI(poi);
            view.displaySuccess("Location saved: " + poi.getName());
            refreshPOIs();
        } catch (Exception e) {
            view.displayError("Failed to save: " + e.getMessage());
        }
    }

    /**
     * Maneja la eliminacion de puntos de interes.
     *
     * @param poi el punto de interes a eliminar
     */
    private void handlePOIDelete(PointOfInterest poi) {
        try {
            poiController.deletePOI(poi.getId());
            view.displaySuccess("Location deleted");
            refreshPOIs();
        } catch (Exception e) {
            view.displayError("Failed to delete: " + e.getMessage());
        }
    }

    /**
     * Actualiza los puntos de interes mostrados.
     */
    private void refreshPOIs() {
        List<PointOfInterest> pois = poiController.getAllPOIs();
        view.displayPOIs(pois);
    }

    /**
     * Actualiza la informacion mostrada al usuario.
     */
    public void refreshAllData() {
        refreshPOIs();
        refreshReports();
        view.displaySuccess("Data refreshed");
    }

    /**
     * Inicia la aplicacion.
     */
    public void start() {
        view.show();
    }

    /**
     * Termina la aplicacion.
     */
    public void shutdown() {
        System.out.println("Shutting down...");
        view.close();
    }
}
