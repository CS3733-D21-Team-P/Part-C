package edu.wpi.p.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.p.App;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import edu.wpi.p.views.custom.FlexiblePane;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javax.imageio.ImageIO;

public class ServiceRequestHomePage {

    @FXML public AnchorPane anchorPane;
    @FXML public GridPane gridPane;
    @FXML public HBox hbox;
    @FXML public StackPane imageStack;
    @FXML public FlexiblePane flexiblePane;
    @FXML public JFXButton languageInterpretersBtn;
    @FXML public JFXButton medicineDeliveryServiceBtn;
    @FXML public JFXButton laundryServicesBtn;
    @FXML public JFXButton externalPatientTransportationBtn;
    @FXML public JFXButton internalPatientTransportationBtn;
    @FXML public JFXButton sanitationServiceBtn;
    @FXML public JFXButton floralDeliveryBtn;
    @FXML public JFXButton foodDeliveryBtn;
    @FXML public JFXButton facilitiesMaintenanceBtn;
    @FXML public JFXButton homeButton;
    @FXML public JFXButton pathButton;
    @FXML public JFXButton editButton;
    @FXML public JFXButton serviceButton;
    @FXML public JFXButton covidButton;

    @FXML private Pane clippoID;
    @FXML private ClippoController clippoIDController;

    private static final double BLUR_AMOUNT = 40;
    private static final Effect frostEffect =
            new BoxBlur(BLUR_AMOUNT, BLUR_AMOUNT, 2);

    @FXML
    public void initialize() {
        clippoIDController.setPage("serviceRequests");
        gridPane.maxWidthProperty().bind(anchorPane.widthProperty());
        gridPane.maxHeightProperty().bind(anchorPane.heightProperty());
        setupFrostedGlassBackground();
    }

    private void setupFrostedGlassBackground() {
        ReadOnlyDoubleProperty y = hbox.heightProperty();
        Node image = imageStack.getChildren().get(0);

        ReadOnlyDoubleProperty w = imageStack.widthProperty();
        ReadOnlyDoubleProperty h = imageStack.heightProperty();

        StackPane frost = freeze(image, y, w, h);
        frost.setVisible(true);
        imageStack.getChildren().set(0, frost);
    }


    private StackPane freeze(Node background, ReadOnlyDoubleProperty y, ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        Image frostImage = background.snapshot(
                new SnapshotParameters(),
                null
        );

        ImageView frost = new ImageView(frostImage);
        frost.fitWidthProperty().bind(width);
        frost.fitHeightProperty().bind(height);
        Rectangle filler = new Rectangle(0, 0, width.doubleValue(), height.doubleValue());
        filler.widthProperty().bind(width);
        filler.heightProperty().bind(height);
        filler.setFill(Color.AZURE);

        Pane frostPane = new Pane(frost);
        frostPane.setEffect(frostEffect);

        StackPane frostView = new StackPane(
                filler,
                frostPane
        );

        Rectangle clipShape = new Rectangle(0, 0, width.doubleValue(), height.doubleValue());
        clipShape.widthProperty().bind(width);
        clipShape.heightProperty().bind(height);
        frostView.setClip(clipShape);

        return frostView;
    }

    @FXML
    private void advanceScene(ActionEvent e) {

    }

    public void languageInterpretersBtn(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/servicerequestsFXML/LanguageInterpreterServiceRequest.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void medicineDeliveryServiceBtn(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/servicerequestsFXML/MedicineDeliveryServiceRequest.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void laundryServicesBtn(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/servicerequestsFXML/LaundryServiceRequest.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void externalPatientTransportationBtn(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/servicerequestsFXML/ExternalpatienttransportationRequest.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void internalPatientTransportationBtnAc(ActionEvent actionEvent){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/servicerequestsFXML/InternalPatientTransportation.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sanitationServiceBtn(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/servicerequestsFXML/SanitationServiceRequest.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void floralDeliveryBtn(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/servicerequestsFXML/FloralDeliveryRequest.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void foodDeliveryBtn(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/servicerequestsFXML/FoodDeliveryRequest.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void facilitiesMaintenanceBtn(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/servicerequestsFXML/FacilitiesMaintenanceRequest.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void entryButtonAc(ActionEvent actionEvent){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/servicerequestsFXML/EntryRequest.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void requestLogBtnAc(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/RequestLogPage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    public void pathButtonAc(ActionEvent actionEvent){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/mapsFXML/PathfindingMap.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void editButtonAc(ActionEvent actionEvent){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/mapsFXML/EditMap.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void serviceButtonAc(ActionEvent actionEvent){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ServiceRequestHomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void covidButtonAc(ActionEvent actionEvent){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("..."));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void giftDelivery(ActionEvent actionEvent) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/servicerequestsFXML/GiftDelivery.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }
}
