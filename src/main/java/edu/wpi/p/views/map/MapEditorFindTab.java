package edu.wpi.p.views.map;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.DBTable;
import edu.wpi.p.database.ServiceRequest;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

import java.util.List;
import java.util.function.Predicate;

public class MapEditorFindTab {

    public JFXTreeTableView nodeTable;
    public JFXTextField filterField;
    private List<Node> nodeList;
    private DBTable dbTable;


    public void initialize() {
        JFXTreeTableColumn<Node, String> nodeName = new JFXTreeTableColumn<>("Name");
        nodeName.setPrefWidth(20);
        nodeName.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Node, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Node, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getShortName());
            }
        });

        JFXTreeTableColumn<Node, String> nodeType = new JFXTreeTableColumn<>("Type");
        nodeType.setPrefWidth(20);
        nodeType.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Node, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Node, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getType());
            }
        });

        JFXTreeTableColumn<Node, String> nodeFloor = new JFXTreeTableColumn<>("Floor");
        nodeFloor.setPrefWidth(20);
        nodeFloor.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Node, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Node, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getFloor());
            }
        });

        JFXTreeTableColumn<Node, String> nodeBuilding = new JFXTreeTableColumn<>("Building");
        nodeBuilding.setPrefWidth(20);
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

        filterField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                nodeTable.setPredicate(new Predicate<TreeItem<Node>>() {
                    @Override
                    public boolean test(TreeItem<Node> nodeTreeItem) {
                        Boolean flag = nodeTreeItem.getValue().getName().equals(newValue);
                        return flag;
                    }
                });
            }
        });

    }

}
