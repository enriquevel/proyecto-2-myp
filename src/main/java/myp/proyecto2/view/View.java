package myp.proyecto2.view;

import java.util.List;
import java.util.function.Consumer;
import myp.proyecto2.model.domain.*;
import myp.proyecto2.model.domain.builder.Route;

/**
 * Interfaz que define el comportamiento de la vista de la aplicacion.
 */
public interface View {

    /**
     * Muestra la ventana principal de la aplicacion.
     */
    void show();

    /**
     * Cierra la ventana principal de la aplicacion.
     */
    void close();

    /**
     * Muestra un mensaje informativo al usuario en un dialogo modal.
     *
     * @param message el mensaje a mostrar
     */
    void displayMessage(String message);

    /**
     * Muestra un mensaje de error al usuario en un dialogo modal y actualiza
     * la barra de estado con el mensaje de error.
     *
     * @param error el mensaje de error a mostrar
     */
    void displayError(String error);

    /**
     * Muestra un mensaje de advertencia al usuario en un dialogo modal y actualiza
     * la barra de estado con el mensaje de advertencia.
     *
     * @param warning el mensaje de advertencia a mostrar
     */
    void displayWarning(String warning);

    /**
     * Muestra un mensaje de exito en la barra de estado sin dialogo modal.
     *
     * @param message el mensaje de exito a mostrar
     */
    void displaySuccess(String message);

    /**
     * Muestra las rutas calculadas en el mapa y en la lista de rutas.
     *
     * @param routes lista de rutas puntuadas a mostrar
     */
    void displayRoutes(List<ScoredRoute> routes);

    /**
     * Resalta una ruta especifica en el mapa cambiando su color y grosor.
     *
     * @param route la ruta a resaltar
     */
    void highlightRoute(Route route);

    /**
     * Limpia todas las rutas del mapa y de la lista de rutas.
     */
    void clearRoutes();

    /**
     * Muestra los reportes en el mapa y en la lista de reportes.
     *
     * @param reports lista de reportes a mostrar
     */
    void displayReports(List<Report> reports);

    /**
     * Limpia todos los reportes del mapa y de la lista de reportes.
     */
    void clearReports();

    /**
     * Muestra los puntos de interes en el mapa, en el panel de POIs y en
     * los selectores de ubicacion.
     *
     * @param pois lista de puntos de interes a mostrar
     */
    void displayPOIs(List<PointOfInterest> pois);

    /**
     * Limpia todos los puntos de interes del mapa y del panel de POIs.
     */
    void clearPOIs();

    /**
     * Limpia todo el contenido del mapa incluyendo rutas, reportes y POIs.
     * Tambien limpia las listas de rutas y reportes.
     */
    void clearMap();

    /**
     * Centra el mapa en una ubicacion especifica.
     *
     * @param location la ubicacion donde centrar el mapa
     */
    void centerMap(Location location);

    /**
     * Habilita el modo de seleccion de ubicacion en el mapa. Cambia el cursor
     * a una cruz y espera a que el usuario haga clic en el mapa para seleccionar
     * una ubicacion.
     *
     * @param callback funcion que se ejecuta cuando se selecciona una ubicacion
     */
    void enableMapClickMode(Consumer<Location> callback);

    /**
     * Deshabilita el modo de seleccion de ubicacion en el mapa y restaura
     * el cursor normal.
     */
    void disableMapClickMode();

    /**
     * Muestra un dialogo de confirmacion con un mensaje y botones Si/No.
     *
     * @param question la pregunta a mostrar al usuario
     * @return true si el usuario confirma, false en caso contrario
     */
    boolean confirm(String question);

    /**
     * Configura el callback que se ejecuta cuando se solicita buscar rutas.
     *
     * @param callback funcion que recibe una solicitud de ruta
     */
    void setOnFindRoutes(Consumer<RouteRequest> callback);

    /**
     * Configura el callback que se ejecuta cuando se envia un nuevo reporte.
     *
     * @param callback funcion que recibe un reporte
     */
    void setOnReportSubmit(Consumer<Report> callback);

    /**
     * Configura el callback que se ejecuta cuando se da voto positivo a un reporte.
     *
     * @param callback funcion que recibe un reporte
     */
    void setOnReportUpvote(Consumer<Report> callback);

    /**
     * Configura el callback que se ejecuta cuando se da voto negativo a un reporte.
     *
     * @param callback funcion que recibe un reporte
     */
    void setOnReportDownvote(Consumer<Report> callback);

    /**
     * Configura el callback que se ejecuta cuando se agrega un nuevo POI.
     *
     * @param callback funcion que recibe un punto de interes
     */
    void setOnPOIAdd(Consumer<PointOfInterest> callback);

    /**
     * Configura el callback que se ejecuta cuando se elimina un POI.
     *
     * @param callback funcion que recibe un punto de interes
     */
    void setOnPOIDelete(Consumer<PointOfInterest> callback);

    /**
     * Configura el callback que se ejecuta cuando se solicita refrescar los datos.
     *
     * @param callback funcion sin parametros
     */
    void setOnRefreshData(Runnable callback);

    /**
     * Configura el callback que se ejecuta cuando se solicita cambiar la configuracion.
     *
     * @param callback funcion que recibe una llave API
     */
    void setOnSettingsRequested(Runnable callback);
}
