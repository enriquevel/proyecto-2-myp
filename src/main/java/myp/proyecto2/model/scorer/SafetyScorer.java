package myp.proyecto2.model.scorer;

import java.util.Map;
import java.util.EnumMap;
import myp.proyecto2.model.domain.ReportType;

/**
 * Calificador de rutas enfocadas en seguridad, que priorizan este rubro.
 */
public class SafetyScorer extends AbstractRouteScorer {

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
        weights.put(ReportType.STREETLIGHT_OUT, 4.5);
        weights.put(ReportType.FLOODING, 3.0);
        weights.put(ReportType.NATURAL_DEBRIS, 2.0);
        weights.put(ReportType.CONSTRUCTION, 1.0);
        weights.put(ReportType.TRAFFIC_JAM, 0.5);       
        weights.put(ReportType.LOST_ITEM, 0.0);
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
     * Calcula el peso que le otorga el calificador al tipo especificado de reporte.
     *
     * @param reportType el tipo de reporte a evaluar.
     * @return el peso que le otorga el calificador al tipo de reporte.
     */
    @Override
    protected double getTypeWeight(ReportType reportType) {
        return this.weights.get(reportType);
    }
}