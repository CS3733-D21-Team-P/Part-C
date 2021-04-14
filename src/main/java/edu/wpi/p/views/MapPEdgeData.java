package edu.wpi.p.views;

import edu.wpi.p.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


import java.io.IOException;

public class MapPEdgeData {

    @FXML
    private Button addEdgeBtn;
    @FXML
    private Button editEdgeBtn;
    @FXML
    private Button deleteEdgeBtn;
    @FXML
    private TableView edgeDataTableView;
    @FXML
    private Button homeButton;


    public void homeButtonAc(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void fillTable(Stage stage) {
        //Label for education
        Label label = new Label("Edge Data:");
        Font font = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12);
        label.setFont(font);
        //Creating a table view
        TableView<EdgeData> table = new TableView<EdgeData>();
        final ObservableList<EdgeData> data = FXCollections.observableArrayList(
                new EdgeData("a", "a", "a"),
                new EdgeData("a", "a", "a"),
                new EdgeData("a", "a", "a")
                );
        //Creating columns
        TableColumn edgeIDCol = new TableColumn("EdgeID");
        edgeIDCol.setCellValueFactory(new PropertyValueFactory<>("edgeID"));
        TableColumn startNodeCol = new TableColumn("StartNode");
        startNodeCol.setCellValueFactory(new PropertyValueFactory("startNode"));
        TableColumn endNodeCol = new TableColumn("EndNode");
        endNodeCol.setCellValueFactory(new PropertyValueFactory("endNode"));
        //Adding data to the table
        ObservableList<String> list = FXCollections.observableArrayList();
        table.setItems(data);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getColumns().addAll(edgeIDCol, startNodeCol, endNodeCol);
        //Setting the size of the table
        table.setMaxSize(350, 200);
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 50, 50, 60));
        vbox.getChildren().addAll(label, table);
        //Setting the scene
        Scene scene = new Scene(vbox, 595, 230);
        stage.setTitle("Edge ID");
        stage.setScene(scene);
        stage.show();
    }

}
