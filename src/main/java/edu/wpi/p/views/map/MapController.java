package edu.wpi.p.views.map;

import edu.wpi.p.AStar.EdgeLine;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.AStar.NodeButton;
import edu.wpi.p.AStar.NodeGraph;
import edu.wpi.p.App;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class MapController {
    NodeGraph graph = new NodeGraph();
    List<NodeButton> buttons = new ArrayList<>();
    List<EdgeLine> edgeLines = new ArrayList<>();

    private double zoomSpeed = 1.005;
    private double minZoomPixels = 800;

    double scaleX;
    double scaleY;

    public String getCurrFloorVal() {
        return currFloorVal;
    }

    private String currFloorVal;

    @FXML public AnchorPane btnPane;
    @FXML public AnchorPane linePane;
    @FXML public ImageView imageView;
    @FXML private ChoiceBox<String> floorChoiceBox;
    @FXML private Button pathHomeBtn;
    private ObservableList<javafx.scene.Node> btnPaneSetup;
    private ObservableList<javafx.scene.Node> linePaneSetup;

    @FXML private Image mapImage;
    @FXML public AnchorPane inputPane;

    /**
     * creates a button associated  with a node
     * adds a line to neighbour nodes
     * @param node
     * @return created NodeButton
     */
    public NodeButton addNodeButton(Node node){
        NodeButton nb = new NodeButton(node); //create button
        if (node.getFloor().equals(getCurrFloorVal())) {

            btnPane.getChildren().add(nb); //add to page

            //add edges
            List<Node> children = node.getNeighbours();
            for (Node n : children) {
                EdgeLine el = addEdgeLine(node, n);
                nb.addLine(el);
                edgeLines.add(el);
            }
            translateNodeButton(nb);
            buttons.add(nb);
        }
        return nb;
    }

    /**
     * creates a line between two nodes
     * @param node1: Node
     * @param node2: Node
     * @return created EdgeLine
     */
    public EdgeLine addEdgeLine(Node node1, Node node2){
        EdgeLine el = new EdgeLine(node1, node2); //create line
        edgeLines.add(el); //save for pan and zoom
        linePane.getChildren().add(el); //add line to screen
        translateEdgeLine(el);
        return el;
    }

    /**
     * finds an edge with given start and end nodes
     * @param start: Node
     * @param end: Node
     * @return : EdgeLine - edge with corredt start and end else returns null
     */
    public EdgeLine findEdgeLine(Node start, Node end){
        System.out.println("Finding:  "+ start.getName()+ " - "+ end.getName());
        for(EdgeLine el: edgeLines){
            if(el.getEndNode() == end && el.getStartNode()== start){
                System.out.println("found: "+el.getStartNode().getName()+ " - " +el.getEndNode());
                return el;
            }
        }
        System.out.println("EDGE NOT FOUND");
        return null;

    }

    public void homeButtonAc(ActionEvent actionEvent){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void changeFloors(){
        final String[] availableFloors = new String[]{"Ground", "L1","L2","1","2","3"};
        floorChoiceBox.setItems(FXCollections.observableArrayList(availableFloors));
        floorChoiceBox.getSelectionModel().select(1);
        currFloorVal = floorChoiceBox.getSelectionModel().getSelectedItem();
        floorChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number oldValue, Number newValue) {
                currFloorVal = availableFloors[newValue.intValue()];
                btnPane.getChildren().clear();
                linePane.getChildren().clear();
                //btnPane.getChildren().add(pathHomeBtn);
                //buttons.clear();
                edgeLines= new ArrayList<>();
                buttons= new ArrayList<>();
                for (Node n: graph.getGraph()){
                    addNodeButton(n);
                }
                //translateGraph(imageView);
                switch (currFloorVal) {
                    case "Ground":
                        mapImage = new Image(getClass().getResourceAsStream("/edu/wpi/p/fxml/Maps/00_thegroundfloor.png"));
                        break;
                    case "L1":
                        mapImage = new Image(getClass().getResourceAsStream("/edu/wpi/p/fxml/Maps/00_thelowerlevel1.png"));
                        break;
                    case "L2":
                        mapImage = new Image(getClass().getResourceAsStream("/edu/wpi/p/fxml/Maps/00_thelowerlevel2.png"));
                        break;
                    case "1":
                        mapImage = new Image(getClass().getResourceAsStream("/edu/wpi/p/fxml/Maps/01_thefirstfloor.png"));
                        break;
                    case "2":
                        mapImage = new Image(getClass().getResourceAsStream("/edu/wpi/p/fxml/Maps/02_thesecondfloor.png"));
                        break;
                    case "3":
                        mapImage = new Image(getClass().getResourceAsStream("/edu/wpi/p/fxml/Maps/03_thethirdfloor.png"));
                        break;
                }
                imageView.setImage(mapImage);
            }
        });
    }

    @FXML
    /**
     * run when page starts
     * adds buttons and edge lines to map
     */
    public void initialize()  {

        buttons = new ArrayList<>();
        edgeLines = new ArrayList<>();
        graph = new NodeGraph();

        graph.genGraph(false);

        changeFloors();

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

            double scrollMin = Math.min( minZoomPixels / viewport.getWidth(), minZoomPixels / viewport.getHeight());
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

    private void pan(ImageView imageView, Point2D delta) {
        Rectangle2D viewport = imageView.getViewport();

        double width = imageView.getImage().getWidth();
        double height = imageView.getImage().getHeight();

        double maxX = width - viewport.getWidth();
        double maxY = height - viewport.getHeight();

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
        for(NodeButton btn : buttons) {
            btn.pan(imageView);
        }
        for(EdgeLine el : edgeLines) {
            el.pan(imageView);
        }
    }

    void translateEdgeLine(EdgeLine el){
        double scaleX = imageView.getViewport().getWidth() / imageView.getFitWidth();
        double scaleY = imageView.getViewport().getHeight() / imageView.getFitHeight();
        el.pan(imageView);

    }

    //map coords to window coords
    void translateNodeButton(NodeButton btn){
        double scaleX = imageView.getViewport().getWidth() / imageView.getFitWidth();
        double scaleY = imageView.getViewport().getHeight() / imageView.getFitHeight();
        btn.pan(imageView);
    }

    //window coords to map coords
    double unScaleX(double x){
        double scaleX = imageView.getViewport().getWidth() / imageView.getFitWidth();
        Rectangle2D viewport = imageView.getViewport();
        return ((x*scaleX) +(viewport.getMinX()));
    }

    double unScaleY(double y){
        double scaleY = imageView.getViewport().getHeight() / imageView.getFitHeight();
        Rectangle2D viewport = imageView.getViewport();
        return ((y*scaleY)+(viewport.getMinY()));
    }
}