package edu.wpi.p.views.map;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.AStar.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DirectionTableEntry extends RecursiveTreeObject<DirectionTableEntry> {
    private ImageView image;
    private String instruction;
    private Node node;

    public DirectionTableEntry(ImageView img, String instruction) {
        this.image = img;
        this.instruction = instruction;
    }

    public void setNode(Node n) { this.node = n; }

    public Node getNode() { return node; }

    public ImageView getImageVew() { return image; }

    public String getInstruction() { return instruction; }
}
