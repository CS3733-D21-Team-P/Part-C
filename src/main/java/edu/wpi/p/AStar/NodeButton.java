package edu.wpi.p.AStar;

import com.jfoenix.controls.JFXButton;
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

    private boolean connectsLevels = false;

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
                nameOfFile = "/image/icons/restroom.jpg";
                break;
            case "RETL":
                nameOfFile = "/image/icons/retail.png";
                break;
            case "ELEV":
                nameOfFile = "/image/icons/elevator.png";
                break;
            case "STAI":
                nameOfFile = "/image/icons/stairs.png";
                break;
            case "EXIT":
                nameOfFile = "/image/icons/exit.jpg";
                break;
        }

        int addedSize = 0;

        if(connectsLevels) {
            setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            addedSize += 2;
            //setStyle("-fx-background-color: #00ff00");
        }

        if (!nameOfFile.isEmpty()) {
            addedSize+=5;
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
        if(el.connectsLevels()){
            connectsLevels=true;
            setStyle();
        }
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