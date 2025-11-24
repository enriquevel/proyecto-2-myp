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
import myp.proyecto2.model.domain.Report;

/**
 * Panel visual que muestra la lista de reportes de incidentes en el area.
 * Proporciona una interfaz para visualizar todos los reportes activos con
 * su tipo, descripcion y sistema de votacion.
 */
public class ReportListPanel extends VBox {

    /** ListView que contiene y muestra todos los reportes de incidentes. */
    private final ListView<Report> listView;

    /** Etiqueta que muestra el conteo total de reportes entre parentesis. */
    private final Label countLabel;

    /** Callback ejecutado cuando se da voto positivo a un reporte. */
    private Consumer<Report> onUpvote;

    /** Callback ejecutado cuando se da voto negativo a un reporte. */
    private Consumer<Report> onDownvote;

    /**
     * Construye un nuevo panel de reportes con su interfaz completa.
     * Inicializa el encabezado con titulo y contador de reportes, configura
     * el ListView con celdas personalizadas que incluyen sistema de votacion,
     * y establece el layout y estilos visuales.
     */
    public ReportListPanel() {
        setSpacing(10);
        setPadding(new Insets(15));
        setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1;");
        setPrefWidth(320);

        HBox header = new HBox(10);

        Label title = new Label("Reportes de incidentes");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        this.countLabel = new Label("(0)");
        this.countLabel.setStyle("-fx-text-fill: #757575;");

        header.getChildren().addAll(title, this.countLabel);

        this.listView = new ListView<>();
        this.listView.setCellFactory(param -> new ReportCell());
        this.listView.setPlaceholder(new Label("No hay reportes en el area"));
        VBox.setVgrow(this.listView, Priority.ALWAYS);

        getChildren().addAll(header, new Separator(), this.listView);
    }

    /**
     * Establece la lista de reportes a mostrar en el panel y actualiza
     * el contador. Reemplaza todo el contenido actual del ListView con
     * los nuevos reportes.
     *
     * @param reports lista de reportes a mostrar, o null para limpiar
     */
    public void setReports(List<Report> reports) {
        this.listView.getItems().clear();
        if (reports != null) {
            this.listView.getItems().addAll(reports);
            this.countLabel.setText("(" + reports.size() + ")");
        } else
            this.countLabel.setText("(0)");
    }

    /**
     * Limpia todos los reportes del panel y reinicia el contador a cero.
     */
    public void clearReports() {
        this.listView.getItems().clear();
        this.countLabel.setText("(0)");
    }

    /**
     * Configura el callback que se ejecuta cuando el usuario da voto
     * positivo a un reporte mediante el boton de flecha arriba.
     *
     * @param callback funcion que recibe el reporte que recibio voto positivo
     */
    public void setOnUpvote(Consumer<Report> callback) {
        this.onUpvote = callback;
    }

    /**
     * Configura el callback que se ejecuta cuando el usuario da voto
     * negativo a un reporte mediante el boton de flecha abajo.
     *
     * @param callback funcion que recibe el reporte que recibio voto negativo
     */
    public void setOnDownvote(Consumer<Report> callback) {
        this.onDownvote = callback;
    }

    /**
     * Clase interna que define como se renderiza cada celda de la lista de reportes.
     */
    private class ReportCell extends ListCell<Report> {

        /**
         * Actualiza el contenido visual de la celda cuando cambia el item asociado.
         * Si la celda esta vacia, limpia el grafico. Si contiene un reporte,
         * crea el layout con tipo, descripcion truncada y botones de votacion.
         *
         * @param report el reporte a mostrar en esta celda
         * @param empty true si la celda esta vacia, false si contiene un item
         */
        @Override
        protected void updateItem(Report report, boolean empty) {
            super.updateItem(report, empty);

            if (empty || report == null) {
                setGraphic(null);
                return;
            }

            VBox box = new VBox(5);
            box.setPadding(new Insets(8));

            HBox header = new HBox(8);

            Label type = new Label(report.getType().getDisplayName());
            type.setStyle("-fx-font-weight: bold;");

            HBox.setHgrow(new Region(), Priority.ALWAYS);
            header.getChildren().addAll(type, new Region());

            String desc = report.getDescription();
            if (desc.length() > 80)
                desc = desc.substring(0, 77) + "...";
            Label description = new Label(desc);
            description.setWrapText(true);
            description.setStyle("-fx-font-size: 11;");

            HBox voting = new HBox(8);

            Button upvote = new Button("▲");
            upvote.setStyle("-fx-font-size: 10; -fx-padding: 2 6;");
            upvote.setOnAction(e -> {
                if (onUpvote != null) onUpvote.accept(report);
            });

            Label votes = new Label(String.valueOf(report.getNetVotes()));
            votes.setStyle("-fx-font-weight: bold;");

            Button downvote = new Button("▼");
            downvote.setStyle("-fx-font-size: 10; -fx-padding: 2 6;");
            downvote.setOnAction(e -> {
                if (onDownvote != null) onDownvote.accept(report);
            });

            HBox.setHgrow(new javafx.scene.layout.Region(), Priority.ALWAYS);
            voting.getChildren().addAll(upvote, votes, downvote, new Region());

            box.getChildren().addAll(header, description, voting);
            setGraphic(box);
        }
    }
}
