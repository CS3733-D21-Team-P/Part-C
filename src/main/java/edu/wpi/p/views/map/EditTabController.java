package edu.wpi.p.views.map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.AStar.NodeButton;
import edu.wpi.p.App;
import edu.wpi.p.csv.CSVData;
import edu.wpi.p.csv.CSVHandler;
import edu.wpi.p.database.CSVDBConverter;
import edu.wpi.p.database.DBMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EditTabController{

    public EditMap editMapController;

    private DBMap dbMap = DBMap.getInstance();

    private Boolean isEditingEdges = false;
    private Boolean isAddingNodes = false;

    private NodeButton edgeNodeStart = null;
    private NodeButton edgeNodeEnd = null;

    @FXML JFXTextField name;
    @FXML  Label id;
    @FXML  Label floor;
    @FXML  Label type;
    @FXML  Label shortName;
    @FXML  Label building;
    @FXML  Label xCoordinate;
    @FXML  Label yCoordinate;
    @FXML  JFXTextField idText;
    @FXML  JFXTextField floorText;
    @FXML  JFXTextField typeText;
    @FXML  JFXTextField shortNameText;
    @FXML  JFXTextField buildingText;
    @FXML  JFXTextField xCoordinateText;
    @FXML  JFXTextField yCoordinateText;
    @FXML  JFXButton submit;
    @FXML  AnchorPane propertiesBox;

    @FXML private Text changesSavedText;

    @FXML private JFXToggleButton toggleEditNodes;
    @FXML private JFXToggleButton toggleEditEdges;

    @FXML
    public void initialize(){
        changesSavedText.setVisible(false);
    }

    @FXML
    private void switchEditNodes(ActionEvent actionEvent){
        isAddingNodes = !isAddingNodes;
        isEditingEdges = false;
        toggleEditEdges.setSelected(false);
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
    private void updateNode(ActionEvent actionEvent)
    {
        Node node = editMapController.nodeButtonHold.getNode();
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
        dbMap.updateNode(node);

        //update how node button looks
        editMapController.nodeButtonHold.update(editMapController.imageView);

        editMapController.EditNodeLocation(editMapController.nodeButtonHold,x,y);

        changesSavedText.setVisible(true);
    }

    @FXML
    public void updateProperties()
    {
        changesSavedText.setVisible(false);
        editMapController.rClickPopup.setVisible(false);
        String x = String.valueOf(editMapController.nodeButtonHold.getNode().getXcoord());
        String y = String.valueOf(editMapController.nodeButtonHold.getNode().getYcoord());
        name.setText(editMapController.nodeButtonHold.getNode().getName());
        idText.setText(editMapController.nodeButtonHold.getNode().getId());
        floorText.setText(editMapController.nodeButtonHold.getNode().getFloor());
        typeText.setText(editMapController.nodeButtonHold.getNode().getType());
        shortNameText.setText(editMapController.nodeButtonHold.getNode().getShortName());
        buildingText.setText(editMapController.nodeButtonHold.getNode().getBuilding());
        xCoordinateText.setText(x);
        yCoordinateText.setText(y);
    }

    @FXML
    private void directToEdgeEditPage(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/mapsFXML/MapPEdgeData.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void alignVertical(){
        System.out.println("align vertical");
        List<NodeButton> selected = editMapController.getSelected();
        double averageX = 0;

        for(NodeButton nb: selected){
            averageX+=nb.getNode().getXcoord();
        }
        averageX= averageX/selected.size();

        for(NodeButton nb: selected){
            editMapController.EditNodeLocation(nb,averageX,nb.getNode().getYcoord());
        }
    }

    @FXML
    private void alignHorizontal(){
        System.out.println("align horizontal");
        List<NodeButton> selected = editMapController.getSelected();
        double averageY = 0;

        for(NodeButton nb: selected){
            averageY+=nb.getNode().getYcoord();
        }
        averageY= averageY/selected.size();

        for(NodeButton nb: selected){
            editMapController.EditNodeLocation(nb,nb.getNode().getXcoord(),averageY);
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
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        File file = fileChooser.showOpenDialog(App.getPrimaryStage()); //open dialogue
        return file;
    }

    @FXML
    private void importNodeCSVBtn(ActionEvent actionEvent) throws Exception {
        File file = chooseFile();
        CSVData nodeData = CSVHandler.readCSVFile(file.getName());

        dbMap.clearNodes();//clear database
        CSVDBConverter.addCSVNodesToTable(dbMap, nodeData); //update database

        //clear buttons and lines
        editMapController.btnPane.getChildren().clear();
        editMapController.linePane.getChildren().clear();
        editMapController.initialize(); //reload to create all new buttons and lines
    }

    @FXML
    private void importEdgeCSVBtn(ActionEvent actionEvent) throws Exception {
        File file = chooseFile();
        CSVData edgeData = CSVHandler.readCSVFile(file.getName());

        dbMap.clearEdges();//clear database
        CSVDBConverter.addCSVEdgesToTable(dbMap, edgeData); //update database

        //clear buttons and lines
        editMapController.btnPane.getChildren().clear();
        editMapController.linePane.getChildren().clear();
        editMapController.initialize(); //reload to create all new buttons and lines
    }

    @FXML
    private void importCSVBtn(ActionEvent actionEvent) throws Exception{
        File file = chooseFile();
        CSVData data = CSVHandler.readCSVFile(file.getAbsolutePath());

        //check if edges or nodes
        int numColumns = data.getAllColumns().size();
        if(numColumns>5){
            dbMap.clearNodes();//clear database
            CSVDBConverter.addCSVNodesToTable(dbMap, data); //update database
        }
        else{
            dbMap.clearEdges();//clear database
            CSVDBConverter.addCSVEdgesToTable(dbMap, data); //update database
        }

        //clear buttons and lines
        editMapController.btnPane.getChildren().clear();
        editMapController.linePane.getChildren().clear();
        editMapController.initialize(); //reload to create all new buttons and lines
    }

    @FXML
    private void exportCSVBtn(ActionEvent actionEvent) {
        CSVData newNodes = CSVDBConverter.csvNodesFromTable(dbMap);
        CSVData newEdges = CSVDBConverter.csvEdgesFromTable(dbMap);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Node File");//set tile
        fileChooser.getExtensionFilters().addAll(//limit to a CSV file
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        File file = fileChooser.showSaveDialog(App.getPrimaryStage()); //open dialogue

        System.out.println(file.getName());
        String name = file.getName().split(".csv")[0];
        String path = file.getAbsolutePath();
        String nativeDir = path.substring(0, path.lastIndexOf(File.separator));
        nativeDir+=File.separator;
        System.out.println(file.getPath()+name);
        System.out.println(nativeDir);


        CSVHandler.writeCSVData(newNodes, nativeDir+name+"-Nodes.csv");
        CSVHandler.writeCSVData(newEdges, nativeDir+name+"-Edges.csv");
    }

    public void injectEditMap(EditMap editMap){ this.editMapController = editMap; }

    public NodeButton getEdgeNodeStart() {
        return edgeNodeStart;
    }

    public void setEdgeNodeStart(NodeButton edgeNodeStart) {
        this.edgeNodeStart = edgeNodeStart;
    }

    public NodeButton getEdgeNodeEnd() {
        return edgeNodeEnd;
    }

    public void setEdgeNodeEnd(NodeButton edgeNodeEnd) {
        this.edgeNodeEnd = edgeNodeEnd;
    }

    public Boolean getEditingEdges() {
        return isEditingEdges;
    }

    public void setEditingEdges(Boolean editingEdges) {
        isEditingEdges = editingEdges;
    }

    public Boolean getAddingNodes() {
        return isAddingNodes;
    }

    public void setAddingNodes(Boolean addingNodes) {
        isAddingNodes = addingNodes;
    }

}
