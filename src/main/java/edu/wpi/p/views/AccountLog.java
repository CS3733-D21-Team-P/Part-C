package edu.wpi.p.views;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.csv.CSVData;
import edu.wpi.p.csv.CSVHandler;
import edu.wpi.p.database.*;
import edu.wpi.p.App;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.util.Callback;
import com.jfoenix.controls.JFXTreeTableView;
import org.sqlite.core.DB;

import java.sql.SQLException;
import java.util.List;

public class AccountLog {
    public Button addPeopleBtn;
    public Button deletePeopleBtn;
    public TableView nodeDataTableView;
    public Button homeButton;
    public TextField tfname;
    public TextField tfusername;
    public TextField tfstatus;
    public TextField tfpassword;
    public TextField tfblank;

    public void EditName(TableColumn.CellEditEvent cellEditEvent) {
    }

    public void EditUsername(TableColumn.CellEditEvent cellEditEvent) {
    }

    public void EditPassword(TableColumn.CellEditEvent cellEditEvent) {
    }

    public void EditStatus(TableColumn.CellEditEvent cellEditEvent) {
    }

    public void EditBlank(TableColumn.CellEditEvent cellEditEvent) {
    }

    public void homeButtonAc(ActionEvent actionEvent) {
    }

    public void addPeopleAc(ActionEvent actionEvent) {
    }

    public void deletePeopleAc(ActionEvent actionEvent) {
    }
}
