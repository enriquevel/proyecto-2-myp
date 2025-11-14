package myp.proyecto2.model.builder;

import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.RouteSegment;

public interface RouteBuilder {

    RouteBuilder setId(String id);
    
    RouteBuilder setOrigin(Location origin);

    RouteBuilder setDestination(Location destination);

    RouteBuilder setDistance(double distance);

    RouteBuilder setDuration(int seconds);

    RouteBuilder addSegment(RouteSegment segment);

    RouteBuilder addPathPoint(Location point);

    Route build();
}