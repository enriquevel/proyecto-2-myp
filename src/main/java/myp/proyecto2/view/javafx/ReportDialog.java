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
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.Report;
import myp.proyecto2.model.domain.ReportType;
import myp.proyecto2.model.util.IDGenerator;

/**
 * Dialogo modal para crear o editar reportes de incidentes en el sistema.
 * Proporciona campos para seleccionar el tipo de incidente, ingresar una
 * descripcion detallada y seleccionar la ubicacion donde ocurre el incidente.
 */
public class ReportDialog extends Dialog<Report> {

    /** Escenario que contiene el dialogo modal. */
    private final Stage stage;

    /** ComboBox para seleccionar el tipo de incidente reportado. */
    private final ComboBox<ReportType> typeCombo;

    /** Area de texto para ingresar la descripcion del incidente. */
    private final TextArea descriptionArea;

    /** Etiqueta que muestra las coordenadas de la ubicacion seleccionada. */
    private final Label locationLabel;

    /** Boton para activar el modo de seleccion de ubicacion en el mapa. */
    private final Button mapClickButton;

    /** Callback ejecutado cuando se solicita seleccionar ubicacion en el mapa. */
    private Consumer<Consumer<Location>> onRequestMapClick;

    /** Ubicacion geografica seleccionada para el reporte. */
    private Location selectedLocation;

    /** Callback ejecutado cuando se completa o cancela el dialogo. */
    private Consumer<Report> onResult;

    /**
     * Construye un nuevo dialogo de reporte para crear o editar.
     * Si se proporciona un reporte existente, el dialogo se configura en modo
     * edicion con los datos precargados. Si no, se configura en modo creacion
     * con valores por defecto.
     *
     * @param report el reporte a editar, o null para crear uno nuevo
     * @param defaultLocation la ubicacion por defecto si no se proporciona un reporte
     */
    public ReportDialog(Report report, Location defaultLocation) {
        this.selectedLocation = (report != null) ? report.getLocation() : defaultLocation;
        boolean editMode = (report != null);

        this.stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(editMode ? "Editar Reporte" : "Agregar Nuevo Reporte");
        stage.setResizable(false);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.TOP_LEFT);

        int row = 0;

        Label typeLabel = new Label("Tipo de incidente:");
        grid.add(typeLabel, 0, row);

        typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(ReportType.values());
        typeCombo.setValue(editMode ? report.getType() : ReportType.TRAFFIC_JAM);
        typeCombo.setPrefWidth(300);
        grid.add(typeCombo, 1, row);
        row++;

        Label descLabel = new Label("Descripcion:");
        grid.add(descLabel, 0, row);

        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Minimo 10 caracteres...");
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(4);
        descriptionArea.setPrefWidth(300);
        descriptionArea.setText(editMode ? report.getDescription() : "");
        grid.add(descriptionArea, 1, row);
        row++;

        Label locLabel = new Label("Ubicacion:");
        grid.add(locLabel, 0, row);

        HBox locationBox = new HBox(10);
        locationBox.setAlignment(Pos.CENTER_LEFT);

        locationLabel = new Label(formatLocation(selectedLocation));
        locationLabel.setStyle("-fx-border-color: #ccc; -fx-border-width: 1; " +
                "-fx-padding: 5; -fx-background-color: #f5f5f5; -fx-min-width: 150;");

        mapClickButton = new Button("@ Mapa");
        mapClickButton.setOnAction(e -> handleMapClick());

        locationBox.getChildren().addAll(locationLabel, mapClickButton);
        grid.add(locationBox, 1, row);
        row++;

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        Button submitButton = new Button(editMode ? "Actualizar" : "Agregar");
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(80);
        submitButton.setOnAction(e -> handleSubmit());

        Button cancelButton = new Button("Cancelar");
        cancelButton.setCancelButton(true);
        cancelButton.setPrefWidth(80);
        cancelButton.setOnAction(e -> handleCancel());

        buttonBox.getChildren().addAll(submitButton, cancelButton);
        grid.add(buttonBox, 1, row);

