package myp.proyecto2.model.scorer;

import java.util.Map;
import java.util.List;
import myp.proyecto2.model.builder.Route;
import myp.proyecto2.model.domain.Report;

public class BalancedScorerStrategy implements RouteScorerStrategy {

    private Map<ReportType, Double> weights;

    @Override
    public double score(Route route, List<Report> affectingReports) {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public double calculatePenalty(Report report) {
        return 0;
    }
}