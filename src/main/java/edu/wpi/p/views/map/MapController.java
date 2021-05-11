package edu.wpi.p.views.map;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.p.AStar.EdgeLine;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.AStar.NodeButton;
import edu.wpi.p.AStar.NodeGraph;
import edu.wpi.p.App;
import edu.wpi.p.database.DBUser;
import edu.wpi.p.database.UserFromDB;
import edu.wpi.p.userstate.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class MapController {

    @FXML
    private MapEditorFindTab findTabController;


    NodeGraph graph = new NodeGraph();
    List<NodeButton> buttons = new ArrayList<>(); //the current list of buttons on current floor
    List<EdgeLine> edgeLines = new ArrayList<>(); //the current list of edges on current floor

    Boolean isEditingMap = false;
    public EdgeLine edgeHold;
    Node nodeHold;
    NodeButton nodeButtonHold;
    public boolean pathfindPage = false;
    public boolean multipleFloors = false;

    HashMap<String, List<NodeButton>> buttonLists = new HashMap<String, List<NodeButton>>(); //list of buttons by floor
    HashMap<String, List<EdgeLine>> edgeLists = new HashMap<String, List<EdgeLine>>(); //list of edges by floor

    final String[] availableFloors = new String[]{ "Ground", "L1", "L2", "1", "2", "3"};

    private double zoomSpeed = 1.005;
    private double minZoomPixels = 800;

    double scaleX;
    double scaleY;

    public String getCurrFloorVal() {
        return currFloorVal;
    }

    public void setCurrFloorVal(String val) {
        this.currFloorVal = val;
    }

    private String currFloorVal;

    @FXML
    public AnchorPane btnPane;
    @FXML
    public AnchorPane linePane;
    @FXML
    public ImageView imageView;
    @FXML
    JFXComboBox<String> floorChoiceBox;
    @FXML
    private Button pathHomeBtn;
    private ObservableList<javafx.scene.Node> btnPaneSetup;
    private ObservableList<javafx.scene.Node> linePaneSetup;

    @FXML
    private Image mapImage;
    @FXML
    public AnchorPane inputPane;
    private DBUser dbuser = DBUser.getInstance();
//    private List<UserFromDB> users = new ArrayList<UserFromDB>();
    private NodeGraph nodeGraph = new NodeGraph();

    /**
     * creates a button associated  with a node
     * adds a line to neighbour nodes
     *
     * @param node
     * @return created NodeButton
     */
    public NodeButton addNodeButton(Node node) {
        NodeButton nb = new NodeButton(node); //create button

        if (!node.getFloor().equals(getCurrFloorVal())) { //make node invisible if not on floor
            nb.setVisible(false);
        }

        btnPane.getChildren().add(nb); //add to page

        translateNodeButton(nb);
        buttonLists.get(node.getFloor()).add(nb);
        //buttons.add(nb);
//        }
        return nb;
    }

    /**
     * creates a line between two nodes
     *
     * @param node1: Node
     * @param node2: Node
     * @return created EdgeLine
     */
    public EdgeLine addEdgeLine(Node node1, Node node2) {
        EdgeLine el = new EdgeLine(node1, node2); //create line
        btnPane.getChildren().add(el); //add line to screen
//        edges.add(el); //not used?
        el.toBack();

        translateEdgeLine(el);//scale edge

        //if not on current floor make invisible/hidden
        if (!node1.getFloor().equals(getCurrFloorVal()) || el.connectsLevels()) {
            el.setVisible(false);
        }
        if (node1.getFloor().equals(node2.getFloor())) { //if floors equal each other
            edgeLists.get(node1.getFloor()).add(el); //add to list of lists
            if ((node1.getFloor().equals(currFloorVal))) {
                edgeLines.add(el); //save for pan and zoom
            }
        }
        return el;
    }

    public void openEdgePopup(double sceneX, double sceneY) {
    }

    ;

    /**
     * finds an edge with given start and end nodes
     *
     * @param start: Node
     * @param end:   Node
     * @return : EdgeLine - edge with corredt start and end else returns null
     */
    public EdgeLine findEdgeLine(Node start, Node end) {
        System.out.println("Finding:  " + start.getName() + " - " + end.getName());
        for (EdgeLine el : edgeLines) {
            if (el.getEndNode() == end && el.getStartNode() == start) {
                System.out.println("found: " + el.getStartNode().getName() + " - " + el.getEndNode());
                return el;
            }
        }
        System.out.println("EDGE NOT FOUND");
        return null;

    }

    public void homeButtonAc(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * selects the given node and deselects previous node
     * @param nb
     */
    public void selectNode(NodeButton nb) {
        if(nodeButtonHold!=null) {
            nodeButtonHold.deselect();
        }
        nodeHold = nb.getNode();
        nodeButtonHold = nb;
        nodeButtonHold.getNode().setIsSelected(true);
        nodeButtonHold.setButtonStyle();
    }

    /**
     * selects given edge and deselects previous edge
     * @param el
     */
    public void selectEdge(EdgeLine el){
        if (edgeHold != null) {
            edgeHold.setSelected(false);
            edgeHold.updateStyle();
        }
        el.setSelected(true);
        el.updateStyle();
        edgeHold = el;
    }


    public void changeFloors(String currFloorVal) {

        //make previous floor buttons hidden
        for (NodeButton nb : buttons) {
            nb.setVisible(false);
        }
        //make previous floor edgelines hidden
        for (EdgeLine el : edgeLines) {
            el.setVisible(false);
        }

        //set current lists of buttons and lines
        buttons = buttonLists.get(currFloorVal);
        edgeLines = edgeLists.get(currFloorVal);

        //make buttons for current floor visible
        for (NodeButton nb : buttons) {
            nb.setVisible(true);
            if (nb.getNode().getWasPathfinding()) {
                System.out.print("\nThis should print each time there is a start/end node on the new floor\n");
                nb.getNode().setIsPathfinding(true);
                nb.setButtonStyle();
                nb.getNode().setIsPathfinding(false);
            }
        }
        for (EdgeLine el : edgeLines) { //set edgelines for new floor visible
            if (!el.connectsLevels()) {
                el.setVisible(true);
            }
        }
        translateGraph(imageView);

        switch (currFloorVal) {
            case "Ground":
                mapImage = new Image(getClass().getResourceAsStream("/edu/wpi/p/fxml/mapsFXML/Maps/00_thegroundfloor.png"));
                break;
            case "L1":
                mapImage = new Image(getClass().getResourceAsStream("/edu/wpi/p/fxml/mapsFXML/Maps/00_thelowerlevel1.png"));
                break;
            case "L2":
                mapImage = new Image(getClass().getResourceAsStream("/edu/wpi/p/fxml/mapsFXML/Maps/00_thelowerlevel2.png"));
                break;
            case "1":
                mapImage = new Image(getClass().getResourceAsStream("/edu/wpi/p/fxml/mapsFXML/Maps/01_thefirstfloor.png"));
                break;
            case "2":
                mapImage = new Image(getClass().getResourceAsStream("/edu/wpi/p/fxml/mapsFXML/Maps/02_thesecondfloor.png"));
                break;
            case "3":
                mapImage = new Image(getClass().getResourceAsStream("/edu/wpi/p/fxml/mapsFXML/Maps/03_thethirdfloor.png"));
                break;
        }
        imageView.setImage(mapImage);

    }

    public void floorInit() {
        floorChoiceBox.setItems(FXCollections.observableArrayList(availableFloors));
        floorChoiceBox.getSelectionModel().select(1);
        buttons = buttonLists.get(availableFloors[1]);
        currFloorVal = floorChoiceBox.getSelectionModel().getSelectedItem();
        floorChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number oldValue, Number newValue) {
                if (!(newValue.intValue() < 0)) {
                    currFloorVal = availableFloors[newValue.intValue()];
                    changeFloors(currFloorVal);
                }
            }
        });
    }

    @FXML
    /**
     * run when page starts
     * adds buttons and edge lines to map
     */
    public void initialize() {
        findTabController.injectMapController(this);
        buttons = new ArrayList<>();
        edgeLines = new ArrayList<>();
        graph = new NodeGraph();

        graph.genGraph(false);

        for (String s : availableFloors) {
            buttonLists.put(s, new ArrayList<NodeButton>());
            edgeLists.put(s, new ArrayList<EdgeLine>());
        }


        floorInit();

        panAndZoomEvents();
    }

    private void panAndZoomEvents() {
        double winWidth = imageView.getFitWidth();
        double imageWidth = imageView.getImage().getWidth();
        scaleX = winWidth / imageWidth;

        double winHeight = imageView.getFitHeight();
        double imageHeight = imageView.getImage().getHeight();
        scaleY = winHeight / imageHeight;

        resetImageView(imageView, imageWidth, imageHeight);

        //pan
        ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

        btnPane.setOnMousePressed(e -> {
            System.out.println("Pressed");
            Point2D mousePress = imageViewToImage(new Point2D(e.getX(), e.getY()));
            mouseDown.set(mousePress);
        });

        btnPane.setOnMouseDragged(e -> {
            Point2D dragPoint = imageViewToImage(new Point2D(e.getX(), e.getY()));
            pan(imageView, dragPoint.subtract(mouseDown.get()));
            mouseDown.set(imageViewToImage(new Point2D(e.getX(), e.getY())));
        });

        //zoom
        btnPane.setOnScroll(e -> {
            //System.out.println("ZOOOOOOOOOOOM");
            double delta = -e.getDeltaY();
            Rectangle2D viewport = imageView.getViewport();

            double scrollMin = Math.min(minZoomPixels / viewport.getWidth(), minZoomPixels / viewport.getHeight());
            double scrollMax = Math.max(imageWidth / viewport.getWidth(), imageHeight / viewport.getHeight());
            double scale = clamp(Math.pow(zoomSpeed, delta), scrollMin, scrollMax);

            Point2D mouse = imageViewToImage(new Point2D(e.getX(), e.getY()));

            double newWidth = viewport.getWidth() * scale;
            double newHeight = viewport.getHeight() * scale;

            // clamp to stop scroll further out than image size
            double newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale,
                    0, imageWidth - newWidth);
            double newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale,
                    0, imageHeight - newHeight);

            System.out.println("minX:"+ newMinX+" ,minY:"+newMinY);
            System.out.println("width:"+ newWidth+ " ,height:"+ newHeight);
            imageView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
            translateGraph(imageView);
        });

        //reset view
        btnPane.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                //System.out.println("Reset");
                resetImageView(imageView, imageWidth, imageHeight);
            }
        });
    }

    private void resetImageView(ImageView imageView, double imageWidth, double imageHeight) {
        imageView.setViewport(new Rectangle2D(0, 0, imageWidth, imageHeight));
        translateGraph(imageView);
    }

    /**
     * Shifts the map
     * @param imageView
     * @param delta change in pan
     */
    private void pan(ImageView imageView, Point2D delta) {
        Rectangle2D viewport = imageView.getViewport();

        double width = imageView.getImage().getWidth();
        double height = imageView.getImage().getHeight();

        double maxX = width - viewport.getWidth();
        double maxY = height - viewport.getHeight();
//        System.out.println(delta.getX());
        double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

        imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));

        translateGraph(imageView);
    }

    private double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    // imageView Point to image Point
    private Point2D imageViewToImage(Point2D imageViewPoint) {
        double xPercent = imageViewPoint.getX() / imageView.getBoundsInLocal().getWidth();
        double yPercent = imageViewPoint.getY() / imageView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(
                viewport.getMinX() + xPercent * viewport.getWidth(),
                viewport.getMinY() + yPercent * viewport.getHeight());
    }

    void translateGraph(ImageView imageView) {
        double scaleX = imageView.getViewport().getWidth() / imageView.getFitWidth();
        double scaleY = imageView.getViewport().getHeight() / imageView.getFitHeight();
        for (NodeButton btn : buttons) {
            btn.pan(imageView);
        }
        for (EdgeLine el : edgeLines) {
            el.pan(imageView);
        }
//        PathTabController pathTabController = new PathTabController();
//        pathTabController.updateAnimatedPath(imageView);
    }

    void translateEdgeLine(EdgeLine el) {
        double scaleX = imageView.getViewport().getWidth() / imageView.getFitWidth();
        double scaleY = imageView.getViewport().getHeight() / imageView.getFitHeight();
        el.pan(imageView);

    }

    //map coords to window coords
    void translateNodeButton(NodeButton btn) {
        double scaleX = imageView.getViewport().getWidth() / imageView.getFitWidth();
        double scaleY = imageView.getViewport().getHeight() / imageView.getFitHeight();
        btn.pan(imageView);
    }

    //window coords to map coords
    double unScaleX(double x) {
        double scaleX = imageView.getViewport().getWidth() / imageView.getFitWidth();
        Rectangle2D viewport = imageView.getViewport();
        return ((x * scaleX) + (viewport.getMinX()));
    }

    double unScaleY(double y) {
        double scaleY = imageView.getViewport().getHeight() / imageView.getFitHeight();
        Rectangle2D viewport = imageView.getViewport();
        return ((y * scaleY) + (viewport.getMinY()));
    }

    double scaleY(double y) {
        double scaleY = imageView.getViewport().getHeight() / imageView.getFitHeight();
        Rectangle2D viewport = imageView.getViewport();
        double newy = (y / scaleY) - viewport.getMinY() / scaleY;
        return newy;
    }

    double scaleX(double x) {
        double scaleX = imageView.getViewport().getWidth() / imageView.getFitWidth();
        Rectangle2D viewport = imageView.getViewport();
        double newx = (x / scaleX) - viewport.getMinY() / scaleX;
        return newx;
    }

    public void centerNode(NodeButton nb){
        centerPoint(nb.getNode().getXcoord(), nb.getNode().getYcoord(), 2.2);
    }

    public void centerPath(List<Node> path){
        double averageX = 0;
        double averageY = 0;
        int amount =0;

        double maxX=path.get(0).getXcoord();
        double maxY=path.get(0).getYcoord();
        double minX=path.get(0).getXcoord();
        double minY=path.get(0).getYcoord();

        //get average location
        for (Node n: path){
            if(n.getFloor().equals(currFloorVal)) {
                if(n.getXcoord()>maxX){
                    maxX=n.getXcoord();
                }
                else if(n.getXcoord()<minX){
                    minX=n.getXcoord();
                }

                if(n.getYcoord()>maxY){
                    maxY=n.getYcoord();
                }
                else if(n.getYcoord()<minY){
                    minY=n.getYcoord();
                }

                averageX += n.getXcoord();
                averageY += n.getYcoord();
                amount++;
            }
        }
        averageX/=amount;
        averageY/=amount;

        double aspectWidth = 5000.0;
        double aspectHeight= 3400.0;

        double pathWidth = maxX-minX;
        double pathHeight = maxY-minY;

        double scaleW = (aspectWidth/(pathWidth+1000)) ;
        scaleW = clamp(scaleW,1,2.127);

        double scaleH = (aspectHeight/(pathHeight +300));
        scaleH = clamp(scaleH,1,2.127);

        System.out.println("\nPATH WIDTH "+(maxX-minX));
        System.out.println("PATH HEIGHT: "+(maxY-minY));
        System.out.println("SCALE WIDTH: "+ scaleW);
        System.out.println("SCALE HEIGHT: "+ scaleH);

        double scale = scaleW;
        if(scaleH<scaleW){
            scale=scaleH;
        }
        System.out.println("SCALE: "+ scale);

        centerPoint(averageX, averageY, scale);
    }

    //map coordinates
    public void centerPoint(double x, double y, double zoom){
//        //zoom in
//        double zoomWidth = 2342;
//        double zoomHeight = 1593;

        //aspect ratio
        double aspectWidth = 5000.0;
        double aspectHeight= 3400;

        //zoom in
        double zoomWidth = aspectWidth/zoom;
        double zoomHeight = aspectHeight/zoom;

        System.out.println("zoomWidth: "+ zoomWidth);
        System.out.println("zoomHeight: "+ zoomHeight);

        Rectangle2D viewport = imageView.getViewport();

        double width = imageView.getImage().getWidth();
        double height = imageView.getImage().getHeight();

        double maxX = width - zoomWidth;
        double maxY = height - zoomHeight;
//        System.out.println(delta.getX());

        //amount to pan
        double panX = clamp(x- (zoomWidth/2), 0, maxX);
        double panY = clamp(y-(zoomHeight/2), 0, maxY);
//        double panX = x- (zoomWidth/2);
//        double panY = y-(zoomHeight/2);

        imageView.setViewport(new Rectangle2D(panX, panY, zoomWidth, zoomHeight));
        translateGraph(imageView); //update buttons

    }

}
