package edu.wpi.p.views.map;

import edu.wpi.p.AStar.Node;
import edu.wpi.p.App;
import edu.wpi.p.csv.CSVData;
import edu.wpi.p.csv.CSVHandler;
import edu.wpi.p.database.CSVDBConverter;
import edu.wpi.p.database.DBMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.List;

public class MapPNodeData {

    @FXML private TextField tfName;
    @FXML private TextField tfbuilding;
    @FXML private TextField tfxcoord;
    @FXML private TextField tfnodeType;
    @FXML private TextField tfnodeID;
    @FXML private TextField tfycoord;
    @FXML private TextField tffloor;
    @FXML private TextField tfshortName;

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

    private DBMap dbMap = DBMap.getInstance();
    private List<Node> nodeDataList;



    @FXML
    private void initialize() throws Exception {
//        CSVData nodeData = CSVHandler.readCSVFile("src/main/java/edu.wpi.p.AStar/L1Nodes.csv");
//        CSVData edgeData = CSVHandler.readCSVFile("src/main/java/edu.wpi.p.AStar/L1Edges.csv");
//        dbTable = CSVDBConverter.tableFromCSVData(nodeData, edgeData);
        nodeDataList = dbMap.getNodes();

        //set up the columns in the table
        nodeIDCol.setCellValueFactory(new PropertyValueFactory<Node, String>("id"));
        nodeXCoordCol.setCellValueFactory(new PropertyValueFactory<Node, Integer>("xcoord"));
        nodeYCoordCol.setCellValueFactory(new PropertyValueFactory<Node, Integer>("ycoord"));
        nodeFloorCol.setCellValueFactory(new PropertyValueFactory<Node, String>("floor"));
        nodeBuildingCol.setCellValueFactory(new PropertyValueFactory<Node, String>("building"));
        nodeTypeCol.setCellValueFactory(new PropertyValueFactory<Node, String>("type"));
        nodeLongNameCol.setCellValueFactory(new PropertyValueFactory<Node, String>("name"));
        nodeShortNameCol.setCellValueFactory(new PropertyValueFactory<Node, String>("shortName"));

        //load in the edge data
        nodeDataTableView.setItems(getNodeData());
        nodeDataTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        nodeDataTableView.getSelectionModel().setCellSelectionEnabled(true);
        nodeDataTableView.setEditable(true);
        nodeIDCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nodeXCoordCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        nodeYCoordCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        nodeFloorCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nodeBuildingCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nodeTypeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nodeLongNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nodeShortNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    private ObservableList<Node> getNodeData(){
        ObservableList<Node> nodes = FXCollections.observableArrayList();
        for (Node n : nodeDataList){
            nodes.add(n);
        }
        return nodes;
    }

    public void addNodeAc(ActionEvent actionEvent) {
        Node node = new Node(tfName.getText(), tfnodeID.getText(), Integer.parseInt(tfxcoord.getText()), Integer.parseInt(tfycoord.getText()),tffloor.getText(),tfbuilding.getText(),tfnodeType.getText(), tfshortName.getText());
        nodeDataTableView.getItems().add(node);

        DBMap dbMap = DBMap.getInstance();
        dbMap.addNode(node);

    }

    public void deleteNodeAc(ActionEvent actionEvent) {
        //Find Node
        TablePosition nodeIDPos = nodeDataTableView.getSelectionModel().getSelectedCells().get(0);
        int nodeIDRow = nodeIDPos.getRow();
        Node node = nodeDataTableView.getItems().get(nodeIDRow);
        //Remove from DB
        DBMap dbMap = DBMap.getInstance();
        dbMap.removeNode(node.getId());
        //Remove from TableView
        nodeDataTableView.getItems().removeAll(nodeDataTableView.getSelectionModel().getSelectedItem());
    }

    public void Editxcoord(TableColumn.CellEditEvent<Node, Integer> xcoordEditEvent) {
        Node node = nodeDataTableView.getSelectionModel().getSelectedItem();
        node.setXcoord(xcoordEditEvent.getNewValue());

        //Find Edge
        TablePosition nodeIDPos = nodeDataTableView.getSelectionModel().getSelectedCells().get(0);
        int nodeIDRow = nodeIDPos.getRow();
        Node nodeID = nodeDataTableView.getItems().get(nodeIDRow);
        //Update in DB
        DBMap dbMap = DBMap.getInstance();
        dbMap.updateNode(nodeID);
    }

    public void Editycoord(TableColumn.CellEditEvent<Node, Integer> ycoordEditEvent) {
        Node node = nodeDataTableView.getSelectionModel().getSelectedItem();
        node.setYcoord(ycoordEditEvent.getNewValue());

        //Find Edge
        TablePosition nodeIDPos = nodeDataTableView.getSelectionModel().getSelectedCells().get(0);
        int nodeIDRow = nodeIDPos.getRow();
        Node nodeID = nodeDataTableView.getItems().get(nodeIDRow);
        //Update in DB
        DBMap dbMap = DBMap.getInstance();
        dbMap.updateNode(nodeID);
    }

    public void Editfloor(TableColumn.CellEditEvent<Node, String> floorEditEvent) {
        Node node = nodeDataTableView.getSelectionModel().getSelectedItem();
        node.setFloor(floorEditEvent.getNewValue());

        //Find Edge
        TablePosition nodeIDPos = nodeDataTableView.getSelectionModel().getSelectedCells().get(0);
        int nodeIDRow = nodeIDPos.getRow();
        Node nodeID = nodeDataTableView.getItems().get(nodeIDRow);
        //Update in DB
        DBMap dbMap = DBMap.getInstance();
        dbMap.updateNode(nodeID);
    }

    public void Editbuilding(TableColumn.CellEditEvent<Node, String> buildingEditEvent) {
        Node node = nodeDataTableView.getSelectionModel().getSelectedItem();
        node.setBuilding(buildingEditEvent.getNewValue());

        //Find Edge
        TablePosition nodeIDPos = nodeDataTableView.getSelectionModel().getSelectedCells().get(0);
        int nodeIDRow = nodeIDPos.getRow();
        Node nodeID = nodeDataTableView.getItems().get(nodeIDRow);
        //Update in DB
        DBMap dbMap = DBMap.getInstance();
        dbMap.updateNode(nodeID);
    }

    public void EditnodeType(TableColumn.CellEditEvent<Node, String> typeEditEvent) {
        Node node = nodeDataTableView.getSelectionModel().getSelectedItem();
        node.setType(typeEditEvent.getNewValue());

        //Find Edge
        TablePosition nodeIDPos = nodeDataTableView.getSelectionModel().getSelectedCells().get(0);
        int nodeIDRow = nodeIDPos.getRow();
        Node nodeID = nodeDataTableView.getItems().get(nodeIDRow);
        //Update in DB
        DBMap dbMap = DBMap.getInstance();
        dbMap.updateNode(nodeID);
    }

    public void EditlongName(TableColumn.CellEditEvent<Node, String> longNameEditEvent) {
        Node node = nodeDataTableView.getSelectionModel().getSelectedItem();
        node.setName(longNameEditEvent.getNewValue());

        //Find Edge
        TablePosition nodeIDPos = nodeDataTableView.getSelectionModel().getSelectedCells().get(0);
        int nodeIDRow = nodeIDPos.getRow();
        Node nodeID = nodeDataTableView.getItems().get(nodeIDRow);
        //Update in DB
        DBMap dbMap = DBMap.getInstance();
        dbMap.updateNode(nodeID);
    }

    public void EditshortName(TableColumn.CellEditEvent<Node, String> shortNameEditEvent) {
        Node node = nodeDataTableView.getSelectionModel().getSelectedItem();
        node.setShortName(shortNameEditEvent.getNewValue());

        //Find Edge
        TablePosition nodeIDPos = nodeDataTableView.getSelectionModel().getSelectedCells().get(0);
        int nodeIDRow = nodeIDPos.getRow();
        Node nodeID = nodeDataTableView.getItems().get(nodeIDRow);
        //Update in DB
        DBMap dbMap = DBMap.getInstance();
        dbMap.updateNode(nodeID);
    }

    @FXML
    private void importNodeCSVBtn(ActionEvent actionEvent) throws Exception {
        CSVData nodeData = CSVHandler.readCSVFile(nodeFilepathField.getText());

        dbMap.clearNodes();
        CSVDBConverter.addCSVNodesToTable(dbMap, nodeData);
        nodeDataList = dbMap.getNodes();
        nodeDataTableView.setItems(getNodeData());
    }

    @FXML
    private void exportNodeCSVBtn(ActionEvent actionEvent) {
        CSVData newNodes = CSVDBConverter.csvNodesFromTable(dbMap);
        CSVData newEdges = CSVDBConverter.csvEdgesFromTable(dbMap);
        CSVHandler.writeCSVData(newNodes, "newNodes.csv");
        CSVHandler.writeCSVData(newEdges, "newEdges.csv");
    }

}
