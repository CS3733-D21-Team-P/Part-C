package edu.wpi.p.views;

import AStar.EdgeLine;
import AStar.Node;
import AStar.NodeButton;
import edu.wpi.p.database.DBTable;

public class EditMap extends MapController{
    private DBTable dbTable = new DBTable();

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
        nb.setOnMouseReleased(event -> {
            System.out.println("mouse released");
            Editcoord(nb, nb.getLayoutX(), nb.getLayoutY());
            System.out.println(nb.getName());
            for(EdgeLine el: nb.getLines()){
                el.update(scaleX,scaleY, nb.getNode());
                EdgeLine oppositeLine = findEdgeLine(el.getEndNode(), nb.getNode());
                oppositeLine.update(scaleX,scaleY,nb.getNode());
            }
        });

        nb.setOnMouseClicked(event -> {
            System.out.println(nb.getNode().getXcoord());
            System.out.println(nb.getLayoutX());
        });
        return nb;
    }


    public void Editcoord(NodeButton nb, double newX, double newY) {
        Node node = nb.getNode();
        //set to be position without scale
        node.setYcoord((int)(newY/scaleY));
        node.setXcoord((int)(newX/scaleX));

        //Update in DB
        DBTable dbTable = new DBTable();
        dbTable.updateNode(node);
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
