package myp.proyecto2.model.scorer;

import java.util.Map;
import java.util.List;

public class SafetyScorerStrategy implements RouteScorerStrategy {

    private Map<ReportType, Double> weights;


    public double score(Route route, List<Report> affectingReports) {
        return 0;
    }

    public String getName() {
        return null;
    }

    public double calculatePenalty(Report report) {
        return 0;
    }
}