package myp.proyecto2.view.javafx;

import java.util.List;
import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import myp.proyecto2.model.domain.PointOfInterest;

/**
 * Panel visual que muestra la lista de puntos de interes guardados en el sistema.
 * Permite seleccionarlos para centrar el mapa en ellos, eliminarlos mediante
 * un boton de eliminar, y agregar nuevos POIs mediante un boton de agregar.
 */
public class POIPanel extends VBox {

    /**
     * ListView que contiene y muestra todos los puntos de interes.
     */
    private final ListView<PointOfInterest> listView;

    /**
     * Callback ejecutado cuando se selecciona un punto de interes de la lista.
     */
    private Consumer<PointOfInterest> onPOISelected;

    /**
     * Callback ejecutado cuando se solicita eliminar un punto de interes.
     */
    private Consumer<PointOfInterest> onPOIDelete;

    /**
     * Callback ejecutado cuando se presiona el boton de agregar nuevo POI.
     */
    private Runnable onAddPOI;

    /**
     * Construye un nuevo panel de puntos de interes con su interfaz completa.
     * Inicializa el encabezado con titulo y boton de agregar, configura el
     * ListView con celdas personalizadas, y establece el layout y estilos visuales.
     */
    public POIPanel() {
        setSpacing(10);
        setPadding(new Insets(15));
        setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1;");
        setPrefWidth(320);

        HBox header = new HBox(10);

        Label title = new Label("Ubicaciones guardadas");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Button addButton = new Button("+ Agregar");
        addButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-weight: bold;");
        addButton.setOnAction(e -> {
            if (this.onAddPOI != null) this.onAddPOI.run();
        });

        HBox.setHgrow(new Region(), Priority.ALWAYS);
        header.getChildren().addAll(title, new Region(), addButton);

        this.listView = new ListView<>();
        this.listView.setCellFactory(param -> new POICell());
        this.listView.setPlaceholder(new Label("Sin ubicaciones guardadas"));
        VBox.setVgrow(this.listView, Priority.ALWAYS);

        this.listView.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && this.onPOISelected != null)
                this.onPOISelected.accept(newVal);
        });

        getChildren().addAll(header, new Separator(), this.listView);
    }

    /**
     * Establece la lista de puntos de interes a mostrar en el panel.
     * Reemplaza todo el contenido actual del ListView con los nuevos POIs.
     *
     * @param pois lista de puntos de interes a mostrar, o null para limpiar
     */
    public void setPOIs(List<PointOfInterest> pois) {
        this.listView.getItems().clear();
        if (pois != null)
            this.listView.getItems().addAll(pois);
    }

    /**
     * Limpia todos los puntos de interes del panel, dejando la lista vacia.
     */
    public void clearPOIs() {
        this.listView.getItems().clear();
    }

    /**
     * Configura el callback que se ejecuta cuando el usuario selecciona
     * un punto de interes de la lista.
     *
     * @param callback funcion que recibe el POI seleccionado
     */
    public void setOnPOISelected(Consumer<PointOfInterest> callback) {
        this.onPOISelected = callback;
    }

    /**
     * Configura el callback que se ejecuta cuando el usuario presiona
     * el boton de eliminar de un punto de interes.
     *
     * @param callback funcion que recibe el POI a eliminar
     */
    public void setOnPOIDelete(Consumer<PointOfInterest> callback) {
        this.onPOIDelete = callback;
    }

    /**
     * Configura el callback que se ejecuta cuando el usuario presiona
     * el boton de agregar nuevo punto de interes.
     *
     * @param callback funcion sin parametros a ejecutar
     */
    public void setOnAddPOI(Runnable callback) {
        this.onAddPOI = callback;
    }

    /**
     * Clase interna que define como se renderiza cada celda de la lista de POIs.
     * Muestra el nombre en negrita, el tipo en texto gris pequeno, y un boton
     * rojo de eliminacion a la derecha. El layout se organiza horizontalmente
     * con la informacion del POI creciendo para ocupar el espacio disponible.
     */
    private class POICell extends ListCell<PointOfInterest> {

        /**
         * Actualiza el contenido visual de la celda cuando cambia el item asociado.
         * Si la celda esta vacia, limpia el grafico. Si contiene un POI, crea
         * el layout con nombre, tipo y boton de eliminar.
         *
         * @param poi el punto de interes a mostrar en esta celda
         * @param empty true si la celda esta vacia, false si contiene un item
         */
        @Override
        protected void updateItem(PointOfInterest poi, boolean empty) {
            super.updateItem(poi, empty);

            if (empty || poi == null) {
                setGraphic(null);
                return;
            }

            HBox box = new HBox(10);
            box.setPadding(new Insets(8));

            VBox info = new VBox(3);

            Label name = new Label(poi.getName());
            name.setStyle("-fx-font-weight: bold;");

            Label type = new Label(poi.getType().getDisplayName());
            type.setStyle("-fx-text-fill: #757575; -fx-font-size: 11;");

            info.getChildren().addAll(name, type);
            HBox.setHgrow(info, Priority.ALWAYS);

            Button delete = new Button("x");
            delete.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; " +
                    "-fx-font-size: 16; -fx-font-weight: bold; -fx-padding: 2 8;");
            delete.setOnAction(e -> {
                if (onPOIDelete != null)
                    onPOIDelete.accept(poi);
            });

            box.getChildren().addAll(info, delete);
            setGraphic(box);
        }
    }
}
