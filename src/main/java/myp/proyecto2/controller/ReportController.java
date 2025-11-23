package myp.proyecto2.controller;

import java.util.List;
import myp.proyecto2.model.catalog.ReportCatalog;
//import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.Report;
import myp.proyecto2.model.service.ReportManager;

public class ReportController {
    private final ReportCatalog reportCatalog;
    private final ReportManager reportManager;

    public ReportController(ReportCatalog reportCatalog, ReportManager reportManager) {
        this.reportCatalog = reportCatalog;
        this.reportManager = reportManager;
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

    /*
    public List<Report> findReportsInArea(Location center, double radiusMeters) {
        List<Report> allReports = getActiveReports();
        return this.reportManager.isNearRoute(center, , radiusMeters);
    }

     */


}
