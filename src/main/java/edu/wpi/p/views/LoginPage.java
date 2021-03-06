package edu.wpi.p.views;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sun.javafx.scene.control.behavior.TextAreaBehavior;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.csv.CSVData;
import edu.wpi.p.csv.CSVHandler;
import edu.wpi.p.database.*;
import edu.wpi.p.App;
import edu.wpi.p.userstate.LoginException;
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
import edu.wpi.p.userstate.User;


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

    UserFromDB admin = new UserFromDB("Admin", "admin", "admin", "Admin");
    UserFromDB aemployee = new UserFromDB("David", "David", "123456", "Admin");
    UserFromDB bemployee = new UserFromDB("Alex", "Alex", "123456", "Admin");
    UserFromDB cemployee = new UserFromDB("Andrew", "Andrew", "123456", "Admin");
    UserFromDB demployee = new UserFromDB("Nina", "Nina", "123456", "Admin");
    UserFromDB eemployee = new UserFromDB("Loren", "Loren", "123456", "Admin");
    UserFromDB femployee = new UserFromDB("Yoko", "Yoko", "123456", "Admin");
    UserFromDB gemployee = new UserFromDB("Yang", "Yang", "123456", "Admin");
    UserFromDB hemployee = new UserFromDB("Rohan", "Rohan", "123456", "Admin");
    UserFromDB iemployee = new UserFromDB("Nicolas", "Nicolas", "123456", "Admin");
    UserFromDB jemployee = new UserFromDB("Ian", "Ian", "123456", "Admin");
    UserFromDB kemployee = new UserFromDB("Staff", "staff", "staff", "Employee");
    UserFromDB tempUser = new UserFromDB("tempUser", "tempUser", "tempUser", "tempUser");
    UserFromDB pempUser = new UserFromDB("Patient", "patient", "patient", "Patient");


    @FXML
    private void initialize() {
        DatabaseInterface.init(false);
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
            dbuser = DBUser.getInstance();
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
            dbuser.addUser(kemployee);
            dbuser.addUser(tempUser);
            dbuser.addUser(pempUser);
        } else {
            dbuser = DBUser.getInstance();
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
        User.getInstance().beGuest();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void loginButtonAC(ActionEvent actionEvent) {
        try {
            User.getInstance().login(usernameTXT.getText(), passwordTXT.getText());
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (LoginException loginException) {
            AlertBox.display(loginException.getTitle(), loginException.getMessage());
        }
    }
}


