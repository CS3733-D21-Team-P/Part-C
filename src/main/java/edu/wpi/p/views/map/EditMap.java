package edu.wpi.p.views.map;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.control.ToggleButton;
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
    private Boolean isEditingEdges = false;
    private Boolean isAddingNodes = false;
    private int btnIncrement = 1;

    private NodeButton edgeNodeStart = null;
    private NodeButton edgeNodeEnd = null;

    private Node nodeHold;
    private NodeButton nodeButtonHold;

    private String currFloorVal;
    @FXML private Image mapImage;
    @FXML private TextField nodeFilepathField;
    @FXML private JFXTextField name;
    @FXML private Label id;
    @FXML private Label floor;
    @FXML private Label type;
    @FXML private Label shortName;
    @FXML private Label building;
    @FXML private Label xCoordinate;
    @FXML private Label yCoordinate;
    @FXML private JFXTextField idText;
    @FXML private JFXTextField floorText;
    @FXML private JFXTextField typeText;
    @FXML private JFXTextField shortNameText;
    @FXML private JFXTextField buildingText;
    @FXML private JFXTextField xCoordinateText;
    @FXML private JFXTextField yCoordinateText;
    @FXML private JFXButton submit;
    @FXML private JFXButton close;
    @FXML private AnchorPane propertiesBox;
    @FXML private JFXButton propertiesButton;
    @FXML private JFXButton deleteButton;
    @FXML private VBox rClickPopup;
    @FXML private AnchorPane deleteConfirmation;
    @FXML private AnchorPane btnPane;
    @FXML private Text deleteConfirmText;
    @FXML private Text confirmDeleteNode;
    @FXML private JFXButton yesButton;
    @FXML private JFXButton noButton;
    @FXML private ToggleButton toggleEditNodes;
    @FXML private ToggleButton toggleEditEdges;
    @FXML private Text nodeName;
    @FXML private JFXButton closeButton;
    @FXML private Text changesSavedText;


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
        if (node.getFloor().equals(getCurrFloorVal())) {
            System.out.println("adding button edit");
            NodeButton nb = super.addNodeButton(node);
            //set on click methods
            //drag button
            nb.setOnMouseDragged(e -> {
                nb.setLayoutX(e.getSceneX());
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
                    System.out.println(nb.getNode().getXcoord());
                    System.out.println(nb.getLayoutX());

                    if (isEditingEdges) { //if in mode adding edges
                        if (edgeNodeStart == null) {
                            edgeNodeStart = nb; //set start button
                            nb.toggleIsSelected(true);
                        } else if (edgeNodeEnd == null ) {
                            if(edgeNodeStart != nb) {//both points have been specified so create edge
                                edgeNodeEnd = nb; //set end button

                                //create edge and lines
                                addEdgeBetween(edgeNodeStart, edgeNodeEnd);
                            }

                            edgeNodeStart.toggleIsSelected(false);

                            //reset start and end so another line can be created
                            edgeNodeStart = null;
                            edgeNodeEnd = null;
                        }
                    }
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    nodeHold = nb.getNode();
                    nodeButtonHold = nb;
                    nodeName.setText(nodeHold.getName());
                    rClickPopup.setVisible(true);
                    deleteConfirmation.setVisible(false);
                    TranslateTransition transition = new TranslateTransition(Duration.millis(75), rClickPopup);
                    transition.setToX(event.getSceneX() - rClickPopup.getLayoutX() - rClickPopup.getWidth() / 2 - event.getX() + 90);
                    transition.setToY(event.getSceneY() - rClickPopup.getLayoutY() - rClickPopup.getHeight() / 2 - event.getY() - 55);
                    transition.playFromStart();
                }
            });
            return nb;
        }
        return null;
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
        propertiesBox.setVisible(false);
        rClickPopup.setVisible(false);
        deleteConfirmation.setVisible(false);
        changesSavedText.setVisible(false);

        //add buttons
        for (Node n: graph.getGraph()){
            addNodeButton(n);
        }
        translateGraph(imageView);

        //Add Node when screen is clicked
        btnPane.setOnMouseClicked(event -> {
            if(isAddingNodes ==true){ //if adding nodes
                //set x and y to be position of mouse
                int x = (int) (unScaleX(event.getSceneX()));
                int y = (int) (unScaleY(event.getSceneY()));
                addNodeButtonAtLoc(x,y);
            }
        });
    }

    /**
     * Opens a file dialoge for the user to choose a CSV file
     * @return File: Chosen file
     */
    private File chooseFile(){
        //Open file dialogue to choose a set of nodes
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Node File");//set tile
        fileChooser.getExtensionFilters().addAll(//limit to a CSV file
                new ExtensionFilter("CSV Files", "*.csv")
        );
        File file = fileChooser.showOpenDialog(App.getPrimaryStage()); //open dialogue
        return file;
    }

    @FXML
    private void importNodeCSVBtn(ActionEvent actionEvent) throws Exception {
        File file = chooseFile();
        CSVData nodeData = CSVHandler.readCSVFile(file.getName());

        dbTable.clearNodes();//clear database
        CSVDBConverter.addCSVNodesToTable(dbTable, nodeData); //update database

        //clear buttons and lines
        btnPane.getChildren().clear();
        linePane.getChildren().clear();
        initialize(); //reload to create all new buttons and lines
    }

    @FXML
    private void importEdgeCSVBtn(ActionEvent actionEvent) throws Exception {
        File file = chooseFile();
        CSVData edgeData = CSVHandler.readCSVFile(file.getName());

        dbTable.clearEdges();//clear database
        CSVDBConverter.addCSVEdgesToTable(dbTable, edgeData); //update database

        //clear buttons and lines
        btnPane.getChildren().clear();
        linePane.getChildren().clear();
        initialize(); //reload to create all new buttons and lines
    }

    @FXML
    private void exportCSVBtn(ActionEvent actionEvent) {
        CSVData newNodes = CSVDBConverter.csvNodesFromTable(dbTable);
        CSVData newEdges = CSVDBConverter.csvEdgesFromTable(dbTable);
        CSVHandler.writeCSVData(newNodes, "newNodes.csv");
        CSVHandler.writeCSVData(newEdges, "newEdges.csv");
    }

    @FXML
    private void propertiesClicked(ActionEvent actionEvent)
    {
        changesSavedText.setVisible(false);
        propertiesBox.setVisible(true);
        rClickPopup.setVisible(false);
        propertiesBox.toFront();
        String x = String.valueOf(nodeHold.getXcoord());
        String y = String.valueOf(nodeHold.getYcoord());
        name.setText(nodeHold.getName());
        idText.setText(nodeHold.getId());
        floorText.setText(nodeHold.getFloor());
        typeText.setText(nodeHold.getType());
        shortNameText.setText(nodeHold.getShortName());
        buildingText.setText(nodeHold.getBuilding());
        xCoordinateText.setText(x);
        yCoordinateText.setText(y);
        propertiesBox.toFront();
    }

    @FXML
    private void deleteConfirm(ActionEvent actionEvent)
    {
        deleteConfirmation.setVisible(false);

        deleteNodeButton(nodeButtonHold);
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
    private void openDeletePage(ActionEvent actionEvent)
    {
        deleteConfirmation.setVisible(true);
        propertiesBox.setVisible(false);
        rClickPopup.setVisible(false);
        confirmDeleteNode.setText(nodeHold.getName());
    }

    @FXML
    private void switchEditEdges(ActionEvent actionEvent){
        isEditingEdges = !isEditingEdges;
        isAddingNodes = false;
        toggleEditNodes.setSelected(false);
        edgeNodeStart = null;
        edgeNodeEnd = null;
    }

    @FXML
    private void switchEditNodes(ActionEvent actionEvent){
        isAddingNodes = !isAddingNodes;
        isEditingEdges = false;
        toggleEditEdges.setSelected(false);
    }

    @FXML
    private void propertiesDisappear(ActionEvent actionEvent)
    {
        propertiesBox.setVisible(false);
    }

    @FXML
    private void closePopup(ActionEvent actionEvent)
    {
        rClickPopup.setVisible(false);
    }

    @FXML
    private void updateNode(ActionEvent actionEvent)
    {
        Node node = nodeHold;
        node.setName(name.getText());
        node.setId(idText.getText());
        node.setFloor(floorText.getText());
        node.setType(typeText.getText());
        node.setBuilding(buildingText.getText());
        node.setShortName(shortNameText.getText());
        int x = Integer.parseInt(xCoordinateText.getText());
        int y = Integer.parseInt(yCoordinateText.getText());
        node.setXcoord(x);
        node.setYcoord(y);
        dbTable.updateNode(node);

        //update how node button looks
        nodeButtonHold.update(imageView);

        EditNodeLocation(nodeButtonHold,x,y);

        changesSavedText.setVisible(true);
    }

    @FXML
    private void directToEdgeEditPage(ActionEvent actionEvent)
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/MapPEdgeData.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}