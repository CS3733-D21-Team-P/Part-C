package edu.wpi.p.views.map;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.AStar.NodeButton;
import edu.wpi.p.database.DBTable;
import edu.wpi.p.views.map.Filter.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class MapEditorFindTab {

    public JFXTreeTableView nodeTable;
    public JFXTextField filterField;
    public JFXTextField buildFilter;
    public JFXTextField typeFilter;
    public JFXButton searchBtn;
    private List<Node> nodeList;

    private MapController mapController;
//    private EditMap editMapController;
//    private PathfindingMap pathfindingMapController;
    private DBTable dbTable = new DBTable();
    private JFXTreeTableColumn<NodeTableEntry, String> nodeName;
    private JFXTreeTableColumn<NodeTableEntry, String> nodeType;
    private JFXTreeTableColumn<NodeTableEntry, String> nodeFloor;
    private JFXTreeTableColumn<NodeTableEntry, String> nodeBuilding;

    @FXML JFXComboBox chosenType;

    final String[] availableFloors = new String[]{ "All Floors", "Ground", "L1", "L2", "1", "2", "3"};



    public void initialize() {
        nodeName = new JFXTreeTableColumn<>("Name");
        nodeName.setPrefWidth(70);
        nodeName.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<NodeTableEntry, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<NodeTableEntry, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getNode().getShortName());
            }
        });

        nodeType = new JFXTreeTableColumn<>("Type");
        nodeType.setPrefWidth(70);
        nodeType.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<NodeTableEntry, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<NodeTableEntry, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getNode().getType());
            }
        });

        nodeFloor = new JFXTreeTableColumn<>("Floor");
        nodeFloor.setPrefWidth(70);
        nodeFloor.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<NodeTableEntry, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<NodeTableEntry, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getNode().getFloor());
            }
        });

        nodeBuilding = new JFXTreeTableColumn<>("Building");
        nodeBuilding.setPrefWidth(70);
        nodeBuilding.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<NodeTableEntry, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<NodeTableEntry, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getNode().getBuilding());
            }
        });

        nodeList = dbTable.getNodes();

        updateList(nodeName, nodeType, nodeFloor, nodeBuilding, "All Floors");
//        List<Node> filteredNodes = filterNodes(nodeList);
//
//        ObservableList<NodeTableEntry> nodes = FXCollections.observableArrayList();
//        for (Node node : filteredNodes) {
//            nodes.add(new NodeTableEntry(new Node(node.getName(), node.getId(), node.getXcoord(), node.getYcoord(),  node.getFloor(), node.getBuilding(), node.getType(), node.getShortName())));
//        }
//
//        final TreeItem<NodeTableEntry> root = new RecursiveTreeItem<>(nodes, RecursiveTreeObject::getChildren);
//        nodeTable.getColumns().setAll(nodeName, nodeType, nodeFloor, nodeBuilding);
//        nodeTable.setRoot(root);
//        nodeTable.setShowRoot(false);

