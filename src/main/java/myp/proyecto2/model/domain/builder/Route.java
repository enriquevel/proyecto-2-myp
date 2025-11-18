package myp.proyecto2.model.domain.builder;

import java.util.List;
import java.util.Set;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.RouteSegment;
import myp.proyecto2.model.domain.TransportMode;

public class Route {

    private final String id;

    private final Location origin;

    private final Location destination;

    private final double totalDistance;

    private final int totalDurationSeconds;

    private final List<RouteSegment> segments;

    private final List<Location> pathPoints;

    private final Set<TransportMode> transportModes;

    Route(String id, Location origin, Location destination, double totalDistance, int totalDurationSeconds, List<RouteSegment> segments,
            List<Location> pathPoints, Set<TransportMode> transportModes) {
        if (id == null) 
            throw new NullPointerException("Route's ID cannot be null.");

        if (origin == null) 
            throw new NullPointerException("Route's origin cannot be null.");
        
        if (destination == null)
            throw new NullPointerException("Route's destination cannot be null.");

        if (totalDistance <= 0)
            throw new IllegalArgumentException("Route's total distance needs to be a positive value.");

        if (totalDurationSeconds <= 0)
            throw new IllegalArgumentException("Route's total duration needs to be a positive value.");

        if (segments == null) 
            throw new NullPointerException("Route's segments cannot be null.");

        if (pathPoints == null)
            throw new NullPointerException("Route's path points cannot be null.");

        if (transportModes == null)
            throw new NullPointerException("Route's transport mode cannot be null.");
    
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.totalDistance = totalDistance;
        this.totalDurationSeconds = totalDurationSeconds;
        this.segments = segments;
        this.pathPoints = pathPoints;
        this.transportModes = transportModes;
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

    public Set<TransportMode> getTransportModes() {
        return this.transportModes;
    }

    public void displayRoute() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\nDisplaying route's details:\n")
            .append("\n\nOrigin:\n").append(this.origin)
            .append("\n\nDestination:\n").append(this.destination)
            .append("\n\nTotal distance:\n").append(this.totalDistance).append(" meters.")
            .append("\n\nTotal duration:\n").append(this.totalDurationSeconds).append(" seconds");
    }

    public int getSegmentCount() {
        return this.segments.size();
    }

    public int getTotalDurationMinutes() {
        return this.totalDurationSeconds / 60;
    }

    public double getTotalDistanceKilometers() {
        return this.totalDistance / 1000.0;
    }
}