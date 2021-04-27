package edu.wpi.p.views.map;

import edu.wpi.p.AStar.*;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class PathfindingMap extends MapController {

    @FXML private PathTabController pathTabController;

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
        pathTabController.injectPathfindingMap(this);
        System.out.println("PATHFINDING INIT");
        for (Node n: graph.getGraph()){
            addNodeButton(n);

        }
        translateGraph(imageView);

    }

}