//        NodeButton nodeButton = new NodeButton(nodeList.get(0));

        filterField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                nodeTable.setPredicate(new Predicate<TreeItem<NodeTableEntry>>() {
                    @Override
                    public boolean test(TreeItem<NodeTableEntry> nodeTreeItem) {
                        return nodeTreeItem.getValue().getNode().getShortName().toLowerCase().contains(newValue.toLowerCase());
                        //Boolean flag = nodeTreeItem.getValue().getShortName().contains(newValue);
                        //nodeTreeItem.getValue().setIsSelected(flag);
                        //nodeButton.setButtonStyle();
//                        return flag;
                    }
                });
            }
        });

        /**
         * Listener for selection changes on "find tab" tableview.
         * Updates nodeButton style to highlight node entry selected in table.
         */
        nodeTable.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<TreeItem<NodeTableEntry>>(){
            @Override
            public void changed(ObservableValue<? extends TreeItem<NodeTableEntry>>
                                        observable, TreeItem<NodeTableEntry> oldValue, TreeItem<NodeTableEntry> newValue) {
                if (!(oldValue == newValue)) {
                    TreeTableView.TreeTableViewSelectionModel<NodeTableEntry> sm = nodeTable.getSelectionModel();
                    Node n = (Node) sm.getSelectedItem().getValue().getNode(); //selected node
                    String floor = n.getFloor(); //current floor
                    mapController.floorChoiceBox.getSelectionModel().select(Arrays.asList(mapController.availableFloors).indexOf(floor));

                    //find nodeButton
                    List<NodeButton> list = mapController.buttonLists.get(floor);
                    for(NodeButton nb: list){
                        if(nb.getNode().getId().equals(n.getId())){
                            //found node button
                            System.out.println("found");

                            if(mapController.nodeButtonHold!=null) { //check if there is a current selection
                                //deselect prev node
                                mapController.nodeButtonHold.getNode().setIsSelected(false);
                                mapController.nodeButtonHold.setButtonStyle();
                            }

                            //select new node
                            mapController.selectNode(nb);

                            mapController.centerNode(nb);//center on node
                            continue;
                        }

                    }
                }
            }
        });
    }

    public void injectMapController(MapController mapController){

        this.mapController = mapController;
        chosenType.setItems(FXCollections.observableArrayList(availableFloors));

    }

    /**
     * Filters out hallways and nodes not on given floor
     * @param nodesUnfiltered all nodes
     * @param floor String of floor to keep in list
     * @return list of nodes matching filter
     */
    public List<Node> filterNodes(List<Node> nodesUnfiltered, String floor){
        Criteria noHalls = new CriteriaNoHallways();
        Criteria matchesFloor = new CriteriaMatchesFloor(floor);
        Criteria matchesType = new CriteriaMatchesType(typeFilter.getText());
        Criteria matchesBuilding = new CriteriaMatchesBuilding(buildFilter.getText());

        nodesUnfiltered = noHalls.meetCriteria(nodesUnfiltered);
        if (floor!=null&& !floor.equals("All Floors")) {
            nodesUnfiltered = matchesFloor.meetCriteria(nodesUnfiltered);
        }
        if(!buildFilter.getText().equals("")){
            nodesUnfiltered = matchesBuilding.meetCriteria(nodesUnfiltered);
        }
        if(!typeFilter.getText().equals("")){
            nodesUnfiltered = matchesType.meetCriteria(nodesUnfiltered);
        }

        return nodesUnfiltered;
    }

    /**
     * filters list and updates treetable
     * @param nodeName
     * @param nodeType
     * @param nodeFloor
     * @param nodeBuilding
     */
    public void updateList(JFXTreeTableColumn<NodeTableEntry, String> nodeName, JFXTreeTableColumn<NodeTableEntry, String> nodeType,
                           JFXTreeTableColumn<NodeTableEntry, String> nodeFloor, JFXTreeTableColumn<NodeTableEntry, String> nodeBuilding, String floor){
        List<Node> filteredNodes = filterNodes(nodeList, floor); //get filtered list

        //Add nodes to node table
        ObservableList<NodeTableEntry> nodes = FXCollections.observableArrayList();
        for (Node node : filteredNodes) {
            nodes.add(new NodeTableEntry(new Node(node.getName(), node.getId(), node.getXcoord(), node.getYcoord(),  node.getFloor(), node.getBuilding(), node.getType(), node.getShortName())));
        }

        final TreeItem<NodeTableEntry> root = new RecursiveTreeItem<>(nodes, RecursiveTreeObject::getChildren);
        nodeTable.getColumns().setAll(nodeName, nodeType, nodeFloor, nodeBuilding);
        nodeTable.setRoot(root);
        nodeTable.setShowRoot(false);
    }

    public void updateList(ActionEvent actionEvent) {
        updateList(nodeName, nodeType, nodeFloor, nodeBuilding, (String) chosenType.getValue());
    }
}
