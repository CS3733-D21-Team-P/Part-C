package AStar;

import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Line;

public class EdgeLine extends Line {
    private Node node1;
    private Node node2;


    public EdgeLine(Node start, Node end) {
        super();
        this.node1 = start;
        this.node2= end;
        //set start point
        this.setStartX(start.getXcoord());
        this.setStartY(start.getYcoord());
        //set end point
        this.setEndX(end.getXcoord());
        this.setEndY(end.getYcoord());

        this.setStyle("-fx-stroke: blue;");

    }

    //pan and zoom update transform
    public void pan(Rectangle2D viewport, double scaleX, double scaleY, double offsetScaleX, double offsetScaleY) {
        this.setStartX((node1.getXcoord() / scaleX) - viewport.getMinX() / offsetScaleX);
        this.setStartY((node1.getYcoord() / scaleY) - viewport.getMinY() / offsetScaleY);

        this.setEndX((node2.getXcoord() / scaleX) - viewport.getMinX() / offsetScaleX);
        this.setEndY((node2.getYcoord() / scaleY) - viewport.getMinY() / offsetScaleY);
    }
}
