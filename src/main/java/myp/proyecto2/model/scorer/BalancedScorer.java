package myp.proyecto2.model.scorer;

import java.util.EnumMap;
import java.util.Map;
import myp.proyecto2.model.domain.ReportType;

/**
 * Calificador de rutas enfocadas en un balance entre seguridad y velocidad, que priorizan este balance.
 */
public class BalancedScorer extends AbstractRouteScorer {

    /** Mapa de pesos personalizados para cada tipo de reporte.*/
    private final Map<ReportType, Double> weights;

    /**
     * Constructor que inicializa los pesos especificos balanceados.
     */
    public BalancedScorer() {
        this.weights = new EnumMap<>(ReportType.class);
        defineWeights();
    }

    /**
     * Metodo auxiliar que define el valor de los pesos, con un enfoque en un balance entre seguridad/velocidad.
     */
    private void defineWeights() {
        weights.put(ReportType.CRIME_INCIDENT, 2.5);
        weights.put(ReportType.ACCIDENT, 2.5);
        weights.put(ReportType.TRAFFIC_JAM, 2.5);
        weights.put(ReportType.FLOODING, 2.5);        
        weights.put(ReportType.CONSTRUCTION, 2.0);    
        weights.put(ReportType.STREETLIGHT_OUT, 2.0); 
        weights.put(ReportType.NATURAL_DEBRIS, 1.5);  
        weights.put(ReportType.LOST_ITEM, 0.0);
        weights.put(ReportType.OTHER, 1.5); 
    }

    /**
     * Devuelve el nombre del calificador de rutas.
     * 
     * @return el nombre del calificador de rutas.
     */
    @Override
    public String getName() {
        return "Ruta balanceada";
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