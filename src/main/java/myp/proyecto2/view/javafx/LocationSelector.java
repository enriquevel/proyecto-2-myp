package myp.proyecto2.view.javafx;

import java.util.List;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.PointOfInterest;

public class LocationSelector extends HBox {

    private final ComboBox<LocationOption> comboBox;
    private final Button mapClickButton;

    private Location selectedLocation;
    private Consumer<Void> onMapClickRequested;

    public LocationSelector(String placeholder) {
        setSpacing(5);

        // ComboBox
        comboBox = new ComboBox<>();
        comboBox.setPromptText(placeholder);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(comboBox, Priority.ALWAYS);

        comboBox.setOnAction(e -> {
            LocationOption option = comboBox.getValue();
            if (option != null)
                selectedLocation = option.location;
        });

        // Map click button
        mapClickButton = new Button("📍");
        mapClickButton.setTooltip(new Tooltip("Select on map"));
        mapClickButton.setOnAction(e -> {
            if (onMapClickRequested != null)
                onMapClickRequested.accept(null);
        });

        getChildren().addAll(comboBox, mapClickButton);
    }

    public void setPOIs(List<PointOfInterest> pois) {
        comboBox.getItems().clear();
        for (PointOfInterest poi : pois)
            comboBox.getItems().add(new LocationOption(poi.getName(), poi.getLocation()));
    }

    public void setSelectedLocation(Location location) {
        this.selectedLocation = location;

        if (location != null) {
            // Find in list
            boolean found = false;
            for (LocationOption option : comboBox.getItems()) {
                if (option.location.equals(location)) {
                    comboBox.setValue(option);
                    found = true;
                    break;
                }
            }

            // Add as custom if not found
            if (!found) {
                String label = String.format("%.4f, %.4f", location.getLatitude(), location.getLongitude());
                LocationOption custom = new LocationOption(label, location);
                comboBox.getItems().addFirst(custom);
                comboBox.setValue(custom);
            }
        } else
            comboBox.setValue(null);
    }

    public Location getSelectedLocation() {
        return selectedLocation;
    }

    public boolean hasSelection() {
        return selectedLocation != null;
    }

    public void clear() {
        selectedLocation = null;
        comboBox.setValue(null);
    }

    public void setOnMapClickRequested(Consumer<Void> callback) {
        this.onMapClickRequested = callback;
    }

    private record LocationOption(String label, Location location) {
        @Override
        public String toString() {
            return label;
        }
    }
}
