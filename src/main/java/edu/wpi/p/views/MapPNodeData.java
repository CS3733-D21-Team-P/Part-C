package edu.wpi.p.views;

import AStar.Node;
import edu.wpi.p.App;
import edu.wpi.p.csv.CSVData;
import edu.wpi.p.csv.CSVHandler;
import edu.wpi.p.database.CSVDBConverter;
import edu.wpi.p.database.DBTable;
import edu.wpi.p.database.DatabaseInterface;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.List;

public class MapPNodeData {

    @FXML private TextField nodeFilepathField;
    @FXML private Button importNodeCSV;
    @FXML private Button saveNodeCSV;

    @FXML private Button addNodeBtn;
    @FXML private Button editNodeBtn;
    @FXML private Button deleteNodeBtn;

    @FXML private TableView<Node> nodeDataTableView;
    @FXML private TableColumn<Node, String> nodeIDCol;
    @FXML private TableColumn<Node, Integer> nodeXCoordCol;
    @FXML private TableColumn<Node, Integer> nodeYCoordCol;
    @FXML private TableColumn<Node, String> nodeFloorCol;
    @FXML private TableColumn<Node, String> nodeBuildingCol;
    @FXML private TableColumn<Node, String> nodeTypeCol;
    @FXML private TableColumn<Node, String> nodeLongNameCol;
    @FXML private TableColumn<Node, String> nodeShortNameCol;

    @FXML private Button homeButton;

    public MapPNodeData() throws Exception {
    }

    public void homeButtonAc(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private DBTable dbTable = new DBTable();
    private List<Node> nodeDataList;



    @FXML
    private void initialize() throws Exception {
        CSVData nodeData = CSVHandler.readCSVFile("src/main/java/AStar/L1Nodes.csv");
        CSVData edgeData = CSVHandler.readCSVFile("src/main/java/AStar/L1Edges.csv");
        dbTable = CSVDBConverter.tableFromCSVData(nodeData, edgeData);
        nodeDataList = dbTable.getNodes();

        //set up the columns in the table
        nodeIDCol.setCellValueFactory(new PropertyValueFactory<Node, String>("id"));
        nodeXCoordCol.setCellValueFactory(new PropertyValueFactory<>("xcoord"));
        nodeYCoordCol.setCellValueFactory(new PropertyValueFactory<>("ycoord"));
        nodeFloorCol.setCellValueFactory(new PropertyValueFactory<Node, String>("floor"));
        nodeBuildingCol.setCellValueFactory(new PropertyValueFactory<Node, String>("building"));
        nodeTypeCol.setCellValueFactory(new PropertyValueFactory<Node, String>("type"));
        nodeLongNameCol.setCellValueFactory(new PropertyValueFactory<Node, String>("name"));
        nodeShortNameCol.setCellValueFactory(new PropertyValueFactory<Node, String>("shortName"));

        //load in the edge data
        nodeDataTableView.setItems(getNodeData());
    }

    private ObservableList<Node> getNodeData(){
        ObservableList<Node> nodes = FXCollections.observableArrayList();
        for (Node n : nodeDataList){
            nodes.add(n);
        }
        return nodes;
    }

    public void addNodeAc(ActionEvent actionEvent) {
    }

    public void editNodeAc(ActionEvent actionEvent) {
    }

    public void deleteNodeAc(ActionEvent actionEvent) {
    }

    @FXML
    private void importNodeCSVBtn(ActionEvent actionEvent) throws Exception {
        CSVData nodeData = CSVHandler.readCSVFile(nodeFilepathField.getText());
        CSVData edgeData = CSVHandler.readCSVFile("src/main/java/AStar/L1Edges.csv");
        dbTable = CSVDBConverter.tableFromCSVData(nodeData, edgeData);
        nodeDataList = dbTable.getNodes();
        nodeDataTableView.setItems(getNodeData());
    }

    @FXML
    private void exportNodeCSVBtn(ActionEvent actionEvent) {
        CSVData newNodes = CSVDBConverter.csvNodesFromTable(dbTable);
        CSVData newEdges = CSVDBConverter.csvEdgesFromTable(dbTable);
        CSVHandler.writeCSVData(newNodes, "newNodes.csv");
        CSVHandler.writeCSVData(newEdges, "newEdges.csv");
    }
}
