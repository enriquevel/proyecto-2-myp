package myp.proyecto2.view.javafx;

import javafx.application.Application;
import javafx.stage.Stage;
import myp.proyecto2.model.domain.*;

import java.util.List;

public class ViewTestApp extends Application {
    @Override
    public void start(Stage primaryStage) {

        UniversityBounds bounds = UniversityBounds.ciudadUniversitaria();
        JavaFXView view = new JavaFXView(primaryStage, bounds);

        // Wire test callbacks
        view.setOnFindRoutes(request -> {
            System.out.println("Find routes clicked:");
            System.out.println("  Origin: " + request.origin);
            System.out.println("  Destination: " + request.destination);
            System.out.println("  Modes: " + request.modes);
            System.out.println("  Preference: " + request.preference);

            //view.showLoading("Finding routes...");

            // Simulate async operation
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(() -> {
                        //view.hideLoading();
                        view.displayMessage("Routes would appear here");
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        view.setOnReportSubmit(report -> {
            System.out.println("Report submitted: " + report.getType());
        });

        view.setOnPOIAdd(poi -> {
            System.out.println("POI added: " + poi.getName());
        });

        // Load test data
        loadTestData(view);

        view.show();
    }

    private void loadTestData(JavaFXView view) {
        // Test POIs
        List<PointOfInterest> pois = List.of(
                new PointOfInterest("P1", "Faculty of Sciences", "Main sciences building",
                        new Location(19.3223, -99.1797), POIType.FACULTY),
                new PointOfInterest("P2", "Central Library", "Iconic UNAM library",
                        new Location(19.3317, -99.1855), POIType.OTHER),
                new PointOfInterest("P3", "Olympic Stadium", "University stadium",
                        new Location(19.3324, -99.1919), POIType.RECREATION)
        );

        view.displayPOIs(pois);

        // Test reports
        List<Report> reports = List.of(
                new Report("1", ReportType.TRAFFIC_JAM, new Location(19.3250, -99.1820),
                        "Heavy traffic on main avenue"),
                new Report("2", ReportType.CONSTRUCTION, new Location(19.3280, -99.1850),
                        "Construction near library")
        );

        view.displayReports(reports);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
