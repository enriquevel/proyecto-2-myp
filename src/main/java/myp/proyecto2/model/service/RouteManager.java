package myp.proyecto2.model.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import myp.proyecto2.model.domain.*;
import myp.proyecto2.model.domain.builder.Route;
import myp.proyecto2.model.provider.RouteProvider;
import myp.proyecto2.model.scorer.AbstractRouteScorer;

public class RouteManager {

    private RouteProvider routeProvider;
    private AbstractRouteScorer routeScorer;

    public RouteManager(RouteProvider routeProvider, AbstractRouteScorer routeScorer) {
        this.routeProvider = routeProvider;
        this.routeScorer = routeScorer;
    } 

    public List<ScoredRoute> findAndScoreRoutes(Location to, Location from, TransportMode mode, List<Report> reports) {
        // Delegate to strategy
        List<Route> rawRoutes = this.routeProvider.getRoutes(to, from, mode);

        if (rawRoutes.isEmpty())
            throw new IllegalArgumentException("No routes found");

        List<ScoredRoute> scoredRoutes = new ArrayList<>();

        for (Route route : rawRoutes) {
            double score = this.routeScorer.score(route, reports);
            scoredRoutes.add(new ScoredRoute(route, score, reports, this.routeScorer.getName()));
        }

        return scoredRoutes;
    }

    public List<ScoredRoute> sortByPreference(List<ScoredRoute> routes, RoutePreference preference) {
        switch (preference) {
            case FASTEST: {
                List<ScoredRoute> sorted = new ArrayList<>(routes);
                sorted.sort(Comparator.comparingDouble(sr -> sr.getRoute().getTotalDurationMinutes()));
                return sorted;
            }
            case SAFEST: {
                List<ScoredRoute> sorted = new ArrayList<>(routes);
                sorted.sort(Comparator.comparingDouble(ScoredRoute::getScore).reversed());
                return sorted;
            }
            case BALANCED:
                return routes;
            default:
                throw new IllegalArgumentException("Unknown preference: " + preference);
        }

    }

    public void setProvider(RouteProvider provider) {
        this.routeProvider = provider;
    }

    public void setScorer(AbstractRouteScorer scorer) {
        this.routeScorer = scorer;
    }

    public RouteProvider getProvider() {
        return this.routeProvider;
    }

    public AbstractRouteScorer getScorer() {
        return this.routeScorer;
    }


}