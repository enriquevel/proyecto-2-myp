package myp.proyecto2.view;

import java.util.List;
import java.util.function.Consumer;

import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.PointOfInterest;
import myp.proyecto2.model.domain.Report;
import myp.proyecto2.model.domain.ScoredRoute;
import myp.proyecto2.model.domain.builder.Route;

public interface View {

    void show();
    void close();

    void displayMessage(String message);
    void displayError(String error);
    void displayWarning(String warning);
    void displaySuccess(String message);

    void displayRoutes(List<ScoredRoute> routes);
    void highlightRoute(Route route);
    void clearRoutes();

    void displayReports(List<Report> reports);
    void clearReports();

    void displayPOIs(List<PointOfInterest> pois);
    void clearPOIs();

    void clearMap();
    void centerMap(Location location);
    void enableMapClickMode(Consumer<Location> callback);
    void disableMapClickMode();

    boolean confirm(String question);

}
