package edu.wpi.p.views;

import AStar.Node;
import AStar.NodeButton;
import edu.wpi.p.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class PathfindingMap {
    enum State {
        ENTERSTART,
        ENTEREND,
        NONE
    }
    private State mapState = State.ENTERSTART;

    private boolean enteringStart = false;
    @FXML
    public TextField start;
    @FXML
    public TextField end;
    @FXML
    public AnchorPane btnPane;

    public void enterStart(MouseEvent e){
        mapState = State.ENTERSTART;
        System.out.println("enter start!");
    }

    public void enterEnd(MouseEvent e){
        mapState = State.ENTEREND;
        System.out.println("enter end!");
    }

    public void findPath(ActionEvent actionEvent){
        System.out.println("find path!");
    }

    public void addNode(ActionEvent actionEvent){
        NodeButton button = (NodeButton) actionEvent.getSource();
        System.out.println(button.getText());
        if(mapState.equals(State.ENTERSTART)){
            start.setText(button.getNode().getName());
        }
        else if(mapState.equals(State.ENTEREND)){
            end.setText(button.getNode().getName());
        }
    }

    @FXML
    public void initialize()  {
        System.out.println("initialize");
        Node node1 = new Node("test", 300, 350);
        Node node2 = new Node("test2", 300, 200);
        NodeButton nb1 = new NodeButton(node1);
        nb1.setOnAction(event -> {addNode(event);});
        NodeButton nb2 = new NodeButton(node2);
        nb2.setOnAction(event -> {addNode(event);});
        btnPane.getChildren().add(nb1);
        btnPane.getChildren().add(nb2);
    }
}
