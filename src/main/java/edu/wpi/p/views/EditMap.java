package edu.wpi.p.views;

import AStar.EdgeLine;
import AStar.Node;
import AStar.NodeButton;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.p.App;
import edu.wpi.p.csv.CSVData;
import edu.wpi.p.csv.CSVHandler;
import edu.wpi.p.database.CSVDBConverter;
import edu.wpi.p.database.DBTable;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;

import java.io.File;
import java.util.List;

public class EditMap extends MapController{
    private DBTable dbTable = new DBTable();
    private Boolean isEditingEdges = false;
    private Boolean isAddingNodes = false;
    private int btnIncrement = 1;

    private NodeButton edgeNodeStart = null;
    private NodeButton edgeNodeEnd = null;

    private Node nodeHold;
    private NodeButton nodeButtonHold;

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
            //set on click method
            nb.setOnMouseDragged(e -> {
                nb.setLayoutX(e.getSceneX());
                nb.setLayoutY(e.getSceneY());

            });
            nb.setOnMouseReleased(event -> {
                System.out.println("mouse released");
                Editcoord(nb, nb.getLayoutX(), nb.getLayoutY()); //set new location
                System.out.println(nb.getName());
                for (EdgeLine el : nb.getLines()) {
                    translateEdge(el);
                    //el.update(imageView, nb.getNode());
                    EdgeLine oppositeLine = findEdgeLine(el.getEndNode(), nb.getNode());
                    //oppositeLine.update(imageView,nb.getNode());
                    translateEdge(oppositeLine);
                }
            });

            nb.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    System.out.println(nb.getNode().getXcoord());
                    System.out.println(nb.getLayoutX());

                    if (isEditingEdges) {
                        if (edgeNodeStart == null) {
                            edgeNodeStart = nb;
                        } else if (edgeNodeEnd == null && edgeNodeStart != nb) { //both points have been specified so create edge
                            edgeNodeEnd = nb;

                            //update node neighbors so stay between switching floors
                            edgeNodeStart.getNode().addNeighbour(edgeNodeEnd.getNode());
                            edgeNodeEnd.getNode().addNeighbour(edgeNodeStart.getNode());

                            //create line there
                            EdgeLine el = addEdgeLine(edgeNodeStart.getNode(), edgeNodeEnd.getNode());
                            //scale lines
                            translateEdge(el);
                            edgeNodeStart.addLine(el);
                            edgeLines.add(el); //add to list of lines

                            //create opposite line
                            EdgeLine elOpposite = addEdgeLine(edgeNodeEnd.getNode(), edgeNodeStart.getNode());
                            //scale lines
                            translateEdge(elOpposite);
                            edgeNodeEnd.addLine(elOpposite);
                            edgeLines.add(elOpposite); //add to list of lines

                            //get node ids
                            String startID = edgeNodeStart.getNode().getId();
                            String endID = edgeNodeEnd.getNode().getId();

                            // add edge to database
                            dbTable.addEdge(startID + "_" + endID, startID, endID);



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
     *
     * @param nb
     * @param newX position of button
     * @param newY
     */
    public void Editcoord(NodeButton nb, double newX, double newY) {
        double scaleX = imageView.getViewport().getWidth() / imageView.getFitWidth();
        double scaleY = imageView.getViewport().getHeight() / imageView.getFitHeight();
        Rectangle2D viewport = imageView.getViewport();

        //translateNode(nb);

//        //set based on scale
//        newY*= scaleY;
//        newX*=scaleX;

        double xShift= (viewport.getMinX()/scaleX);
        double yShift = (viewport.getMinY()/scaleY);
//        newY+=yShift;
//        newX+= xShift;
        //Window Zoom Coords to just Window Coords
//        newY*= scaleY;
//        newX*=scaleX;
        //new map coordinates
        newX=unScaleX(newX);
        newY=unScaleY(newY);

        Node node = nb.getNode(); //old map coordinates
        double mx = node.getXcoord();
        double my= node.getYcoord();

        double diffX = (mx-newX);
        double diffY= (my-newY);

        //set to be position without scale
        //set so not scaled to image
//        node.setXcoord((int) unScaleX(newX));
//        node.setYcoord((int) unScaleY(newY));
        node.setXcoord((int) (mx-diffX));
        node.setYcoord((int) (my-diffY));

//        newX= (newX/scaleX);
//        newY= (newY/scaleY);
//
//        node.setYcoord((int)(newY));
//        node.setXcoord((int)(newX));
        //translateNode(nb);

        //Update in DB
        //DBTable dbTable = new DBTable();
        dbTable.updateNode(node);
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

    @Override
    public void initialize()  {
        super.initialize();
        propertiesBox.setVisible(false);
        rClickPopup.setVisible(false);
        deleteConfirmation.setVisible(false);
        changesSavedText.setVisible(false);
        System.out.println("EDIT INIT");

        //add buttons
        for (Node n: graph.getGraph()){
            addNodeButton(n);
        }
        translateGraph(imageView);

        //Add Node when screen is clicked
        btnPane.setOnMouseClicked(event -> {
            if(isAddingNodes ==true){ //if adding nodes
                //set x and y to be position of mouse
                int x = (int) (event.getSceneX());
                int y = (int) (event.getSceneY());
                System.out.println("create button: "+x+" , "+y);

                String newId= getNewID("blankNode"); //get new id;
                System.out.println("ID "+ newId);

                Node node = new Node(newId, newId, x, y, getCurrFloorVal(),"NONE", "NONE", "node"+btnIncrement);
                NodeButton nb =addNodeButton(node);

                Rectangle2D viewport = imageView.getViewport();

                //set so not scaled to image
                node.setXcoord((int) unScaleX(x));
                node.setYcoord((int) unScaleY(y));

                buttons.add(nb);
                graph.addToGraph(node);

                dbTable.addNode(node);//add to database
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
    }

    @FXML
    private void deleteConfirm(ActionEvent actionEvent)
    {
        Node node = nodeHold;
        DBTable dbTable = new DBTable();
        dbTable.removeNode(node.getId());
        deleteConfirmation.setVisible(false);
        nodeButtonHold.setVisible(false); //make invisible
        for(EdgeLine el: nodeButtonHold.getLines()){
            //delete lines get lines
            el.setVisible(false);
            el.getEndNode();
            EdgeLine opp =findEdgeLine(el.getEndNode(),node);
            opp.setVisible(false);
        }
        for(Node neighbour: node.getNeighbours()){ //remove neighbors
            neighbour.getNeighbours().remove(node);
        }
        graph.getGraph().remove(node);// remove from graph
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
        changesSavedText.setVisible(true);
    }
}