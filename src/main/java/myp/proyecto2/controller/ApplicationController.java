package myp.proyecto2.controller;

import myp.proyecto2.model.catalog.POICatalog;
import myp.proyecto2.model.catalog.ReportCatalog;
import myp.proyecto2.model.domain.*;
import myp.proyecto2.model.provider.RouteProvider;
import myp.proyecto2.model.provider.RouteProviderFactory;
import myp.proyecto2.model.scorer.AbstractRouteScorer;
import myp.proyecto2.view.View;

import java.util.List;

public class ApplicationController {

    private final View view;
    private final RouteController routeController;
    private final ReportController reportController;
    private final POIController poiController;

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

    // ==================== Report Operations ====================

    private void handleReportSubmit(Report report) {
        try {
            reportController.submitReport(report);
            view.displaySuccess("Report submitted");
            refreshReports();
        } catch (Exception e) {
            view.displayError("Failed to submit: " + e.getMessage());
        }
    }

    private void handleReportUpvote(Report report) {
        try {
            reportController.upvoteReport(report);
            refreshReports();
        } catch (Exception e) {
            view.displayError("Failed to upvote: " + e.getMessage());
        }
    }

    private void handleReportDownvote(Report report) {
        try {
            reportController.downvoteReport(report);
            refreshReports();
        } catch (Exception e) {
            view.displayError("Failed to downvote: " + e.getMessage());
        }
    }

    private void refreshReports() {
        List<Report> reports = reportController.getActiveReports();
        view.displayReports(reports);
    }

    // ==================== POI Operations ====================

    private void handlePOIAdd(PointOfInterest poi) {
        try {
            poiController.addPOI(poi);
            view.displaySuccess("Location saved: " + poi.getName());
            refreshPOIs();
        } catch (Exception e) {
            view.displayError("Failed to save: " + e.getMessage());
        }
    }

    private void handlePOIDelete(PointOfInterest poi) {
        try {
            poiController.deletePOI(poi.getId());
            view.displaySuccess("Location deleted");
            refreshPOIs();
        } catch (Exception e) {
            view.displayError("Failed to delete: " + e.getMessage());
        }
    }

    private void refreshPOIs() {
        List<PointOfInterest> pois = poiController.getAllPOIs();
        view.displayPOIs(pois);
    }

    // ==================== General ====================

    public void refreshAllData() {
        refreshPOIs();
        refreshReports();
        view.displaySuccess("Data refreshed");
    }

    public void start() {
        view.show();
    }

    public void shutdown() {
        System.out.println("Shutting down...");
        view.close();
    }
}
