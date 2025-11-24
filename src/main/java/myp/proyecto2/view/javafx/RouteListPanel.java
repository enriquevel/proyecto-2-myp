package myp.proyecto2.view.javafx;

import java.util.List;
import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import myp.proyecto2.model.domain.ScoredRoute;
import myp.proyecto2.model.domain.TransportMode;
import myp.proyecto2.model.domain.builder.Route;

public class RouteListPanel extends VBox{
    private final ListView<ScoredRoute> listView;
    private Consumer<Route> onRouteSelected;

    public RouteListPanel() {
        setSpacing(10);
        setPadding(new Insets(15));
        setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1;");
        setPrefWidth(320);

        Label title = new Label("Route Options");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        this.listView = new ListView<>();
        this.listView.setCellFactory(param -> new RouteCell());
        this.listView.setPlaceholder(new Label("No routes to display"));
        VBox.setVgrow(this.listView, Priority.ALWAYS);

        this.listView.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && this.onRouteSelected != null)
                this.onRouteSelected.accept(newVal.getRoute());
        });

        getChildren().addAll(title, new Separator(), this.listView);
    }

    public void setRoutes(List<ScoredRoute> routes) {
        this.listView.getItems().clear();
        if (routes != null) {
            this.listView.getItems().addAll(routes);
            if (!routes.isEmpty())
                this.listView.getSelectionModel().select(0);
        }
    }

    public void clearRoutes() {
        this.listView.getItems().clear();
    }

    public void setOnRouteSelected(Consumer<Route> callback) {
        this.onRouteSelected = callback;
    }

    private class RouteCell extends ListCell<ScoredRoute> {

        @Override
        protected void updateItem(ScoredRoute item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
                return;
            }

            Route route = item.getRoute();

            VBox box = new VBox(5);
            box.setPadding(new Insets(8));

            // Header
            HBox header = new HBox(10);

            Label rank = new Label("Route " + (getIndex() + 1));
            rank.setStyle("-fx-font-weight: bold;");

            Label duration = new Label(formatDuration((int)route.getTotalDurationMinutes()));
            duration.setStyle("-fx-font-weight: bold;");

            Label distance = new Label(String.format("%.1f km", route.getTotalDistanceKilometers()));
            distance.setStyle("-fx-text-fill: #757575;");

            HBox.setHgrow(new Region(), Priority.ALWAYS);
            header.getChildren().addAll(rank, duration, new Region(), distance);

            // Modes
            HBox modes = new HBox(5);
            for (TransportMode mode : route.getTransportModes()) {
                Label modeLabel = new Label(mode.getDisplayName());
                modeLabel.setStyle("-fx-background-color: " + getModeColor(mode) +
                        "; -fx-text-fill: white; -fx-padding: 2 6; -fx-background-radius: 3; -fx-font-size: 10;");
                modes.getChildren().add(modeLabel);
            }

            // Score
            Label score = new Label(String.format("Score: %.1f (%s)", item.getScore(), item.getScorer()));
            score.setStyle("-fx-font-size: 11; -fx-text-fill: #757575;");

            box.getChildren().addAll(header, modes, score);

            if (isSelected())
                box.setStyle("-fx-background-color: #e3f2fd; -fx-border-color: #2196f3; -fx-border-width: 0 0 0 4;");

            setGraphic(box);
        }

        private String formatDuration(int minutes) {
            if (minutes >= 60)
                return String.format("%d h %d min", minutes / 60, minutes % 60);
            return minutes + " min";
        }

        private String getModeColor(TransportMode mode) {
            return switch (mode) {
                case WALKING -> "#4caf50";
                case BICYCLING -> "#2196f3";
                case DRIVING -> "#f44336";
                case BUS -> "#9c27b0";
            };
        }
    }

}
