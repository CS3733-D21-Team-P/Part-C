package edu.wpi.p.views.map;

import com.google.maps.model.TravelMode;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

public class GoogleTabController {
    GoogleMaps googleMaps = new GoogleMaps();
    final String[] availableModes = new String[]{"Driving", "Walking","Biking"};


    @FXML private JFXTextArea textDirectionsField;
    @FXML private JFXTextField start;
    @FXML private JFXTextField end;
    @FXML private JFXComboBox<String> chosenMode;


    @FXML
    public void findDirections(){
        String mode = chosenMode.getSelectionModel().getSelectedItem();
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
        googleMaps.getDirections(start.getText(), end.getText(), travelMode, textDirectionsField);
//        googleMaps.testDirections(textDirectionsField);
    }

    @FXML
    public void textChanged(){

    }

    @FXML
    public void initialize(){
        chosenMode.setItems(FXCollections.observableArrayList(availableModes));
        chosenMode.getSelectionModel().selectFirst();

    }
}
