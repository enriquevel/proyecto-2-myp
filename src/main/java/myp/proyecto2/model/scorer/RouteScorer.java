package myp.proyecto2.model.scorer;

import java.util.List;
import myp.proyecto2.model.domain.builder.Route;
import myp.proyecto2.model.domain.Report;

/**
 * Clase abstracta para representar la plantilla de un calificador de rutas, usando el patron Template.
 */
public abstract class RouteScorer {

    /**
     * Define el score de la ruta tomando en cuenta los reportes que hay en ella.
     * 
     * @param route la ruta a calificar.
     * @param affectingReports los reportes que se encuentran a lo largo de la ruta.
     * @return el puntaje final en segundos de la ruta.
     */
    public double score(Route route, List<Report> affectingReports) {
        if (route == null)
            throw new NullPointerException("Cannot score a null route.");

        if (affectingReports == null) 
            throw new NullPointerException("A list of affecting reports needs to be provided.");
        
        double score = route.getTotalDurationSeconds();
        for (Report report : affectingReports) {
            if (report.isActive()) {
                double penalty = calculatePenalty(report);
                score += penalty;
            }
        }

        return score;
    }

    /**
     * Metodo abstracto para obtener el nombre del calificador de rutas.
     * 
     * @return el nombre del calificador de rutas.
     */
    public abstract String getName();

    /**
     * Metodo abstracto que calcula la penalizacion total para un reporte especifico.
     * 
     * @param report el reporte para el cual calcular la penalizacion.
     * @return la penalizacion en segundos que el reporte aporta al score total de la ruta.
     */
    public abstract double calculatePenalty(Report report);
}