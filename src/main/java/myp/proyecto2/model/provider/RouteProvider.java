package myp.proyecto2.model.provider;

import java.util.List;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.TransportMode;
import myp.proyecto2.model.domain.builder.Route;

public interface RouteProvider {

    List<Route> getRoutes(Location from, Location to, TransportMode mode);

    String getProviderName();
}
