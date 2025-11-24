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
import myp.proyecto2.model.domain.Report;
import myp.proyecto2.model.domain.ReportType;
import myp.proyecto2.model.util.IDGenerator;

public class ReportDialog extends Dialog<Report> {

    private final Stage stage;
    private final ComboBox<ReportType> typeCombo;
    private final TextArea descriptionArea;
    private final Label locationLabel;
    private final Button mapClickButton;
    private Consumer<Consumer<Location>> onRequestMapClick;
    private Location selectedLocation;
    private Consumer<Report> onResult;

    public ReportDialog(Report report, Location defaultLocation) {
        this.selectedLocation = (report != null) ? report.getLocation() : defaultLocation;
        boolean editMode = (report != null);

        this.stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(editMode ? "Edit Report" : "Submit New Report");
        stage.setResizable(false);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.TOP_LEFT);

        int row = 0;

        // Type
        Label typeLabel = new Label("Incident Type:");
        grid.add(typeLabel, 0, row);

        typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(ReportType.values());
        typeCombo.setValue(editMode ? report.getType() : ReportType.TRAFFIC_JAM);
        typeCombo.setPrefWidth(300);
        grid.add(typeCombo, 1, row);
        row++;

        // Description
        Label descLabel = new Label("Description:");
        grid.add(descLabel, 0, row);

        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Describe the incident...");
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(4);
        descriptionArea.setPrefWidth(300);
        descriptionArea.setText(editMode ? report.getDescription() : "");
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

        Button submitButton = new Button(editMode ? "Update" : "Submit");
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(80);
        submitButton.setOnAction(e -> handleSubmit());

        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true);
        cancelButton.setPrefWidth(80);
        cancelButton.setOnAction(e -> handleCancel());

        buttonBox.getChildren().addAll(submitButton, cancelButton);
        grid.add(buttonBox, 1, row);

        // Create scene
        Scene scene = new Scene(grid, 500, 350);
        stage.setScene(scene);

        System.out.println("DEBUG: ReportDialog created successfully");
    }

    /**
     * Handle map click button.
     */
    private void handleMapClick() {
        System.out.println("DEBUG: ReportDialog - Map click button pressed");

        if (onRequestMapClick == null) {
            System.err.println("ERROR: onRequestMapClick is null!");
            showError("Map click not available");
            return;
        }

        mapClickButton.setDisable(true);
        mapClickButton.setText("Click...");

        // Request map click
        onRequestMapClick.accept(location -> {
            System.out.println("DEBUG: ReportDialog - Location callback received: " + location);

            javafx.application.Platform.runLater(() -> {
                selectedLocation = location;
                locationLabel.setText(formatLocation(location));
                mapClickButton.setDisable(false);
                mapClickButton.setText("📍 Map");

                System.out.println("DEBUG: ReportDialog - Location updated in UI");
            });
        });

    }

    private void handleSubmit() {
        System.out.println("DEBUG: ReportDialog - Submit button pressed");

        if (!validate()) {
            System.out.println("DEBUG: ReportDialog - Validation failed");
            return;
        }

        Report report = createReport();
        System.out.println("DEBUG: ReportDialog - Report created: " + report);

        if (onResult != null) {
            System.out.println("DEBUG: ReportDialog - Firing onResult callback");
            onResult.accept(report);
        } else {
            System.err.println("ERROR: onResult callback is null!");
        }

        stage.close();
    }

    private void handleCancel() {
        System.out.println("DEBUG: ReportDialog - Cancel clicked");

        if (onResult != null) {
            onResult.accept(null);
        }

        stage.close();
    }

    /**
     * Format location for display.
     */
    private String formatLocation(Location location) {
        if (location == null) {
            return "No location selected";
        }
        return String.format("%.4f, %.4f", location.getLatitude(), location.getLongitude());
    }

    public void setOnRequestMapClick(Consumer<Consumer<Location>> callback) {
        this.onRequestMapClick = callback;
    }

    public void setOnResult(Consumer<Report> callback) {
        this.onResult = callback;
    }

    private boolean validate() {
        if (typeCombo.getValue() == null) {
            showError("Please select an incident type");
            return false;
        }

        String desc = descriptionArea.getText().trim();
        if (desc.isEmpty()) {
            showError("Please enter a description");
            return false;
        }

        if (desc.length() < 10) {
            showError("Description must be at least 10 characters");
            return false;
        }

        if (selectedLocation == null) {
            showError("Please select a location on the map");
            return false;
        }

        return true;
    }

    private Report createReport() {
        ReportType type = typeCombo.getValue();
        String description = descriptionArea.getText().trim();

        return new Report(IDGenerator.generateSequentialID("REP"), type, selectedLocation, description);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showDialog() {
        System.out.println("DEBUG: ReportDialog - showDialog() called");
        stage.show();
        stage.toFront();
        System.out.println("DEBUG: ReportDialog - stage shown");
    }

    public void hideDialog() {
        System.out.println("DEBUG: ReportDialog - hideDialog() called");
        stage.hide();
    }
}
