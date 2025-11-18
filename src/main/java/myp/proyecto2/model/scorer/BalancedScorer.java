package myp.proyecto2.model.scorer;

import java.util.EnumMap;
import java.util.Map;
import myp.proyecto2.model.domain.Report;
import myp.proyecto2.model.domain.ReportType;

public class BalancedScorer extends RouteScorer {

    private final Map<ReportType, Double> weights;

    public BalancedScorer() {
        this.weights = new EnumMap<>(ReportType.class);
        defineWeights();
    }

    private void defineWeights() {
        weights.put(ReportType.CRIME_INCIDENT, 3.0);
        weights.put(ReportType.ACCIDENT, 3.0);        
        weights.put(ReportType.TRAFFIC_JAM, 3.0);     
        weights.put(ReportType.FLOODING, 2.5);        
        weights.put(ReportType.CONSTRUCTION, 2.0);    
        weights.put(ReportType.STREETLIGHT_OUT, 2.0); 
        weights.put(ReportType.NATURAL_DEBRIS, 1.5);  
        weights.put(ReportType.LOST_ITEM, 0.5);       
        weights.put(ReportType.OTHER, 1.5); 
    }

    @Override
    public String getName() {
        return "Balanced scorer";
    }

    @Override
    public double calculatePenalty(Report report) {
        if (report == null || !report.isActive()) {
            return 0.0;
        }

        ReportType type = report.getType();
        double basePenalty = type.getDefaultPenalty();
        double balancedWeight = weights.getOrDefault(type, 1.5);
        
        return basePenalty + (basePenalty * balancedWeight);
    }
}