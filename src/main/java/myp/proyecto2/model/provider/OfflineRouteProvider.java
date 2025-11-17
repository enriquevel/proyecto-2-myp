package myp.proyecto2.model.provider;

import java.util.List;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.TransportMode;
import myp.proyecto2.model.domain.builder.Route;

public class OfflineRouteProvider implements RouteProvider {

    /**
     * @param from 
     * @param to
     * @param mode
     * @return
     */
    @Override
    public List<Route> getRoutes(Location from, Location to, TransportMode mode) {
        return List.of();
    }

    /**
     * @return 
     */
    @Override
    public String getProviderName() {
        return "Offline";
    }
}
