package AStar;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
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


    //pan and zoom update transform
    public void pan(ImageView imageView) {
        Rectangle2D viewport = imageView.getViewport();
        double scaleX = imageView.getViewport().getWidth() / imageView.getFitWidth();
        double scaleY = imageView.getViewport().getHeight() / imageView.getFitHeight();

        this.setStartX((startNode.getXcoord() / scaleX) - viewport.getMinX() / scaleX);
        this.setStartY((startNode.getYcoord() / scaleY) - viewport.getMinY() / scaleY);

        this.setEndX((endNode.getXcoord() / scaleX) - viewport.getMinX() / scaleX);
        this.setEndY((endNode.getYcoord() / scaleY) - viewport.getMinY() / scaleY);
    }
}
