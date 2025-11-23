package myp.proyecto2.controller;

import java.util.ArrayList;
import java.util.List;
import myp.proyecto2.model.catalog.ReportCatalog;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.Report;

public class ReportController {

    private final ReportCatalog reportCatalog;

    public ReportController(ReportCatalog reportCatalog) {
        this.reportCatalog = reportCatalog;
    }

    public void submitReport(Report report) {
        this.reportCatalog.save(report);
    }

    public void upvoteReport(Report report) {
        report.upvote();
        this.reportCatalog.update(report);
    }

    public void downvoteReport(Report report) {
        report.downvote();
        this.reportCatalog.update(report);
    }

    public void deleteReport(String reportId) {
        this.reportCatalog.delete(this.reportCatalog.findById(reportId));
    }

    public List<Report> getAllReports() {
        return this.reportCatalog.findAll();
    }

    public List<Report> getActiveReports() {
        return this.reportCatalog.findActive();
    }

    public List<Report> findReportsInArea(Location center, double radiusMeters) {
        List<Report> nearby = new ArrayList<>();

        for (Report report : getActiveReports()) {
            double distance = center.distanceTo(report.getLocation());
            if (distance <= radiusMeters)
                nearby.add(report);
        }

        return nearby;
    }

    public List<Report> findReportsNearRoute(List<Location> pathPoints, double thresholdMeters) {
        List<Report> nearby = new ArrayList<>();

        for (Report report : getActiveReports()) {
            if (isReportNearRoute(report, pathPoints, thresholdMeters))
                nearby.add(report);
        }

        return nearby;
    }

    /**
     * Check if report is near any point on route.
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
