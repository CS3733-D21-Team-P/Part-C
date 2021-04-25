package edu.wpi.p.AStar;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class NodeButton extends Button {
    public Node getNode() {
        return node;
    }

    private Node node;
    private double maxWidth = 12;
    private double maxHeight = 12;

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

        setStyle();


    }

    private void setStyle(){
        //add icons to certain types of nodes
        String nameOfFile = "";
        switch (node.getType()) {
            case "BATH":
            case "REST":
                nameOfFile = "/edu/wpi/p/image/icons/restroom.jpg";
                break;
            case "RETL":
                nameOfFile = "/edu/wpi/p/image/icons/retail.png";
                break;
            case "ELEV":
                nameOfFile = "/edu/wpi/p/image/icons/elevator.png";
                break;
            case "STAI":
                nameOfFile = "/edu/wpi/p/image/icons/stairs.png";
                break;
            case "EXIT":
                nameOfFile = "/edu/wpi/p/image/icons/exit.jpg";
                break;
        }
        if (!nameOfFile.isEmpty()) {
            int addedSize = 5;
            setButtonSize(maxHeight+addedSize);
            Image buttonIcon = new Image(getClass().getResourceAsStream(nameOfFile),maxWidth+addedSize,maxHeight+addedSize,true, true);
            BackgroundImage backgroundImage = new BackgroundImage( buttonIcon, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
            Background background = new Background(backgroundImage);
            setBackground(background);
        }
    }

    public void update(ImageView imageView){
        setStyle();
        pan(imageView);
    }

    public void addLine(EdgeLine el){
        lines.add(el);
    }

    public String getName() {
        return node.getName();
    }

    public void pan(ImageView imageView) {
        double scaleX = imageView.getViewport().getWidth() / imageView.getFitWidth();
        double scaleY = imageView.getViewport().getHeight() / imageView.getFitHeight();
        Rectangle2D viewport = imageView.getViewport();
        this.setLayoutX((node.getXcoord() / scaleX) - viewport.getMinX() / scaleX);
        this.setLayoutY((node.getYcoord() / scaleY) - viewport.getMinY() / scaleY);
    }

    public void setButtonSize(double buttonSize) {
        this.setMinWidth(buttonSize);
        this.setMinHeight(buttonSize);
        this.setMaxWidth(buttonSize);
        this.setMaxHeight(buttonSize);
    }
}