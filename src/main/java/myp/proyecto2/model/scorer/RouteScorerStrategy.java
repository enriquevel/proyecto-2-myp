package myp.proyecto2.model.scorer;

import java.util.List;

public interface RouteScorerStrategy {

    double score(Route route, List<Report> affectingReports);

    String getName();

    double calculatePenalty(Report report);
}