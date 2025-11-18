package myp.proyecto2.model.scorer;

import java.util.Map;
import java.util.EnumMap;
import myp.proyecto2.model.domain.Report;
import myp.proyecto2.model.domain.ReportType;

/**
 * Calificador de rutas enfocadas en seguridad, que priorizan este rubro.
 */
public class SafetyScorer extends RouteScorer {

    /** Mapa de pesos personalizados para cada tipo de reporte.*/
    private final Map<ReportType, Double> weights;

    /**
     * Constructor que inicializa los pesos especificos para seguridad.
     */
    public SafetyScorer() {
        this.weights = new EnumMap<>(ReportType.class);
        defineWeights();
    }

    /**
     * Metodo auxiliar que define el valor de los pesos, con un enfoque en seguridad.
     */
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

    /**
     * Devuelve el nombre del calificador de rutas.
     * 
     * @return el nombre del calificador de rutas.
     */
    @Override
    public String getName() {
        return "Safety scorer";
    }

    /**
     * Calcula la penalizacion para un reporte especifico, tomando en cuenta los pesos de seguridad.
     * 
     * @param report el reporte a evaluar.
     * @return la penalizacion en segundos que el reporte aporta al score de la ruta.
     */
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