package edu.wpi.p.views;

import AStar.AStar;
import AStar.EdgeLine;
import AStar.Node;
import AStar.NodeButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import AStar.NodeGraph;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PathfindingMap {
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
    NodeGraph graph = new NodeGraph();
    //List<Node> graph = new ArrayList<>();


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

    /**
     * Finds a path between the two nodes entered and makes it red
     * @param actionEvent
     */
    public void findPath(ActionEvent actionEvent){
        if(startNode!=null && endNode!=null) {
            //find path
            List<Node> path = new ArrayList<>();
            path = search.findShortestPath(startNode, endNode);

            //print path
            System.out.println("Path: ");
            for (Node n : path) {
                System.out.print(n.getName() + " ");
            }

            //clear old path
            if(pathLine.size()!=0) {
                int numChildren = linePane.getChildren().size();
                int oldPathSize = pathLine.size();
                linePane.getChildren().remove(numChildren-oldPathSize, numChildren);
                pathLine.clear();
            }


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
            //graph= createGraph();
//            for (Node n : graph) {
//                n.setVisited(false);
//            }

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
     * @param node
     * @return created NodeButton
     */
    public NodeButton addNodeButton(Node node){
        NodeButton nb = new NodeButton(node); //create button
        //set on click method
        nb.setOnAction(event -> {
            addNodeToSearch(event);});
        btnPane.getChildren().add(nb); //add to page
        List<Node> children = node.getNeighbours();
        for(Node n: children){
            addEdgeLine(node, n);
        }
        return nb;
    }

    /**
     * creates a line between two nodes
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
        try {
            graph.genGraph(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (Node n: graph.getGraph()){
            addNodeButton(n);
        }

    }

    /*
    private List<Node> createGraph(){

        //   B - C
        //  /   / \
        // A - E - D
        //  \  |  /
        //     F - G - H - I - J

        Node a = new Node("A", 0, 200);
        Node b = new Node("B", 50, 300);
        Node c = new Node("C", 150, 300);
        Node d = new Node("D", 200, 200);
        Node e = new Node("E", 100, 200);
        Node f = new Node("F", 100, 100);
        Node g = new Node("G", 200, 100);
        Node h = new Node("H", 300, 100);
        Node i = new Node("I", 400, 100);
        Node j = new Node("J", 500, 100);

        a.addNeighbour(b);
        a.addNeighbour(e);
        a.addNeighbour(f);

        b.addNeighbour(a);
        b.addNeighbour(c);

        c.addNeighbour(b);
        c.addNeighbour(e);
        c.addNeighbour(d);

        d.addNeighbour(c);
        d.addNeighbour(e);
        d.addNeighbour(f);

        e.addNeighbour(a);
        e.addNeighbour(c);
        e.addNeighbour(d);
        e.addNeighbour(f);

        f.addNeighbour(a);
        f.addNeighbour(d);
        f.addNeighbour(e);
        f.addNeighbour(g);

        g.addNeighbour(f);
        g.addNeighbour(h);

        h.addNeighbour(g);
        h.addNeighbour(i);

        i.addNeighbour(h);
        i.addNeighbour(j);

        j.addNeighbour(i);

        graph.add(a);
        graph.add(b);
        graph.add(c);
        graph.add(d);
        graph.add(e);
        graph.add(f);
        graph.add(g);
        graph.add(h);
        graph.add(i);
        graph.add(j);

        return graph;
    }

     */
}
