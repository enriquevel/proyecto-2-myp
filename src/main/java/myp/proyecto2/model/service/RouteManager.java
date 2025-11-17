package myp.proyecto2.model.service;

import java.util.List;
import java.util.Set;
import myp.proyecto2.model.builder.Route;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.Report;
import myp.proyecto2.model.domain.RoutePreference;
import myp.proyecto2.model.domain.ScoredRoute;
import myp.proyecto2.model.domain.TransportMode;
import myp.proyecto2.model.scorer.RouteScorerStrategy;

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