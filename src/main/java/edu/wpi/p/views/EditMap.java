package edu.wpi.p.views;

import AStar.EdgeLine;
import AStar.Node;
import AStar.NodeButton;
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

    @FXML private TextField nodeFilepathField;
    @FXML private Text name;
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


    /**
     * creates a button associated  with a node
     * adds a line to neighbour nodes
     * adds on click functions
     * @param node
     * @return created NodeButton
     */
    @Override
    public NodeButton addNodeButton(Node node){
        NodeButton nb = super.addNodeButton(node);
        //set on click method
        nb.setOnMouseDragged(e -> {
            nb.setLayoutX(e.getSceneX());
            nb.setLayoutY(e.getSceneY());

        });
        nb.setOnMouseReleased(event -> {
            System.out.println("mouse released");
            Editcoord(nb, nb.getLayoutX(), nb.getLayoutY());
            System.out.println(nb.getName());
            for(EdgeLine el: nb.getLines()){
                el.update(scaleX,scaleY, nb.getNode());
                EdgeLine oppositeLine = findEdgeLine(el.getEndNode(), nb.getNode());
                oppositeLine.update(scaleX,scaleY,nb.getNode());
            }
        });

        nb.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY)
            {
            System.out.println(nb.getNode().getXcoord());
            System.out.println(nb.getLayoutX());

            if(isEditingEdges){
                if(edgeNodeStart==null){
                    edgeNodeStart=nb;
                }
                else if(edgeNodeEnd==null && edgeNodeStart!=nb){ //both points have been specified so create edge
                    edgeNodeEnd=nb;
                    //create line there
                    EdgeLine el =addEdgeLine(edgeNodeStart.getNode(), edgeNodeEnd.getNode());
                    //scale lines
                    el.update(scaleX,scaleY,edgeNodeStart.getNode());
                    el.update(scaleX,scaleY,edgeNodeEnd.getNode());
                    edgeNodeStart.addLine(el);
                    allLines.add(el); //add to list of lines

                    //create opposite line
                    EdgeLine elOpposite =addEdgeLine(edgeNodeEnd.getNode(), edgeNodeStart.getNode());
                    //scale lines
                    elOpposite.update(scaleX,scaleY,edgeNodeStart.getNode());
                    elOpposite.update(scaleX,scaleY,edgeNodeEnd.getNode());
                    edgeNodeEnd.addLine(elOpposite);
                    allLines.add(elOpposite); //add to list of lines

                    //get node ids
                    String startID =edgeNodeStart.getNode().getId();
                    String endID = edgeNodeEnd.getNode().getId();

                    // add edge to database
                    dbTable.addEdge(startID+"_"+endID,startID, endID);

                    //reset start and end so another line can be created
                    edgeNodeStart=null;
                    edgeNodeEnd=null;
                }
            }
            }
            else if (event.getButton() == MouseButton.SECONDARY)
            {
                nodeHold = nb.getNode();
                nodeName.setText(nodeHold.getName());
                rClickPopup.setVisible(true);
                deleteConfirmation.setVisible(false);
                TranslateTransition transition = new TranslateTransition(Duration.millis(75), rClickPopup);
                transition.setToX(event.getSceneX() - rClickPopup.getLayoutX() - rClickPopup.getWidth() / 2 - event.getX() + 85);
                transition.setToY(event.getSceneY() - rClickPopup.getLayoutY() - rClickPopup.getHeight() / 2 - event.getY() - 55);
                transition.playFromStart();
            }

        });
        return nb;
    }


    public void Editcoord(NodeButton nb, double newX, double newY) {
        Node node = nb.getNode();
        //set to be position without scale
        node.setYcoord((int)(newY/scaleY));
        node.setXcoord((int)(newX/scaleX));

        //Update in DB
        DBTable dbTable = new DBTable();
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
        System.out.println("EDIT INIT");

        //add buttons
        for (Node n: graph.getGraph()){
            addNodeButton(n);
        }

        //Add Node when screen is clicked
        btnPane.setOnMouseClicked(event -> {
            if(isAddingNodes ==true){ //if adding nodes
                //set x and y to be position of mouse
                int x = (int) (event.getSceneX());
                int y = (int) (event.getSceneY());
                System.out.println("create button: "+x+" , "+y);

                String newId= getNewID("blankNode"); //get new id;
                System.out.println("ID "+ newId);
                Node node = new Node(newId, newId, x, y, "1","NONE", "NONE", "node"+btnIncrement);
                addNodeButton(node);

                //set so not scaled to image
                node.setXcoord((int)(x/scaleX));
                node.setYcoord((int)(y/scaleY));
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
        deleteConfirmation.setVisible(false);
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
    private void clickOff(ActionEvent actionEvent)
    {
        rClickPopup.setVisible(false);
    }
}
