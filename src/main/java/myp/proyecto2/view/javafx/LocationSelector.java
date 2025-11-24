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

/**
 * Clase para seleccionar localizaciones en el mapa, manejar sus atributos
 * y facilitar la experiencia del usuario.
 */
public class LocationSelector extends HBox {

    /** ComboBox que contiene las opciones de ubicacion disponibles. */
    private final ComboBox<LocationOption> comboBox;

    /** Boton para activar el modo de seleccion en el mapa. */
    private final Button mapClickButton;

    /** Ubicacion actualmente seleccionada por el usuario. */
    private Location selectedLocation;

    /** Callback ejecutado cuando se solicita seleccionar una ubicacion en el mapa. */
    private Consumer<Void> onMapClickRequested;

    /**
     * Construye un nuevo selector de ubicaciones con el placeholder especificado.
     * Inicializa el ComboBox y el boton de seleccion en mapa, y configura sus
     * eventos y disposicion visual.
     *
     * @param placeholder texto que se muestra cuando no hay seleccion
     */
    public LocationSelector(String placeholder) {
        setSpacing(5);

        comboBox = new ComboBox<>();
        comboBox.setPromptText(placeholder);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(comboBox, Priority.ALWAYS);

        comboBox.setOnAction(e -> {
            LocationOption option = comboBox.getValue();
            if (option != null)
                selectedLocation = option.location;
        });

        mapClickButton = new Button("@");
        mapClickButton.setTooltip(new Tooltip("Select on map"));
        mapClickButton.setOnAction(e -> {
            if (onMapClickRequested != null)
                onMapClickRequested.accept(null);
        });

        getChildren().addAll(comboBox, mapClickButton);
    }

    /**
     * Establece la lista de puntos de interes disponibles en el ComboBox.
     * Reemplaza todas las opciones existentes con los nuevos POIs proporcionados.
     *
     * @param pois lista de puntos de interes a mostrar como opciones
     */
    public void setPOIs(List<PointOfInterest> pois) {
        comboBox.getItems().clear();
        for (PointOfInterest poi : pois)
            comboBox.getItems().add(new LocationOption(poi.getName(), poi.getLocation()));
    }

    /**
     * Establece la ubicacion seleccionada actual. Si la ubicacion existe en la
     * lista de opciones, la selecciona. Si no existe, crea una opcion personalizada
     * con las coordenadas geograficas y la agrega al inicio de la lista.
     *
     * @param location la ubicacion a seleccionar, o null para limpiar la seleccion
     */
    public void setSelectedLocation(Location location) {
        this.selectedLocation = location;

        if (location != null) {
            boolean found = false;
            for (LocationOption option : comboBox.getItems()) {
                if (option.location.equals(location)) {
                    comboBox.setValue(option);
                    found = true;
                    break;
                }
            }

            if (!found) {
                String label = String.format("%.4f, %.4f", location.getLatitude(), location.getLongitude());
                LocationOption custom = new LocationOption(label, location);
                comboBox.getItems().addFirst(custom);
                comboBox.setValue(custom);
            }
        } else
            comboBox.setValue(null);
    }

    /**
     * Obtiene la ubicacion actualmente seleccionada.
     *
     * @return la ubicacion seleccionada, o null si no hay seleccion
     */
    public Location getSelectedLocation() {
        return selectedLocation;
    }

    /**
     * Verifica si hay una ubicacion seleccionada actualmente.
     *
     * @return true si hay una ubicacion seleccionada, false en caso contrario
     */
    public boolean hasSelection() {
        return selectedLocation != null;
    }

    /**
     * Limpia la seleccion actual, eliminando tanto la ubicacion seleccionada
     * como el valor del ComboBox.
     */
    public void clear() {
        selectedLocation = null;
        comboBox.setValue(null);
    }

    /**
     * Configura el callback que se ejecuta cuando el usuario solicita
     * seleccionar una ubicacion desde el mapa.
     *
     * @param callback funcion a ejecutar cuando se presiona el boton de mapa
     */
    public void setOnMapClickRequested(Consumer<Void> callback) {
        this.onMapClickRequested = callback;
    }

    /**
     * Clase interna que encapsula una opcion de ubicacion para el ComboBox.
     * Asocia una etiqueta descriptiva con una ubicacion geografica.
     *
     * @param label texto descriptivo mostrado al usuario
     * @param location ubicacion geografica asociada
     */
    private record LocationOption(String label, Location location) {
        @Override
        public String toString() {
            return label;
        }
    }
}
