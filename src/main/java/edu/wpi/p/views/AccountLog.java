package edu.wpi.p.views;

import edu.wpi.p.database.*;
import edu.wpi.p.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.IOException;
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
    public TableView<UserFromDB> accountDataTableView;
    public TableColumn<UserFromDB, String> NameCol;
    public TableColumn<UserFromDB, String> UsernameCol;
    public TableColumn<UserFromDB, String> PasswordCol;
    public TableColumn<UserFromDB, String> StatusCol;
    public TableColumn<UserFromDB, String> Blank;


    private final DBUser dbuser = new DBUser();
    private List<UserFromDB> userDataList;

    @FXML
    private void initialize() throws Exception {

        userDataList = dbuser.getUsers();

        NameCol.setCellValueFactory(new PropertyValueFactory<UserFromDB, String>("Name"));
        UsernameCol.setCellValueFactory(new PropertyValueFactory<UserFromDB, String>("Username"));
        PasswordCol.setCellValueFactory(new PropertyValueFactory<UserFromDB, String>("Password"));
        StatusCol.setCellValueFactory(new PropertyValueFactory<UserFromDB, String>("Status"));
        Blank.setCellValueFactory(new PropertyValueFactory<UserFromDB, String>("Blank"));

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

    private ObservableList<UserFromDB> getUserData(){
        ObservableList<UserFromDB> users = FXCollections.observableArrayList();
        for (UserFromDB n : userDataList){
            users.add(n);
        }
        return users;
    }

    public void EditName(TableColumn.CellEditEvent<UserFromDB, String> cellEditEvent) {
        UserFromDB user = accountDataTableView.getSelectionModel().getSelectedItem();
        user.setName(cellEditEvent.getNewValue());

        TablePosition userPos = accountDataTableView.getSelectionModel().getSelectedCells().get(0);
        int userRow = userPos.getRow();
        UserFromDB user1 = accountDataTableView.getItems().get(userRow);

        DBUser dbUser = new DBUser();
        dbUser.updateUser(user1);
    }

    public void EditUsername(TableColumn.CellEditEvent<UserFromDB, String> cellEditEvent) {
        UserFromDB user = accountDataTableView.getSelectionModel().getSelectedItem();
        user.setUsername(cellEditEvent.getNewValue());

        TablePosition userPos = accountDataTableView.getSelectionModel().getSelectedCells().get(0);
        int userRow = userPos.getRow();
        UserFromDB user1 = accountDataTableView.getItems().get(userRow);

        DBUser dbUser = new DBUser();
        dbUser.updateUser(user1);
    }

    public void EditPassword(TableColumn.CellEditEvent<UserFromDB, String> cellEditEvent) {
        UserFromDB user = accountDataTableView.getSelectionModel().getSelectedItem();
        user.setPassword(cellEditEvent.getNewValue());

        TablePosition userPos = accountDataTableView.getSelectionModel().getSelectedCells().get(0);
        int userRow = userPos.getRow();
        UserFromDB user1 = accountDataTableView.getItems().get(userRow);

        DBUser dbUser = new DBUser();
        dbUser.updateUser(user1);
    }

    public void EditStatus(TableColumn.CellEditEvent<UserFromDB, String> cellEditEvent) {
        UserFromDB user = accountDataTableView.getSelectionModel().getSelectedItem();
        user.setStatus(cellEditEvent.getNewValue());

        TablePosition userPos = accountDataTableView.getSelectionModel().getSelectedCells().get(0);
        int userRow = userPos.getRow();
        UserFromDB user1 = accountDataTableView.getItems().get(userRow);

        DBUser dbUser = new DBUser();
        dbUser.updateUser(user1);
    }

    public void EditBlank(TableColumn.CellEditEvent<UserFromDB, String> cellEditEvent) {
        UserFromDB user = accountDataTableView.getSelectionModel().getSelectedItem();
        user.setBlank(cellEditEvent.getNewValue());

        TablePosition userPos = accountDataTableView.getSelectionModel().getSelectedCells().get(0);
        int userRow = userPos.getRow();
        UserFromDB user1 = accountDataTableView.getItems().get(userRow);

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
        UserFromDB user = new UserFromDB(tfname.getText(), tfusername.getText(),tfpassword.getText(),tfstatus.getText(),tfblank.getText());
        accountDataTableView.getItems().add(user);

        DBUser dbUser = new DBUser();
        dbUser.addUser(user);
    }

    public void deletePeopleAc(ActionEvent actionEvent) {
        TablePosition userpos = accountDataTableView.getSelectionModel().getSelectedCells().get(0);
        int userRow = userpos.getRow();
        UserFromDB user = accountDataTableView.getItems().get(userRow);
        DBUser dbUser = new DBUser();
        dbUser.removeUser(user.getUsername());
        accountDataTableView.getItems().removeAll(accountDataTableView.getSelectionModel().getSelectedItem());
    }

}
