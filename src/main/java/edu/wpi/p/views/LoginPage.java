package edu.wpi.p.views;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.AStar.Node;
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
    public void loginButtonAC(ActionEvent actionEvent) {
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



    private DBUser dbuser = new DBUser();
    private List<User> userDataList;

    private ObservableList<User> getUserData(){
        ObservableList<User> users = FXCollections.observableArrayList();
        for (User n : userDataList){
            users.add(n);
        }
        return users;
    }

    public boolean checkUsernameAc(ActionEvent actionEvent) {
        if(dbuser.checkUsername(usernameTXT.getText()) == passwordTXT.getText()){
            if(dbuser.checkIdentity(usernameTXT.getText()) == "Employee"){
                return true;//Return to different pages
            }else if(dbuser.checkIdentity(usernameTXT.getText()) == "Admin"){
                return true;//Return to different pages
            }

        }
        return false;//Print error message
    }

}
