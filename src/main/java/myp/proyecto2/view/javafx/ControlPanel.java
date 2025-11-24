package myp.proyecto2.view.javafx;

import java.util.HashSet;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.RoutePreference;
import myp.proyecto2.model.domain.TransportMode;

public class ControlPanel extends VBox {

    private final LocationSelector originSelector;
    private final LocationSelector destinationSelector;

    private final CheckBox walkingCheck;
    private final CheckBox bicyclingCheck;
    private final CheckBox drivingCheck;
    private final CheckBox transitCheck;

    private final ComboBox<RoutePreference> preferenceCombo;

    private final Button findButton;
    private final Button clearButton;
    private final Button swapButton;

    private Runnable onFindRoutes;
    private Runnable onClear;

    public ControlPanel() {
        setSpacing(10);
        setPadding(new Insets(15));
        setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1;");
        setPrefWidth(320);

        // Title
        Label title = new Label("Route Configuration");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Origin
        Label originLabel = new Label("Origin:");
        originLabel.setStyle("-fx-font-weight: bold;");
        this.originSelector = new LocationSelector("Select starting point...");

        // Destination
        Label destLabel = new Label("Destination:");
        destLabel.setStyle("-fx-font-weight: bold;");
        this.destinationSelector = new LocationSelector("Select ending point...");

        // Swap button
        this.swapButton = new Button("⇅ Swap");
        this.swapButton.setMaxWidth(Double.MAX_VALUE);
        this.swapButton.setOnAction(e -> swap());

        // Transport modes
        Label modesLabel = new Label("Transport Modes:");
        modesLabel.setStyle("-fx-font-weight: bold;");

        this.walkingCheck = new CheckBox("Walking");
        this.walkingCheck.setSelected(true);

        this.bicyclingCheck = new CheckBox("Bicycling");
        this.bicyclingCheck.setSelected(true);

        this.drivingCheck = new CheckBox("Driving");
        this.transitCheck = new CheckBox("Transit");

        // Preference
        Label prefLabel = new Label("Route Preference:");
        prefLabel.setStyle("-fx-font-weight: bold;");

        this.preferenceCombo = new ComboBox<>();
        this.preferenceCombo.getItems().addAll(RoutePreference.values());
        this.preferenceCombo.setValue(RoutePreference.BALANCED);
        this.preferenceCombo.setMaxWidth(Double.MAX_VALUE);

        // Buttons
        HBox buttonBox = new HBox(10);

        this.findButton = new Button("Find Routes");
        this.findButton.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; -fx-font-weight: bold;");
        this.findButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(this.findButton, Priority.ALWAYS);
        this.findButton.setOnAction(e -> {
            if (validate() && this.onFindRoutes != null)
                this.onFindRoutes.run();
        });

        this.clearButton = new Button("Clear");
        this.clearButton.setStyle("-fx-background-color: #757575; -fx-text-fill: white;");
        this.clearButton.setOnAction(e -> {
            if (this.onClear != null)
                this.onClear.run();
        });

        buttonBox.getChildren().addAll(this.findButton, this.clearButton);

        // Add all
        getChildren().addAll(
                title,
                new Separator(),
                originLabel, this.originSelector,
                this.swapButton,
                destLabel, this.destinationSelector,
                new Separator(),
                modesLabel,
                this.walkingCheck, this.bicyclingCheck, this.drivingCheck, this.transitCheck,
                new Separator(),
                prefLabel, this.preferenceCombo,
                new Separator(),
                buttonBox
        );
    }

    public LocationSelector getOriginSelector() {
        return this.originSelector;
    }

    public LocationSelector getDestinationSelector() {
        return this.destinationSelector;
    }

    public Set<TransportMode> getSelectedModes() {
        Set<TransportMode> modes = new HashSet<>();
        if (this.walkingCheck.isSelected()) modes.add(TransportMode.WALKING);
        if (this.bicyclingCheck.isSelected()) modes.add(TransportMode.BICYCLING);
        if (this.drivingCheck.isSelected()) modes.add(TransportMode.DRIVING);
        if (this.transitCheck.isSelected()) modes.add(TransportMode.BUS);
        return modes;
    }

    public RoutePreference getPreference() {
        return this.preferenceCombo.getValue();
    }

    public void clearSelections() {
        this.originSelector.clear();
        this.destinationSelector.clear();
    }

    public void setOnFindRoutes(Runnable callback) {
        this.onFindRoutes = callback;
    }

    public void setOnClear(Runnable callback) {
        this.onClear = callback;
    }

    private void swap() {
        Location temp = this.originSelector.getSelectedLocation();
        this.originSelector.setSelectedLocation(this.destinationSelector.getSelectedLocation());
        this.destinationSelector.setSelectedLocation(temp);
    }

    private boolean validate() {
        if (!this.originSelector.hasSelection()) {
            showAlert("Please select an origin");
            return false;
        }

        if (!this.destinationSelector.hasSelection()) {
            showAlert("Please select a destination");
            return false;
        }

        if (getSelectedModes().isEmpty()) {
            showAlert("Please select at least one transport mode");
            return false;
        }

        return true;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
