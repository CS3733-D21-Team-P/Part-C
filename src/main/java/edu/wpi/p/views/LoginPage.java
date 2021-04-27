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


import java.io.IOException;

public class LoginPage {
    @FXML
    public JFXTextArea usernameTXT;
    @FXML
    public JFXPasswordField passwordTXT;
    @FXML
    public JFXButton loginButton;
    @FXML
    public JFXButton guestButton;

    private DBUser dbuser;

    User admin = new User("Admin", "admin", "admin", "Admin");
    User aemployee = new User("David", "David", "123456", "Employee");
    User bemployee = new User("Alex", "Alex", "123456", "Employee");
    User cemployee = new User("Andrew", "Andrew", "123456", "Employee");
    User demployee = new User("Nina", "Nina", "123456", "Employee");
    User eemployee = new User("Loren", "Loren", "123456", "Employee");
    User femployee = new User("Yoko", "Yoko", "123456", "Employee");
    User gemployee = new User("Yang", "Yang", "123456", "Employee");
    User hemployee = new User("Rohan", "Rohan", "123456", "Employee");
    User iemployee = new User("Nicolas", "Nicolas", "123456", "Employee");
    User jemployee = new User("Ian", "Ian", "123456", "Employee");


    @FXML
    private void initialize(){
        DatabaseInterface.init();
        List<String> tableNames = DatabaseInterface.getTableNames();
        if(!tableNames.contains("EDGES") || !tableNames.contains("NODES")) {
            try {
                CSVData nodeData = CSVHandler.readCSVFile("bwPnodes.csv");
                CSVData edgeData = CSVHandler.readCSVFile("bwPedges.csv");
                CSVDBConverter.tableFromCSVData(nodeData, edgeData);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (!tableNames.contains("USER")) {
            dbuser = new DBUser();
            dbuser.addUser(admin);
            dbuser.addUser(aemployee);
            dbuser.addUser(bemployee);
            dbuser.addUser(cemployee);
            dbuser.addUser(demployee);
            dbuser.addUser(eemployee);
            dbuser.addUser(femployee);
            dbuser.addUser(gemployee);
            dbuser.addUser(hemployee);
            dbuser.addUser(iemployee);
            dbuser.addUser(jemployee);
        }
        else {
            dbuser = new DBUser();
        }


    }

    public void guestButtonAC(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void loginButtonAC(ActionEvent actionEvent){

        if((dbuser.checkUsername(usernameTXT.getText())).equals(passwordTXT.getText())){
            if((dbuser.checkIdentity(usernameTXT.getText())).equals("Employee")){
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
                    App.getPrimaryStage().getScene().setRoot(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }else if((dbuser.checkIdentity(usernameTXT.getText())).equals("Admin")){
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
                    App.getPrimaryStage().getScene().setRoot(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            }else {
            loginButton.setOnAction(e -> AlertBox.display("Wrong Information", "Please enter the correct Username and Password"));
        }

        }
    }


