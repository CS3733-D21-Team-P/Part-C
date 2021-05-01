package edu.wpi.p.views;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sun.javafx.scene.control.behavior.TextAreaBehavior;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import com.jfoenix.controls.JFXTreeTableView;
import org.sqlite.core.DB;

import java.awt.*;
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
    private void initialize() {
        DatabaseInterface.init();
        List<String> tableNames = DatabaseInterface.getTableNames();
        if (!tableNames.contains("EDGES") || !tableNames.contains("NODES")) {
            try {
                CSVData nodeData = CSVHandler.readCSVString("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName");
                CSVData edgeData = CSVHandler.readCSVString("edgeID,startNode,endNode");
                CSVDBConverter.tableFromCSVData(nodeData, edgeData);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (!tableNames.contains("USERS")) {
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
        } else {
            dbuser = new DBUser();
        }

        usernameTXT.requestFocus();
        usernameTXT.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();

                if (code == KeyCode.TAB && !event.isShiftDown() && !event.isControlDown()) {
                    event.consume();
                    passwordTXT.requestFocus();
                }

            }
        });
        passwordTXT.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();

                if (code == KeyCode.ENTER) {
                    System.out.println("enter pressed");
                    event.consume();
                    loginButtonAC(null);
                }
            }
        });

    }

    public void guestButtonAC(ActionEvent actionEvent) {
        HomePage.Isguest = true;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void loginButtonAC(ActionEvent actionEvent) {
        if(passwordTXT.getText().equals("")){
            AlertBox.display("Wrong Information", "Please enter the Password");
            return;
        }

        if ((dbuser.checkUsername(usernameTXT.getText())).equals(passwordTXT.getText())) {
            if ((dbuser.checkIdentity(usernameTXT.getText())).equals("Employee")) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
                    App.getPrimaryStage().getScene().setRoot(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else if ((dbuser.checkIdentity(usernameTXT.getText())).equals("Admin")) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
                    App.getPrimaryStage().getScene().setRoot(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }else {
            AlertBox.display("Wrong Information", "Please enter the correct Username and Password");
        }
    }
}


