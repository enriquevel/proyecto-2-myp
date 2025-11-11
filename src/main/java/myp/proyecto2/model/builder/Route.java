package myp.proyecto2.model.builder;

import java.util.List;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.RouteSegment;

public class Route {

    private final String id;

    private final Location origin;

    private final Location destination;

    private final double totalDistance;

    private final int totalDurationSeconds;

    private final List<RouteSegment> segments;

    private final List<Location> pathPoints;

    public Route(String id, Location origin, Location destination, double totalDistance, double totalDurationSeconds,
                        List<RouteSegment> segments, List<Location> pathPoints) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.totalDistance = totalDistance;
        this.totalDurationSeconds = totalDurationSeconds;
        this.segments = segments;
        this.pathPoints = pathPoints;
    }

    public String getId() {
        return this.id;
    }

    public Location getOrigin() {
        return this.origin;
    }

    public Location getDestination() {
        return this.destination;
    }

    public double getTotalDistance() {
        return this.totalDistance;
    }

    public int getTotalDurationSeconds() {
        return this.totalDurationSeconds;
    }

    public List<RouteSegment> getSegments() {
        return this.segments;
    }

    public List<Location> getPathPoints() {
        return this.pathPoints;
    }

    public void displayRoute() {
    }

    public int getSegmentCount {
        return this.segments.size();
    }

    public int getTotalDurationMinutes() {
        return this.totalDurationSeconds / 60;
    }

    public double getTotalDistanceKilometers() {
        return this.totalDistance / 1000;
    }
}