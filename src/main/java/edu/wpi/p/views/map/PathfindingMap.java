package edu.wpi.p.views.map;

import edu.wpi.p.AStar.*;
import edu.wpi.p.views.map.MapController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class PathfindingMap extends MapController {

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
//    NodeGraph graph = new NodeGraph();
    //List<Node> graph = new ArrayList<>();


    private boolean enteringStart = false;
    @FXML public TextField start;
    @FXML public TextField end;
//    @FXML
//    public AnchorPane btnPane;
//    @FXML
//    public AnchorPane linePane;
//    @FXML
//    public ImageView imageView;

    /**
     * run when user clicks into start text field and changes state
     * @param e
     */
    public void enterStart(MouseEvent e){
        mapState = State.ENTERSTART;
        System.out.println("enter start!");
    }

    /**
     * run when user clicks into end textfield and changes state
     * @param e
     */
    public void enterEnd(MouseEvent e){
        mapState = State.ENTEREND;
        System.out.println("enter end!");
    }

    /**
     * Finds a path between the two nodes entered and makes it red
     * @param actionEvent
     */
    public void findPath(ActionEvent actionEvent){
        if(startNode!=null && endNode!=null) {
            //find path
            List<Node> path = new ArrayList<>();
            path = search.findShortestPath(startNode, endNode);

            //Path To Text
            PathToText textPath = new PathToText();
            textPath.getDirections(path);
            textPath.PrintPath();

            //print path
            System.out.println("Path: ");
            for (Node n : path) {
                System.out.print(n.getName() + " ");
            }

            for(EdgeLine el: pathLine){
                linePane.getChildren().remove(el);
            }
            pathLine.clear();


            //Make path red
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i).getName() + " ");
                if(i>0) {
                    EdgeLine line = addEdgeLine(path.get(i), path.get(i-1));
                    line.setStyle("-fx-stroke: red; -fx-stroke-width: 5px;");
                    pathLine.add(line);
                }
            }

            graph.resetNodeGraph();

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
            startNode= button.getNode();
            start.setText(button.getName()); //set text field text to be node name
            System.out.println("start: "+ button.getName());
        }
        else if(mapState.equals(State.ENTEREND)){
            endNode = button.getNode();
            end.setText(button.getName()); //set text field text to be node name
            System.out.println("end: "+ button.getName());
        }
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

            //set on click method
            nb.setOnAction(event -> {
                addNodeToSearch(event);
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
        System.out.println("PATHFINDING INIT");
        for (Node n: graph.getGraph()){
            addNodeButton(n);

        }
        translateGraph(imageView);

    }

}
