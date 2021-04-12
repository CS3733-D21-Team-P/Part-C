package AStar;

import javafx.scene.control.Button;

public class NodeButton extends Button {
    public Node getNode() {
        return node;
    }

    private Node node;


    public NodeButton(Node newNode) {
        super();
        this.node = newNode;
        this.setLayoutX(node.getxCoord());
        this.setLayoutY(node.getyCoord());
        this.setText(node.getName());
        System.out.println(node.getName());

    }

    public String getName() {
        return node.getName();
    }


}