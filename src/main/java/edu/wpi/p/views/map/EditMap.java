package edu.wpi.p.views.map;

import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.p.AStar.EdgeLine;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.AStar.NodeButton;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.p.App;
import edu.wpi.p.csv.CSVData;
import edu.wpi.p.csv.CSVHandler;
import edu.wpi.p.database.CSVDBConverter;
import edu.wpi.p.database.DBTable;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EditMap extends MapController {

    private DBTable dbTable = new DBTable();
    private int btnIncrement = 1;

    @FXML private EditTabController editTabController;

    private String currFloorVal;
    @FXML private Image mapImage;

    @FXML public VBox rClickPopup;
    @FXML public VBox rClickPopup1;
    @FXML private AnchorPane deleteConfirmation;
    @FXML private AnchorPane deleteConfirmation1;
    @FXML public AnchorPane btnPane;
    @FXML private Text deleteConfirmText;
    @FXML private Text confirmDeleteNode;
    @FXML private JFXButton yesButton;
    @FXML private JFXButton noButton;
    @FXML private Text deleteConfirmText1;
    @FXML private Text confirmDeleteNode1;
    @FXML private Text confirmDeleteNode2;
    @FXML private JFXButton yesButton1;
    @FXML private JFXButton noButton1;
    @FXML private Text nodeName;
    @FXML private Text nodeName1;
    @FXML private Text nodeName2;
    @FXML private JFXButton closeButton;
    @FXML private JFXButton closeButton1;

    /**
     * creates a button associated  with a node
     * adds a line to neighbour nodes
     * adds on click functions
     * @param node
     * @return created NodeButton
     */
    @Override
    public NodeButton addNodeButton(Node node){
        //MAKE BUTTON IF ON CURRENT FLOOR
//        if (node.getFloor().equals(getCurrFloorVal())) {
            //System.out.println("adding button edit");
            NodeButton nb = super.addNodeButton(node);
            //set on click methods
            //drag button
            nb.setOnMouseDragged(e -> {
                nb.setLayoutX(e.getSceneX()-100);
                nb.setLayoutY(e.getSceneY());
            });
            //drag released
            nb.setOnMouseReleased(event -> {
                System.out.println("mouse released");
                double newX=unScaleX(nb.getLayoutX());
                double newY=unScaleY(nb.getLayoutY());

                EditNodeLocation(nb, newX, newY); //set new location

            });

            nb.setOnMouseClicked(event -> {
                for(EdgeLine el: nb.getLines()){
                    System.out.println(el.getEndNode().getFloor());
                }

                if (event.getButton() == MouseButton.PRIMARY) {
                    if (nodeButtonHold != null)
                    {
                        nodeButtonHold.deselect();
                        nodeButtonHold = null;
                    }
                    if (edgeHold != null)
                    {
                        edgeHold.setSelected(false);
                        edgeHold.updateStyle();
                    }
                    nodeButtonHold = nb;
                    editTabController.updateProperties();
                    System.out.println(nb.getNode().getXcoord());
                    System.out.println(nb.getLayoutX());

                    nodeClicked(nb);

                    if (editTabController.getEditingEdges()) { //if in mode adding edges
                        if (editTabController.getEdgeNodeStart() == null) {
                            editTabController.setEdgeNodeStart(nb); //set start button
                            nb.toggleIsSelected(true);
                        } else if (editTabController.getEdgeNodeEnd() == null ) {
                            if(editTabController.getEdgeNodeStart() != nb) {//both points have been specified so create edge
                                editTabController.setEdgeNodeEnd(nb); //set end button

                                //create edge and lines
                                addEdgeBetween(editTabController.getEdgeNodeStart(),editTabController.getEdgeNodeEnd());
                            }

                            editTabController.getEdgeNodeStart().toggleIsSelected(false);

                            //reset start and end so another line can be created
                            editTabController.setEdgeNodeStart(null);
                            editTabController.setEdgeNodeEnd(null);
                        }
                    }
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    if (nodeButtonHold != null)
                    {
                        nodeButtonHold.deselect();
                        nodeButtonHold = null;
                    }
                    if (edgeHold != null)
                    {
                        edgeHold.setSelected(false);
                        edgeHold.updateStyle();
                    }
                    nodeClicked(nb);
                    nodeName.setText(nodeHold.getName());
                    rClickPopup.setVisible(true);
                    rClickPopup1.setVisible(false);
                    deleteConfirmation.setVisible(false);
                    deleteConfirmation1.setVisible(false);
                    TranslateTransition transition = new TranslateTransition(Duration.millis(75), rClickPopup);
                    transition.setToX(event.getSceneX() - rClickPopup.getLayoutX() - rClickPopup.getWidth() / 2 - event.getX() + 90);
                    transition.setToY(event.getSceneY() - rClickPopup.getLayoutY() - rClickPopup.getHeight() / 2 - event.getY() - 55);
                    transition.playFromStart();
                }
            });
            return nb;
//        }
//        return null;
    }

    public void nodeClicked(NodeButton nb)
    {
        nodeHold = nb.getNode();
        nodeButtonHold = nb;
        nodeButtonHold.getNode().setIsSelected(true);
        nodeButtonHold.setButtonStyle();
    }

    /**
     * Creates an edge between the two buttons, adds lines, and adds to database
     * @param start
     * @param end
     */
    public void addEdgeBetween(NodeButton start, NodeButton end){
        //update node neighbors so stay between switching floors
        start.getNode().addNeighbour(end.getNode());
        end.getNode().addNeighbour(start.getNode());

        //create line there
        EdgeLine el = addEdgeLine(start.getNode(), end.getNode());
        start.addLine(el);

        //create opposite line
        EdgeLine elOpposite = addEdgeLine(end.getNode(), start.getNode());
        end.addLine(elOpposite);

        //get node ids
        String startID = start.getNode().getId();
        String endID = end.getNode().getId();

        // add edge to database
        dbTable.addEdge(startID + "_" + endID, startID, endID);
    }

    /**
     * given map/database coordinates and sets buttons new location and on window
     * @param nb
     * @param newX position of button in map/database coordinates
     * @param newY
     */
    public void EditNodeLocation(NodeButton nb, double newX, double newY) {
        Node node = nb.getNode(); //old map coordinates

        //set to be position without scale
        //set so not scaled to image
        node.setXcoord((int) (newX));
        node.setYcoord((int) (newY));

        nb.pan(imageView); //move button

        //Update in DB
        dbTable.updateNode(node);

        //update edges
        for (EdgeLine el : nb.getLines()) {
            translateEdgeLine(el);
            EdgeLine oppositeLine = findEdgeLine(el.getEndNode(), nb.getNode());
            if(oppositeLine!=null) {
                translateEdgeLine(oppositeLine);
            }

        }
    }

    /**
     * returns an unique Id with the base+number where the number increases till the id is unique
     * @param base: String- base name
     * @return id: String
     */
    public String getNewID(String base){
        List<String> ids = dbTable.getIDs();
        String id = base;
        boolean idFound = false;
        while(!idFound) {
            id = base + btnIncrement;
            if (ids.contains(id)) {
                System.out.println("contains "+ id);
                btnIncrement++;
            }
            else{
                idFound=true;
            }
        }
        return id;
    }

    /**
     * Add a new Node Button to given map location
     * @param x map location
     * @param y
     * @return New NodeButton
     */
    private NodeButton addNodeButtonAtLoc(int x, int y){
        String floor = getCurrFloorVal();

        String newId= getNewID("FLOOR"+floor+"-"); //get new id;
        System.out.println("ID "+ newId);

        Node node = new Node(newId, newId, x, y, floor,"NONE", "NONE", "node"+btnIncrement);
        NodeButton nb =addNodeButton(node);

        graph.addToGraph(node);

        dbTable.addNode(node);//add to database
        return nb;
    }

    @Override
    public void initialize()  {
        super.initialize();
        editTabController.injectEditMap(this);
        rClickPopup.setVisible(false);
        rClickPopup1.setVisible(false);
        deleteConfirmation.setVisible(false);
        deleteConfirmation1.setVisible(false);
        isEditingMap = true;

        //add buttons
        for (Node n: graph.getGraph()){
            addNodeButton(n);
        }
        translateGraph(imageView);

        //Add Node when screen is clicked
        btnPane.setOnMouseClicked(event -> {
            if (nodeButtonHold != null)
            {
                nodeButtonHold.deselect();
                nodeButtonHold = null;
            }
            if(editTabController.getAddingNodes() ==true){ //if adding nodes
                //set x and y to be position of mouse
                int x = (int) (unScaleX(event.getSceneX()-100));
                int y = (int) (unScaleY(event.getSceneY()));
                addNodeButtonAtLoc(x,y);
            }
        });
    }

    @FXML
    private void deleteConfirm(ActionEvent actionEvent)
    {
        deleteConfirmation.setVisible(false);

        deleteNodeButton(nodeButtonHold);
    }
    // deletes selected edge from screen and database
    @FXML
    private void deleteConfirm1(ActionEvent actionEvent)
    {
        deleteConfirmation1.setVisible(false);
        edgeHold.setVisible(false);
        dbTable.removeEdge(edgeHold.getStartNode().getId(), edgeHold.getEndNode().getId());
        edgeHold.getEndNode();
        EdgeLine opp =findEdgeLine(edgeHold.getEndNode(), edgeHold.getStartNode());
        if(opp!=null) {
            dbTable.removeEdge(edgeHold.getEndNode().getId(), edgeHold.getStartNode().getId());
            opp.setVisible(false);
        }
        System.out.println("Deleted");
    }


    /**
     * delete given nodeButton and corresponding node and edges
     * @param nb: NodeButton
     */
    private void deleteNodeButton(NodeButton nb){
        Node node = nb.getNode();
        nb.setVisible(false); //make invisible
        for(EdgeLine el: nodeButtonHold.getLines()){
            //delete lines get lines
            el.setVisible(false);
            dbTable.removeEdge(nodeHold.getId(), el.getEndNode().getId());
            el.getEndNode();
            EdgeLine opp =findEdgeLine(el.getEndNode(),node);
            if(opp!=null) {
                dbTable.removeEdge(el.getEndNode().getId(), nodeHold.getId());
                opp.setVisible(false);
            }
        }
        for(Node neighbour: node.getNeighbours()){ //remove neighbors
            neighbour.getNeighbours().remove(node);
        }

        graph.getGraph().remove(node);// remove from graph
        dbTable.removeNode(node.getId()); //remove from database

    }

    @FXML
    private void deleteCancel(ActionEvent actionEvent)
    {
        deleteConfirmation.setVisible(false);
    }

    @FXML
    private void deleteCancel1(ActionEvent actionEvent)
    {
        deleteConfirmation1.setVisible(false);
    }

    @FXML
    private void openDeletePage(ActionEvent actionEvent)
    {
        deleteConfirmation.setVisible(true);
        rClickPopup.setVisible(false);
        confirmDeleteNode.setText(nodeHold.getName());
    }

    @FXML
    private void openDeletePage1(ActionEvent actionEvent)
    {
        deleteConfirmation1.setVisible(true);
        rClickPopup1.setVisible(false);
        confirmDeleteNode1.setText(edgeHold.getStartNode().getName());
        confirmDeleteNode2.setText(edgeHold.getEndNode().getName());

    }

    @FXML
    private void closePopup(ActionEvent actionEvent)
    {
        rClickPopup.setVisible(false);
    }

    @FXML
    private void closePopup1(ActionEvent actionEvent)
    {
        rClickPopup1.setVisible(false);
    }

    @Override
    public void openEdgePopup(double sceneX, double sceneY)
    {
        nodeName1.setText(edgeHold.getStartNode().getName());
        nodeName2.setText(edgeHold.getEndNode().getName());
        rClickPopup1.setVisible(true);
        rClickPopup.setVisible(false);
        deleteConfirmation.setVisible(false);
        deleteConfirmation1.setVisible(false);
        TranslateTransition transition = new TranslateTransition(Duration.millis(75), rClickPopup1);
        transition.setToX(sceneX - 430);
        transition.setToY(sceneY - 215);
        transition.playFromStart();
    }
}