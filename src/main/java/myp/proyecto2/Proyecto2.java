package myp.proyecto2;

import javafx.application.Application;
import javafx.stage.Stage;
import myp.proyecto2.controller.ApplicationController;
import myp.proyecto2.view.javafx.JavaFXView;
import myp.proyecto2.view.javafx.UniversityBounds;

public class Proyecto2 extends Application {

    private ApplicationController controller;

    @Override
    public void start(Stage primaryStage) {
        try {
            Parameters params = getParameters();

            String apiKey = params.getNamed().get("apikey");
            if (apiKey == null || apiKey.isBlank()) {
                System.err.println("Missing --apikey=<value> argument.");
                System.exit(1);
            }

            String provider = params.getNamed().get("provider");
            if (provider == null || provider.isBlank()) {
                System.err.println("Missing --provider=<value> argument.");
                System.exit(1);
            }

            UniversityBounds bounds = UniversityBounds.ciudadUniversitaria();
            JavaFXView view = new JavaFXView(primaryStage, bounds);

            controller = new ApplicationController(view, provider, apiKey);
            controller.start();

        } catch (Exception e) {
            System.err.println("Failed to start:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void stop() {
        if (controller != null) {
            controller.shutdown();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
