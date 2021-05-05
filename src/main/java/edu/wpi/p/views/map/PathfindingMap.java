package edu.wpi.p.views.map;

import com.jfoenix.controls.JFXButton;
import edu.wpi.p.AStar.*;
import edu.wpi.p.database.DBTable;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.AStar.NodeButton;

import java.util.List;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.event.ActionEvent;

public class PathfindingMap extends MapController {

    @FXML private VBox saveNodePopup;
    @FXML private PathTabController pathTabController;
    @FXML public AnchorPane nextFloorBox;
    @FXML public Text nextFloorText;
    @FXML public JFXButton nextFloorButton;
    @FXML public JFXButton lastFloorButton;
    @FXML private Text nodeName;
    @FXML public VBox saveNodePopup1;
    @FXML public AnchorPane btnPane;

    private DBTable dbTable = new DBTable();
    private int btnIncrement = 1;



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

        nb.setOnMouseClicked(event -> {
            for(EdgeLine el: nb.getLines()){
                System.out.println(el.getEndNode().getFloor());
            }

            if (event.getButton() == MouseButton.PRIMARY) {
                if (nodeButtonHold != null)
                {
                    nodeButtonHold.deselect();
                    nodeButtonHold = null;
                }
                if (edgeHold != null)
                {
                    edgeHold.setSelected(false);
                    edgeHold.updateStyle();
                }
                nodeButtonHold = nb;
                System.out.println(nb.getNode().getXcoord());
                System.out.println(nb.getLayoutX());

                nodeClicked(nb);

            } else if (event.getButton() == MouseButton.SECONDARY) {
                if (nodeButtonHold != null)
                {
                    nodeButtonHold.deselect();
                    nodeButtonHold = null;
                }
                if (edgeHold != null)
                {
                    edgeHold.setSelected(false);
                    edgeHold.updateStyle();
                }
                nodeClicked(nb);
                nodeName.setText(nodeHold.getName());
                saveNodePopup.setVisible(true);
                TranslateTransition transition = new TranslateTransition(Duration.millis(75), saveNodePopup);
                transition.setToX(event.getSceneX() - saveNodePopup.getLayoutX() - saveNodePopup.getWidth() / 2 - event.getX() + 90);
                transition.setToY(event.getSceneY() - saveNodePopup.getLayoutY() - saveNodePopup.getHeight() / 2 - event.getY() - 55);
                transition.playFromStart();
            }
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
        for (Node n : graph.getGraph()) {
            addNodeButton(n);

        }
        translateGraph(imageView);
        isEditingMap = false;
        saveNodePopup.setVisible(false);

        btnPane.setOnMouseClicked(event -> {
            if (nodeButtonHold != null)
            {
                nodeButtonHold.deselect();
                nodeButtonHold = null;
            }
                //set x and y to be position of mouse
                int x = (int) (unScaleX(event.getSceneX()-100));
                int y = (int) (unScaleY(event.getSceneY()));

        });
    }

    public void saveParkingAc(ActionEvent actionEvent) {
        ParkingSaving parkingSaving = new ParkingSaving();
        parkingSaving.saveParkingAc(nodeButtonHold);
    }

    public void closePopup(ActionEvent actionEvent) {
        saveNodePopup.setVisible(false);
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
//            setCurrFloorVal(pathTabController.lastFloor);
            changeFloors(pathTabController.floorsInPath.get(pathTabController.getCurrentFloorInList()));
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
//            setCurrFloorVal(pathTabController.nextFloor);
            changeFloors(pathTabController.floorsInPath.get(pathTabController.getCurrentFloorInList()));
        }
    }
    @Override
    public void updateNextFloorBox() {
        pathTabController.colorButtons();
    }

    @Override
    public void makeBigAndRed() {
        pathTabController.startNodeButton.getNode().setIsPathfinding(true);
        pathTabController.endNodeButton.getNode().setIsPathfinding(true);
        pathTabController.startNodeButton.toFront();
        pathTabController.endNodeButton.toFront();
        pathTabController.startNodeButton.setStyle(
                "-fx-background-radius: 5em; " +
//                            "-fx-min-width: 12px; " +
//                            "-fx-min-height: 12px; " +
////                            "-fx-max-width: 12px; " +
////                            "-fx-max-height: 12px;" +
                        "-fx-background-color: #4a7ede"
        );
        pathTabController.endNodeButton.setStyle(
                "-fx-background-radius: 5em; " +
//                            "-fx-min-width: 12px; " +
//                            "-fx-min-height: 12px; " +
////                            "-fx-max-width: 12px; " +
////                            "-fx-max-height: 12px;" +
                        "-fx-background-color: red"
        );
        pathTabController.startNodeButton.setOpacity(0.7);
        pathTabController.endNodeButton.setOpacity(0.7);
//            if (!this.getNode().getWasPathfinding()) {
        pathTabController.startNodeButton.currentSize = 20;
        pathTabController.startNodeButton.setButtonSize(pathTabController.startNodeButton.currentSize); //set button size
        pathTabController.startNodeButton.setTranslateX(-(pathTabController.startNodeButton.currentSize / 2));
        pathTabController.startNodeButton.setTranslateY(-(pathTabController.startNodeButton.currentSize / 2));

        pathTabController.endNodeButton.currentSize *= 2;
        pathTabController.endNodeButton.setButtonSize(pathTabController.startNodeButton.currentSize); //set button size
        pathTabController.endNodeButton.setTranslateX(-(pathTabController.startNodeButton.currentSize / 2));
        pathTabController.endNodeButton.setTranslateY(-(pathTabController.startNodeButton.currentSize / 2));
    }
    public void nodeClicked(NodeButton nb)
    {
        nodeHold = nb.getNode();
        nodeButtonHold = nb;
        nodeButtonHold.getNode().setIsSelected(true);
        nodeButtonHold.setButtonStyle();
    }
}
