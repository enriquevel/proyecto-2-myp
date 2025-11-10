package myp.proyecto2.model.service;

import java.util.List;
import java.util.Set;

public class RouteManager {

    private GoogleMapsRouteProvider routeProvider;
    private ReportManager reportManager;
    private ScorerFactory scorerFactory;

    public RouteManager(GoogleMapsRouteProvider routeProvider, ReportManager reportManager, ScorerFactory scorerFactory) {
        this.routeProvider = routeProvider;
        this.reportManager = reportManager;
        this.scorerFactory = scorerFactory;
    } 

    public List<ScoredRoute> findRoutes(Location to, Location from, Set<TransportMode> modes, RoutePreference preference) {

    }

    public List<ScoredRoute> scoreRoutes(List<Route> routes, List<Report> allReports, RouteScorerStrategy scorer) {

    }

}