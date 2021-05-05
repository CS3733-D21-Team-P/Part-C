package edu.wpi.p.views.map;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import edu.wpi.p.AStar.*;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PathTabController {

    @FXML private JFXTextArea textDirectionsField;
    private PathfindingMap pathfindingMap;


    enum State {
        ENTERSTART,
        ENTEREND,
        NONE
    }
    private State mapState = State.ENTERSTART;

    private AStar search = new AStar();
    private Node startNode;
    private Node endNode;
    private NodeButton startNodeButton;
    private NodeButton endNodeButton;
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
    }


    /**
     * run when user clicks into start text field and changes state
     * @param e
     */
    public void enterStart(MouseEvent e){
        mapState = PathTabController.State.ENTERSTART;
        System.out.println("enter start!");

    }


    public void textChanged(){
        System.out.println("text changed");
        if(start.getText()==null || start.getText().equals("")){
            instructions.setText("click a point to enter a location");
        }
        else if(end.getText()==null || end.getText().equals("")){
            instructions.setText("enter and end location");
        }
        else if(end.getText().equals(start.getText())){
            instructions.setText("choose two different locations");
        }
        else{
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
            List<Node> path = new ArrayList<>();
            path = search.findShortestPath(startNode, endNode);
            floorsInPath = getFloorsInPath(path);
            if (floorsInPath.size() > 1) {
                pathfindingMap.nextFloorBox.setVisible(true);
                pathfindingMap.multipleFloors = true;
            }
            for (int i = 0; i < floorsInPath.size(); i++) {
                if (floorsInPath.get(i).equals(pathfindingMap.getCurrFloorVal())) {
                    currentFloorInList = i;
                }
            }
            colorButtons();

            //Path To Text
            PathToText textPath = new PathToText();
            textPath.makeDirections(path);
            textDirectionsField.clear();
            for (String text : textPath.getDirections()){
                textDirectionsField.appendText(text);
                textDirectionsField.appendText("\n");
            }


            //print path
            System.out.println("Path: ");
            for (Node n : path) {
                System.out.print(n.getName() + " ");
            }

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
            if (startNodeButton != startNodeButtonHold) {
                startNodeButton.setButtonStyle();
                if (startNodeButtonHold != null) {
                    startNodeButtonHold.endPathfinding();
                }
            }
            if (endNodeButton != endNodeButtonHold) {
                endNodeButton.setButtonStyle();
                if (endNodeButtonHold != null) {
                    endNodeButtonHold.endPathfinding();
                }
            }

            startNodeButton.getNode().setIsPathfinding(false);
            endNodeButton.getNode().setIsPathfinding(false);
            startNodeButton.getNode().setWasPathfinding(true);
            endNodeButton.getNode().setWasPathfinding(true);
            startNodeButtonHold = startNodeButton;
            endNodeButtonHold = endNodeButton;

            pathfindingMap.graph.resetNodeGraph();

        }
        else{
            System.out.println("please enter a start AND and end location");
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
