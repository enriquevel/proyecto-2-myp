package myp.proyecto2.view.javafx;

import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.Report;
import myp.proyecto2.model.domain.ReportType;

public class ReportDialog extends Dialog<Report> {

    private final ComboBox<ReportType> typeCombo;
    private final TextArea descriptionArea;
    private final TextField latField;
    private final TextField lngField;

    private final Report existingReport;

    public ReportDialog(Report report, Location defaultLocation) {
        this.existingReport = report;

        boolean editMode = (report != null);

        setTitle(editMode ? "Edit Report" : "Submit New Report");
        setHeaderText(editMode ? "Modify incident report" : "Report a new incident");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Type
        grid.add(new Label("Incident Type:"), 0, 0);
        typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(ReportType.values());
        typeCombo.setValue(editMode ? report.getType() : ReportType.TRAFFIC_JAM);
        grid.add(typeCombo, 1, 0);

        // Description
        grid.add(new Label("Description:"), 0, 1);
        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Describe the incident...");
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(4);
        descriptionArea.setText(editMode ? report.getDescription() : "");
        grid.add(descriptionArea, 1, 1);

        // Location
        grid.add(new Label("Latitude:"), 0, 2);
        latField = new TextField();
        if (editMode) {
            latField.setText(String.valueOf(report.getLocation().getLatitude()));
        } else if (defaultLocation != null) {
            latField.setText(String.valueOf(defaultLocation.getLatitude()));
        }
        grid.add(latField, 1, 2);

        grid.add(new Label("Longitude:"), 0, 3);
        lngField = new TextField();
        if (editMode) {
            lngField.setText(String.valueOf(report.getLocation().getLongitude()));
        } else if (defaultLocation != null) {
            lngField.setText(String.valueOf(defaultLocation.getLongitude()));
        }
        grid.add(lngField, 1, 3);

        getDialogPane().setContent(grid);

        ButtonType submitButton = new ButtonType(editMode ? "Update" : "Submit", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(submitButton, ButtonType.CANCEL);

        setResultConverter(button -> {
            if (button == submitButton && validate()) {
                return createReport();
            }
            return null;
        });
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

    private Report createReport() {
        ReportType type = typeCombo.getValue();
        String description = descriptionArea.getText().trim();
        double lat = Double.parseDouble(latField.getText());
        double lng = Double.parseDouble(lngField.getText());

        Location location = new Location(lat, lng);

        /*
        if (existingReport != null) {
            return Report.fromCSV(
                    existingReport.getId(),
                    type,
                    location,
                    description,
                    existingReport.getTimestamp(),
                    submitter,
                    existingReport.getStatus(),
                    existingReport.getUpvotes(),
                    existingReport.getDownvotes()
            );
        } else {
            return Report.create(type, location, description, submitter);
        }
         */
        //return new Report(IDGenerator.generateSequentialID("REP"), type, location, description);
        return new Report("", type, location, description);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Optional<Report> showNewReport(Location location) {
        return new ReportDialog(null, location).showAndWait();
    }

    public static Optional<Report> showEditReport(Report report) {
        return new ReportDialog(report, null).showAndWait();
    }
}
