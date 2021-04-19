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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;

public class EditMap extends MapController{
    private DBTable dbTable = new DBTable();
    private Boolean isEditingEdges = false;

    private NodeButton edgeNodeStart = null;
    private NodeButton edgeNodeEnd = null;

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
            System.out.println(nb.getNode().getXcoord());
            System.out.println(nb.getLayoutX());
            propertiesBox.setVisible(true);
            nodeClicked(nb.getNode());

            if(isEditingEdges){
                if(edgeNodeStart==null){
                    edgeNodeStart=nb;
                }
                else if(edgeNodeEnd==null){ //both points have been specified so create edge
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

    @Override
    public void initialize()  {
        super.initialize();
        propertiesBox.setVisible(false);
        System.out.println("EDIT INIT");

        //add buttons
        for (Node n: graph.getGraph()){
            addNodeButton(n);
        }


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
    private void nodeClicked(Node node)
    {
        String x = String.valueOf(node.getXcoord());
        String y = String.valueOf(node.getYcoord());

        name.setText(node.getName());
        idText.setText(node.getId());
        floorText.setText(node.getFloor());
        typeText.setText(node.getType());
        shortNameText.setText(node.getShortName());
        buildingText.setText(node.getBuilding());
        xCoordinateText.setText(x);
        yCoordinateText.setText(y);
    }

    @FXML
    private void switchEditEdges(ActionEvent actionEvent){
        isEditingEdges = !isEditingEdges;
        edgeNodeStart = null;
        edgeNodeEnd = null;
    }

    @FXML
    private void propertiesDisappear(ActionEvent actionEvent)
    {
        propertiesBox.setVisible(false);
    }
}
