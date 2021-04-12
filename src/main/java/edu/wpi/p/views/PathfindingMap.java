package edu.wpi.p.views;

import AStar.EdgeLine;
import AStar.Node;
import AStar.NodeButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class PathfindingMap {
    enum State {
        ENTERSTART,
        ENTEREND,
        NONE
    }
    private State mapState = State.ENTERSTART;

    private boolean enteringStart = false;
    @FXML
    public TextField start;
    @FXML
    public TextField end;
    @FXML
    public AnchorPane btnPane;
    @FXML
    public AnchorPane linePane;

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

    public void findPath(ActionEvent actionEvent){
        System.out.println("find path!");
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
            start.setText(button.getNode().getName()); //set text field text to be node name
        }
        else if(mapState.equals(State.ENTEREND)){
            end.setText(button.getNode().getName()); //set text field text to be node name
        }
    }

    /**
     * creates a button associated  with a node
     * @param node
     * @return created NodeButton
     */
    public NodeButton addNodeButton(Node node){
        NodeButton nb = new NodeButton(node); //create button
        //set on click method
        nb.setOnAction(event -> {
            addNodeToSearch(event);});
        btnPane.getChildren().add(nb); //add to page
        return nb;
    }

    /**
     * creates a line between two nodebuttons
     * @param node1
     * @param node2
     * @return created EdgeLine
     */
    public EdgeLine addEdgeLine(Node node1, Node node2){
        EdgeLine el = new EdgeLine(node1, node2); //create line
        linePane.getChildren().add(el); //add line to screen
        return el;
    }

    @FXML
    /**
     * run when page starts
     * adds buttons and edge lines to map
     */
    public void initialize()  {
        Node node1 = new Node("test", 300, 350);
        Node node2 = new Node("test2", 300, 200);
        addNodeButton(node1);
        addNodeButton(node2);
        addEdgeLine(node1,node2);
    }
}
