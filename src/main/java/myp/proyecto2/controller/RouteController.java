package myp.proyecto2.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import myp.proyecto2.model.domain.*;
import myp.proyecto2.model.domain.builder.Route;
import myp.proyecto2.model.provider.RouteProvider;
import myp.proyecto2.model.scorer.AbstractRouteScorer;

public class RouteController {

    private RouteProvider provider;

    public RouteController(RouteProvider provider) {
        this.provider = provider;
    }

    public List<ScoredRoute> findAndScoreRoutes(Location to, Location from, TransportMode mode, List<Report> reports,
                                                RoutePreference preference, AbstractRouteScorer scorer) {
        List<Route> rawRoutes = this.provider.getRoutes(to, from, mode);

        if (rawRoutes.isEmpty())
            throw new IllegalArgumentException("No routes found");

        List<ScoredRoute> scoredRoutes = new ArrayList<>();

        for (Route route : rawRoutes) {
            double score = scorer.score(route, reports);
            scoredRoutes.add(new ScoredRoute(route, score, reports, scorer.getName()));
        }

        return sortByPreference(scoredRoutes, preference);
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
        this.provider = provider;
    }

    public RouteProvider getProvider() {
        return this.provider;
    }
}
