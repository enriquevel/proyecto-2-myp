package myp.proyecto2.model.scorer;

import java.util.List;
import myp.proyecto2.model.domain.ReportType;
import myp.proyecto2.model.domain.builder.Route;
import myp.proyecto2.model.domain.Report;

/**
 * Clase abstracta para representar la plantilla de un calificador de rutas, usando el patron Template.
 */
public abstract class AbstractRouteScorer {

    /** Umbral de baja confianza de votos en un reporte. */
    protected static final int TRUST_THRESHOLD_LOW = -3;

    /** Umbral de alta confianza de votos en un reporte. */
    protected static final int TRUST_THRESHOLD_HIGH = 3;

    /**
     * Define el score de la ruta tomando en cuenta los reportes que hay en ella.
     * 
     * @param route la ruta a calificar.
     * @param affectingReports los reportes que se encuentran a lo largo de la ruta.
     * @return el puntaje final en segundos de la ruta.
     * @throws NullPointerException si la ruta o la lista de reportes son null.
     */
    public final double score(Route route, List<Report> affectingReports) {
        if (route == null)
            throw new NullPointerException("Cannot score a null route.");

        if (affectingReports == null) 
            throw new NullPointerException("A list of affecting reports needs to be provided.");
        
        double baseScore = calculateBaseScore(route);
        double totalPenalty = 0.0;

        for (Report report : affectingReports) {
            if (report.isActive())
                totalPenalty += calculatePenalty(report);
        }

        return baseScore + totalPenalty;
    }

    /**
     * Metodo hook para calcular el score base de una ruta.
     * Puede ser sobrescrito por los herederos de esta clase, pero no es necesario.
     *
     * @param route la ruta a calificar.
     * @return el score base de la ruta calificada.
     * @throws NullPointerException si la ruta es null.
     */
    protected double calculateBaseScore(Route route) {
        if (route == null)
            throw new NullPointerException("Cannot score a null route.");
        return route.getTotalDurationMinutes();
    }

    /**
     * Metodo abstracto primitivo para obtener el nombre del calificador de rutas.
     * 
     * @return el nombre del calificador de rutas.
     */
    public abstract String getName();

    /**
     * Metodo final auxiliar que calcula la penalizacion total para un reporte especifico.
     * 
     * @param report el reporte para el cual calcular la penalizacion.
     * @return la penalizacion que el reporte aporta al score total de la ruta.
     */
    protected final double calculatePenalty(Report report) {
        double basePenalty = report.getType().getDefaultPenalty();
        double typeWeight = getTypeWeight(report.getType());
        double trustFactor = calculateTrustFactor(report);

        return basePenalty * typeWeight * trustFactor;
    }

    /**
     * Metodo abstracto primitivo para calcular el peso que le otorga cada calificador a
     * los distintos tipos de reporte.
     *
     * @param reportType el tipo de reporte que se desea conocer su peso.
     * @return el peso del tipo del reporte.
     */
    protected abstract double getTypeWeight(ReportType reportType);

    /**
     * Metodo final auxiliar que calcula el factor de confianza de un reporte con base
     * en sus votos.
     *
     * @param report el reporte para el cual calcular la penalizacion.
     * @return la penalizacion de confianza que aporta a la penalizacion total.
     */
    protected final double calculateTrustFactor(Report report) {
        int netVotes = report.getNetVotes();

        if (netVotes < TRUST_THRESHOLD_LOW)
            return 0.5;

        if (netVotes > TRUST_THRESHOLD_HIGH)
            return 1.5;

        return 1.0 + (netVotes * 0.1);
    }
}