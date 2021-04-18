package AStar;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NodeButton extends Button {
    public Node getNode() {
        return node;
    }

    private Node node;
    private double maxWidth = 10;
    private double maxHeight = 10;


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

    public String getName() {
        return node.getName();
    }


}