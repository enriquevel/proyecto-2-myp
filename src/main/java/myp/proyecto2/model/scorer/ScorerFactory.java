package myp.proyecto2.model.scorer;

import myp.proyecto2.model.domain.RoutePreference;

public class ScorerFactory {

    private ScorerFactory() {}

    public static AbstractRouteScorer createScorer(RoutePreference routePreference) {
        return switch (routePreference) {
            case SAFEST -> new SafetyScorer();
            case FASTEST -> new SpeedScorer();
            case BALANCED -> new BalancedScorer();
        };
    }
}
