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
    private List<EdgeLine> pathLine= new ArrayList<>();

    @FXML public JFXTextField start;
    @FXML public JFXTextField end;
    @FXML public Label instructions;


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


            //Make path red
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i).getName() + " ");
                if(i>0) {
                    EdgeLine line = pathfindingMap.addEdgeLine(path.get(i), path.get(i-1));
                    line.setStyle("-fx-stroke: red; -fx-stroke-width: 5px;");
                    pathLine.add(line);
                }
            }

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
            mapState = State.ENTEREND;
        }
        else if(mapState.equals(State.ENTEREND)){
//            instructions.setText("press search to find a path");
            endNode = button.getNode();
            end.setText(button.getName()); //set text field text to be node name
            System.out.println("end: "+ button.getName());
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
}