package edu.wpi.p.views.map;

import com.jfoenix.controls.JFXButton;
import edu.wpi.p.AStar.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.event.ActionEvent;

public class PathfindingMap extends MapController {

    @FXML private PathTabController pathTabController;
    @FXML public AnchorPane nextFloorBox;
    @FXML public Text nextFloorText;
    @FXML public JFXButton nextFloorButton;
    @FXML public JFXButton lastFloorButton;

    /**
     * creates a button associated  with a node
     * adds a line to neighbour nodes
     * adds click function
     * @param node
     * @return created NodeButton
     */
    @Override
    public NodeButton addNodeButton(Node node){
        //MAKE BUTTON IF ON CURRENT FLOOR
            NodeButton nb = super.addNodeButton(node);

            //set on click method
            nb.setOnAction(event -> {
                pathTabController.addNodeToSearch(event);

                //select button
                if (nodeButtonHold != null)
                {
                    nodeButtonHold.deselect();
                }
                nodeButtonHold = nb;
                nodeClicked(nb);
            });

            return nb;
    }



    @FXML
    /**
     * run when page starts
     * adds buttons and edge lines to map
     */
    @Override
    public void initialize()  {
        super.initialize();
        pathfindPage = true;
        pathTabController.injectPathfindingMap(this);
        nextFloorBox.setVisible(false);
        System.out.println("PATHFINDING INIT");
        for (Node n: graph.getGraph()){
            addNodeButton(n);

        }
        translateGraph(imageView);
        isEditingMap = false;
    }

    @FXML
    private void lastFloorAc(javafx.event.ActionEvent actionEvent) {
        if (pathTabController.getCurrentFloorInList() > 0) {
//            if (pathTabController.getCurrentFloorInList() > 1) {
//                pathTabController.lastFloor = pathTabController.floorsInPath.get(pathTabController.getCurrentFloorInList() - 2);
//            }
//            else {
//                pathTabController.lastFloor = null;
//            }
//
//            pathTabController.nextFloor = pathTabController.floorsInPath.get(pathTabController.getCurrentFloorInList());
            pathTabController.setCurrentFloorInList(pathTabController.getCurrentFloorInList() - 1);
            setCurrFloorVal(pathTabController.lastFloor);
            changeFloors(pathTabController.lastFloor);
        }
    }
    @FXML
    private void nextFloorAc(javafx.event.ActionEvent actionEvent) {
        if ((pathTabController.floorsInPath.size() - 1) > pathTabController.currentFloorInList) {
//            if ((pathTabController.floorsInPath.size()) > pathTabController.currentFloorInList) {
//                pathTabController.nextFloor = pathTabController.floorsInPath.get(pathTabController.getCurrentFloorInList() + 2);
//            }
//            else {
//                pathTabController.nextFloor = null;
//            }
//            pathTabController.lastFloor = pathTabController.floorsInPath.get(pathTabController.getCurrentFloorInList());
            pathTabController.setCurrentFloorInList(pathTabController.getCurrentFloorInList() + 1);
            setCurrFloorVal(pathTabController.nextFloor);
            changeFloors(pathTabController.nextFloor);
        }
    }
    @Override
    public void updateNextFloorBox() {
        pathTabController.colorButtons();
    }
}
