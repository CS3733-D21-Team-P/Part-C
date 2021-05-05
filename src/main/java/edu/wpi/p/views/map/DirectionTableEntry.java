package edu.wpi.p.views.map;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DirectionTableEntry extends RecursiveTreeObject<DirectionTableEntry> {
    private ImageView image;
    private String instruction;

    public DirectionTableEntry(ImageView img, String instruction) {
        this.image = img;
        this.instruction = instruction;
    }

    public ImageView getImageVew() { return image; }

    public String getInstruction() { return instruction; }
}
