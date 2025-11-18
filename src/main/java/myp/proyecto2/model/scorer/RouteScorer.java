package myp.proyecto2.model.scorer;

import java.util.List;
import myp.proyecto2.model.domain.builder.Route;
import myp.proyecto2.model.domain.Report;

public abstract class RouteScorer {

    public double score(Route route, List<Report> affectingReports) {
        if (route == null)
            throw new NullPointerException("Cannot score a null route.");

        if (affectingReports == null) 
            throw new NullPointerException("A list of affecting reports needs to be provided.");
        
        double score = route.getTotalDurationSeconds();
        for (Report report : affectingReports) {
            if (report.isActive()) {
                double penalty = calculatePenalty(report);
                score += penalty;
            }
        }

        return score;
    }

    public abstract String getName();

    public abstract double calculatePenalty(Report report);
}