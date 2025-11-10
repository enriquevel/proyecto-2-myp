package myp.proyecto2.model.domain;
import java.util.List;

public class ScoredRoute {
    
    private Route route;
    private double score;
    private List<Report> affectingReports;
    private String scoredBy;

    public ScoredRoute(Route route, double score, List<Report> affectingReports, String scoredBy) {
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
}