package myp.proyecto2.controller;

import myp.proyecto2.model.catalog.POICatalog;
import myp.proyecto2.model.catalog.ReportCatalog;
import myp.proyecto2.model.domain.PointOfInterest;
import myp.proyecto2.model.domain.Report;
import myp.proyecto2.model.provider.RouteProvider;
import myp.proyecto2.model.provider.RouteProviderFactory;
import myp.proyecto2.view.View;

import java.util.List;

public class ApplicationController {

    private final View view;
    private final RouteController routeController;
    private final ReportController reportController;
    private final POIController poiController;

    public ApplicationController(View view, boolean openSource, String apiKey) {
        this.view = view;
        System.out.println("Inicializando aplicacion.");

        RouteProvider routeProvider = RouteProviderFactory.createRouteProvider(openSource, apiKey);
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
                request.modes,
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
}
