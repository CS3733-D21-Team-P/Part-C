package edu.wpi.p.views;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.AStar.NodeButton;
import edu.wpi.p.database.*;
import edu.wpi.p.App;
import edu.wpi.p.userstate.User;
import edu.wpi.p.views.map.NodeTableEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    public TableColumn<UserFromDB, String> NameCol;
    public TableColumn<UserFromDB, String> UsernameCol;
    public TableColumn<UserFromDB, String> PasswordCol;
    public TableColumn<UserFromDB, String> StatusCol;
    public TableColumn<UserFromDB, String> Blank;


    private final DBUser dbuser = new DBUser();
    public JFXTreeTableView accountDataTableView;
    private List<UserFromDB> userDataList;
    private List<UserFromDB> removableList = new ArrayList<UserFromDB>();

    @FXML
    private void initialize(){
        JFXTreeTableColumn<UserFromDB, String> username = new JFXTreeTableColumn<>("Username");
        username.setPrefWidth(70);
        username.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<UserFromDB, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<UserFromDB, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getUsername());
            }
        });
        JFXTreeTableColumn<UserFromDB, String> name = new JFXTreeTableColumn<>("Name");
        name.setPrefWidth(70);
        name.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<UserFromDB, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<UserFromDB, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getName());
            }
        });
        JFXTreeTableColumn<UserFromDB, String> password = new JFXTreeTableColumn<>("Password");
        password.setPrefWidth(70);
        password.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<UserFromDB, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<UserFromDB, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getPassword());
            }
        });
        JFXTreeTableColumn<UserFromDB, String> status = new JFXTreeTableColumn<>("Status");
        status.setPrefWidth(70);
        status.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<UserFromDB, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<UserFromDB, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getStatus());
            }
        });
        JFXTreeTableColumn<UserFromDB, String> parking = new JFXTreeTableColumn<>("Parking Spot");
        parking.setPrefWidth(70);
        parking.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<UserFromDB, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<UserFromDB, String> p) {
                if (p.getValue().getValue().getParkingNodeID() == null) {
                    return null;
                }
                else return new SimpleStringProperty(p.getValue().getValue().getParkingNodeID());
            }
        });

        userDataList = dbuser.getUsers();
        ObservableList<UserFromDB> users = FXCollections.observableArrayList();
        for (UserFromDB user : userDataList) {
            users.add(new UserFromDB(user.getName(), user.getUsername(), user.getPassword(), user.getStatus(), user.getParkingNodeID()));
        }

        accountDataTableView.setEditable(true);
        name.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        name.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<UserFromDB, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<UserFromDB, String> event) {
                TreeItem<UserFromDB> currentEditingUser = accountDataTableView.getTreeItem(event.getTreeTablePosition().getRow());
                currentEditingUser.getValue().setName(event.getNewValue());
                dbuser.removeUser(currentEditingUser.getValue().getUsername());
                dbuser.addUser(currentEditingUser.getValue());            }
        });
        password.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        password.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<UserFromDB, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<UserFromDB, String> event) {
                TreeItem<UserFromDB> currentEditingUser = accountDataTableView.getTreeItem(event.getTreeTablePosition().getRow());
                currentEditingUser.getValue().setPassword(event.getNewValue());
                dbuser.removeUser(currentEditingUser.getValue().getUsername());
                dbuser.addUser(currentEditingUser.getValue());            }
        });
        status.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        status.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<UserFromDB, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<UserFromDB, String> event) {
                TreeItem<UserFromDB> currentEditingUser = accountDataTableView.getTreeItem(event.getTreeTablePosition().getRow());
                currentEditingUser.getValue().setIdentity(event.getNewValue());
                dbuser.removeUser(currentEditingUser.getValue().getUsername());
                dbuser.addUser(currentEditingUser.getValue());
            }
        });

        final TreeItem<UserFromDB> root = new RecursiveTreeItem<>(users, RecursiveTreeObject::getChildren);
        accountDataTableView.getColumns().setAll(name, username, password, status, parking);
        accountDataTableView.setRoot(root);
        accountDataTableView.setShowRoot(false);

        accountDataTableView.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<TreeItem<UserFromDB>>(){
            @Override
            public void changed(ObservableValue<? extends TreeItem<UserFromDB>>
                                        observable, TreeItem<UserFromDB> oldValue, TreeItem<UserFromDB> newValue) {
                if (!(oldValue == newValue)) {
                    TreeTableView.TreeTableViewSelectionModel<UserFromDB> sm = accountDataTableView.getSelectionModel();
                    UserFromDB n = sm.getSelectedItem().getValue(); //selected node
                    saveSelectedUser(n);
                }
            }
        });


