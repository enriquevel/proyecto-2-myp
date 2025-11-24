package myp.proyecto2.view.javafx;

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

    private final RadioButton walkingRadio;
    private final RadioButton bicyclingRadio;
    private final RadioButton drivingRadio;
    private final RadioButton transitRadio;
    private final ToggleGroup modeGroup;

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

        this.modeGroup = new ToggleGroup();

        this.walkingRadio = new RadioButton("Walking");
        this.walkingRadio.setToggleGroup(modeGroup);
        this.walkingRadio.setSelected(true);

        this.bicyclingRadio = new RadioButton("Bicycling");
        this.bicyclingRadio.setToggleGroup(modeGroup);

        this.drivingRadio = new RadioButton("Driving");
        this.drivingRadio.setToggleGroup(modeGroup);

        this.transitRadio = new RadioButton("Transit");
        this.transitRadio.setToggleGroup(modeGroup);

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
                this.walkingRadio, this.bicyclingRadio, this.drivingRadio, this.transitRadio,
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

    public TransportMode getSelectedMode() {
        Toggle selected = modeGroup.getSelectedToggle();
        if (selected == null)
            return null;

        if (selected == walkingRadio)   return TransportMode.WALKING;
        if (selected == bicyclingRadio) return TransportMode.BICYCLING;
        if (selected == drivingRadio)   return TransportMode.DRIVING;
        if (selected == transitRadio)   return TransportMode.BUS;

        return null;

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

        if (getSelectedMode() == null) {
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
