package edu.wpi.p.views.servicerequests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.p.views.Toolbar;

public class EntryRequest extends Toolbar {
    public JFXTextField name;
    public JFXTimePicker time;
    public JFXTextArea reason;
    public JFXButton surveyBtn;
    public JFXButton submit;
    public JFXButton back;
    public boolean approved;

    public void submitForm(){
        approved = false;
        final String n = name.getText();
        final String r = reason.getText();
    }
}
