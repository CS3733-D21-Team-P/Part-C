package edu.wpi.p.views;

import edu.wpi.p.App;
import edu.wpi.p.database.DatabaseInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.SQLException;

public class AdminConfig {
    public Button homebutton;
    public Button ServerButton;
    public Label typelabel;

    public void homebuttonAC(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void StartServer(ActionEvent actionEvent) throws SQLException {
        DatabaseInterface.closeEmbedded();
        DatabaseInterface.conn.close();
        DatabaseInterface.init(true);
        initialize();
    }

    public void initialize() {
        boolean dbtype = DatabaseInterface.DBtype;
        if(dbtype){
            typelabel.setText("Server");
            ServerButton.setVisible(false);
        }else {
            typelabel.setText("Embedded");
        }
    }
}
