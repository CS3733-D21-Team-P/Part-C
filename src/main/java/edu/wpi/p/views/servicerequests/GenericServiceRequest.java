package edu.wpi.p.views.servicerequests;

import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.rowdata.ServiceRequest;
import edu.wpi.p.views.Toolbar;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.event.ActionEvent;

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
            sR.addDetail(key, this.values.get(key).getValue().toString());
        }
        DBServiceRequest dbServiceRequest = new DBServiceRequest();
        dbServiceRequest.addServiceRequest(sR);
    }

}
