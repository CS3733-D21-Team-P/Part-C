package edu.wpi.p.views.map;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.AStar.*;
import edu.wpi.p.views.map.Filter.Criteria;
import edu.wpi.p.views.map.Filter.CriteriaNoHallways;
import edu.wpi.p.views.map.GoogleDirections.AutoCompletePopup;
import edu.wpi.p.views.map.GoogleDirections.DirectionsList;
import edu.wpi.p.views.map.GoogleDirections.CenterPopup;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class PathTabController {

    //@FXML private AnchorPane directionsPane;
    //@FXML private Accordion accordionDirections;
    @FXML private VBox directionsVBox;
    private List<DirectionsList> textDirectionsTables;

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
//    private NodeButton pathOldStartHold;
//    private NodeButton pathOldEndHold;
    private List<EdgeLine> pathLine= new ArrayList<>();
//    private List<Arrow> arrowLine= new ArrayList<>();

    public List<String> floorsInPath= new ArrayList<>();
    public int currentFloorInList = 0;
    public String nextFloor = null;
    public String lastFloor = null;

    @FXML public JFXTextField start;
    @FXML public JFXTextField end;
    @FXML public Label instructions;
    @FXML public Label parkingText;

    private List<Node> animatedPath = new ArrayList<>();
    final static ArrayList<PathTransition> pathTransitions = new ArrayList<>();
    private static boolean animationMade = false;
    private double pathDistance;

    private AutoCompletePopup acpStart ;
    private AutoCompletePopup acpEnd ;

    @FXML
    public void initialize() {
        changeState(State.ENTERSTART);
    }

    public int getCurrentFloorInList() {
        return currentFloorInList;
    }

    public void setCurrentFloorInList(int currentFloorInList) {
        this.currentFloorInList = currentFloorInList;
    }


    private boolean enteringStart = false;

    public void injectPathfindingMap(PathfindingMap pathfindingMap){
        animationMade = false;

        this.pathfindingMap = pathfindingMap;
        search = new Pathfinder(pathfindingMap.graph);


        //add autocomplete to start and end text fields
        acpStart = new AutoCompletePopup(start);
        acpEnd = new AutoCompletePopup(end);

        Criteria noHalls = new CriteriaNoHallways();
        //filter out hallways from nodes
        List<Node> filteredNodes = noHalls.meetCriteria(pathfindingMap.graph.getGraph());

        //get names of nodes
        List<String> nodeNames = new ArrayList<>();
        for (Node n: filteredNodes){nodeNames.add(n.getName());}

        //add names to list of possible autocomplete suggestions
        acpStart.getSuggestions().addAll(nodeNames);
        acpEnd.getSuggestions().addAll(nodeNames);
    }


    /**
     * run when user clicks into start text field and changes state
     * @param e
     */
    public void enterStart(MouseEvent e){
        changeState(State.ENTERSTART);
//        mapState = PathTabController.State.ENTERSTART;
        System.out.println("enter start!");

    }


    /**
     * run when text in start or end location is changed to update instructions for entering locations
     */
    public void textChanged(){
        System.out.println("text changed");
        if(start.getText()==null || start.getText().equals("")){ //nothing has been entered in start location
            instructions.setText("click a point to enter a location");
            changeState(State.ENTERSTART);
        }
        else if(end.getText()==null || end.getText().equals("")){ //nothing in end location
            instructions.setText("enter and end location");
            changeState(State.ENTEREND);
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
        changeState(State.ENTEREND);
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

            parkingSpot.setEnd(true);
            if(endNodeButton!=null && endNodeButtonHold != endNodeButton) {
                endNodeButton.setEnd(false);
                endNodeButton.setButtonStyle();
            }

            endNode = parkingSpot.getNode();
            endNodeButton = parkingSpot;
            changeState(State.ENTERSTART);
            parkingSpot.setButtonStyle();

            acpEnd.hide();

            if(!start.getText().equals("")){ //if a starting location is entered
                findPath(actionEvent);
            }

        }
    }

    @FXML
    public void parkingHelp(){
//        System.out.println("Click ");
        CenterPopup cp = new CenterPopup("Click right on a location to save it as your parking spot. The location will be highlighted in yellow.", pathfindingMap.base);
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
            animatedPath = path;
            updateDirectionsTable(textPath.getTableDirections());
            animatePath(pathfindingMap.imageView);

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

//            for(Arrow el: arrowLine){
//                pathfindingMap.btnPane.getChildren().remove(el);
//            }
//            arrowLine.clear();



            //Make path red
            for (int i = 0; i < path.size(); i++) {
                System.out.println("MAKING PATH");
                System.out.print(path.get(i).getName() + " ");
                if(i>0) {
//                    //TRYING TO MAKE ARROWS
//                    Arrow arrow = new Arrow(path.get(i), path.get(i-1));
//                    arrow.setStyle("-fx-stroke: red; -fx-stroke-width: 5px;");
//                    arrowLine.add(arrow);
//                    arrow.toFront();

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


            //UPDATE START AND END BUTTONS
            endNodeButton.getNode().setIsPathfinding(true);
            startNodeButton.getNode().setIsPathfinding(true);
            startNodeButton.setButtonStyle();
            endNodeButton.setButtonStyle();

            //clear old path nodes
            if(startNodeButtonHold!=null){
                startNodeButtonHold.getNode().setIsPathfinding(false);
                startNodeButtonHold.setStart(false);
            }
            if(endNodeButtonHold!=null){
                endNodeButtonHold.getNode().setIsPathfinding(false);
                endNodeButtonHold.setEnd(false);
            }
            startNodeButtonHold = startNodeButton;
            endNodeButtonHold = endNodeButton;


            pathfindingMap.centerPath(path);

        }
        else{
            System.out.println("please enter a start AND and end location");
        }

    }

    private void updateDirectionsTable(List<DirectionTableEntry> directions) {
        //Add instructions to DirectionsTable
//        ObservableList<DirectionTableEntry> tableDirections = FXCollections.observableArrayList();
//        for (DirectionTableEntry entry : directions) {
//            tableDirections.add(entry);
//        }

        //split directions list by floor
        ObservableList<ObservableList<DirectionTableEntry>> tableDirections = FXCollections.observableArrayList();
        String currentFloor = "";
        int numOfLists = 0;
        for(DirectionTableEntry entry : directions) {
            if(!currentFloor.equals(entry.getNode().getFloor())) {
                currentFloor = entry.getNode().getFloor();
                tableDirections.add(FXCollections.observableArrayList());
                numOfLists += 1;
            }
            tableDirections.get(numOfLists - 1).add(entry);
        }

        textDirectionsTables = new ArrayList<>();
        //create text directions table list
        for (int i = 0; i < numOfLists; i++) {
            DirectionsList newList = new DirectionsList();
            newList.updateDirectionsTable(tableDirections.get(i));

            textDirectionsTables.add(newList);
        }

        //fill accordion
        directionsVBox.getChildren().clear();
//        for(DirectionsList table : textDirectionsTables) {
//            numOfLists -= 1;
//            TitledPane pane = new TitledPane("Floor: " + tableDirections.get(numOfLists).get(0).getNode().getFloor() , new Label(""));
//            System.out.println("Floor: " + tableDirections.get(numOfLists).get(0).getNode().getFloor());
//            pane.setContent(table);
//            directionsVBox.getChildren().add(pane);
//        }
        for (int i = 0; i < textDirectionsTables.size(); i++) {
            DirectionsList table = textDirectionsTables.get(i);
            TitledPane pane = new TitledPane("Floor: " + tableDirections.get(i).get(0).getNode().getFloor() , new Label(""));
            System.out.println("Floor: " + tableDirections.get(i).get(0).getNode().getFloor());
            pane.setContent(table);
            directionsVBox.getChildren().add(pane);
        }
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

            //set style
            button.setStart(true);
            button.setButtonStyle();
            if(startNodeButton!=null && startNodeButtonHold!=startNodeButton) {
                startNodeButton.setStart(false);
                startNodeButton.setButtonStyle();
            }

            startNode= button.getNode();
            start.setText(button.getName()); //set text field text to be node name
            System.out.println("start: "+ button.getName());
            startNodeButton = button;
            changeState(State.ENTEREND);

        }
        else if(mapState.equals(State.ENTEREND)){
            //set style
            button.setEnd(true);
            button.setButtonStyle();
            if(endNodeButton!=null && endNodeButtonHold != endNodeButton) {
                endNodeButton.setEnd(false);
                endNodeButton.setButtonStyle();
            }

            endNode = button.getNode();
            end.setText(button.getName()); //set text field text to be node name
            System.out.println("end: "+ button.getName());
            endNodeButton = button;
            changeState(State.ENTERSTART);

        }

        //print statements
        String text1= start.getText();
        System.out.println(text1);
        String text2= end.getText();
        System.out.println(text2);

        textChanged(); //update instructions

        //don't show suggestions because button was pressed
        acpEnd.hide();
        acpStart.hide();

    }

    /**
     * changes state of inputing start and end location
     * updates the style of the text boxes to show if start of end is currently being inputted
     * @param newState
     */
    public void changeState(State newState){
        System.out.println("change state");
        mapState = newState;
        if(newState.equals(State.ENTERSTART)){//add border
            start.setStyle(start.getStyle()+";-fx-border-color: black;" +
                    "-fx-border-width: 3px");
            end.setStyle(end.getStyle() + //take away border
                    ";-fx-border-width: 0px");
        }
        else if( newState.equals(State.ENTEREND)){
            //add border
            end.setStyle(end.getStyle()+";-fx-border-color: black;" +
                    "-fx-border-width: 3px");
            //take away border
            start.setStyle(start.getStyle() +
                    ";-fx-border-width: 0px");
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

    public void animatePath(ImageView imageview) {
        animationMade = true;
        clearAnimations();
        if (animatedPath == null || animatedPath.size() < 2) {
            return;
        }
        Path path;
        path = createPathForAnimation(animatedPath, imageview);
        if (!(path.getElements().size() < 2)) {
            double startX =  scaleX(((MoveTo) path.getElements().get(0)).getX(), imageview);
            double startY = scaleY(((MoveTo) path.getElements().get(0)).getY(), imageview);
            for (int i =0; i<path.getElements().size() * 3; i++) {
                Circle aCircle = new Circle(
                        startX,
                        startY,
                        5,
                        Color.color(0/255f, 0/255f,0/255f));
                aCircle.setRadius(6);
                pathfindingMap.linePane.getChildren().add(aCircle);
                PathTransition pl = new PathTransition();
                pl.setNode(aCircle);
                pl.setPath(path);
                pl.setDuration(Duration.seconds(pathDistance/100));
                pl.setDelay(Duration.seconds(i*pathDistance/(100*path.getElements().size())));
                pl.setAutoReverse(false);
                pl.setCycleCount(Animation.INDEFINITE);
                pl.play();

                pathTransitions.add(pl);
            }
        }
    }

    private void clearAnimations() {
        if (!(pathfindingMap == null)) {
            for (PathTransition pl : pathTransitions) {
                pl.stop();
                pathfindingMap.linePane.getChildren().remove(pl.getNode());
                pathDistance = 0;
            }
        }
    }

    public Path createPathForAnimation(List<Node> path, ImageView imageView) {
        Path onePath = new Path();
        for (int i = 0; i < path.size() - 1; i++) {
            if (path.get(i).getFloor().equals(pathfindingMap.getCurrFloorVal())
                    && path.get(i+1).getFloor().equals(pathfindingMap.getCurrFloorVal())) {
                MoveTo moveTo = new MoveTo();
                moveTo.setX((float) scaleX(path.get(i).getXcoord(), imageView));
                moveTo.setY((float) scaleY(path.get(i).getYcoord(), imageView));

                LineTo lineTo = new LineTo();
                lineTo.setX((float) scaleX(path.get(i+1).getXcoord(), imageView));
                lineTo.setY((float) scaleY(path.get(i+1).getYcoord(), imageView));

                onePath.getElements().add(onePath.getElements().size(), moveTo);
                onePath.getElements().add(onePath.getElements().size(), lineTo);
                pathDistance += Math.sqrt(Math.pow(lineTo.getX()-moveTo.getX(),2)
                        +Math.pow(lineTo.getY()-moveTo.getY(), 2));
            }
        }
        return onePath;
    }
    public double scaleX(double xCoord, ImageView imageView) {
        double scaleX = imageView.getViewport().getWidth() / imageView.getFitWidth();
        Rectangle2D viewport = imageView.getViewport();
        double x = (xCoord / scaleX) - viewport.getMinX() / scaleX;
        return x;
    }

    public double scaleY(double yCoord, ImageView imageView) {
        double scaleY = imageView.getViewport().getHeight() / imageView.getFitHeight();
        Rectangle2D viewport = imageView.getViewport();
        double y = (yCoord / scaleY) - viewport.getMinY() / scaleY;
        return y;
    }

    public void updateAnimatedPath(ImageView imageview) {
        if (animationMade) {
            animatePath(imageview);
        }
    }

}
