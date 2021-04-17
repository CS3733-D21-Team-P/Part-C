package edu.wpi.p.views;

import AStar.Node;
import AStar.NodeButton;

public class EditMap extends MapController{


    /**
     * creates a button associated  with a node
     * adds a line to neighbour nodes
     * adds on click functions
     * @param node
     * @return created NodeButton
     */
    @Override
    public NodeButton addNodeButton(Node node){
        NodeButton nb = super.addNodeButton(node);
        //set on click method
        nb.setOnMouseDragged(e -> {
            nb.setLayoutX(e.getSceneX());
            nb.setLayoutY(e.getSceneY());
        });

        return nb;
    }

    @Override
    public void initialize()  {
        super.initialize();
        System.out.println("EDIT INIT");

        //add buttons
        for (Node n: graph.getGraph()){
            addNodeButton(n);
        }


    }
}
