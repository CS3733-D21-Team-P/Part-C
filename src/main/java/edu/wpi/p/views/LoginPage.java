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
        dbuser = new DBUser();
    }

    public void loginButtonAc(ActionEvent actionEvent) {
        final String user = usernameTXT.getText();
        final String pass = passwordTXT.getText();
        if(user.equals("admin")&& pass.equals("pass")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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



    private DBUser dbuser;
    User admin = new User("Yang", "admin", "admin", "Admin");
    User aemployee = new User("David", "David", "123456", "Employee");


    public void loginButtonAC(ActionEvent actionEvent){
        dbuser.addUser(admin);
        dbuser.addUser(aemployee);
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
            }

        }
    }


