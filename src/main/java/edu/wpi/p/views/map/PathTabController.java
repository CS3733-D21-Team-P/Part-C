package edu.wpi.p.views.map;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.AStar.*;
import edu.wpi.p.views.map.Filter.Criteria;
import edu.wpi.p.views.map.Filter.CriteriaNoHallways;
import edu.wpi.p.views.map.GoogleDirections.AutoCompletePopup;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class PathTabController {

    @FXML private JFXTreeTableView textDirectionsTable;
    private JFXTreeTableColumn<DirectionTableEntry, ImageView> directionImageView;
    private JFXTreeTableColumn<DirectionTableEntry, String> directionText;

    private PathfindingMap pathfindingMap;

    enum State {
        ENTERSTART,
        ENTEREND,
        NONE
    }
    private State mapState = State.ENTERSTART;

    private AStar search = new AStar();
    private Node startNode;
    private Node endNode;
    private List<EdgeLine> pathLine= new ArrayList<>();

    @FXML public JFXTextField start;
    @FXML public JFXTextField end;
    @FXML public Label instructions;


    private boolean enteringStart = false;

    public void injectPathfindingMap(PathfindingMap pathfindingMap){

        this.pathfindingMap = pathfindingMap;

        //add autocomplete to start and end text fields
        AutoCompletePopup acpStart = new AutoCompletePopup(start);
        AutoCompletePopup acpEnd = new AutoCompletePopup(end);

        Criteria noHalls = new CriteriaNoHallways();
        //filter out hallways from nodes
        List<Node> filteredNodes = noHalls.meetCriteria(pathfindingMap.graph.getGraph());

        //get names of nodes
        List<String> nodeNames = new ArrayList<>();
        for (Node n: filteredNodes){nodeNames.add(n.getName());}

        //add names to list of possible autocomplete suggestions
        acpStart.getSuggestions().addAll(nodeNames);
        acpEnd.getSuggestions().addAll(nodeNames);

        //setup DirectionsTable
        directionImageView = new JFXTreeTableColumn<>("icon");
        directionImageView.setPrefWidth(50);
        directionImageView.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DirectionTableEntry, ImageView>, ObservableValue<ImageView>>() {
            public ObservableValue<ImageView> call(TreeTableColumn.CellDataFeatures<DirectionTableEntry, ImageView> p) {
                return new SimpleObjectProperty(p.getValue().getValue().getImageVew());
            }
        });

        directionText = new JFXTreeTableColumn<>("instruction");
        directionText.setPrefWidth(200);
        directionText.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DirectionTableEntry, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DirectionTableEntry, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getInstruction());
            }
        });
    }

    /**
     * run when user clicks into start text field and changes state
     * @param e
     */
    public void enterStart(MouseEvent e){
        mapState = PathTabController.State.ENTERSTART;
        System.out.println("enter start!");

    }


    public void textChanged(){
        System.out.println("text changed");
        if(start.getText()==null || start.getText().equals("")){
            instructions.setText("click a point to enter a location");
        }
        else if(end.getText()==null || end.getText().equals("")){
            instructions.setText("enter and end location");
        }
        else if(end.getText().equals(start.getText())){
            instructions.setText("choose two different locations");
        }
        else{
            instructions.setText("press search to find a path");
        }
    }

    /**
     * run when user clicks into end textfield and changes state
     * @param e
     */
    public void enterEnd(MouseEvent e){
        mapState = PathTabController.State.ENTEREND;
        System.out.println("enter end!");
    }

    /**
     * Finds a path between the two nodes entered and makes it red
     * @param actionEvent
     */
    public void findPath(ActionEvent actionEvent){
        if (startNode==null || !start.getText().equals(startNode.getName())){
            System.out.println("set start");
            String startText = start.getText();
            startNode = pathfindingMap.graph.getNodeByName(startText);
        }
        if (endNode ==null || !end.getText().equals(endNode.getName())){
            System.out.println("set end");
            String endText = end.getText();
            endNode = pathfindingMap.graph.getNodeByName(endText);
        }

        if(startNode!=null && endNode!=null) {
            //find path
            List<Node> path = new ArrayList<>();
            path = search.findShortestPath(startNode, endNode);

            //Path To Text
            PathToText textPath = new PathToText();
            textPath.makeDirections(path);
            updateDirectionsTable(textPath.getTableDirections());

            //print path
            System.out.println("Path: ");
            for (Node n : path) {
                System.out.print(n.getName() + " ");
            }

            for(EdgeLine el: pathLine){
                pathfindingMap.btnPane.getChildren().remove(el);
            }
            pathLine.clear();


            //Make path red
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i).getName() + " ");
                if(i>0) {
                    EdgeLine line = pathfindingMap.addEdgeLine(path.get(i), path.get(i-1));
                    line.setStyle("-fx-stroke: red; -fx-stroke-width: 5px;");
                    pathLine.add(line);
                }
            }

            pathfindingMap.graph.resetNodeGraph();

        }
        else{
            System.out.println("please enter a start AND and end location");
        }


    }

    private void updateDirectionsTable(List<DirectionTableEntry> directions) {

        //TODO word wrap

        //Add instructions to DirectionsTable
        ObservableList<DirectionTableEntry> tableDirections = FXCollections.observableArrayList();
        for (DirectionTableEntry entry : directions) {
            tableDirections.add(entry);
        }

        final TreeItem<DirectionTableEntry> root = new RecursiveTreeItem<>(tableDirections, RecursiveTreeObject::getChildren);
        textDirectionsTable.getColumns().setAll(directionImageView, directionText);
        textDirectionsTable.setRoot(root);
        textDirectionsTable.setShowRoot(false);
    }


    /**
     * adds text to either start or end text field based on the current state of where the mouse has clicked
     * Run when a node button is clicked
     * @param actionEvent
     */
    public void addNodeToSearch(ActionEvent actionEvent){
        //get button that was clicked on
        NodeButton button = (NodeButton) actionEvent.getSource();


        if(mapState.equals(State.ENTERSTART)){
//            instructions.setText("enter and end node");
            startNode= button.getNode();
            start.setText(button.getName()); //set text field text to be node name
            System.out.println("start: "+ button.getName());
            mapState = State.ENTEREND;
        }
        else if(mapState.equals(State.ENTEREND)){
//            instructions.setText("press search to find a path");
            endNode = button.getNode();
            end.setText(button.getName()); //set text field text to be node name
            System.out.println("end: "+ button.getName());
        }

        String text1= start.getText();
        System.out.println(text1);
        String text2= end.getText();
        System.out.println(text2);

        if(start.getText()==null || start.getText().equals("")){
            instructions.setText("click a point to ender a location");
        }
        else if(end.getText()==null || end.getText().equals("")){
            instructions.setText("enter and end node");
        }
        else{
            instructions.setText("press search to find a path");
        }
    }
}