        Scene scene = new Scene(grid, 500, 350);
        stage.setScene(scene);
    }

    /**
     * Maneja el evento del boton de seleccion en mapa. Solicita al controlador
     * que active el modo de seleccion en el mapa y espera a que el usuario
     * seleccione una ubicacion.
     */
    private void handleMapClick() {
        if (onRequestMapClick == null) {
            System.err.println("ERROR: onRequestMapClick is null!");
            showError("Map click not available");
            return;
        }

        mapClickButton.setDisable(true);
        mapClickButton.setText("Click...");

        onRequestMapClick.accept(location -> javafx.application.Platform.runLater(() -> {
            selectedLocation = location;
            locationLabel.setText(formatLocation(location));
            mapClickButton.setDisable(false);
            mapClickButton.setText("@ Mapa");
        }));
    }

    /**
     * Maneja el evento del boton de enviar/actualizar. Valida los datos ingresados,
     * crea el objeto Report correspondiente y ejecuta el callback de resultado.
     * Cierra el dialogo si todo es exitoso.
     */
    private void handleSubmit() {
        if (!validate())
            return;

        Report report = createReport();

        if (onResult != null)
            onResult.accept(report);
        else
            System.err.println("ERROR: onResult callback is null!");

        stage.close();
    }

    /**
     * Maneja el evento del boton de cancelar. Ejecuta el callback de resultado
     * con valor null para indicar cancelacion y cierra el dialogo.
     */
    private void handleCancel() {
        if (onResult != null)
            onResult.accept(null);

        stage.close();
    }

    /**
     * Formatea una ubicacion geografica como texto para mostrar al usuario.
     * Muestra las coordenadas con 4 decimales de precision o un mensaje si
     * no hay ubicacion seleccionada.
     *
     * @param location la ubicacion a formatear
     * @return el texto formateado con las coordenadas o mensaje de no seleccion
     */
    private String formatLocation(Location location) {
        if (location == null)
            return "No location selected";

        return String.format("%.4f, %.4f", location.getLatitude(), location.getLongitude());
    }

    /**
     * Configura el callback que se ejecuta cuando se solicita seleccionar
     * una ubicacion en el mapa. El callback recibe como parametro otro callback
     * que debe ser invocado con la ubicacion seleccionada.
     *
     * @param callback funcion que recibe un callback de ubicacion
     */
    public void setOnRequestMapClick(Consumer<Consumer<Location>> callback) {
        this.onRequestMapClick = callback;
    }

    /**
     * Configura el callback que se ejecuta cuando se completa o cancela el dialogo.
     * Recibe el reporte creado/editado, o null si se cancelo.
     *
     * @param callback funcion que recibe el resultado del dialogo
     */
    public void setOnResult(Consumer<Report> callback) {
        this.onResult = callback;
    }

    /**
     * Valida todos los campos del formulario antes de crear el reporte.
     * Verifica que se haya seleccionado un tipo de incidente, que la descripcion
     * tenga al menos 10 caracteres y que se haya elegido una ubicacion.
     * Muestra mensajes de error especificos para cada validacion fallida.
     *
     * @return true si todos los campos son validos, false en caso contrario
     */
    private boolean validate() {
        if (typeCombo.getValue() == null) {
            showError("Por favor selecciona un tipo de incidente");
            return false;
        }

        String desc = descriptionArea.getText().trim();
        if (desc.isEmpty()) {
            showError("Por favor ingresa una descripcion");
            return false;
        }

        if (desc.length() < 10) {
            showError("Descripcion debe tener minimo 10 caracteres");
            return false;
        }

        if (selectedLocation == null) {
            showError("Por favor selecciona una ubicacion en el mapa");
            return false;
        }

        return true;
    }

    /**
     * Crea un nuevo objeto Report con los datos ingresados en el formulario.
     * Genera un ID unico secuencial y construye el reporte con tipo, ubicacion
     * y descripcion seleccionados.
     *
     * @return el reporte creado con los datos del formulario
     */
    private Report createReport() {
        ReportType type = typeCombo.getValue();
        String description = descriptionArea.getText().trim();

        return new Report(IDGenerator.generateSequentialID("REP"), type, selectedLocation, description);
    }

    /**
     * Muestra un dialogo de error modal con el mensaje especificado.
     *
     * @param message el mensaje de error a mostrar
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de validacion");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Muestra el dialogo modal y lo trae al frente de la ventana.
     * Imprime mensajes de depuracion para rastrear el flujo de ejecucion.
     */
    public void showDialog() {
        stage.show();
        stage.toFront();
    }

    /**
     * Oculta el dialogo sin cerrarlo completamente. Util cuando se necesita
     * cambiar temporalmente a otra ventana (como al seleccionar en el mapa).
     * Imprime mensaje de depuracion para rastrear el flujo de ejecucion.
     */
    public void hideDialog() {
        stage.hide();
    }
}
