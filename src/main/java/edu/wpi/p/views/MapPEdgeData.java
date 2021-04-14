package edu.wpi.p.views;

import edu.wpi.p.App;
import edu.wpi.p.csv.CSVData;
import edu.wpi.p.csv.CSVHandler;
import edu.wpi.p.database.CSVDBConverter;
import edu.wpi.p.database.DBTable;
import edu.wpi.p.database.DatabaseInterface;
import edu.wpi.p.database.Edge;
import javafx.beans.property.SimpleStringProperty;
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
import java.util.List;

public class MapPEdgeData{

    @FXML
    private Button addEdgeBtn;
    @FXML
    private Button editEdgeBtn;
    @FXML
    private Button deleteEdgeBtn;
    @FXML
    private TableView<Edge> edgeDataTableView;
    @FXML
    private TableColumn<Edge, String> edgeIDCol;
    @FXML
    private TableColumn<Edge, String> startNodeCol;
    @FXML
    private TableColumn<Edge, String> endNodeCol;
    @FXML
    private Button homeButton;

    @FXML
    private void homeButtonAc(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private DBTable dbTable = new DBTable();
    private List<Edge> edgeDataList;

    @FXML
    private void initialize() throws Exception {
        DatabaseInterface.init();
        CSVData nodeData = CSVHandler.readCSVFile("src/main/java/AStar/L1Nodes.csv");
        CSVData edgeData = CSVHandler.readCSVFile("src/main/java/AStar/L1Edges.csv");
        dbTable = CSVDBConverter.tableFromCSVData(nodeData, edgeData);
        edgeDataList = dbTable.getEdges();
        //set up the columns in the table
        edgeIDCol.setCellValueFactory(new PropertyValueFactory<Edge, String>("edgeID"));
        startNodeCol.setCellValueFactory(new PropertyValueFactory<Edge, String>("startNode"));
        endNodeCol.setCellValueFactory(new PropertyValueFactory<Edge, String>("endNode"));

        //load in the edge data
        edgeDataTableView.setItems(getEdgeData());
    }

    private ObservableList<Edge> getEdgeData(){
        ObservableList<Edge> edges = FXCollections.observableArrayList();
        for (Edge e: edgeDataList)
            edges.add(e);
        return edges;
    }

    @FXML
    private void addEdgeAc(ActionEvent actionEvent) {

    }

    @FXML
    private void editEdgeAc(ActionEvent actionEvent) {
    }

    @FXML
    private void deleteEdgeAc(ActionEvent actionEvent) {
    }
}
