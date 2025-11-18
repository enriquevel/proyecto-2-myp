package myp.proyecto2.model.scorer;

import java.util.Map;
import java.util.EnumMap;
import myp.proyecto2.model.domain.Report;
import myp.proyecto2.model.domain.ReportType;

public class SafetyScorer extends RouteScorer {

    private final Map<ReportType, Double> weights;

    public SafetyScorer() {
        this.weights = new EnumMap<>(ReportType.class);
        defineWeights();
    }

    private void defineWeights() {
        weights.put(ReportType.CRIME_INCIDENT, 5.0);
        weights.put(ReportType.ACCIDENT, 4.0);          
        weights.put(ReportType.STREETLIGHT_OUT, 4.0);   
        weights.put(ReportType.FLOODING, 2.5);          
        weights.put(ReportType.NATURAL_DEBRIS, 1.5);    
        weights.put(ReportType.CONSTRUCTION, 0.8);      
        weights.put(ReportType.TRAFFIC_JAM, 0.5);       
        weights.put(ReportType.LOST_ITEM, 0.1);         
        weights.put(ReportType.OTHER, 1.0);
    }

    @Override
    public String getName() {
        return "Safety scorer";
    }

    @Override
    public double calculatePenalty(Report report) {
        if (report == null || !report.isActive()) {
            return 0.0;
        }

        ReportType type = report.getType();
        double basePenalty = type.getDefaultPenalty();
        double safetyWeight = weights.getOrDefault(type, 1.0);
       
        return basePenalty + (basePenalty * safetyWeight);
    }
}