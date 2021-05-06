package edu.wpi.p.AStar;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class NodeButton extends JFXButton {
    public Node getNode() {
        return node;
    }

    private boolean connectsLevels = false;

    private Node node;

    private double baseSize = 10; //base size for scaling
    public double currentSize; //size of button
    private double scaleFactor = 5; //scale amount for zoom and pan

    private String nameOfFile = "";
    private Image buttonIcon;


    public List<EdgeLine> getLines() {
        return lines;
    }

    private List<EdgeLine> lines = new ArrayList<EdgeLine>();

    public boolean toggleIsSelected(boolean isSelected){
        node.setIsSelected(isSelected);
        setButtonStyle();
        return node.getIsSelected();
    }


    public NodeButton(Node newNode) {
        super();
        this.node = newNode;
        this.setLayoutX(node.getXcoord());
        this.setLayoutY(node.getYcoord());

        setButtonStyle();
//        currentSize = 10;
//        this.setTranslateX(-currentSize/2);
//        this.setTranslateY(-(currentSize/2));
    }

    public void setButtonStyle(){
        //add icons to certain types of nodes

        switch (node.getType()) {
            case "BATH":
            case "REST":
                nameOfFile = "/edu/wpi/p/fxml/image/icons/restroom.jpg";
                break;
            case "RETL":
                nameOfFile = "/edu/wpi/p/fxml/image/icons/retail.png";
                break;
            case "ELEV":
                nameOfFile = "/edu/wpi/p/fxml/image/icons/elevator.png";
                break;
            case "STAI":
                nameOfFile = "/edu/wpi/p/fxml/image/icons/stairs.png";
                break;
            case "EXIT":
                nameOfFile = "/edu/wpi/p/fxml/image/icons/exit.jpg";
                break;
        }

        if (this.getNode().getIsPathfinding()) { //end node
            this.toFront();
            this.setStyle(
                    "-fx-background-radius: 5em; " +
                            "-fx-background-color: red"
            );
            this.setOpacity(0.7);
            baseSize = 20;
            currentSize = 20;
            updateSize();
//            setButtonSize(currentSize); //set button size
//            this.setTranslateX(-currentSize / 2);
//            this.setTranslateY(-(currentSize / 2));

        }

        else if (!this.getNode().getWasPathfinding()){ //not a pathfinding node
            if (nameOfFile.isEmpty()) {
                if (!getNode().getIsSelected()) {//no image and not selected
                    this.setStyle(getStyle() +
                            ";-fx-background-radius: 5em; " +
                            "-fx-background-color: #2F3159"
                    );
                    setOpacity(0.7);
                } else {//no image and is selected
                    this.setStyle(
                            "-fx-background-radius: 5em; " +
                                    "-fx-background-color: red"
                    );
                }
                if (connectsLevels) { //if connecting levels
                    setStyle(getStyle() + ";-fx-border-color: #00d1b5; " +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 5em");
                }
            } else { //set image if there is an image specified
                buttonIcon = new Image(getClass().getResourceAsStream(nameOfFile), 25, 25, true, true);
                BackgroundSize bgsize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
                BackgroundImage backgroundImage = new BackgroundImage(buttonIcon, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bgsize);
                Background background = new Background(backgroundImage);
                setBackground(background);
                if (!connectsLevels) {
                    setStyle("-fx-border-color: #2F3159; -fx-border-width: 2px;");
                } else {
                    setStyle("-fx-border-color: #00d1b5; -fx-border-width: 2px;");
                }
                if (getNode().getIsSelected()) //if is image and is selected
                {
                    setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                }

            }
        }
        updateSize();

    }

    /**
     * updates size of button based on current set size and centers button
     */
    private void updateSize(){
        setButtonSize(currentSize); //set button size
        //center
        this.setTranslateX(-currentSize/2);
        this.setTranslateY(-(currentSize/2));

    }

    /**
     * sets min and max of button size
     * @param buttonSize
     */
    public void setButtonSize(double buttonSize) {
        this.setMinWidth(buttonSize);
        this.setMinHeight(buttonSize);
        this.setMaxWidth(buttonSize);
        this.setMaxHeight(buttonSize);
    }

    public void update(ImageView imageView){
        setButtonStyle();
        pan(imageView);
    }

    /**
     * adds line to list of lines and checks in connects between levels
     * @param el
     */
    public void addLine(EdgeLine el){
        lines.add(el);
        if(el.connectsLevels()){
            connectsLevels=true;
            setButtonStyle();
        }
    }

    public String getName() {
        return node.getName();
    }

    public void pan(ImageView imageView) {
        double scaleX = imageView.getViewport().getWidth() / imageView.getFitWidth();
        double scaleY = imageView.getViewport().getHeight() / imageView.getFitHeight();
        Rectangle2D viewport = imageView.getViewport();
        double x = (node.getXcoord() / scaleX) - viewport.getMinX() / scaleX;
        double y = (node.getYcoord() / scaleY) - viewport.getMinY() / scaleY;
        this.setLayoutX(x);
        this.setLayoutY(y);


//        BASIC SCALING
        double scale = baseSize+((3.2-scaleX)*scaleFactor);

        double addedSize = 0;
        if(!nameOfFile.isEmpty()){
            addedSize=10; //make images bigger
        }
        currentSize = scale+addedSize;

        updateSize(); //update size based on new set size

    }

    public void deselect()
    {
        this.getNode().setIsSelected(false);
        this.setButtonStyle();
    }

    public void endPathfinding() {
        currentSize = 10;
        this.setStyle(getStyle() +
                ";-fx-background-radius: 10em; " +
                "-fx-min-width: 10px; " +
                "-fx-min-height: 10px; " +
                "-fx-max-width: 10px; " +
                "-fx-max-height: 10px;" +
                "-fx-background-color: #2F3159"
        );
        this.setTranslateX(-5);
        this.setTranslateY(-5);
        this.getNode().setWasPathfinding(false);
    }

    public void makeBigNode() {
        this.toFront();
        this.setStyle(
                "-fx-background-radius: 5em; " +
//                            "-fx-min-width: 12px; " +
//                            "-fx-min-height: 12px; " +
////                            "-fx-max-width: 12px; " +
////                            "-fx-max-height: 12px;" +
                        "-fx-background-color: red"
        );
        this.setOpacity(0.7);
//            if (!this.getNode().getWasPathfinding()) {
        currentSize *= 2;
        setButtonSize(currentSize); //set button size
        this.setTranslateX(-currentSize / 2);
        this.setTranslateY(-(currentSize / 2));
//            }
    }

    public void makeBlue() {
        this.setStyle(
                "-fx-background-radius: 5em; " +
                        "-fx-background-color: #4a7ede"
        );
    }
}