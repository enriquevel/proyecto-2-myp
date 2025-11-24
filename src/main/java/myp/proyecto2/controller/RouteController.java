package myp.proyecto2.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import myp.proyecto2.model.domain.*;
import myp.proyecto2.model.domain.builder.Route;
import myp.proyecto2.model.provider.RouteProvider;
import myp.proyecto2.model.scorer.AbstractRouteScorer;
import myp.proyecto2.model.scorer.ScorerFactory;

/**
 * Controlador para gestionar la busqueda, puntuacion y ordenamiento de rutas.
 * Se encarga de obtener rutas, calcularles puntuaciones basadas en preferencias del usuario
 * y ordenarlas segun diferentes criterios.
 */
public class RouteController {

    /** El proveedor de rutas. */
    private RouteProvider provider;

    /**
     * Construye un nuevo controlador de rutas con el proveedor especificado.
     *
     * @param provider el proveedor de rutas que se utilizara para obtener las rutas
     */
    public RouteController(RouteProvider provider) {
        this.provider = provider;
    }

    /**
     * Encuentra las rutas de un lugar de origen a otro de destino con el proveedor escogido.
     *
     * @param origin el punto de origen de la ruta
     * @param destination el punto de destino de la ruta
     * @param mode el modo de transporte
     * @return una lista con las rutas provistas
     */
    public List<Route> findRoutes(Location origin, Location destination, TransportMode mode) {
        List<Route> routes = provider.getRoutes(origin, destination, mode);

        if (routes.isEmpty())
            throw new IllegalArgumentException("No routes found");

        return routes;
    }

    /**
     * Puntua una ruta con base en los reportes que tiene cercanos a ella.
     *
     * @param route la ruta a puntuar
     * @param nearbyReports los reportes a tomar un cuenta
     * @return una ruta puntuada con los reportes dados
     */
    public ScoredRoute scoreRoute(Route route, List<Report> nearbyReports, RoutePreference routePreference) {
        ScorerFactory scorerFactory = new ScorerFactory();
        AbstractRouteScorer scorer = scorerFactory.createScorer(routePreference);
        double score = scorer.score(route, nearbyReports);
        return new ScoredRoute(route, score, nearbyReports, scorer.getName());
    }

    /**
     * Ordena una lista de rutas puntuadas segun la preferencia especificada.
     *
     * @param routes la lista de rutas puntuadas a ordenar
     * @param preference la preferencia de ordenamiento
     * @return una nueva lista con las rutas ordenadas segun la preferencia
     * @throws IllegalArgumentException si la preferencia no es reconocida
     */
    public List<ScoredRoute> sortByPreference(List<ScoredRoute> routes, RoutePreference preference) {
        switch (preference) {
            case FASTEST: {
                List<ScoredRoute> sorted = new ArrayList<>(routes);
                sorted.sort(Comparator.comparingDouble(sr -> sr.getRoute().getTotalDurationMinutes()));
                return sorted;
            }
            case SAFEST: {
                List<ScoredRoute> sorted = new ArrayList<>(routes);
                sorted.sort(Comparator.comparingDouble(ScoredRoute::getScore));
                return sorted;
            }
            case BALANCED:
                return routes;
            default:
                throw new IllegalArgumentException("Unknown preference: " + preference);
        }
    }

    /**
     * Establece el proveedor de rutas a utilizar por este controlador.
     *
     * @param provider el nuevo proveedor de rutas
     */
    public void setProvider(RouteProvider provider) {
        this.provider = provider;
    }

    /**
     * Obtiene el proveedor de rutas actual utilizado por este controlador.
     *
     * @return el proveedor de rutas actual
     */
    public RouteProvider getProvider() {
        return this.provider;
    }
}
