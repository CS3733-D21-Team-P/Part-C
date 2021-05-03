package edu.wpi.p.views.map;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.AStar.NodeButton;
import edu.wpi.p.App;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.DBTable;
import edu.wpi.p.database.DBUser;
import edu.wpi.p.database.UserFromDB;
import edu.wpi.p.database.rowdata.ServiceRequest;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import com.jfoenix.controls.JFXTreeTableView;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class ParkingSaving {
    JFXButton saveParking;
    private final DBUser dbuser = new DBUser();

    public void saveParkingAc(NodeButton nb) {
        UserFromDB user = new UserFromDB("tempUser", "tempUser");
        Node node = nb.getNode();
        user.setParkingNodeID(node.getId());
        dbuser.addUser(user);
        nb.toggleIsSelected(true);
        nb.setButtonStyle();
    }
}
