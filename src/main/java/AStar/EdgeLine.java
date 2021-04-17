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
        this.setStartX(start.getXcoord());
        this.setStartY(start.getYcoord());
        //set end point
        this.setEndX(end.getXcoord());
        this.setEndY(end.getYcoord());

        this.setStyle("-fx-stroke: blue;");

    }

    public void update(double scaleX, double scaleY, Node node){
        if(node1 == node){
            //set start point
            this.setStartX(node1.getXcoord()*scaleX);
            this.setStartY(node1.getYcoord()*scaleY);
        }
        else{
            //set end point
            this.setEndX(node2.getXcoord()*scaleX);
            this.setEndY(node2.getYcoord()*scaleY);
        }


    }
}
