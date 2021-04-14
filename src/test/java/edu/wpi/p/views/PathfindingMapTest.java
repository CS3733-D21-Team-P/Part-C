package edu.wpi.p.views;

import AStar.Node;
import AStar.NodeButton;
import edu.wpi.p.App;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class PathfindingMapTest extends ApplicationTest {
    private static final String name1 = "node1";

    @Override
    public void start(Stage primaryStage) throws IOException {
        //App.setPrimaryStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/PathfindingMap.fxml"));
        Scene scene = new Scene(root, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Test
    void addNodeButton() {

//        PathfindingMap pMap = new PathfindingMap();
//        Node testNode = new Node("test", name1, 100, 100);
        //pMap.btnPane.getChildren().size();
        //pMap.addNodeButton(testNode);
//        NodeButton testButton = pMap.addNodeButton(testNode);
//        System.out.println(testButton.getName());
//        System.out.println(testNode.getName());
//        assertEquals(testButton.getName(), testNode.getName());
    }

//    @Test
//    void addEdgeLine() {
//    }
}