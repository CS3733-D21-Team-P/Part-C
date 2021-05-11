package edu.wpi.p.views.map;

import com.jfoenix.controls.JFXButton;
import edu.wpi.p.AStar.EdgeLine;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import edu.wpi.p.database.DBTable;
import edu.wpi.p.database.DBUser;
import edu.wpi.p.database.UserFromDB;
import edu.wpi.p.userstate.User;
import edu.wpi.p.views.ClippoController;
import edu.wpi.p.userstate.UserEntryLocation;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.AStar.NodeButton;

//import javax.swing.text.html.ImageView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PathfindingMap extends MapController {

    @FXML private Pane clippoID;
    @FXML private ClippoController clippoIDController;

    @FXML private VBox saveNodePopup;
    @FXML private PathTabController pathTabController;
    @FXML private AlgorithmSelectionController algorithmSelectionController;
    @FXML public AnchorPane nextFloorBox;
    @FXML public Text nextFloorText;
    @FXML public JFXButton nextFloorButton;
    @FXML public JFXButton lastFloorButton;
    @FXML private Text nodeName;
    @FXML public VBox saveNodePopup1;
    @FXML public AnchorPane btnPane;
    @FXML public JFXButton saveBtn;
    @FXML public GoogleTabController googleTabController;
    @FXML public AnchorPane base;
    @FXML public TabPane tabPane;
    @FXML public Tab tabPath;

    private DBTable dbTable = new DBTable();
    private int btnIncrement = 1;
    private ParkingSaving parkingSaving = new ParkingSaving();


    public ParkingSaving getParkingSaving() {
        return parkingSaving;
    }



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

        List<UserFromDB> users = new ArrayList<UserFromDB>();
        DBUser dbuser = new DBUser();
        users = dbuser.getUsers();

        //check if parking spot button
        String parkingID = User.getInstance().getParkingNodeID();
        if(node.getId().equals(parkingID)){
            nb.setSaved(true);
            nb.setButtonStyle();
            parkingSaving.oldSpot=nb;
            pathTabController.parkingText.setText(nb.getName());
        }

        //set on click method
        nb.setOnAction(event -> {
            Tab currTab =  tabPane.getSelectionModel().getSelectedItem();
            if(currTab.equals(tabPath)) {
                pathTabController.addNodeToSearch(event);
            }

            selectNode(nb);
        });

        nb.setOnMouseClicked(event -> {

            if (event.getButton() == MouseButton.PRIMARY) {
                selectNode(nb);

            } else if (event.getButton() == MouseButton.SECONDARY) {
                selectNode(nb);//select node

                nodeName.setText(nodeHold.getName());
                if(nodeButtonHold.isSaved()){
                    saveBtn.setText("Unsave");
                }
                else{
                    saveBtn.setText("Save Location");
                }
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
        clippoIDController.setPage("pathfinding");
        pathfindPage = true;
        pathTabController.injectPathfindingMap(this);
        googleTabController.injectPathfindingMap(this);


        nextFloorBox.setVisible(false);
        System.out.println("PATHFINDING INIT");

        // EXAMPLE OF CHECKING THE ENTRY LOCATION
        System.out.println("User entry location is: " + User.getInstance().getEntryLocation());
        for (Node n : graph.getGraph()) {
            addNodeButton(n);
        }
        translateGraph(imageView);
        isEditingMap = false;
        saveNodePopup.setVisible(false);

        btnPane.setOnMouseClicked(event -> { //deselect when clicked off node
            if (nodeButtonHold != null)
            {
                nodeButtonHold.deselect();
                nodeButtonHold = null;
            }

        });



    }

    public void saveParkingAc(ActionEvent actionEvent) {
        if(!nodeButtonHold.isSaved()) {
            parkingSaving.saveParkingAc(nodeButtonHold);
            pathTabController.parkingText.setText(nodeButtonHold.getName());
        }
        else{
            parkingSaving.unsaveParkingAc(nodeButtonHold);
            pathTabController.parkingText.setText("None");
        }
        saveNodePopup.setVisible(false);
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
            PathfindingMap.super.floorChoiceBox.getSelectionModel().select(
                    Arrays.asList(PathfindingMap.super.availableFloors).indexOf(
                            pathTabController.floorsInPath.get(pathTabController.getCurrentFloorInList())));
            pathTabController.updateAnimatedPath(imageView);
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
            PathfindingMap.super.floorChoiceBox.getSelectionModel().select(
                    Arrays.asList(PathfindingMap.super.availableFloors).indexOf(
                            pathTabController.floorsInPath.get(pathTabController.getCurrentFloorInList())));
            pathTabController.updateAnimatedPath(imageView);
        }
    }
//    @Override
    public void updateNextFloorBox() {
        pathTabController.colorButtons();
    }

//    @Override
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

    void translateGraph(ImageView imageView) {
        double scaleX = imageView.getViewport().getWidth() / imageView.getFitWidth();
        double scaleY = imageView.getViewport().getHeight() / imageView.getFitHeight();
        for (NodeButton btn : buttons) {
            btn.pan(imageView);
        }
        for (EdgeLine el : edgeLines) {
            el.pan(imageView);
        }
        pathTabController.updateAnimatedPath(imageView);
    }

}
