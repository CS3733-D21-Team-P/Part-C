package edu.wpi.p.views.map;

import edu.wpi.p.AStar.EdgeLine;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.AStar.NodeButton;
import com.jfoenix.controls.JFXButton;
import edu.wpi.p.database.DBMap;
import edu.wpi.p.views.ClippoController;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class EditMap extends MapController {

    @FXML private Pane clippoID;
    @FXML private ClippoController clippoIDController;

    private DBMap dbMap = DBMap.getInstance();
    private int btnIncrement = 1;

    @FXML private EditTabController editTabController;

    private String currFloorVal;
    @FXML private Image mapImage;

    public List<NodeButton> getSelected() {
        return selected;
    }

    private List<NodeButton> selected = new ArrayList<>();

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

            //add edges
            List<Node> children = node.getNeighbours();
            for (Node n : children) {
                EdgeLine el = addEdgeLine(node, n);
                nb.addLine(el);
            }

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

                if (event.getButton() == MouseButton.PRIMARY) {
                    if (event.isShiftDown()){
                        shiftClick(nb);
                        System.out.println("shift click");
                    }
                    else {
                        deselectAllNodes();
                    }

                    selectNode(nb);

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
                    deselectAllNodes();

                    selectNode(nb);
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
    }

    @Override
    public EdgeLine addEdgeLine(Node node1, Node node2) {
        EdgeLine el = super.addEdgeLine(node1,node2);

        //set events for selecting edge
        el.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                selectEdge(el);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                selectEdge(el);
                openEdgePopup(event.getSceneX(), event.getSceneY());
            }
        });
        return el;
    }


    @Override
    public void selectNode(NodeButton nb)
    {
        nodeHold = nb.getNode();
        nodeButtonHold = nb;
        nodeButtonHold.getNode().setIsSelected(true);
        nodeButtonHold.setButtonStyle();

        selected.add(nb);
        editTabController.updateProperties();
        System.out.println(nb.getNode().getXcoord());
        System.out.println(nb.getLayoutX());
    }

    public void shiftClick(NodeButton nb){
        if(nodeButtonHold==null){
            nodeButtonHold = nb;
        }
        nb.toggleIsSelected(true);
        selected.add(nb);
    }

    public void deselectAllNodes(){
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
        for(NodeButton nb: selected){
            nb.deselect();
        }
        selected.clear();
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
        dbMap.addEdge(startID + "_" + endID, startID, endID);
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
        dbMap.updateNode(node);

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
        List<String> ids = dbMap.getIDs();
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

        dbMap.addNode(node);//add to database
        return nb;
    }

    @Override
    public void initialize()  {
        super.initialize();
        clippoIDController.setPage("editMap");
        pathfindPage = false;
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
            deselectAllNodes();
//            if (nodeButtonHold != null)
//            {
//                nodeButtonHold.deselect();
//                nodeButtonHold = null;
//            }
            if(editTabController.getAddingNodes() ==true){ //if adding nodes
                //set x and y to be position of mouse
                int x = (int) (unScaleX(event.getSceneX()-100));
                int y = (int) (unScaleY(event.getSceneY()));
                NodeButton nb = addNodeButtonAtLoc(x,y);
                selectNode(nb);
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
        dbMap.removeEdge(edgeHold.getStartNode().getId(), edgeHold.getEndNode().getId());
        edgeHold.getEndNode();
        EdgeLine opp =findEdgeLine(edgeHold.getEndNode(), edgeHold.getStartNode());
        if(opp!=null) {
            dbMap.removeEdge(edgeHold.getEndNode().getId(), edgeHold.getStartNode().getId());
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
            dbMap.removeEdge(nodeHold.getId(), el.getEndNode().getId());
            el.getEndNode();
            EdgeLine opp =findEdgeLine(el.getEndNode(),node);
            if(opp!=null) {
                dbMap.removeEdge(el.getEndNode().getId(), nodeHold.getId());
                opp.setVisible(false);
            }
        }
        for(Node neighbour: node.getNeighbours()){ //remove neighbors
            neighbour.getNeighbours().remove(node);
        }

        graph.getGraph().remove(node);// remove from graph
        dbMap.removeNode(node.getId()); //remove from database

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