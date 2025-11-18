package myp.proyecto2.model.domain;

import myp.proyecto2.model.domain.builder.Route;
import java.util.List;

public class ScoredRoute implements Comparable<ScoredRoute> {
    
    private Route route;
    private double score;
    private List<Report> affectingReports;
    private String scoredBy;

    public ScoredRoute(Route route, double score, List<Report> affectingReports, String scoredBy) {
        if (route == null)
            throw new NullPointerException("The route cannot be null.");
        
        if (score < 0)
            throw new IllegalArgumentException("The route's score needs to be positive.");
        
        if (affectingReports == null)
            throw new NullPointerException("Route's affecting reports cannot be null.");

        if (scoredBy == null)
            throw new NullPointerException("Route's scorer cannot be null.");

        this.route = route;
        this.score = score;
        this.affectingReports = affectingReports;
        this.scoredBy = scoredBy;
    }

    public Route getRoute() {
        return this.route;
    }

    public double getScore() {
        return this.score;
    }

    public List<Report> getAffectingReports() {
        return this.affectingReports;
    }

    public String getScorer() {
        return this.scoredBy;
    }

    public int getReportCount() {
        return this.affectingReports.size();
    }

    public boolean hasReports() {
        return !this.affectingReports.isEmpty();
    }

    @Override
    public int compareTo(ScoredRoute other) {
        return Double.compare(this.score, other.score);
    }
}