package myp.proyecto2.view.javafx;

import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import myp.proyecto2.model.provider.RouteProviderFactory;

public class SettingsDialog {

    private final Stage stage;
    private final ComboBox<String> providerCombo;
    private Consumer<String> onProviderChange;
    private String currentProvider;

    public SettingsDialog(String initialProvider) {
        this.stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Configuracion");
        stage.setResizable(false);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.TOP_LEFT);

        int row = 0;

        grid.add(new Separator(), 0, row, 2, 1);
        row++;

        // Route Provider
        Label providerLabel = new Label("Proveedor de rutas:");
        grid.add(providerLabel, 0, row);

        providerCombo = new ComboBox<>();
        providerCombo.getItems().addAll(RouteProviderFactory.getAvailableProviders());
        providerCombo.setValue(initialProvider);
        this.currentProvider = initialProvider;
        providerCombo.setPrefWidth(250);
        grid.add(providerCombo, 1, row);
        row++;

        // Info label
        Label infoLabel = new Label("Nota: Cambios en el proveedor aplican en la proxima busqueda de rutas");
        infoLabel.setStyle("-fx-font-size: 10; -fx-text-fill: #666;");
        grid.add(infoLabel, 0, row, 2, 1);
        row++;

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        Button applyButton = new Button("Aplicar");
        applyButton.setDefaultButton(true);
        applyButton.setPrefWidth(80);
        applyButton.setOnAction(e -> handleApply());

        Button closeButton = new Button("Cerrar");
        closeButton.setCancelButton(true);
        closeButton.setPrefWidth(80);
        closeButton.setOnAction(e -> stage.close());

        buttonBox.getChildren().addAll(applyButton, closeButton);
        grid.add(buttonBox, 0, row, 2, 1);

        Scene scene = new Scene(grid, 450, 280);
        stage.setScene(scene);
    }

    private void handleApply() {
        String selectedProvider = providerCombo.getValue();
        if (!selectedProvider.equals(this.currentProvider)) {
            if (onProviderChange != null) {
                onProviderChange.accept(selectedProvider);
                this.currentProvider = selectedProvider;
            }
        }

        // Show confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Configuracion guardada");
        alert.setHeaderText(null);
        alert.setContentText("La configuracion se ha actualizado!");
        alert.showAndWait();

        stage.close();
    }

    public void setOnProviderChange(Consumer<String> callback) {
        this.onProviderChange = callback;
    }


    public void showDialog() {
        stage.showAndWait();
    }

}
