package myp.proyecto2.model.service;
import java.util.List;

public class ReportManager {

    private ReportCatalog reportCatalog;

    public ReportManager(ReportCatalog reportCatalog) {
        this.reportCatalog = reportCatalog;
    }

    public Report submitReport(ReportType type, Location location, String description) {
        return null;
    }

    public List<Report> getActiveReports() {
        return null;
    }

    public List<Report> getReportById() {
        return null;
    }

    public List<Report> expireOldReports() {
        return null;
    }

    public List<Report> findReportsAffectingRoute(Route route, List<Report> allReports, double bufferMeters) {
        return null;
    }

    public boolean isNearRoute(Location reportLocation, List<Location> pathPoints, double bufferMeters) {
        return false;
    }
}