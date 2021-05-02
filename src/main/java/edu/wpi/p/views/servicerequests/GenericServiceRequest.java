package edu.wpi.p.views.servicerequests;

import com.jfoenix.controls.JFXRadioButton;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.rowdata.ServiceRequest;
import edu.wpi.p.views.Toolbar;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.ToggleGroup;

import java.util.HashMap;
import java.util.UUID;

public class GenericServiceRequest extends Toolbar {
    protected String name;
    protected ReadOnlyStringProperty locationProperty;
    protected HashMap<String, ReadOnlyProperty<?>> values = new HashMap<>();

    public void cancelPressed(ActionEvent actionEvent) {
        return;
    }

    public void submitPressed(ActionEvent actionEvent) {
        ServiceRequest sR = new ServiceRequest(this.name, locationProperty.get(), UUID.randomUUID().toString(), null);
        for (String key : this.values.keySet()) {
            if (this.values.get(key).getValue() != null) {
                sR.addDetail(key, this.values.get(key).getValue().toString());
            }
        }
        DBServiceRequest dbServiceRequest = new DBServiceRequest();
        dbServiceRequest.addServiceRequest(sR);
    }

    protected StringProperty createJFXRadioButtonStringProperty(JFXRadioButton ... buttons) {
        ToggleGroup toggleGroup = new ToggleGroup();
        for (JFXRadioButton b : buttons) {
            b.setToggleGroup(toggleGroup);
        }
        StringProperty s = new SimpleStringProperty("");
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            JFXRadioButton b = (JFXRadioButton) newValue;
            s.setValue(b.getText());
        });
        return s;
    }
}
