package AStar;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class NodeButton extends Button {
    public Node getNode() {
        return node;
    }

    private Node node;
    private double maxWidth = 10;
    private double maxHeight = 10;

    public List<EdgeLine> getLines() {
        return lines;
    }

    private List<EdgeLine> lines = new ArrayList<EdgeLine>();


    public NodeButton(Node newNode) {
        super();
        this.node = newNode;
        this.setLayoutX(node.getXcoord());
        this.setLayoutY(node.getYcoord());

        //set size of button
        this.setMinWidth(maxWidth);
        this.setMinHeight(maxHeight);
        this.setMaxWidth(maxWidth);
        this.setMaxHeight(maxHeight);

        //translate so node point is in the center of the button
        this.setTranslateX(-maxWidth/2);
        this.setTranslateY(-(maxHeight/2));

        this.setText(newNode.getName());


    }

    public void addLine(EdgeLine el){
        lines.add(el);
    }

    public String getName() {
        return node.getName();
    }

    public void pan(Rectangle2D viewport, double scaleX, double scaleY, double offsetScaleX, double offsetScaleY) {
        this.setLayoutX((node.getXcoord() / scaleX) - viewport.getMinX() / offsetScaleX);
        this.setLayoutY((node.getYcoord() / scaleY) - viewport.getMinY() / offsetScaleY);
    }

    public void setButtonSize(double buttonSize) {
        this.setMinWidth(buttonSize);
        this.setMinHeight(buttonSize);
        this.setMaxWidth(buttonSize);
        this.setMaxHeight(buttonSize);
    }
}