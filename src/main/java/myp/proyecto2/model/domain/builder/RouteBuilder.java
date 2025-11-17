package myp.proyecto2.model.domain.builder;

import java.util.List;
import java.util.Set;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.RouteSegment;
import myp.proyecto2.model.domain.TransportMode;

public interface RouteBuilder {

    RouteBuilder setId(String id);
    
    RouteBuilder setOrigin(Location origin);

    RouteBuilder setDestination(Location destination);

    RouteBuilder setDistance(double distance);

    RouteBuilder setDuration(int seconds);

    RouteBuilder setSegments(List<RouteSegment> segment);

    RouteBuilder setPathPoints(List<Location> point);

    RouteBuilder setTransportModes(Set<TransportMode> transportModes);

    Route build();
}