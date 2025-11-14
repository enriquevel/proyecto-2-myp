package myp.proyecto2.model.scorer;

import java.util.List;
import myp.proyecto2.model.domain.builder.Route;
import myp.proyecto2.model.domain.Report;

public interface RouteScorerStrategy {

    double score(Route route, List<Report> affectingReports);

    String getName();

    double calculatePenalty(Report report);
}