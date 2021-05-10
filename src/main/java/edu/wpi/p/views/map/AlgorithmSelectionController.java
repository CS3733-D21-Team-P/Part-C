package edu.wpi.p.views.map;

import edu.wpi.p.database.DBSettings;
import edu.wpi.p.userstate.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

public class AlgorithmSelectionController {

    DBSettings savedAlgorithm = DBSettings.getInstance();
    private static String savedSetting = "searchAlgorithm";

    @FXML private ComboBox algorithmChoiceBox;
    @FXML private AnchorPane algorithmSelectAnchorPane;

    @FXML
    private void initialize() {
        User user = User.getInstance();
        if(user.getPermissions().equals("Admin")) {

            algorithmSelectAnchorPane.setVisible(true);

            algorithmChoiceBox.setItems(FXCollections.observableArrayList(
                    "AStar", "BFS", "DFS", "Greedy", "Dijkstra")
            );

            String lastAlgorithm = savedAlgorithm.getSetting(savedSetting);
            if (lastAlgorithm != null) {
                algorithmChoiceBox.setValue(lastAlgorithm);
            } else {
                algorithmChoiceBox.setValue("AStar");
            }
        } else {
            algorithmSelectAnchorPane.setVisible(false);
        }
    }

    public void chooseSearchAlgorithm() {
        savedAlgorithm.setSetting(savedSetting, algorithmChoiceBox.getValue().toString());
    }
}
