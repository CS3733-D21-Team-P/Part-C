package edu.wpi.p.views.map;

import com.google.maps.model.TravelMode;
import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.views.map.GoogleDirections.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GoogleTabController {
    GoogleMaps googleMaps = new GoogleMaps();
    final String[] availableModes = new String[]{"Driving", "Walking","Biking"};
    final String[] availableParking = new String[]{"The hospital", "45 Francis Street Garage (Patient Only)", "60 Fenwood Road Garage (Patient Only)","75 Francis Street (Patient Valet)", "80 Francis Street Garage (Patient & Visitor)", "221 Longwood Avenue (Patient Valet)"};
    private PathfindingMap pathfindingMap;


    @FXML private JFXTextArea textDirectionsField;
//    @FXML private JFXTextField start;
    @FXML private JFXComboBox<String> end;
    @FXML private JFXComboBox<String> chosenMode;
    @FXML private VBox locateVBox;
    @FXML private JFXTextField autoStart;
    @FXML private AnchorPane directionsPane;

    private DirectionsList directionsList= new DirectionsList();

//    AutoCompleteTextField field;

    @FXML
    public void findDirections(){
        String mode = chosenMode.getSelectionModel().getSelectedItem();
        System.out.println(mode);
        TravelMode travelMode = TravelMode.DRIVING;
        switch (mode) {
            case ("Driving"):
                travelMode = TravelMode.DRIVING;
                break;
            case ("Walking"):
                travelMode = TravelMode.WALKING;
                break;
            case ("Biking"):
                travelMode = TravelMode.BICYCLING;
                break;
            case ("Public Transport"):
                travelMode = TravelMode.TRANSIT;
                break;
        }

        String chosenEnd = end.getSelectionModel().getSelectedItem();
        switch (chosenEnd){
            case ("The hospital"):
                chosenEnd = "Brigham and Women's Hospital, 221 Longwood Ave, Boston, MA 02115";
                break;
            case ("45 Francis Street Garage (Patient Only)"):
                chosenEnd = "45 Francis St, Boston, MA 0211";
                break;
            case ("60 Fenwood Road Garage (Patient Only)"):
                chosenEnd = "60 Fenwood Road, Boston, MA 0211";
                break;
            case ("75 Francis Street (Patient Valet)"):
                chosenEnd = "75 Francis Street, Boston, MA 0211";
                break;
            case ("80 Francis Street Garage (Patient & Visitor)"):
                chosenEnd = "80 Francis Street, Boston, MA 0211";
                break;
            case ("221 Longwood Avenue (Patient Valet)"):
                chosenEnd = "221 Longwood Avenue, Boston, MA 0211";
                break;
        }


        googleMaps.getDirections(autoStart.getText(), chosenEnd, travelMode, textDirectionsField, directionsList);
    }

    @FXML
    public void textChanged(AutoCompletePopup autoCompletePopup){
        System.out.println("text changed");
        googleMaps.getOptions(autoStart.getText(), autoCompletePopup);
        //TODO: make text field entries be google api autocomplete
    }

    @FXML
    public void openLink(){
        String mode = chosenMode.getSelectionModel().getSelectedItem();
        System.out.println(mode);
        TravelMode travelMode = TravelMode.DRIVING;
        switch (mode) {
            case ("Driving"):
                travelMode = TravelMode.DRIVING;
                break;
            case ("Walking"):
                travelMode = TravelMode.WALKING;
                break;
            case ("Biking"):
                travelMode = TravelMode.BICYCLING;
                break;
            case ("Public Transport"):
                travelMode = TravelMode.TRANSIT;
                break;
        }

        String chosenEnd = end.getSelectionModel().getSelectedItem();
        switch (chosenEnd){
            case ("The hospital"):
                chosenEnd = "Brigham and Women's Hospital, 221 Longwood Ave, Boston, MA 02115";
                break;
            case ("45 Francis Street Garage (Patient Only)"):
                chosenEnd = "45 Francis St, Boston, MA 0211";
                break;
            case ("60 Fenwood Road Garage (Patient Only)"):
                chosenEnd = "60 Fenwood Road, Boston, MA 0211";
                break;
            case ("75 Francis Street (Patient Valet)"):
                chosenEnd = "75 Francis Street, Boston, MA 0211";
                break;
            case ("80 Francis Street Garage (Patient & Visitor)"):
                chosenEnd = "80 Francis Street, Boston, MA 0211";
                break;
            case ("221 Longwood Avenue (Patient Valet)"):
                chosenEnd = "221 Longwood Avenue, Boston, MA 0211";
                break;
        }
        googleMaps.directionsLink(autoStart.getText(), chosenEnd, travelMode);
    }


    public void scanQR(){
        String mode = chosenMode.getSelectionModel().getSelectedItem();
        System.out.println(mode);
        TravelMode travelMode = TravelMode.DRIVING;
        switch (mode) {
            case ("Driving"):
                travelMode = TravelMode.DRIVING;
                break;
            case ("Walking"):
                travelMode = TravelMode.WALKING;
                break;
            case ("Biking"):
                travelMode = TravelMode.BICYCLING;
                break;
            case ("Public Transport"):
                travelMode = TravelMode.TRANSIT;
                break;
        }

        String chosenEnd = end.getSelectionModel().getSelectedItem();
        switch (chosenEnd){
            case ("The hospital"):
                chosenEnd = "Brigham and Women's Hospital, 221 Longwood Ave, Boston, MA 02115";
                break;
            case ("45 Francis Street Garage (Patient Only)"):
                chosenEnd = "45 Francis St, Boston, MA 0211";
                break;
            case ("60 Fenwood Road Garage (Patient Only)"):
                chosenEnd = "60 Fenwood Road, Boston, MA 0211";
                break;
            case ("75 Francis Street (Patient Valet)"):
                chosenEnd = "75 Francis Street, Boston, MA 0211";
                break;
            case ("80 Francis Street Garage (Patient & Visitor)"):
                chosenEnd = "80 Francis Street, Boston, MA 0211";
                break;
            case ("221 Longwood Avenue (Patient Valet)"):
                chosenEnd = "221 Longwood Avenue, Boston, MA 0211";
                break;
        }
        String path = "src/main/resources/edu/wpi/p/fxml/image/QRdirections.png";
//        File file = new File(path);
        File newFile = googleMaps.createQRCode(autoStart.getText(),chosenEnd,travelMode, path);
        if(newFile.exists()){
            System.out.println(newFile.getAbsolutePath());
            CenterPopup qrPopup = new CenterPopup("Scan QR Code", newFile.getAbsolutePath(),pathfindingMap.base);
//            pathfindingMap.showInfoDialog("Scan QR Code", newFile.getAbsolutePath());
        }
        else{
            System.out.println("doesnt exist!!!!");
        }
    }



        public void injectPathfindingMap(PathfindingMap pathfindingMap) {

        this.pathfindingMap = pathfindingMap;
    }


    @FXML
    public void initialize(){
        chosenMode.setItems(FXCollections.observableArrayList(availableModes));
        chosenMode.getSelectionModel().selectFirst();

        end.setItems(FXCollections.observableArrayList(availableParking));
        end.getSelectionModel().selectFirst();

        AutoCompletePopup acp = new AutoCompletePopup(autoStart);
        System.out.println("made popup");


        autoStart.setOnKeyReleased(event -> {
            System.out.println("key released");
            textChanged(acp);
        });

        directionsPane.getChildren().add(directionsList);

    }
}
