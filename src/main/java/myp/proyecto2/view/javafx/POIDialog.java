package myp.proyecto2.view.javafx;

import java.util.Optional;
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

/**
 * Dialogo modal para crear o editar puntos de interes en el sistema.
 * Proporciona campos para ingresar nombre, tipo, descripcion y ubicacion del POI.
 */
public class POIDialog extends Dialog<PointOfInterest> {

    /** Escenario que contiene el dialogo modal. */
    private final Stage stage;

    /** Campo de texto para el nombre del punto de interes. */
    private final TextField nameField;

    /** Area de texto para la descripcion del punto de interes. */
    private final TextArea descriptionArea;

    /** ComboBox para seleccionar el tipo de punto de interes. */
    private final ComboBox<POIType> typeCombo;

    /** Etiqueta que muestra las coordenadas de la ubicacion seleccionada. */
    private final Label locationLabel;

    /** Boton para activar el modo de seleccion de ubicacion en el mapa. */
    private final Button mapClickButton;

    /** Ubicacion geografica seleccionada para el punto de interes. */
    private Location selectedLocation;

    /** Callback ejecutado cuando se solicita seleccionar ubicacion en el mapa, */
    private Consumer<Consumer<Location>> onRequestMapClick;

    /** Callback ejecutado cuando se completa o cancela el dialogo, */
    private Consumer<PointOfInterest> onResult;

    /**
     * Construye un nuevo dialogo de punto de interes para crear o editar.
     * Si se proporciona un POI existente, el dialogo se configura en modo edicion
     * con los datos precargados. Si no, se configura en modo creacion con valores
     * por defecto.
     *
     * @param poi el punto de interes a editar, o null para crear uno nuevo
     * @param defaultLocation la ubicacion por defecto si no se proporciona un POI
     */
    public POIDialog(PointOfInterest poi, Location defaultLocation) {
        this.selectedLocation = (poi != null) ? poi.getLocation() : defaultLocation;
        boolean editMode = (poi != null);

        this.stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(editMode ? "Editar punto de interes" : "Agregar nuevo punto de interes");
        stage.setResizable(false);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.TOP_LEFT);

        int row = 0;
        Label nameLabel = new Label("Nombre:");
        grid.add(nameLabel, 0, row);

        nameField = new TextField();
        nameField.setPromptText("p.e., Facultad de Ciencias");
        nameField.setText(editMode ? poi.getName() : "");
        nameField.setPrefWidth(300);
        grid.add(nameField, 1, row);
        row++;

        Label typeLabel = new Label("Tipo:");
        grid.add(typeLabel, 0, row);

        typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(POIType.values());
        typeCombo.setValue(editMode ? poi.getType() : POIType.OTHER);
        typeCombo.setPrefWidth(300);
        grid.add(typeCombo, 1, row);
        row++;

        Label descLabel = new Label("Descripcion:");
        grid.add(descLabel, 0, row);

        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Minimo 10 caracteres...");
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setPrefWidth(300);
        descriptionArea.setText(editMode ? poi.getDescription() : "");
        grid.add(descriptionArea, 1, row);
        row++;

        Label locLabel = new Label("Ubicacion:");
        grid.add(locLabel, 0, row);

        HBox locationBox = new HBox(10);
        locationBox.setAlignment(Pos.CENTER_LEFT);

        locationLabel = new Label(formatLocation(selectedLocation));
        locationLabel.setStyle("-fx-border-color: #ccc; -fx-border-width: 1; " +
                "-fx-padding: 5; -fx-background-color: #f5f5f5; -fx-min-width: 150;");

        mapClickButton = new Button("📍 Mapa");
        mapClickButton.setOnAction(e -> handleMapClick());

        locationBox.getChildren().addAll(locationLabel, mapClickButton);
        grid.add(locationBox, 1, row);
        row++;

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        Button saveButton = new Button(editMode ? "Actualizar" : "Guardar");
        saveButton.setDefaultButton(true);
        saveButton.setPrefWidth(80);
        saveButton.setOnAction(e -> handleSubmit());

