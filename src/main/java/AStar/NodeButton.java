package AStar;

import javafx.scene.control.Button;

public class NodeButton extends Button {
    public Node getNode() {
        return node;
    }

    private Node node;
    private double maxWidth = 20;
    private double maxHeight = 20;


    public NodeButton(Node newNode) {
        super();
        this.node = newNode;
        this.setLayoutX(node.getxCoord());
        this.setLayoutY(node.getyCoord());

        //set size of button
        this.setMinWidth(maxWidth);
        this.setMinHeight(maxHeight);
        this.setMaxWidth(maxWidth);
        this.setMaxHeight(maxHeight);

        //translate so node point is in the center of the button
        this.setTranslateX(-maxWidth/2);
        this.setTranslateY(-(maxHeight/2));


    }

    public String getName() {
        return node.getName();
    }


}