package myp.proyecto2.model.service;

import java.util.ArrayList;
import java.util.List;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.Report;
import myp.proyecto2.model.domain.builder.Route;

public class ReportManager {

    private static final double DEFAULT_BUFFER_METERS = 100.0;

    public ReportManager() {}

    public List<Report> findReportsAffectingRoute(Route route, List<Report> allReports) {
        List<Report> affectingReports = new ArrayList<>();
        List<Location> polyline = route.getPathPoints();

        if (polyline.isEmpty())
            return affectingReports;

        for (Report report : allReports) {
            if (!report.isActive())
                continue;

            if (isNearRoute(report.getLocation(), polyline, DEFAULT_BUFFER_METERS))
                affectingReports.add(report);
        }

        return affectingReports;

    }

    public boolean isNearRoute(Location reportLocation, List<Location> pathPoints, double bufferMeters) {
        if (pathPoints.isEmpty())
            return false;

        double minDistance = calculateMinimumDistance(reportLocation, pathPoints);
        return minDistance <= bufferMeters;
    }

    public double calculateMinimumDistance(Location point, List<Location> pathPoints) {
        if (pathPoints.isEmpty())
            return Double.MAX_VALUE;

        if (pathPoints.size() == 1)
            return point.distanceTo(pathPoints.getFirst());

        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < pathPoints.size() - 1; i++) {
            Location segmentStart = pathPoints.get(i);
            Location segmentEnd = pathPoints.get(i + 1);

            double distance = distanceToSegment(point, segmentStart, segmentEnd);
            minDistance = Math.min(minDistance, distance);
        }

        return minDistance;
    }


    public double distanceToSegment(Location point, Location segmentStart, Location segmentEnd) {
        if (segmentStart.equals(segmentEnd))
            return point.distanceTo(segmentStart);

        double dx = segmentEnd.getLongitude() - segmentStart.getLongitude();
        double dy = segmentEnd.getLatitude() - segmentStart.getLatitude();

        double px = point.getLongitude() - segmentStart.getLongitude();
        double py = point.getLatitude() - segmentStart.getLatitude();

        // Producto punto para calcular la distancia
        double dotProduct = px * dx + py * dy;
        double segmentLengthSq = dx * dx + dy * dy;

        double t = dotProduct / segmentLengthSq;
        Location closestPoint;

        if (t <= 0)
            closestPoint = segmentStart;
        else if (t >= 1)
            closestPoint = segmentEnd;
        else {
            double projLat = segmentStart.getLatitude() + t * dy;
            double projLon = segmentStart.getLongitude() + t * dx;
            closestPoint = new Location(projLat, projLon);
        }

        return point.distanceTo(closestPoint);
    }
}
