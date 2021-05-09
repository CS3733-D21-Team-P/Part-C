package edu.wpi.p.views.map;

import edu.wpi.p.database.DBSettings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;

public class AlgorithmSelectionController {

    DBSettings savedAlgorithm = new DBSettings();
    private static String savedSetting = "searchAlgorithm";

    @FXML public ComboBox algorithmChoiceBox;

    @FXML
    private void initialize() {
        algorithmChoiceBox.setItems(FXCollections.observableArrayList(
                "AStar", "BFS", "DFS", "Greedy", "Dijkstra")
        );

        String lastAlgorithm = savedAlgorithm.getSetting(savedSetting);
        if(lastAlgorithm != null) {
            algorithmChoiceBox.setValue(lastAlgorithm);
        } else {
            algorithmChoiceBox.setValue("AStar");
        }
    }

    public void chooseSearchAlgorithm() {
        savedAlgorithm.setSetting(savedSetting, algorithmChoiceBox.getValue().toString());
    }
}
