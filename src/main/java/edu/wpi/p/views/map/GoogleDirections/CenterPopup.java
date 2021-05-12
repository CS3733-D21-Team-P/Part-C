package edu.wpi.p.views.map.GoogleDirections;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.awt.*;

public class CenterPopup extends JFXDialog{

    //make popup with image
    public CenterPopup(String header, String imageFile, Pane base){
        StackPane mySP = new StackPane();
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(header));
        Image img = new Image("file:"+imageFile);
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(img);

        content.setBody(imageView);

        content.setStyle("-fx-alignment: center;" +
                "-fx-background-color:  #b6d6f2");
        this.setDialogContainer(mySP);
        this.setContent(content);
        this.setTransitionType(DialogTransition.CENTER);
//            JFXDialog dialog = new JFXDialog(mySP,content, JFXDialog.DialogTransition.CENTER);
        this.setOverlayClose(false);
        this.setMinHeight(mySP.getPrefHeight());
        this.setMinWidth(mySP.getPrefWidth());
        JFXButton button = new JFXButton("OK");
        button.setOnAction((ActionEvent event) -> {
            base.getChildren().remove(mySP);
            this.close();
            mySP.setVisible(false);
        });
        content.setActions(button);
        AnchorPane.setLeftAnchor(mySP,0.0);
        AnchorPane.setRightAnchor(mySP,0.0);
        AnchorPane.setTopAnchor(mySP, 0.0);
        AnchorPane.setBottomAnchor(mySP, 0.0);
        base.getChildren().add(mySP);
        this.show();
    }


    //make popup with image
    public CenterPopup( String text, Pane base){
        StackPane mySP = new StackPane();
        JFXDialogLayout content = new JFXDialogLayout();
//        content.setHeading(new Text(header));
//        Image img = new Image("file:"+imageFile);
//        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(img);
        Text labelText = new Text(text);
        content.setBody(labelText);

        content.setStyle("-fx-alignment: center;" +
                "-fx-background-color:  #b6d6f2");
        this.setDialogContainer(mySP);
        this.setContent(content);
        this.setTransitionType(DialogTransition.CENTER);
//            JFXDialog dialog = new JFXDialog(mySP,content, JFXDialog.DialogTransition.CENTER);
        this.setOverlayClose(false);
        this.setMinHeight(mySP.getPrefHeight());
        this.setMinWidth(mySP.getPrefWidth());
        JFXButton button = new JFXButton("OK");
        button.setOnAction((ActionEvent event) -> {
            base.getChildren().remove(mySP);
            this.close();
            mySP.setVisible(false);
        });
        content.setActions(button);
        AnchorPane.setLeftAnchor(mySP,0.0);
        AnchorPane.setRightAnchor(mySP,0.0);
        AnchorPane.setTopAnchor(mySP, 0.0);
        AnchorPane.setBottomAnchor(mySP, 0.0);
        base.getChildren().add(mySP);
        this.show();
    }
}
