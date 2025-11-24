package myp.proyecto2.model.scorer;

import java.util.Map;
import java.util.EnumMap;
import myp.proyecto2.model.domain.ReportType;

/**
 * Calificador de rutas enfocadas en velocidad, que priorizan este rubro.
 */
public class SpeedScorer extends AbstractRouteScorer {

    /** Mapa de pesos personalizados para cada tipo de reporte.*/
    private final Map<ReportType, Double> weights;

    /**
     * Constructor que inicializa los pesos especificos para velocidad.
     */
    public SpeedScorer() {
        this.weights = new EnumMap<>(ReportType.class);
        defineWeights();
    }

    /**
     * Metodo auxiliar que define el valor de los pesos, con un enfoque en velocidad.
     */
    private void defineWeights() {
        weights.put(ReportType.TRAFFIC_JAM, 5.0);
        weights.put(ReportType.CONSTRUCTION, 4.0);
        weights.put(ReportType.FLOODING, 4.5);
        weights.put(ReportType.ACCIDENT, 3.0);
        weights.put(ReportType.STREETLIGHT_OUT, 0.5);
        weights.put(ReportType.NATURAL_DEBRIS, 2.5);
        weights.put(ReportType.CRIME_INCIDENT, 0.5);
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
        return "Ruta mas rapida";
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