package AStar;

import javafx.scene.shape.Line;

public class EdgeLine extends Line {
    private Node node1;
    private Node node2;


    public EdgeLine(Node start, Node end) {
        super();
        this.node1 = start;
        this.node2= end;
        //set start point
        this.setStartX(start.getxCoord());
        this.setStartY(start.getyCoord());
        //set end point
        this.setEndX(end.getxCoord());
        this.setEndY(end.getyCoord());

    }
}
