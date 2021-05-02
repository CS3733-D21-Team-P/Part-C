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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.util.converter.IntegerStringConverter;
import org.sqlite.core.DB;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AccountLog {
    public Button addPeopleBtn;
    public Button deletePeopleBtn;
    public Button homeButton;
    public TextField tfname;
    public TextField tfusername;
    public TextField tfstatus;
    public TextField tfpassword;
    public TextField tfblank;
    public TableView<User> accountDataTableView;
    public TableColumn<User, String> NameCol;
    public TableColumn<User, String> UsernameCol;
    public TableColumn<User, String> PasswordCol;
    public TableColumn<User, String> StatusCol;
    public TableColumn<User, String> Blank;


    private final DBUser dbuser = new DBUser();
    private List<User> userDataList;

    @FXML
    private void initialize() throws Exception {

        userDataList = dbuser.getUsers();

        NameCol.setCellValueFactory(new PropertyValueFactory<User, String>("Name"));
        UsernameCol.setCellValueFactory(new PropertyValueFactory<User, String>("Username"));
        PasswordCol.setCellValueFactory(new PropertyValueFactory<User, String>("Password"));
        StatusCol.setCellValueFactory(new PropertyValueFactory<User, String>("Status"));
        Blank.setCellValueFactory(new PropertyValueFactory<User, String>("Blank"));

        //load in the edge data
        accountDataTableView.setItems(getUserData());
        accountDataTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        accountDataTableView.getSelectionModel().setCellSelectionEnabled(true);
        accountDataTableView.setEditable(true);
        NameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        UsernameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        PasswordCol.setCellFactory(TextFieldTableCell.forTableColumn());
        StatusCol.setCellFactory(TextFieldTableCell.forTableColumn());
        Blank.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    private ObservableList<User> getUserData(){
        ObservableList<User> users = FXCollections.observableArrayList();
        for (User n : userDataList){
            users.add(n);
        }
        return users;
    }

    public void EditName(TableColumn.CellEditEvent<User, String> cellEditEvent) {
        User user = accountDataTableView.getSelectionModel().getSelectedItem();
        user.setName(cellEditEvent.getNewValue());

        TablePosition userPos = accountDataTableView.getSelectionModel().getSelectedCells().get(0);
        int userRow = userPos.getRow();
        User user1 = accountDataTableView.getItems().get(userRow);

        DBUser dbUser = new DBUser();
        dbUser.updateUser(user1);
    }

    public void EditUsername(TableColumn.CellEditEvent<User, String> cellEditEvent) {
        User user = accountDataTableView.getSelectionModel().getSelectedItem();
        user.setName(cellEditEvent.getNewValue());

        TablePosition userPos = accountDataTableView.getSelectionModel().getSelectedCells().get(0);
        int userRow = userPos.getRow();
        User user1 = accountDataTableView.getItems().get(userRow);

        DBUser dbUser = new DBUser();
        dbUser.updateUser(user1);
    }

    public void EditPassword(TableColumn.CellEditEvent<User, String> cellEditEvent) {
        User user = accountDataTableView.getSelectionModel().getSelectedItem();
        user.setName(cellEditEvent.getNewValue());

        TablePosition userPos = accountDataTableView.getSelectionModel().getSelectedCells().get(0);
        int userRow = userPos.getRow();
        User user1 = accountDataTableView.getItems().get(userRow);

        DBUser dbUser = new DBUser();
        dbUser.updateUser(user1);
    }

    public void EditStatus(TableColumn.CellEditEvent<User, String> cellEditEvent) {
        User user = accountDataTableView.getSelectionModel().getSelectedItem();
        user.setName(cellEditEvent.getNewValue());

        TablePosition userPos = accountDataTableView.getSelectionModel().getSelectedCells().get(0);
        int userRow = userPos.getRow();
        User user1 = accountDataTableView.getItems().get(userRow);

        DBUser dbUser = new DBUser();
        dbUser.updateUser(user1);
    }

    public void EditBlank(TableColumn.CellEditEvent<User, String> cellEditEvent) {
        User user = accountDataTableView.getSelectionModel().getSelectedItem();
        user.setName(cellEditEvent.getNewValue());

        TablePosition userPos = accountDataTableView.getSelectionModel().getSelectedCells().get(0);
        int userRow = userPos.getRow();
        User user1 = accountDataTableView.getItems().get(userRow);

        DBUser dbUser = new DBUser();
        dbUser.updateUser(user1);
    }

    public void homeButtonAc(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void addPeopleAc(ActionEvent actionEvent) {
        User user = new User(tfname.getText(), tfusername.getText(),tfpassword.getText(),tfstatus.getText(),tfblank.getText());
        accountDataTableView.getItems().add(user);

        DBUser dbUser = new DBUser();
        dbUser.addUser(user);
    }

    public void deletePeopleAc(ActionEvent actionEvent) {
        TablePosition userpos = accountDataTableView.getSelectionModel().getSelectedCells().get(0);
        int userRow = userpos.getRow();
        User user = accountDataTableView.getItems().get(userRow);
        DBUser dbUser = new DBUser();
        dbUser.removeUser(user.getUsername());
        accountDataTableView.getItems().removeAll(accountDataTableView.getSelectionModel().getSelectedItem());
    }

}
