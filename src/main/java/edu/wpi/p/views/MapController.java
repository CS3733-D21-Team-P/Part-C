package edu.wpi.p.views;

import AStar.EdgeLine;
import AStar.Node;
import AStar.NodeButton;
import AStar.NodeGraph;
import edu.wpi.p.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapController {
    NodeGraph graph = new NodeGraph();
    List<NodeButton> buttons = new ArrayList<>();

    private double maxWidth = 15;
    private double maxHeight = 15;

    @FXML
    public AnchorPane btnPane;
    @FXML
    public AnchorPane linePane;
    @FXML
    public ImageView imageView;

    /**
     * creates a button associated  with a node
     * adds a line to neighbour nodes
     * @param node
     * @return created NodeButton
     */
    public NodeButton addNodeButton(Node node){
        NodeButton nb = new NodeButton(node); //create button

        //add icons to certain types of nodes
        String nameOfFile ="";
        switch (node.getType()) {
            case "BATH":
            case "REST":
                nameOfFile = "/image/icons/restroom.jpg";
                break;
            case "RETL":
                nameOfFile = "/image/icons/retail.png";
                break;
            case "ELEV":
                nameOfFile = "/image/icons/elevator.png";
                break;
            case "STAI":
                nameOfFile = "/image/icons/stairs.png";
                break;
            case "EXIT":
                nameOfFile = "/image/icons/exit.jpg";
                break;
        }
        if (!nameOfFile.isEmpty()) {
            Image buttonIcon = new Image(getClass().getResourceAsStream(nameOfFile));
            ImageView iconImage = new ImageView(buttonIcon);
            iconImage.setFitHeight(maxHeight);
            iconImage.setFitWidth(maxWidth);
            nb.setGraphic(iconImage);
        }
        //set on click method
//        nb.setOnAction(event -> {
//            addNodeToSearch(event);});
        btnPane.getChildren().add(nb); //add to page
        List<Node> children = node.getNeighbours();
        for(Node n: children){
            addEdgeLine(node, n);
        }

        buttons.add(nb);
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

    public void homeButtonAc(ActionEvent actionEvent){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    /**
     * run when page starts
     * adds buttons and edge lines to map
     */
    public void initialize(ImageView imageView)  {

        graph.genGraph(false);

        System.out.println("INIT SUPER!!!!");
        //TODO fix aspect ratio & offset
        double winWidth = imageView.getFitWidth();
        double imageWidth = imageView.getImage().getWidth();
        double scaleX = winWidth / imageWidth;

        double winHeight = imageView.getFitHeight();
        double imageHeight = imageView.getImage().getHeight();
        double scaleY = winHeight / imageHeight;

        graph.scaleGraph(scaleX, scaleY);

        for (Node n: graph.getGraph()){
            addNodeButton(n);
        }

    }

}
