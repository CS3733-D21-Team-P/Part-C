package edu.wpi.p.views.map.GoogleDirections;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXAutoCompleteEvent;


public class AutoCompletePopup extends JFXAutoCompletePopup<String> {
    private JFXTextField textField;
    private boolean isUpdatable = true;

    public AutoCompletePopup(JFXTextField textField){
        this.textField=textField;

        this.setSelectionHandler(event -> {
            JFXAutoCompleteEvent se = (JFXAutoCompleteEvent) event ;
            textField.setText(se.getObject().toString());

            // you can do other actions here when text completed
        });

        // filtering options
        textField.textProperty().addListener(observable -> {
            updateFilter();
        });

    }

    public void updateFilter(){
        this.filter( string -> string.toLowerCase().contains(textField.getText().toLowerCase()));
        if (getFilteredSuggestions().isEmpty() || textField.getText().isEmpty()) {
            hide();
            // if you remove textField.getText.isEmpty() when text field is empty it suggests all options
            // so you can choose
        } else {
            show(textField);
        }
    }

    public void clearSuggestions(){
        this.getSuggestions().clear();
    }
}
