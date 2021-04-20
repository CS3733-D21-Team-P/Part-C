package AStar;

import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Line;

public class EdgeLine extends Line {
    public Node getStartNode() {
        return startNode;
    }

    private Node startNode;

    public Node getEndNode() {
        return endNode;
    }

    private Node endNode;

    public EdgeLine(Node start, Node end) {
        super();
        this.startNode = start;
        this.endNode = end;
        //set start point
        this.setStartX(start.getXcoord());
        this.setStartY(start.getYcoord());
        //set end point
        this.setEndX(end.getXcoord());
        this.setEndY(end.getYcoord());

        this.setStyle("-fx-stroke: blue;");

    }

    /**
     * update the lines location based on nodes new location
     * @param scaleX : x scale of map
     * @param scaleY :y scale of map
     * @param node : the node that has changed
     */
    public void update(double scaleX, double scaleY, Node node){
        if(startNode == node){
            //set start point
            this.setStartX(startNode.getXcoord()*scaleX);
            this.setStartY(startNode.getYcoord()*scaleY);
        }
        else{
            //set end point
            this.setEndX(endNode.getXcoord()*scaleX);
            this.setEndY(endNode.getYcoord()*scaleY);
        }
        System.out.println("updating:  " + startNode.getName() + " - "+ endNode.getName());


    }

    //pan and zoom update transform
    public void pan(Rectangle2D viewport, double scaleX, double scaleY, double offsetScaleX, double offsetScaleY) {
        this.setStartX((startNode.getXcoord() / scaleX) - viewport.getMinX() / offsetScaleX);
        this.setStartY((startNode.getYcoord() / scaleY) - viewport.getMinY() / offsetScaleY);

        this.setEndX((endNode.getXcoord() / scaleX) - viewport.getMinX() / offsetScaleX);
        this.setEndY((endNode.getYcoord() / scaleY) - viewport.getMinY() / offsetScaleY);
    }
}
