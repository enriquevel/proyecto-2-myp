package myp.proyecto2.view.javafx;

import java.util.List;
import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import myp.proyecto2.model.domain.ScoredRoute;
import myp.proyecto2.model.domain.TransportMode;
import myp.proyecto2.model.domain.builder.Route;

/**
 * Panel visual que muestra la lista de opciones de rutas calculadas.
 * Proporciona una interfaz para visualizar todas las rutas encontradas con
 * su duracion, distancia, modos de transporte y puntuacion.
 */
public class RouteListPanel extends VBox {

    /** ListView que contiene y muestra todas las rutas puntuadas. */
    private final ListView<ScoredRoute> listView;

    /** Callback ejecutado cuando se selecciona una ruta de la lista. */
    private Consumer<Route> onRouteSelected;

    /**
     * Construye un nuevo panel de rutas con su interfaz completa.
     * Inicializa el encabezado con titulo y configura el ListView con celdas
     * personalizadas que muestran detalles de cada ruta.
     */
    public RouteListPanel() {
        setSpacing(10);
        setPadding(new Insets(15));
        setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1;");
        setPrefWidth(320);

        Label title = new Label("Opciones de ruta");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        this.listView = new ListView<>();
        this.listView.setCellFactory(param -> new RouteCell());
        this.listView.setPlaceholder(new Label("No hay rutas por mostrar"));
        VBox.setVgrow(this.listView, Priority.ALWAYS);

        this.listView.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && this.onRouteSelected != null)
                this.onRouteSelected.accept(newVal.getRoute());
        });

        getChildren().addAll(title, new Separator(), this.listView);
    }

    /**
     * Establece la lista de rutas puntuadas a mostrar en el panel.
     * Reemplaza todo el contenido actual del ListView con las nuevas rutas
     * y selecciona automaticamente la primera ruta si existe alguna.
     *
     * @param routes lista de rutas puntuadas a mostrar, o null para limpiar
     */
    public void setRoutes(List<ScoredRoute> routes) {
        this.listView.getItems().clear();
        if (routes != null) {
            this.listView.getItems().addAll(routes);
            if (!routes.isEmpty())
                this.listView.getSelectionModel().select(0);
        }
    }

    /**
     * Limpia todas las rutas del panel, dejando la lista vacia.
     */
    public void clearRoutes() {
        this.listView.getItems().clear();
    }

    /**
     * Configura el callback que se ejecuta cuando el usuario selecciona
     * una ruta de la lista.
     *
     * @param callback funcion que recibe la ruta seleccionada
     */
    public void setOnRouteSelected(Consumer<Route> callback) {
        this.onRouteSelected = callback;
    }

    /**
     * Clase interna que define como se renderiza cada celda de la lista de rutas.
     */
    private class RouteCell extends ListCell<ScoredRoute> {

        /**
         * Actualiza el contenido visual de la celda cuando cambia el item asociado.
         *
         * @param item la ruta puntuada a mostrar en esta celda
         * @param empty true si la celda esta vacia, false si contiene un item
         */
        @Override
        protected void updateItem(ScoredRoute item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
                return;
            }

            Route route = item.getRoute();

            VBox box = new VBox(5);
            box.setPadding(new Insets(8));

            // Header
            HBox header = new HBox(10);

            Label rank = new Label("Ruta " + (getIndex() + 1));
            rank.setStyle("-fx-font-weight: bold;");

            Label duration = new Label(formatDuration((int)route.getTotalDurationMinutes()));
            duration.setStyle("-fx-font-weight: bold;");

            Label distance = new Label(String.format("%.1f km", route.getTotalDistanceKilometers()));
            distance.setStyle("-fx-text-fill: #757575;");

            HBox.setHgrow(new Region(), Priority.ALWAYS);
            header.getChildren().addAll(rank, duration, new Region(), distance);

            HBox modes = new HBox(5);
            for (TransportMode mode : route.getTransportModes()) {
                Label modeLabel = new Label(mode.getDisplayName());
                modeLabel.setStyle("-fx-background-color: " + getModeColor(mode) +
                        "; -fx-text-fill: white; -fx-padding: 2 6; -fx-background-radius: 3; -fx-font-size: 10;");
                modes.getChildren().add(modeLabel);
            }

            Label score = new Label(String.format("Puntuacion: %.1f (%s)", item.getScore(), item.getScorer()));
            score.setStyle("-fx-font-size: 11; -fx-text-fill: #757575;");

            box.getChildren().addAll(header, modes, score);

            if (isSelected())
                box.setStyle("-fx-background-color: #e3f2fd; -fx-border-color: #2196f3; -fx-border-width: 0 0 0 4;");

            setGraphic(box);
        }

        /**
         * Formatea la duracion en minutos a un texto legible.
         * Si la duracion es mayor o igual a 60 minutos, muestra horas y minutos.
         * Si es menor a 60 minutos, muestra solo los minutos.
         *
         * @param minutes la duracion total en minutos
         * @return texto formateado con la duracion
         */
        private String formatDuration(int minutes) {
            if (minutes >= 60)
                return String.format("%d h %d min", minutes / 60, minutes % 60);
            return minutes + " min";
        }

        /**
         * Obtiene el color hexadecimal asociado a un modo de transporte.
         * Cada modo tiene un color distintivo para facilitar su identificacion visual.
         *
         * @param mode el modo de transporte
         * @return codigo de color hexadecimal como String
         */
        private String getModeColor(TransportMode mode) {
            return switch (mode) {
                case WALKING -> "#4caf50";
                case BICYCLING -> "#2196f3";
                case DRIVING -> "#f44336";
                case BUS -> "#9c27b0";
            };
        }
    }

}
