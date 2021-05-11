package edu.wpi.p.views.map;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.AStar.*;
import edu.wpi.p.views.map.Filter.Criteria;
import edu.wpi.p.views.map.Filter.CriteriaNoHallways;
import edu.wpi.p.views.map.GoogleDirections.AutoCompletePopup;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PathTabController {

    @FXML private JFXTreeTableView textDirectionsTable;
    private JFXTreeTableColumn<DirectionTableEntry, ImageView> directionImageView;
    private JFXTreeTableColumn<DirectionTableEntry, Label> directionText;

    @FXML private JFXToggleButton toggleHandicap;

    private PathfindingMap pathfindingMap;

    enum State {
        ENTERSTART,
        ENTEREND,
        NONE
    }
    private State mapState = State.ENTERSTART;

    private Pathfinder search;
    private Node startNode;
    private Node endNode;
    public NodeButton startNodeButton;
    public NodeButton endNodeButton;
    private NodeButton startNodeButtonHold;
    private NodeButton endNodeButtonHold;
    private List<EdgeLine> pathLine= new ArrayList<>();
    private List<Arrow> arrowLine= new ArrayList<>();

    public List<String> floorsInPath= new ArrayList<>();
    public int currentFloorInList = 0;
    public String nextFloor = null;
    public String lastFloor = null;

    @FXML public JFXTextField start;
    @FXML public JFXTextField end;
    @FXML public Label instructions;

    public int getCurrentFloorInList() {
        return currentFloorInList;
    }

    public void setCurrentFloorInList(int currentFloorInList) {
        this.currentFloorInList = currentFloorInList;
    }


    private boolean enteringStart = false;

    public void injectPathfindingMap(PathfindingMap pathfindingMap){

        this.pathfindingMap = pathfindingMap;
        search = new Pathfinder(pathfindingMap.graph);


        //add autocomplete to start and end text fields
        AutoCompletePopup acpStart = new AutoCompletePopup(start);
        AutoCompletePopup acpEnd = new AutoCompletePopup(end);

        Criteria noHalls = new CriteriaNoHallways();
        //filter out hallways from nodes
        List<Node> filteredNodes = noHalls.meetCriteria(pathfindingMap.graph.getGraph());

        //get names of nodes
        List<String> nodeNames = new ArrayList<>();
        for (Node n: filteredNodes){nodeNames.add(n.getName());}

        //add names to list of possible autocomplete suggestions
        acpStart.getSuggestions().addAll(nodeNames);
        acpEnd.getSuggestions().addAll(nodeNames);

        //setup DirectionsTable
        directionImageView = new JFXTreeTableColumn<>("icon");
        directionImageView.setPrefWidth(50);
        directionImageView.setSortable(false);
        directionImageView.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DirectionTableEntry, ImageView>, ObservableValue<ImageView>>() {
            public ObservableValue<ImageView> call(TreeTableColumn.CellDataFeatures<DirectionTableEntry, ImageView> p) {
                return new SimpleObjectProperty(p.getValue().getValue().getImageVew());
            }
        });

        directionText = new JFXTreeTableColumn<>("instruction");
        directionText.setPrefWidth(200);
        directionText.setSortable(false);
        directionText.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DirectionTableEntry, Label>, ObservableValue<Label>>() {
            public ObservableValue<Label> call(TreeTableColumn.CellDataFeatures<DirectionTableEntry, Label> p) {
                Label l = new Label(p.getValue().getValue().getInstruction());
                l.setWrapText(true);
                return new SimpleObjectProperty(l);
            }
        });

        textDirectionsTable.setPlaceholder(new Label(""));
    }


    /**
     * run when user clicks into start text field and changes state
     * @param e
     */
    public void enterStart(MouseEvent e){
        mapState = PathTabController.State.ENTERSTART;
        System.out.println("enter start!");

    }


    /**
     * run when text in start or end location is changed to update instructions for entering locations
     */
    public void textChanged(){
        System.out.println("text changed");
        if(start.getText()==null || start.getText().equals("")){ //nothing has been entered in start location
            instructions.setText("click a point to enter a location");
        }
        else if(end.getText()==null || end.getText().equals("")){ //nothing in end location
            instructions.setText("enter and end location");
        }
        else if(end.getText().equals(start.getText())){ //both locations are the same
            instructions.setText("choose two different locations");
        }
        else{ //both have been filled
            instructions.setText("press search to find a path");
        }
    }

    /**
     * run when user clicks into end textfield and changes state
     * @param e
     */
    public void enterEnd(MouseEvent e){
        mapState = PathTabController.State.ENTEREND;
        System.out.println("enter end!");
    }

    @FXML
    private void toggleHandicapPath(ActionEvent actionEvent){
        search.setHandicapPath(!search.isHandicapPath());
    }

    public void enterParkingSpot(ActionEvent actionEvent){
        NodeButton parkingSpot =pathfindingMap.getParkingSaving().oldSpot;
        if (parkingSpot !=null){
            end.setText(parkingSpot.getName());
            textChanged();

            if(!start.getText().equals("")){
                findPath(actionEvent);
            }
        }
    }

    /**
     * Finds a path between the two nodes entered and makes it red
     * @param actionEvent
     */
    public void findPath(ActionEvent actionEvent){
        resetVariables();
        if (startNode==null || !start.getText().equals(startNode.getName())){
            System.out.println("set start");
            String startText = start.getText();
            startNode = pathfindingMap.graph.getNodeByName(startText);
        }
        if (endNode ==null || !end.getText().equals(endNode.getName())){
            System.out.println("set end");
            String endText = end.getText();
            endNode = pathfindingMap.graph.getNodeByName(endText);
        }

        if(startNode!=null && endNode!=null) {
            //find path
            List<Node> path;
            path = search.findPath(startNode, endNode);
            floorsInPath = getFloorsInPath(path);
            if (floorsInPath.size() > 1) {
                pathfindingMap.nextFloorBox.setVisible(true);
                pathfindingMap.multipleFloors = true;
                currentFloorInList = 0;
                pathfindingMap.setCurrFloorVal(floorsInPath.get(currentFloorInList));
                pathfindingMap.changeFloors(floorsInPath.get(currentFloorInList));
                pathfindingMap.floorChoiceBox.getSelectionModel().select(pathfindingMap.floorInList(floorsInPath.get(currentFloorInList)));
            }
            colorButtons();

            //Path To Text
            PathToText textPath = new PathToText();
            textPath.makeDirections(path);
            updateDirectionsTable(textPath.getTableDirections());

            //print path
            System.out.println("Path: ");
            for (Node n : path) {
                System.out.print(n.getName() + " ");
            }


            //clearing path
            for(EdgeLine el: pathLine){
                pathfindingMap.btnPane.getChildren().remove(el);
            }
            pathLine.clear();

            for(Arrow el: arrowLine){
                pathfindingMap.btnPane.getChildren().remove(el);
            }
            arrowLine.clear();



            //Make path red
            for (int i = 0; i < path.size(); i++) {
                System.out.println("MAKING PATH");
                System.out.print(path.get(i).getName() + " ");
                if(i>0) {
                    //TRYING TO MAKE ARROWS
                    Arrow arrow = new Arrow(path.get(i), path.get(i-1));
                    arrow.setStyle("-fx-stroke: red; -fx-stroke-width: 5px;");
                    arrowLine.add(arrow);
                    arrow.toFront();

                    EdgeLine line = pathfindingMap.addEdgeLine(path.get(i), path.get(i-1));
                    line.setStyle("-fx-stroke: red; -fx-stroke-width: 5px;");
                    pathLine.add(line);
                    line.toFront();
                }
            }

//            for (int i = 0; i < path.size(); i++) {
//                if (i > 0) {
//                    EdgeLine line = pathfindingMap.addEdgeLine(path.get(i), path.get(i-1));
//                    if (path.get(i).getFloor().equals(pathfindingMap.getCurrFloorVal())) {
//                        line.setStyle("-fx-stroke: red; -fx-stroke-width: 5px;");
//                        pathLine.add(line);
//                        line.toFront();
//                    }
//                    else {
//                        line.setStyle("-fx-stroke: grey; -fx-stroke-width: 5px;");
//                        pathLine.add(line);
//                        line.toFront();
//                    }
//                }
//            }

            //TODO: set old node button nodes to be isPathfinding =false;
            //TODO: set new old node button variable to be the current start and end node

            startNodeButton.getNode().setIsPathfinding(true);
            endNodeButton.getNode().setIsPathfinding(true);
            startNodeButton.setButtonStyle();
            startNodeButton.makeBlue();
            endNodeButton.setButtonStyle();

//            if (startNodeButton != startNodeButtonHold) {
//                startNodeButton.setButtonStyle();
//                startNodeButton.makeBlue();
//                if (startNodeButtonHold != null) {
//                    startNodeButtonHold.endPathfinding();
//                }
//            }
//            if (endNodeButton != endNodeButtonHold) {
//                endNodeButton.setButtonStyle();
//                if (endNodeButtonHold != null) {
//                    endNodeButtonHold.endPathfinding();
//                }
//            }

            startNodeButton.getNode().setIsPathfinding(false);
            endNodeButton.getNode().setIsPathfinding(false);
            startNodeButton.getNode().setWasPathfinding(true);
            endNodeButton.getNode().setWasPathfinding(true);
            startNodeButtonHold = startNodeButton;
            endNodeButtonHold = endNodeButton;

        }
        else{
            System.out.println("please enter a start AND and end location");
        }


    }

    private void updateDirectionsTable(List<DirectionTableEntry> directions) {
        //Add instructions to DirectionsTable
        ObservableList<DirectionTableEntry> tableDirections = FXCollections.observableArrayList();
        for (DirectionTableEntry entry : directions) {
            tableDirections.add(entry);
        }

        final TreeItem<DirectionTableEntry> root = new RecursiveTreeItem<>(tableDirections, RecursiveTreeObject::getChildren);
        textDirectionsTable.getColumns().setAll(directionImageView, directionText);
        textDirectionsTable.setRoot(root);
        textDirectionsTable.setShowRoot(false);
    }


    /**
     * adds text to either start or end text field based on the current state of where the mouse has clicked
     * Run when a node button is clicked
     * @param actionEvent
     */
    public void addNodeToSearch(ActionEvent actionEvent){
        //get button that was clicked on
        NodeButton button = (NodeButton) actionEvent.getSource();


        if(mapState.equals(State.ENTERSTART)){
//            instructions.setText("enter and end node");
            startNode= button.getNode();
            start.setText(button.getName()); //set text field text to be node name
            System.out.println("start: "+ button.getName());
            startNodeButton = button;
            mapState = State.ENTEREND;
        }
        else if(mapState.equals(State.ENTEREND)){
//            instructions.setText("press search to find a path");
            endNode = button.getNode();
            end.setText(button.getName()); //set text field text to be node name
            System.out.println("end: "+ button.getName());
            endNodeButton = button;
        }

        String text1= start.getText();
        System.out.println(text1);
        String text2= end.getText();
        System.out.println(text2);

        if(start.getText()==null || start.getText().equals("")){
            instructions.setText("click a point to ender a location");
        }
        else if(end.getText()==null || end.getText().equals("")){
            instructions.setText("enter and end node");
        }
        else{
            instructions.setText("press search to find a path");
        }
    }

    private ArrayList<String> getFloorsInPath(List<Node> path) {
        ArrayList<String> floorList = new ArrayList<>();
        floorList.add(path.get(0).getFloor());
        for (int i = 0; i < path.size() - 1; i++) {
            if (!path.get(i).getFloor().equals(path.get(i + 1).getFloor())) {
                floorList.add(path.get(i + 1).getFloor());
            }
        }
        System.out.print(floorList);
        return floorList;
    }

    private void resetVariables() {
        nextFloor = null;
        lastFloor = null;
        currentFloorInList = 0;
        floorsInPath = new ArrayList<>();
        pathfindingMap.nextFloorBox.setVisible(false);
    }

    public void colorButtons() {
        //tests if there are future directions on another floor
        if ((floorsInPath.size() - 1) > currentFloorInList) {
            pathfindingMap.nextFloorButton.setStyle("-fx-background-color: #5990d9");
            nextFloor = floorsInPath.get(currentFloorInList + 1);
        } else {
            pathfindingMap.nextFloorButton.setStyle("-fx-background-color: #9fbbc8");
            nextFloor = null;
        }
        //tests if there are previous directions on another floor
        if (currentFloorInList > 0) {
            pathfindingMap.lastFloorButton.setStyle("-fx-background-color: #5990d9");
            lastFloor = floorsInPath.get(currentFloorInList - 1);
        } else {
            pathfindingMap.lastFloorButton.setStyle("-fx-background-color: #9fbbc8");
            nextFloor = null;
        }
    }
}
