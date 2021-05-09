package edu.wpi.p.views;

import edu.wpi.p.App;
import edu.wpi.p.database.DatabaseInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.io.IOException;
import java.sql.SQLException;

public class AdminConfig {
    public CheckBox check;
    public Button homebutton;

    public void Checked(ActionEvent actionEvent) throws SQLException {
        check.setSelected(true);
        DatabaseInterface.DBtype = true;
        //DatabaseInterface.closeembedded();
        DatabaseInterface.conn.close();
        DatabaseInterface.init();
    }

    public void homebuttonAC(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
