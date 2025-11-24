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
     * Busca y puntua rutas desde un origen hasta un destino segun el modo de transporte,
     * reportes disponibles y la preferencia del usuario.
     *
     * @param to la ubicacion de destino
     * @param from la ubicacion de origen
     * @param mode el modo de transporte
     * @param reports lista de reportes que afectan las rutas
     * @param preference la preferencia del usuario
     * @return una lista de rutas puntuadas ordenadas segun la preferencia del usuario
     * @throws IllegalArgumentException si no se encuentran rutas disponibles
     */
    public List<ScoredRoute> findAndScoreRoutes(Location to, Location from, TransportMode mode, List<Report> reports,
                                                RoutePreference preference) {
        List<Route> rawRoutes = this.provider.getRoutes(to, from, mode);

        if (rawRoutes.isEmpty())
            throw new IllegalArgumentException("No routes found");

        List<ScoredRoute> scoredRoutes = new ArrayList<>();
        AbstractRouteScorer scorer = ScorerFactory.createScorer(preference);

        for (Route route : rawRoutes) {
            double score = scorer.score(route, reports);
            scoredRoutes.add(new ScoredRoute(route, score, reports, scorer.getName()));
        }

        return sortByPreference(scoredRoutes, preference);
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
                sorted.sort(Comparator.comparingDouble(ScoredRoute::getScore).reversed());
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
