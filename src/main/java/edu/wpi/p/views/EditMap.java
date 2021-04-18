package edu.wpi.p.views;

import AStar.EdgeLine;
import AStar.Node;
import AStar.NodeButton;
import edu.wpi.p.App;
import edu.wpi.p.csv.CSVData;
import edu.wpi.p.csv.CSVHandler;
import edu.wpi.p.database.CSVDBConverter;
import edu.wpi.p.database.DBTable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;

public class EditMap extends MapController{
    private DBTable dbTable = new DBTable();
    private Boolean isEditingEdges = false;

    private NodeButton edgeNodeStart = null;
    private NodeButton edgeNodeEnd = null;

    @FXML private TextField nodeFilepathField;

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
    private void switchEditEdges(ActionEvent actionEvent){
        isEditingEdges = !isEditingEdges;
        edgeNodeStart = null;
        edgeNodeEnd = null;
    }
}
