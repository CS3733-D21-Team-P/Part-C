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

public class LoginPage {
    @FXML
    private TextField usernameTXT;
    @FXML private TextField passwordTXT;


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
