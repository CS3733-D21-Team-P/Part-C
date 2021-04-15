package edu.wpi.p.views;

import edu.wpi.p.App;
import edu.wpi.p.csv.CSVData;
import edu.wpi.p.csv.CSVHandler;
import edu.wpi.p.database.CSVDBConverter;
import edu.wpi.p.database.DBTable;
import edu.wpi.p.database.Edge;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.IOException;
import java.util.List;

public class MapPEdgeData{

    public TextField textFieldEndNode;
    public TextField textFeildStartNode;
    public TextField textFieldEdgeID;
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
        edgeDataTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        edgeDataTableView.getSelectionModel().setCellSelectionEnabled(true);
        edgeDataTableView.setEditable(true);
        edgeIDCol.setCellFactory(TextFieldTableCell.forTableColumn());
        startNodeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        endNodeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    private ObservableList<Edge> getEdgeData(){
        ObservableList<Edge> edges = FXCollections.observableArrayList();
        for (Edge e: edgeDataList)
            edges.add(e);
        return edges;
    }

    @FXML
    private void addEdgeAc(ActionEvent actionEvent) {
        Edge edge = new Edge(textFieldEdgeID.getText(), textFeildStartNode.getText(), textFieldEndNode.getText());
        edgeDataTableView.getItems().add(edge);

        DBTable dbTable = new DBTable();
        dbTable.addEdge(textFieldEdgeID.getText(),textFeildStartNode.getText(), textFieldEndNode.getText());
    }

    @FXML
    private void editStartNode(TableColumn.CellEditEvent<Edge, String> startNodeEditEvent) {
        Edge edge = edgeDataTableView.getSelectionModel().getSelectedItem();
        edge.setStartNode(startNodeEditEvent.getNewValue());

        //Find Edge
        TablePosition edgeIDPos = edgeDataTableView.getSelectionModel().getSelectedCells().get(0);
        int edgeIDRow = edgeIDPos.getRow();
        Edge edgeID = edgeDataTableView.getItems().get(edgeIDRow);
        //Update in DB
        DBTable dbTable = new DBTable();
        dbTable.updateEdge(edgeID.getEdgeID(), edgeID);
    }

    @FXML
    private void editEndNode(TableColumn.CellEditEvent<Edge, String> endNodeEditEvent) {
        Edge edge = edgeDataTableView.getSelectionModel().getSelectedItem();
        edge.setEndNode(endNodeEditEvent.getNewValue());

        //Find Edge
        TablePosition edgeIDPos = edgeDataTableView.getSelectionModel().getSelectedCells().get(0);
        int edgeIDRow = edgeIDPos.getRow();
        Edge edgeID = edgeDataTableView.getItems().get(edgeIDRow);
        //Update in DB
        DBTable dbTable = new DBTable();
        dbTable.updateEdge(edgeID.getEdgeID(), edgeID);

    }

    @FXML
    private void deleteEdgeAc(ActionEvent actionEvent) {
        //Find Edge
        TablePosition edgeIDPos = edgeDataTableView.getSelectionModel().getSelectedCells().get(0);
        int edgeIDRow = edgeIDPos.getRow();
        Edge edge = edgeDataTableView.getItems().get(edgeIDRow);
        //Remove from DB
        DBTable dbTable = new DBTable();
        dbTable.removeEdge(edge.getStartNode(), edge.getEndNode());
        //Remove from TableView
        edgeDataTableView.getItems().removeAll(edgeDataTableView.getSelectionModel().getSelectedItem());
    }
}
