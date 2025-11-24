package myp.proyecto2.view.javafx;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import myp.proyecto2.model.domain.RoutePreference;
import myp.proyecto2.model.domain.TransportMode;

/**
 * Clase que define un panel de control con botones y grupos para construir rutas.
 */
public class ControlPanel extends VBox {

    /** El selector de origen. */
    private final LocationSelector originSelector;

    /** El selector de destino. */
    private final LocationSelector destinationSelector;

    /** El boton para el modo caminar. */
    private final RadioButton walkingRadio;

    /** El boton para el modo bicicleta. */
    private final RadioButton bicyclingRadio;

    /** El boton para el modo carro. */
    private final RadioButton drivingRadio;

    /** El boton para el modo transporte publico. */
    private final RadioButton transitRadio;

    /** El grupo de modos de transporte. */
    private final ToggleGroup modeGroup;

    /** La caja de preferencias de rutas. */
    private final ComboBox<RoutePreference> preferenceCombo;

    /** El boton de encontrar rutas. */
    private final Button findButton;

    /** El boton de restablecer campos. */
    private final Button clearButton;

    /** Una funcion callback para encontrar rutas. */
    private Runnable onFindRoutes;

    /** Una funcion callback para restablecer campos. */
    private Runnable onClear;

    /**
     * Constructor principal para construir un panel de control.
     */
    public ControlPanel() {
        setSpacing(10);
        setPadding(new Insets(15));
        setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1;");
        setPrefWidth(320);

        // Title
        Label title = new Label("Configuracion de rutas");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Origin
        Label originLabel = new Label("Origen:");
        originLabel.setStyle("-fx-font-weight: bold;");
        this.originSelector = new LocationSelector("Selecciona punto de partida...");

        // Destination
        Label destLabel = new Label("Destino:");
        destLabel.setStyle("-fx-font-weight: bold;");
        this.destinationSelector = new LocationSelector("Selecciona punto de destino...");

        // Transport modes
        Label modesLabel = new Label("Modos de transporte:");
        modesLabel.setStyle("-fx-font-weight: bold;");

        this.modeGroup = new ToggleGroup();

        this.walkingRadio = new RadioButton("Caminando");
        this.walkingRadio.setToggleGroup(modeGroup);
        this.walkingRadio.setSelected(true);

        this.bicyclingRadio = new RadioButton("Bicicleta");
        this.bicyclingRadio.setToggleGroup(modeGroup);

        this.drivingRadio = new RadioButton("Manejando");
        this.drivingRadio.setToggleGroup(modeGroup);

        this.transitRadio = new RadioButton("Transporte publico");
        this.transitRadio.setToggleGroup(modeGroup);

        // Preference
        Label prefLabel = new Label("Preferencia de ruta:");
        prefLabel.setStyle("-fx-font-weight: bold;");

        this.preferenceCombo = new ComboBox<>();
        this.preferenceCombo.getItems().addAll(RoutePreference.values());
        this.preferenceCombo.setValue(RoutePreference.BALANCED);
        this.preferenceCombo.setMaxWidth(Double.MAX_VALUE);

        // Buttons
        HBox buttonBox = new HBox(10);

        this.findButton = new Button("Encontrar rutas");
        this.findButton.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; -fx-font-weight: bold;");
        this.findButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(this.findButton, Priority.ALWAYS);
        this.findButton.setOnAction(e -> {
            if (validate() && this.onFindRoutes != null)
                this.onFindRoutes.run();
        });

        this.clearButton = new Button("Restablecer");
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

    /**
     * Devuelve el selector de origen del panel.
     *
     * @return el selector de origen del panel.
     */
    public LocationSelector getOriginSelector() {
        return this.originSelector;
    }

    /**
     * Devuelve el selector de destino del panel.
     *
     * @return el selector de destino del panel
     */
    public LocationSelector getDestinationSelector() {
        return this.destinationSelector;
    }

    /**
     * Devuelve el modo de transporte seleccionado en el panel.
     *
     * @return el modo de transporte seleccionado en el panel
     */
    public TransportMode getSelectedMode() {
        Toggle selected = modeGroup.getSelectedToggle();
        if (selected == null)
            return null;

        if (selected == walkingRadio)
            return TransportMode.WALKING;
        if (selected == bicyclingRadio)
            return TransportMode.BICYCLING;
        if (selected == drivingRadio)
            return TransportMode.DRIVING;
        if (selected == transitRadio)
            return TransportMode.BUS;

        return null;

    }

    /**
     * Devuelve la preferencia de ruta seleccionada en el panel.
     *
     * @return la preferencia de ruta seleccionada
     */
    public RoutePreference getPreference() {
        return this.preferenceCombo.getValue();
    }

    /**
     * Restablece los campos del panel.
     */
    public void clearSelections() {
        this.originSelector.clear();
        this.destinationSelector.clear();
    }

    /**
     * Define una funcion callback utilizada al mandar a encontrar rutas con los campos del panel.
     *
     * @param callback la funcion a establecer
     */
    public void setOnFindRoutes(Runnable callback) {
        this.onFindRoutes = callback;
    }

    /**
     * Define una funcion callback utilizada al restablecer los campos del panel.
     *
     * @param callback la funcion a establecer
     */
    public void setOnClear(Runnable callback) {
        this.onClear = callback;
    }

    /**
     * Verifica que todos los campos esten completos.
     *
     * @return <code>true</code> si todos los campos son validos, <code>false</code> en otro caso
     */
    private boolean validate() {
        if (!this.originSelector.hasSelection()) {
            showAlert("Por favor selecciona un punto de origen");
            return false;
        }

        if (!this.destinationSelector.hasSelection()) {
            showAlert("Por favor selecciona un punto de destino");
            return false;
        }

        if (getSelectedMode() == null) {
            showAlert("Por favor selecciona un modo de transporte");
            return false;
        }

        return true;
    }

    /**
     * Muestra una alerta de validacion
     *
     * @param message el mensaje a mostrar
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error de validacion");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