        Button cancelButton = new Button("Cancelar");
        cancelButton.setCancelButton(true);
        cancelButton.setPrefWidth(80);
        cancelButton.setOnAction(e -> handleCancel());

        buttonBox.getChildren().addAll(saveButton, cancelButton);
        grid.add(buttonBox, 1, row);

        Scene scene = new Scene(grid, 500, 400);
        stage.setScene(scene);
    }

    /**
     * Maneja el evento del boton de seleccion en mapa. Solicita al controlador
     * que active el modo de seleccion en el mapa y espera a que el usuario
     * seleccione una ubicacion. Actualiza la etiqueta con las coordenadas
     * seleccionadas cuando se completa.
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
            mapClickButton.setText("📍 Mapa");
        }));

    }

    /**
     * Maneja el evento del boton de guardar/actualizar. Valida los datos ingresados,
     * crea el objeto PointOfInterest correspondiente y ejecuta el callback de resultado.
     * Cierra el dialogo si todo es exitoso.
     */
    private void handleSubmit() {
        if (!validate())
            return;

        PointOfInterest poi = createPOI();

        if (onResult != null)
            onResult.accept(poi);
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
     * Recibe el punto de interes creado/editado, o null si se cancelo.
     *
     * @param callback funcion que recibe el resultado del dialogo
     */
    public void setOnResult(Consumer<PointOfInterest> callback) {
        this.onResult = callback;
    }

    /**
     * Valida todos los campos del formulario antes de crear el punto de interes.
     * Verifica que el nombre tenga al menos 3 caracteres, que se haya seleccionado
     * un tipo y que se haya elegido una ubicacion. Muestra mensajes de error
     * especificos para cada validacion fallida.
     *
     * @return true si todos los campos son validos, false en caso contrario
     */
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

    /**
     * Crea un nuevo objeto PointOfInterest con los datos ingresados en el formulario.
     * Genera un ID unico secuencial y construye el POI con nombre, descripcion,
     * ubicacion y tipo seleccionados.
     *
     * @return el punto de interes creado con los datos del formulario
     */
    private PointOfInterest createPOI() {
        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();
        POIType type = typeCombo.getValue();

        return new PointOfInterest(IDGenerator.generateSequentialID("POI"), name, description, selectedLocation, type);
    }

    /**
     * Muestra un dialogo de error modal con el mensaje especificado.
     *
     * @param message el mensaje de error a mostrar
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Metodo estatico para mostrar un dialogo de creacion de nuevo punto de interes.
     * Configura el dialogo con una ubicacion por defecto y el callback de seleccion
     * en mapa, luego lo muestra y espera el resultado.
     *
     * @param defaultLocation ubicacion inicial sugerida
     * @param mapClickRequester callback para solicitar seleccion en mapa
     * @return Optional con el POI creado, o vacio si se cancelo
     */
    public static Optional<PointOfInterest> showNewPOI(Location defaultLocation,
            Consumer<Consumer<Location>> mapClickRequester) {
        POIDialog dialog = new POIDialog(null, defaultLocation);
        dialog.onRequestMapClick = mapClickRequester;
        return dialog.showAndWait();
    }

    /**
     * Metodo estatico para mostrar un dialogo de edicion de punto de interes existente.
     * Configura el dialogo con los datos del POI a editar y el callback de seleccion
     * en mapa, luego lo muestra y espera el resultado.
     *
     * @param poi el punto de interes a editar
     * @param mapClickRequester callback para solicitar seleccion en mapa
     * @return Optional con el POI editado, o vacio si se cancelo
     */
    public static Optional<PointOfInterest> showEditPOI(PointOfInterest poi,
            Consumer<Consumer<Location>> mapClickRequester) {
        POIDialog dialog = new POIDialog(poi, poi.getLocation());
        dialog.onRequestMapClick = mapClickRequester;
        return dialog.showAndWait();
    }

    /**
     * Muestra el dialogo modal y lo trae al frente de la ventana.
     */
    public void showDialog() {
        stage.show();
        stage.toFront();
    }

    /**
     * Oculta el dialogo sin cerrarlo completamente.
     */
    public void hideDialog() {
        stage.hide();
    }
}
