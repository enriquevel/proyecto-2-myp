package myp.proyecto2.controller;

import java.util.ArrayList;
import java.util.List;
import myp.proyecto2.model.catalog.ReportCatalog;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.Report;

/**
 * Controlador para gestionar reportes de incidentes en el sistema.
 * Esta clase proporciona funcionalidad para crear, actualizar, eliminar y consultar
 * reportes generados por usuarios. Tambien permite buscar reportes cercanos a una
 * ubicacion especifica o a lo largo de una ruta.
 */
public class ReportController {

    /** El catalogo donde se almacenan los reportes. */
    private final ReportCatalog reportCatalog;

    /**
     * Construye un nuevo controlador de reportes con el catalogo especificado.
     *
     * @param reportCatalog el catalogo de reportes que se utilizara para el almacenamiento
     *                      y recuperacion de datos
     */
    public ReportController(ReportCatalog reportCatalog) {
        this.reportCatalog = reportCatalog;
    }

    /**
     * Registra un nuevo reporte en el sistema. El reporte es guardado en el
     * catalogo y queda disponible para consultas y evaluacion de rutas.
     *
     * @param report el reporte a registrar en el sistema
     */
    public void submitReport(Report report) {
        this.reportCatalog.save(report);
    }

    /**
     * Incrementa el conteo de votos positivos de un reporte. Los votos positivos
     * indican que otros usuarios confirman o apoyan la veracidad del reporte.
     * El reporte actualizado se persiste en el catalogo.
     *
     * @param report el reporte al que se le dara voto positivo
     */
    public void upvoteReport(Report report) {
        report.upvote();
        this.reportCatalog.update(report);
    }

    /**
     * Incrementa el conteo de votos negativos de un reporte. Los votos negativos
     * indican que otros usuarios cuestionan la validez o actualidad del reporte.
     * El reporte actualizado se persiste en el catalogo.
     *
     * @param report el reporte al que se le dara voto negativo
     */
    public void downvoteReport(Report report) {
        report.downvote();
        this.reportCatalog.update(report);
    }

    /**
     * Elimina un reporte del sistema usando su identificador. El reporte es
     * buscado por su ID y luego eliminado del catalogo.
     *
     * @param reportId el identificador unico del reporte a eliminar
     */
    public void deleteReport(String reportId) {
        this.reportCatalog.delete(this.reportCatalog.findById(reportId));
    }

    /**
     * Obtiene todos los reportes almacenados en el sistema. Esto incluye tanto
     * reportes activos como inactivos o expirados.
     *
     * @return una lista con todos los reportes del sistema
     */
    public List<Report> getAllReports() {
        return this.reportCatalog.findAll();
    }

    /**
     * Obtiene unicamente los reportes activos del sistema. Los reportes activos
     * son aquellos que estan vigentes y deben considerarse para la evaluacion de
     * rutas. Los reportes expirados o inactivos son excluidos.
     *
     * @return una lista con todos los reportes activos
     */
    public List<Report> getActiveReports() {
        return this.reportCatalog.findActive();
    }

    /**
     * Encuentra todos los reportes activos dentro de un area circular. Busca
     * reportes cuya ubicacion este dentro del radio especificado desde un punto
     * central. Util para conocer incidentes en una zona especifica.
     *
     * @param center el punto central desde donde se mide la distancia
     * @param radiusMeters el radio en metros que define el area de busqueda
     * @return una lista de reportes activos dentro del area especificada
     */
    public List<Report> findReportsInArea(Location center, double radiusMeters) {
        List<Report> nearby = new ArrayList<>();

        for (Report report : getActiveReports()) {
            double distance = center.distanceTo(report.getLocation());
            if (distance <= radiusMeters)
                nearby.add(report);
        }

        return nearby;
    }

    /**
     * Encuentra todos los reportes activos cercanos a una ruta especifica. Busca
     * reportes que esten a una distancia menor o igual al umbral especificado de
     * cualquier punto de la ruta. Esto permite identificar incidentes que puedan
     * afectar el recorrido de la ruta.
     *
     * @param pathPoints lista de puntos que definen la ruta a evaluar
     * @param thresholdMeters distancia maxima en metros para considerar un reporte
     *                        como "cercano" a la ruta
     * @return una lista de reportes activos cercanos a la ruta
     */
    public List<Report> findReportsNearRoute(List<Location> pathPoints, double thresholdMeters) {
        List<Report> nearby = new ArrayList<>();

        for (Report report : getActiveReports()) {
            if (isReportNearRoute(report, pathPoints, thresholdMeters))
                nearby.add(report);
        }

        return nearby;
    }

    /**
     * Verifica si un reporte esta cerca de algun punto de una ruta. Itera sobre
     * todos los puntos de la ruta y calcula la distancia del reporte a cada punto.
     * Si alguna distancia es menor o igual al umbral, el reporte se considera
     * cercano a la ruta.
     *
     * @param report el reporte a evaluar
     * @param pathPoints lista de puntos que conforman la ruta
     * @param thresholdMeters distancia maxima en metros para considerar cercania
     * @return true si el reporte esta cerca de algun punto de la ruta, false en caso contrario
     */
    private boolean isReportNearRoute(Report report, List<Location> pathPoints, double thresholdMeters) {
        Location reportLoc = report.getLocation();

        for (Location point : pathPoints) {
            double distance = reportLoc.distanceTo(point);
            if (distance <= thresholdMeters)
                return true;
        }

        return false;
    }


}
