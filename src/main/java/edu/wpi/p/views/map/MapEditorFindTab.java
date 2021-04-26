package edu.wpi.p.views.map;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.AStar.NodeButton;
import edu.wpi.p.database.DBTable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import java.util.List;
import java.util.function.Predicate;

public class MapEditorFindTab {

    public JFXTreeTableView nodeTable;
    public JFXTextField filterField;
    public JFXButton searchBtn;
    private List<Node> nodeList;

    private EditMap editMapController;
    private PathfindingMap pathfindingMapController;
    private DBTable dbTable = new DBTable();


    public void initialize() {
        JFXTreeTableColumn<Node, String> nodeName = new JFXTreeTableColumn<>("Name");
        nodeName.setPrefWidth(70);
        nodeName.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Node, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Node, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getShortName());
            }
        });

        JFXTreeTableColumn<Node, String> nodeType = new JFXTreeTableColumn<>("Type");
        nodeType.setPrefWidth(70);
        nodeType.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Node, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Node, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getType());
            }
        });

        JFXTreeTableColumn<Node, String> nodeFloor = new JFXTreeTableColumn<>("Floor");
        nodeFloor.setPrefWidth(70);
        nodeFloor.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Node, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Node, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getFloor());
            }
        });

        JFXTreeTableColumn<Node, String> nodeBuilding = new JFXTreeTableColumn<>("Building");
        nodeBuilding.setPrefWidth(70);
        nodeBuilding.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Node, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Node, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getBuilding());
            }
        });

        nodeList = dbTable.getNodes();
        ObservableList<Node> nodes = FXCollections.observableArrayList();
        for (Node node : nodeList) {
            nodes.add(new Node(node.getName(), node.getId(), node.getXcoord(), node.getYcoord(),  node.getFloor(), node.getBuilding(), node.getType(), node.getShortName()));
        }

        final TreeItem<Node> root = new RecursiveTreeItem<>(nodes, RecursiveTreeObject::getChildren);
        nodeTable.getColumns().setAll(nodeName, nodeType, nodeFloor, nodeBuilding);
        nodeTable.setRoot(root);
        nodeTable.setShowRoot(false);

        NodeButton nodeButton = new NodeButton(nodeList.get(0));

        filterField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                nodeTable.setPredicate(new Predicate<TreeItem<Node>>() {
                    @Override
                    public boolean test(TreeItem<Node> nodeTreeItem) {
                        Boolean flag = nodeTreeItem.getValue().getShortName().contains(newValue);
                        //nodeTreeItem.getValue().setIsSelected(flag);
                        //nodeButton.setButtonStyle();
                        return flag;
                    }
                });
            }
        });

        /**
         * Listener for selection changes on "find tab" tableview.
         * Updates nodeButton style to highlight node entry selected in table.
         */
        nodeTable.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<TreeItem<Node>>(){
            @Override
            public void changed(ObservableValue<? extends TreeItem<Node>>
                                        observable, TreeItem<Node> oldValue, TreeItem<Node> newValue) {
                if (!(oldValue == newValue)) {
                    TreeTableView.TreeTableViewSelectionModel<Node> sm = nodeTable.getSelectionModel();
                    Node n = (Node) sm.getSelectedItem().getValue(); //selected node
                    String floor = n.getFloor(); //current floor

                    //find nodeButton
                    List<NodeButton> list = editMapController.buttonLists.get(floor);
                    for(NodeButton nb: list){
                        if(nb.getNode().getId().equals(n.getId())){
                            //found node button
                            System.out.println("found");

                            if(editMapController.nodeButtonHold!=null) { //check if there is a current selection
                                //deselect prev node
                                editMapController.nodeButtonHold.getNode().setIsSelected(false);
                                editMapController.nodeButtonHold.setButtonStyle();
                            }

                            //select new node
                            editMapController.nodeClicked(nb);
                            continue;
                        }

                    }
                }
            }
        });
    }

    public void injectEditMap(EditMap editMap){
        this.editMapController = editMap;
    }

    public void searchBtnAc(ActionEvent actionEvent) {

    }
}
