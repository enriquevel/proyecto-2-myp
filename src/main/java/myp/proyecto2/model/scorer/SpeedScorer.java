package myp.proyecto2.model.scorer;

import java.util.Map;
import java.util.EnumMap;
import myp.proyecto2.model.domain.Report;
import myp.proyecto2.model.domain.ReportType;

public class SpeedScorer extends RouteScorer {

    private final Map<ReportType, Double> weights;

    public SpeedScorer() {
        this.weights = new EnumMap<>(ReportType.class);
        defineWeights();
    }

    private void defineWeights() {
        weights.put(ReportType.TRAFFIC_JAM, 5.0);
        weights.put(ReportType.CONSTRUCTION, 4.5);
        weights.put(ReportType.FLOODING, 3.0);
        weights.put(ReportType.ACCIDENT, 3.0);
        weights.put(ReportType.NATURAL_DEBRIS, 2.5);
        weights.put(ReportType.CRIME_INCIDENT, 0.5);
        weights.put(ReportType.LOST_ITEM, 0.0);
        weights.put(ReportType.OTHER, 1.0);
    }

    @Override
    public String getName() {
        return "Speed scorer";
    }

    @Override
    public double calculatePenalty(Report report) {
        if (report == null || !report.isActive()) {
            return 0.0;
        }

        ReportType type = report.getType();

        double basePenalty = type.getDefaultPenalty();
        double speedWeight = weights.getOrDefault(type, 1.0);
         
        return basePenalty + (basePenalty * speedWeight);
    }
}