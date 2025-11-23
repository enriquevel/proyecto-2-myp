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

public class ReportListPanel extends VBox {

    private final ListView<Report> listView;
    private final Label countLabel;

    private Consumer<Report> onUpvote;
    private Consumer<Report> onDownvote;

    public ReportListPanel() {
        setSpacing(10);
        setPadding(new Insets(15));
        setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1;");
        setPrefWidth(320);

        HBox header = new HBox(10);

        Label title = new Label("Incident Reports");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        this.countLabel = new Label("(0)");
        this.countLabel.setStyle("-fx-text-fill: #757575;");

        header.getChildren().addAll(title, this.countLabel);

        this.listView = new ListView<>();
        this.listView.setCellFactory(param -> new ReportCell());
        this.listView.setPlaceholder(new Label("No reports in area"));
        VBox.setVgrow(this.listView, Priority.ALWAYS);

        getChildren().addAll(header, new Separator(), this.listView);
    }

    public void setReports(List<Report> reports) {
        this.listView.getItems().clear();
        if (reports != null) {
            this.listView.getItems().addAll(reports);
            this.countLabel.setText("(" + reports.size() + ")");
        } else
            this.countLabel.setText("(0)");
    }

    public void clearReports() {
        this.listView.getItems().clear();
        this.countLabel.setText("(0)");
    }

    public void setOnUpvote(Consumer<Report> callback) {
        this.onUpvote = callback;
    }

    public void setOnDownvote(Consumer<Report> callback) {
        this.onDownvote = callback;
    }

    private class ReportCell extends ListCell<Report> {

        @Override
        protected void updateItem(Report report, boolean empty) {
            super.updateItem(report, empty);

            if (empty || report == null) {
                setGraphic(null);
                return;
            }

            VBox box = new VBox(5);
            box.setPadding(new Insets(8));

            // Header
            HBox header = new HBox(8);

            Label type = new Label(report.getType().getDisplayName());
            type.setStyle("-fx-font-weight: bold;");

            HBox.setHgrow(new Region(), Priority.ALWAYS);
            header.getChildren().addAll(type, new Region());

            // Description
            String desc = report.getDescription();
            if (desc.length() > 80)
                desc = desc.substring(0, 77) + "...";
            Label description = new Label(desc);
            description.setWrapText(true);
            description.setStyle("-fx-font-size: 11;");

            // Voting
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
