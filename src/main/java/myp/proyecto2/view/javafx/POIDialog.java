package myp.proyecto2.view.javafx;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.POIType;
import myp.proyecto2.model.domain.PointOfInterest;
import myp.proyecto2.model.util.IDGenerator;

public class POIDialog extends Dialog<PointOfInterest> {

    private final Stage stage;
    private final TextField nameField;
    private final TextArea descriptionArea;
    private final ComboBox<POIType> typeCombo;
    private final Label locationLabel;
    private final Button mapClickButton;
    private Location selectedLocation;
    private Consumer<Consumer<Location>> onRequestMapClick;
    private Consumer<PointOfInterest> onResult;

    public POIDialog(PointOfInterest poi, Location defaultLocation) {
        this.selectedLocation = (poi != null) ? poi.getLocation() : defaultLocation;
        boolean editMode = (poi != null);

        this.stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(editMode ? "Edit Location" : "Add New Location");
        stage.setResizable(false);

        // Create main layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.TOP_LEFT);

        int row = 0;
// Name
        Label nameLabel = new Label("Name:");
        grid.add(nameLabel, 0, row);

        nameField = new TextField();
        nameField.setPromptText("e.g., Faculty of Sciences");
        nameField.setText(editMode ? poi.getName() : "");
        nameField.setPrefWidth(300);
        grid.add(nameField, 1, row);
        row++;

        // Type
        Label typeLabel = new Label("Type:");
        grid.add(typeLabel, 0, row);

        typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(POIType.values());
        typeCombo.setValue(editMode ? poi.getType() : POIType.OTHER);
        typeCombo.setPrefWidth(300);
        grid.add(typeCombo, 1, row);
        row++;

        // Description
        Label descLabel = new Label("Description:");
        grid.add(descLabel, 0, row);

        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Optional description...");
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setPrefWidth(300);
        descriptionArea.setText(editMode ? poi.getDescription() : "");
        grid.add(descriptionArea, 1, row);
        row++;

        // Location
        Label locLabel = new Label("Location:");
        grid.add(locLabel, 0, row);

        HBox locationBox = new HBox(10);
        locationBox.setAlignment(Pos.CENTER_LEFT);

        locationLabel = new Label(formatLocation(selectedLocation));
        locationLabel.setStyle("-fx-border-color: #ccc; -fx-border-width: 1; " +
                "-fx-padding: 5; -fx-background-color: #f5f5f5; -fx-min-width: 150;");

        mapClickButton = new Button("📍 Map");
        mapClickButton.setOnAction(e -> handleMapClick());

        locationBox.getChildren().addAll(locationLabel, mapClickButton);
        grid.add(locationBox, 1, row);
        row++;

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        Button saveButton = new Button(editMode ? "Update" : "Save");
        saveButton.setDefaultButton(true);
        saveButton.setPrefWidth(80);
        saveButton.setOnAction(e -> handleSubmit());

        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true);
        cancelButton.setPrefWidth(80);
        cancelButton.setOnAction(e -> handleCancel());

        buttonBox.getChildren().addAll(saveButton, cancelButton);
        grid.add(buttonBox, 1, row);

        // Create scene
        Scene scene = new Scene(grid, 500, 400);
        stage.setScene(scene);

        System.out.println("DEBUG: POIDialog created successfully");
    }

    private void handleMapClick() {
        System.out.println("DEBUG: POIDialog - Map click button pressed");

        if (onRequestMapClick == null) {
            System.err.println("ERROR: onRequestMapClick is null!");
            showError("Map click not available");
            return;
        }

        mapClickButton.setDisable(true);
        mapClickButton.setText("Click...");

        // Request map click
        onRequestMapClick.accept(location -> {
            System.out.println("DEBUG: POIDialog - Location callback received: " + location);

            javafx.application.Platform.runLater(() -> {
                selectedLocation = location;
                locationLabel.setText(formatLocation(location));
                mapClickButton.setDisable(false);
                mapClickButton.setText("📍 Map");

                System.out.println("DEBUG: POIDialog - Location updated in UI");
            });
        });

    }

    private void handleSubmit() {
        System.out.println("DEBUG: POIDialog - Submit button pressed");

        if (!validate()) {
            System.out.println("DEBUG: POIDialog - Validation failed");
            return;
        }

        PointOfInterest poi = createPOI();
        System.out.println("DEBUG: POIDialog - POI created: " + poi);

        if (onResult != null) {
            System.out.println("DEBUG: POIDialog - Firing onResult callback");
            onResult.accept(poi);
        } else {
            System.err.println("ERROR: onResult callback is null!");
        }

        stage.close();

    }

    private void handleCancel() {
        System.out.println("DEBUG: POIDialog - Cancel clicked");

        if (onResult != null) {
            onResult.accept(null);
        }

        stage.close();
    }


    private String formatLocation(Location location) {
        if (location == null) {
            return "No location selected";
        }
        return String.format("%.4f, %.4f", location.getLatitude(), location.getLongitude());
    }

    public void setOnRequestMapClick(Consumer<Consumer<Location>> callback) {
        System.out.println("DEBUG: POIDialog - setOnRequestMapClick called");
        this.onRequestMapClick = callback;
    }

    public void setOnResult(Consumer<PointOfInterest> callback) {
        System.out.println("DEBUG: POIDialog - setOnResult called");
        this.onResult = callback;
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

        if (selectedLocation == null) {
            showError("Please select a location on the map");
            return false;
        }

        return true;
    }

    private PointOfInterest createPOI() {
        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();
        POIType type = typeCombo.getValue();

        return new PointOfInterest(IDGenerator.generateSequentialID("POI"), name, description, selectedLocation, type);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Optional<PointOfInterest> showNewPOI(
            Location defaultLocation,
            Consumer<Consumer<Location>> mapClickRequester
    ) {
        POIDialog dialog = new POIDialog(null, defaultLocation);
        dialog.onRequestMapClick = mapClickRequester;
        return dialog.showAndWait();
    }

    public static Optional<PointOfInterest> showEditPOI(
            PointOfInterest poi,
            Consumer<Consumer<Location>> mapClickRequester
    ) {
        POIDialog dialog = new POIDialog(poi, poi.getLocation());
        dialog.onRequestMapClick = mapClickRequester;
        return dialog.showAndWait();
    }

    public void showDialog() {
        System.out.println("DEBUG: POIDialog - show() called");
        stage.show();
        stage.toFront();
        System.out.println("DEBUG: POIDialog - stage shown");
    }

    public void hideDialog() {
        System.out.println("DEBUG: POIDialog - hide() called");
        stage.hide();
    }
}
