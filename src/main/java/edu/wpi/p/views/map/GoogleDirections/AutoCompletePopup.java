package edu.wpi.p.views.map.GoogleDirections;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXAutoCompleteEvent;
import javafx.collections.ObservableList;

import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.List;

public class AutoCompletePopup extends JFXAutoCompletePopup<String> {
    public AutoCompletePopup(JFXTextField textField){
//        getSuggestions().addAll("option1", "option2", "...");
//        getSuggestions().addAll(options);
        this.setSelectionHandler(event -> {
            JFXAutoCompleteEvent se = (JFXAutoCompleteEvent) event ;
            textField.setText(se.getObject().toString());

            // you can do other actions here when text completed
        });

        // filtering options
        textField.textProperty().addListener(observable -> {
            this.filter( string -> string.toLowerCase().contains(textField.getText().toLowerCase()));
            if (getFilteredSuggestions().isEmpty() || textField.getText().isEmpty()) {
                hide();
                // if you remove textField.getText.isEmpty() when text field is empty it suggests all options
                // so you can choose
            } else {
                show(textField);
            }
        });
        
        
    }
}