//        NameCol.setCellValueFactory(new PropertyValueFactory<UserFromDB, String>("Name"));
//        UsernameCol.setCellValueFactory(new PropertyValueFactory<UserFromDB, String>("Username"));
//        PasswordCol.setCellValueFactory(new PropertyValueFactory<UserFromDB, String>("Password"));
//        StatusCol.setCellValueFactory(new PropertyValueFactory<UserFromDB, String>("Status"));
//        Blank.setCellValueFactory(new PropertyValueFactory<UserFromDB, String>("Blank"));
//
//        //load in the edge data
//        accountDataTableView.setItems(getUserData());
//        accountDataTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//        accountDataTableView.getSelectionModel().setCellSelectionEnabled(true);
//        accountDataTableView.setEditable(true);
//        NameCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        UsernameCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        PasswordCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        StatusCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        Blank.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    private void saveSelectedUser(UserFromDB n) {
        removableList.add(n);
        for (int i =0; i < removableList.size(); i++) {
            removableList.remove(i);
        }
        removableList.add(n);
    }

    private ObservableList<UserFromDB> getUserData(){
        ObservableList<UserFromDB> users = FXCollections.observableArrayList();
        for (UserFromDB n : userDataList){
            users.add(n);
        }
        return users;
    }

    public void EditName(TableColumn.CellEditEvent<UserFromDB, String> cellEditEvent) {
//        UserFromDB user = accountDataTableView.getSelectionModel().getSelectedItem();
//        user.setName(cellEditEvent.getNewValue());
//
//        TablePosition userPos = accountDataTableView.getSelectionModel().getSelectedCells().get(0);
//        int userRow = userPos.getRow();
//        UserFromDB user1 = accountDataTableView.getItems().get(userRow);
//
//        DBUser dbUser = new DBUser();
//        dbUser.updateUser(user1);
    }
//
    public void EditUsername(TableColumn.CellEditEvent<UserFromDB, String> cellEditEvent) {
//        UserFromDB user = accountDataTableView.getSelectionModel().getSelectedItem();
//        user.setUsername(cellEditEvent.getNewValue());
//
//        TablePosition userPos = accountDataTableView.getSelectionModel().getSelectedCells().get(0);
//        int userRow = userPos.getRow();
//        UserFromDB user1 = accountDataTableView.getItems().get(userRow);
//
//        DBUser dbUser = new DBUser();
//        dbUser.updateUser(user1);
    }
//
    public void EditPassword(TableColumn.CellEditEvent<UserFromDB, String> cellEditEvent) {
//        UserFromDB user = accountDataTableView.getSelectionModel().getSelectedItem();
//        user.setPassword(cellEditEvent.getNewValue());
//
//        TablePosition userPos = accountDataTableView.getSelectionModel().getSelectedCells().get(0);
//        int userRow = userPos.getRow();
//        UserFromDB user1 = accountDataTableView.getItems().get(userRow);
//
//        DBUser dbUser = new DBUser();
//        dbUser.updateUser(user1);
    }
//
    public void EditStatus(TableColumn.CellEditEvent<UserFromDB, String> cellEditEvent) {
//        UserFromDB user = accountDataTableView.getSelectionModel().getSelectedItem();
//        user.setStatus(cellEditEvent.getNewValue());
//
//        TablePosition userPos = accountDataTableView.getSelectionModel().getSelectedCells().get(0);
//        int userRow = userPos.getRow();
//        UserFromDB user1 = accountDataTableView.getItems().get(userRow);
//
//        DBUser dbUser = new DBUser();
//        dbUser.updateUser(user1);
    }
//
    public void EditBlank(TableColumn.CellEditEvent<UserFromDB, String> cellEditEvent) {
//        UserFromDB user = accountDataTableView.getSelectionModel().getSelectedItem();
//        user.setBlank(cellEditEvent.getNewValue());
//
//        TablePosition userPos = accountDataTableView.getSelectionModel().getSelectedCells().get(0);
//        int userRow = userPos.getRow();
//        UserFromDB user1 = accountDataTableView.getItems().get(userRow);
//
//        DBUser dbUser = new DBUser();
//        dbUser.updateUser(user1);
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
        DBUser dbUser = new DBUser();
        dbUser.addUser(user);
        initialize();
    }

    public void deletePeopleAc(ActionEvent actionEvent) {
        dbuser.removeUser(removableList.get(0).getUsername());
        initialize();

//        TablePosition userpos = accountDataTableView.getSelectionModel().getSelectedCells().get(0);
//        int userRow = userpos.getRow();
//        UserFromDB user = accountDataTableView.getItems().get(userRow);
//        DBUser dbUser = new DBUser();
//        dbUser.removeUser(user.getUsername());
//        accountDataTableView.getItems().removeAll(accountDataTableView.getSelectionModel().getSelectedItem());
    }

}
