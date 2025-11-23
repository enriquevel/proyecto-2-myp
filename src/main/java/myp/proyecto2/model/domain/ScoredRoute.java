package myp.proyecto2.model.domain;

import myp.proyecto2.model.domain.builder.Route;
import java.util.List;

/**
 * Clase que define a las rutas puntuadas. Las rutas puntuadas son en general una ruta
 * mas informacion relevante asociada a dicha ruta, en especifico: la puntuacion de la ruta, 
 * el metodo que se utiliza para calcular dicha puntuacion y una lista de reportes de puntos de la ruta.
 */
public class ScoredRoute implements Comparable<ScoredRoute> {
    
    /** Ruta. */
    private Route route;

    /** Puntuacion de la ruta. */
    private double score;

    /** Lista de reportes asociados a la ruta. Para ser claros, a puntos de la ruta. */
    private List<Report> affectingReports;

    /** Metodo que se uso para calcular la puntuacion de la ruta. */
    private String scoredBy;

    /**
     * Constructor principal de la clase {@link ScoredRoute}. Permite 
     * crear una ruta puntuada.
     *
     * @param route ruta de donde se va a obtener la informacion.
     * @param score puntuacion de la ruta.
     * @param affectingReports lista de reportes asociados a la ruta.
     * @param scoredBy manera en que se calculo la puntuacion de la ruta.
     * @throws NullPointerException si la ruta, la lista de reportes, o las puntuaciones son <code>null</code>.
     * @throws IllegalArgumentException si la puntuacion de la ruta es menor que cero.
     */
    public ScoredRoute(Route route, double score, List<Report> affectingReports, String scoredBy) {
        if (route == null)
            throw new NullPointerException("The route cannot be null.");
        
        if (score < 0)
            throw new IllegalArgumentException("The route's score needs to be positive.");
        
        if (affectingReports == null)
            throw new NullPointerException("Route's affecting reports cannot be null.");

        if (scoredBy == null)
            throw new NullPointerException("Route's scorer cannot be null.");

        this.route = route;
        this.score = score;
        this.affectingReports = affectingReports;
        this.scoredBy = scoredBy;
    }
    
    /**
     * Regresa la ruta.
     *
     * @return la ruta.
     */
    public Route getRoute() {
        return this.route;
    }

    /**
     * Regresa la puntuacion que se calculo con la ruta.
     *
     * @return la puntuacion que se calculo con la ruta.
     */
    public double getScore() {
        return this.score;
    }

    /**
     * Regresa la lista de reportes asociados a la ruta.
     *
     * @return la lista de reportes asociados a la ruta.
     */
    public List<Report> getAffectingReports() {
        return this.affectingReports;
    }

    /**
     * Regresa la manera en la que se calculo la puntuacion de la ruta.
     *
     * @return la manera en la que se calculo la puntuacion de la ruta.
     */
    public String getScorer() {
        return this.scoredBy;
    }

    /**
     * Regresa el numero de reportes asociados a la ruta.
     *
     * @return el numero de reportes asociados a la ruta.
     */
    public int getReportCount() {
        return this.affectingReports.size();
    }

    /**
     * Nos dice si en la ruta existen reportes.
     *
     * @return <code>true</code> si la lista de reportes de la ruta no esta vacia,
     *          <code>false</code> en caso contrario.
     */
    public boolean hasReports() {
        return !this.affectingReports.isEmpty();
    }

    /**
     * Permite comparar a dos rutas puntuadas; nos dice cual de las dos rutas puntuadas es mayor. 
     * Esto se calcula con base en sus puntuaciones.
     *
     * @param other ruta puntuada con la que se va a comparar.
     * @return 0 si ambas rutas tienen la misma puntuacion. Un valor positivo
     *          si la puntuacion de la ruta puntuada que llama al metodo es mayor. Un valor
     *          negativo si la puntuacion de la ruta puntuada que invoca al metodo es menor.
     */
    @Override
    public int compareTo(ScoredRoute other) {
        return Double.compare(this.score, other.score);
    }
}