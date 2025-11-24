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

public class POIPanel extends VBox {

    private final ListView<PointOfInterest> listView;

    private Consumer<PointOfInterest> onPOISelected;
    private Consumer<PointOfInterest> onPOIDelete;
    private Runnable onAddPOI;

    public POIPanel() {
        setSpacing(10);
        setPadding(new Insets(15));
        setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1;");
        setPrefWidth(320);

        HBox header = new HBox(10);

        Label title = new Label("Saved Locations");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Button addButton = new Button("+ Add");
        addButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-weight: bold;");
        addButton.setOnAction(e -> {
            if (this.onAddPOI != null) this.onAddPOI.run();
        });

        HBox.setHgrow(new Region(), Priority.ALWAYS);
        header.getChildren().addAll(title, new Region(), addButton);

        this.listView = new ListView<>();
        this.listView.setCellFactory(param -> new POICell());
        this.listView.setPlaceholder(new Label("No saved locations"));
        VBox.setVgrow(this.listView, Priority.ALWAYS);

        this.listView.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && this.onPOISelected != null)
                this.onPOISelected.accept(newVal);
        });

        getChildren().addAll(header, new Separator(), this.listView);
    }

    public void setPOIs(List<PointOfInterest> pois) {
        this.listView.getItems().clear();
        if (pois != null)
            this.listView.getItems().addAll(pois);
    }

    public void clearPOIs() {
        this.listView.getItems().clear();
    }

    public void setOnPOISelected(Consumer<PointOfInterest> callback) {
        this.onPOISelected = callback;
    }

    public void setOnPOIDelete(Consumer<PointOfInterest> callback) {
        this.onPOIDelete = callback;
    }

    public void setOnAddPOI(Runnable callback) {
        this.onAddPOI = callback;
    }

    private class POICell extends ListCell<PointOfInterest> {

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

            Button delete = new Button("×");
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
