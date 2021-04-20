package edu.wpi.p.views;

import AStar.EdgeLine;
import AStar.Node;
import AStar.NodeButton;
import AStar.NodeGraph;
import edu.wpi.p.App;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapController {
    NodeGraph graph = new NodeGraph();
    List<NodeButton> buttons = new ArrayList<>();

    private double maxWidth = 15;
    private double maxHeight = 15;

    private String currFloorVal;

    @FXML public AnchorPane btnPane;
    @FXML public AnchorPane linePane;
    @FXML public ImageView imageView;
    @FXML private ChoiceBox<String> floorChoiceBox;
    @FXML private Button pathHomeBtn;
    private ObservableList<javafx.scene.Node> btnPaneSetup;
    private ObservableList<javafx.scene.Node> linePaneSetup;


    @FXML private Image mapImage;

    /**
     * creates a button associated  with a node
     * adds a line to neighbour nodes
     * @param node
     * @return created NodeButton
     */
    public NodeButton addNodeButton(Node node){
        NodeButton nb = new NodeButton(node); //create button

        //add icons to certain types of nodes
        String nameOfFile ="";
        switch (node.getType()) {
            case "BATH":
            case "REST":
                nameOfFile = "/image/icons/restroom.jpg";
                break;
            case "RETL":
                nameOfFile = "/image/icons/retail.png";
                break;
            case "ELEV":
                nameOfFile = "/image/icons/elevator.png";
                break;
            case "STAI":
                nameOfFile = "/image/icons/stairs.png";
                break;
            case "EXIT":
                nameOfFile = "/image/icons/exit.jpg";
                break;
        }
        if (!nameOfFile.isEmpty()) {
            Image buttonIcon = new Image(getClass().getResourceAsStream(nameOfFile));
            ImageView iconImage = new ImageView(buttonIcon);
            iconImage.setFitHeight(maxHeight);
            iconImage.setFitWidth(maxWidth);
            nb.setGraphic(iconImage);
        }
        //set on click method
//        nb.setOnAction(event -> {
//            addNodeToSearch(event);});
        if (node.getFloor().equals(currFloorVal)) {
          btnPane.getChildren().add(nb); //add to page
          List<Node> children = node.getNeighbours();
          for(Node n: children){
            addEdgeLine(node, n);
          }
        }
        buttons.add(nb);
        return nb;
    }

    /**
     * creates a line between two nodes
     * @param node1
     * @param node2
     * @return created EdgeLine
     */
    public EdgeLine addEdgeLine(Node node1, Node node2){
        EdgeLine el = new EdgeLine(node1, node2); //create line
        linePane.getChildren().add(el); //add line to screen
        return el;
    }

    public void homeButtonAc(ActionEvent actionEvent){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void changeFloors(){
        final String[] availableFloors = new String[]{"Ground", "L1","L2","1","2","3"};
        floorChoiceBox.setItems(FXCollections.observableArrayList(availableFloors));
        floorChoiceBox.getSelectionModel().select(1);
        currFloorVal = floorChoiceBox.getSelectionModel().getSelectedItem();
        floorChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number oldValue, Number newValue) {
                currFloorVal = availableFloors[newValue.intValue()];
                btnPane.getChildren().clear();
                linePane.getChildren().clear();
                btnPane.getChildren().add(pathHomeBtn);
                //buttons.clear();
                for (Node n: graph.getGraph()){
                    addNodeButton(n);
                }
                switch (currFloorVal) {
                    case "Ground":
                        mapImage = new Image(getClass().getResourceAsStream("/edu/wpi/p/fxml/Maps/00_thegroundfloor.png"));
                        break;
                    case "L1":
                        mapImage = new Image(getClass().getResourceAsStream("/edu/wpi/p/fxml/Maps/00_thelowerlevel1.png"));
                        break;
                    case "L2":
                        mapImage = new Image(getClass().getResourceAsStream("/edu/wpi/p/fxml/Maps/00_thelowerlevel2.png"));
                        break;
                    case "1":
                        mapImage = new Image(getClass().getResourceAsStream("/edu/wpi/p/fxml/Maps/01_thefirstfloor.png"));
                        break;
                    case "2":
                        mapImage = new Image(getClass().getResourceAsStream("/edu/wpi/p/fxml/Maps/02_thesecondfloor.png"));
                        break;
                    case "3":
                        mapImage = new Image(getClass().getResourceAsStream("/edu/wpi/p/fxml/Maps/03_thethirdfloor.png"));
                        break;
                }
                imageView.setImage(mapImage);
            }
        });
    }

    @FXML
    /**
     * run when page starts
     * adds buttons and edge lines to map
     */
    public void initialize(ImageView imageView)  {

        graph.genGraph(false);

        System.out.println("INIT SUPER!!!!");
        //TODO fix aspect ratio & offset
        double winWidth = imageView.getFitWidth();
        double imageWidth = imageView.getImage().getWidth();
        double scaleX = winWidth / imageWidth;

        double winHeight = imageView.getFitHeight();
        double imageHeight = imageView.getImage().getHeight();
        double scaleY = winHeight / imageHeight;
        changeFloors();

        graph.scaleGraph(scaleX, scaleY);

        for (Node n: graph.getGraph()){
            addNodeButton(n);
        }

    }

}
