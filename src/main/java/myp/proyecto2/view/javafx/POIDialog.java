package myp.proyecto2.view.javafx;

import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.POIType;
import myp.proyecto2.model.domain.PointOfInterest;

public class POIDialog extends Dialog<PointOfInterest> {

    private final TextField nameField;
    private final TextArea descriptionArea;
    private final ComboBox<POIType> typeCombo;
    private final TextField latField;
    private final TextField lngField;

    private final PointOfInterest existingPOI;

    public POIDialog(PointOfInterest poi, Location defaultLocation, String defaultSubmitter) {
        this.existingPOI = poi;

        boolean editMode = (poi != null);

        setTitle(editMode ? "Edit Location" : "Add New Location");
        setHeaderText(editMode ? "Modify location details" : "Save a new location");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Name
        grid.add(new Label("Name:"), 0, 0);
        nameField = new TextField();
        nameField.setPromptText("e.g., Faculty of Sciences");
        nameField.setText(editMode ? poi.getName() : "");
        grid.add(nameField, 1, 0);

        // Type
        grid.add(new Label("Type:"), 0, 1);
        typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(POIType.values());
        typeCombo.setValue(editMode ? poi.getType() : POIType.OTHER);
        grid.add(typeCombo, 1, 1);

        // Description
        grid.add(new Label("Description:"), 0, 2);
        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Optional description...");
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setText(editMode ? poi.getDescription() : "");
        grid.add(descriptionArea, 1, 2);

        // Latitude
        grid.add(new Label("Latitude:"), 0, 3);
        latField = new TextField();
        if (editMode) {
            latField.setText(String.valueOf(poi.getLocation().getLatitude()));
        } else if (defaultLocation != null) {
            latField.setText(String.valueOf(defaultLocation.getLatitude()));
        }
        grid.add(latField, 1, 3);

        // Longitude
        grid.add(new Label("Longitude:"), 0, 4);
        lngField = new TextField();
        if (editMode) {
            lngField.setText(String.valueOf(poi.getLocation().getLongitude()));
        } else if (defaultLocation != null) {
            lngField.setText(String.valueOf(defaultLocation.getLongitude()));
        }
        grid.add(lngField, 1, 4);

        getDialogPane().setContent(grid);

        ButtonType saveButton = new ButtonType(editMode ? "Update" : "Save", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        setResultConverter(button -> {
            if (button == saveButton && validate()) {
                return createPOI();
            }
            return null;
        });
    }

    private boolean validate() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showError("Please enter a name");
            return false;
        }

        if (name.length() < 3) {
            showError("Name must be at least 3 characters");
            return false;
        }

        if (typeCombo.getValue() == null) {
            showError("Please select a location type");
            return false;
        }

        try {
            double lat = Double.parseDouble(latField.getText());
            double lng = Double.parseDouble(lngField.getText());

            if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
                showError("Invalid coordinates");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Invalid coordinate format");
            return false;
        }

        return true;
    }

    private PointOfInterest createPOI() {
        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();
        POIType type = typeCombo.getValue();
        double lat = Double.parseDouble(latField.getText());
        double lng = Double.parseDouble(lngField.getText());

        Location location = new Location(lat, lng);

        /*
        if (existingPOI != null) {
            return PointOfInterest.fromCSV(
                    existingPOI.getId(),
                    name,
                    description,
                    location,
                    type,
                    submitter
            );
        } else {
            return PointOfInterest.create(name, description, location, type, submitter);
        }
         */
        //return new PointOfInterest(IDGenerator.generateSequentialID("POI"), name, description, location, type);
        return new PointOfInterest("", name, description, location, type);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Optional<PointOfInterest> showNewPOI(Location location, String submitter) {
        return new POIDialog(null, location, submitter).showAndWait();
    }

    public static Optional<PointOfInterest> showEditPOI(PointOfInterest poi) {
        return new POIDialog(poi, null, null).showAndWait();
    }
}
