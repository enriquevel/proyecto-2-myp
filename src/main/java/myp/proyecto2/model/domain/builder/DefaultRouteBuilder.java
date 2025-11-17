package myp.proyecto2.model.domain.builder;

import java.util.List;
import java.util.Set;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.RouteSegment;
import myp.proyecto2.model.domain.TransportMode;

public class DefaultRouteBuilder implements RouteBuilder {

    private String id;

    private Location origin;

    private Location destination;

    private double totalDistance;

    private int totalDurationSeconds;

    private List<RouteSegment> segments;

    private List<Location> pathPoints;

    private Set<TransportMode> transportModes;

    public DefaultRouteBuilder() {}
    
    @Override
    public RouteBuilder setId(String id) {
        if (id == null) 
            throw new NullPointerException("Route's ID cannot be null.");

        this.id = id;
        return this;
    }

    @Override
    public RouteBuilder setOrigin(Location origin) {
        if (origin == null) 
            throw new NullPointerException("Route's origin cannot be null.");

        this.origin = origin;
        return this;
    }

    @Override
    public RouteBuilder setDestination(Location destination) {
        if (destination == null) 
            throw new NullPointerException("Route's destination cannot be null.");
            
        this.destination = destination;
        return this;
    }

    @Override
    public RouteBuilder setDistance(double distance) {
        if (distance <= 0) 
            throw new IllegalArgumentException("Route's distance has to be positive.");

        this.totalDistance = distance;
        return this;
    }

    @Override
    public RouteBuilder setDuration(int seconds) {
        if (seconds <= 0)
            throw new IllegalArgumentException("Route's duration has to be positive.");

        this.totalDurationSeconds = seconds;
        return this;
    }

    @Override
    public RouteBuilder setSegments(List<RouteSegment> segments) {
        if (segments == null)
            throw new NullPointerException("Route segment cannot be null.");

        this.segments = segments;
        return this;
    }
    
    @Override
    public RouteBuilder setPathPoints(List<Location> pathPoints) {
        if (pathPoints == null)
            throw new NullPointerException("Path point cannot be null.");

        this.pathPoints = pathPoints;
        return this;
    }

    @Override
    public RouteBuilder setTransportModes(Set<TransportMode> modes) {
        if (modes == null)
            throw new NullPointerException("Transport modes cannot be null.");

        this.transportModes = modes;
        return this;
    }

    @Override
    public Route build() {
        validateCompleteBuild();
        return new Route(this.id, this.origin, this.destination, this.totalDistance,
                this.totalDurationSeconds, this.segments, this.pathPoints, this.transportModes);
    }

    private void validateCompleteBuild() {
        if (this.id == null)  
            throw new IllegalStateException("Cannot build route: ID is required");

        if (this.origin == null)  
            throw new IllegalStateException("Cannot build route: An origin is required");

        if (this.destination == null)  
            throw new IllegalStateException("Cannot build route: A destination is required");

        if (this.totalDistance == 0)
            throw new IllegalStateException("Cannot build route: A route distance is required");

        if (this.totalDurationSeconds == 0.0)
            throw new IllegalStateException("Cannot build route: A route duration is required");

        if (this.segments == null)
            throw new IllegalStateException("Cannot build route: No route segments have been added.");

        if (this.pathPoints == null)
            throw new IllegalStateException("Cannot build route: No path points have been added.");

        if (this.transportModes == null)
            throw new IllegalStateException("Cannot build route: No transport modes have been added.");
    }
}