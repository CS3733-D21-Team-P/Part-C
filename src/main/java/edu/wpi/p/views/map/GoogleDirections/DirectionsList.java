package edu.wpi.p.views.map.GoogleDirections;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.views.map.DirectionTableEntry;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class DirectionsList extends JFXTreeTableView {
    private JFXTreeTableColumn<DirectionTableEntry, ImageView> directionImageView;
    private JFXTreeTableColumn<DirectionTableEntry, Label> directionText;

    public DirectionsList(){
        //setup DirectionsTable
        directionImageView = new JFXTreeTableColumn<>("icon");
        directionImageView.setPrefWidth(50);
        directionImageView.setSortable(false);
        directionImageView.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DirectionTableEntry, ImageView>, ObservableValue<ImageView>>() {
            public ObservableValue<ImageView> call(TreeTableColumn.CellDataFeatures<DirectionTableEntry, ImageView> p) {
                return new SimpleObjectProperty(p.getValue().getValue().getImageVew());
            }
        });

        directionText = new JFXTreeTableColumn<>("instruction");
        directionText.setPrefWidth(205);
        directionText.setSortable(false);
        directionText.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DirectionTableEntry, Label>, ObservableValue<Label>>() {
            public ObservableValue<Label> call(TreeTableColumn.CellDataFeatures<DirectionTableEntry, Label> p) {
                Label l = new Label(p.getValue().getValue().getInstruction());
                l.setWrapText(true);
                return new SimpleObjectProperty(l);
            }
        });

        this.setPlaceholder(new Label(""));
        AnchorPane.setRightAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setTopAnchor(this, 50.0);
        AnchorPane.setBottomAnchor(this, 0.0);

        this.setStyle("-fx-background-color:  #b6d6f2;" +
                "-fx-background-radius:  15px");

//        this.widthProperty().addListener(new ChangeListener<Number>()
//        {
//            @Override
//            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth)
//            {
//                System.out.println("changed table");
//                //Don't show header
////                Pane header = (Pane) this.lookup("TableHeaderRow");
////                if (header.isVisible()){
////                    header.setMaxHeight(0);
////                    header.setMinHeight(0);
////                    header.setPrefHeight(0);
////                    header.setVisible(false);
////                }
//            }
//        });

    }


    public void updateDirectionsTable(List<DirectionTableEntry> directions) {
        //Add instructions to DirectionsTable
        ObservableList<DirectionTableEntry> tableDirections = FXCollections.observableArrayList();
        for (DirectionTableEntry entry : directions) {
            tableDirections.add(entry);
        }

        final TreeItem<DirectionTableEntry> root = new RecursiveTreeItem<>(tableDirections, RecursiveTreeObject::getChildren);
        this.getColumns().setAll(directionText, directionImageView);
        this.setRoot(root);
        this.setShowRoot(false);

        Pane header =(Pane) this.lookup("TableHeaderRow");
        if(header!=null && header.isVisible()) {
            header.setMaxHeight(0);
            header.setMinHeight(0);
            header.setPrefHeight(0);
            header.setVisible(false);
            header.setManaged(false);
        }
    }

    public List<DirectionTableEntry> makeGoogleDirections(List<String> directionsText) {
        List<DirectionTableEntry> tableEntryDirections = new ArrayList<>();

        for(String s: directionsText){
            tableEntryDirections.add(getTableInstruction(s));
        }
        return tableEntryDirections;
    }

    private DirectionTableEntry getTableInstruction(String text) {
        ImageView imgView = new ImageView();
        imgView.setFitWidth(50);
        imgView.setFitHeight(50);

        if(text.contains("right")){
            imgView.setImage(new Image("edu/wpi/p/fxml/image/icons/arrow_turn_right.png"));
        }
        else if(text.contains("left")){
            imgView.setImage(new Image("edu/wpi/p/fxml/image/icons/arrow_turn_left.png"));
        }
        return new DirectionTableEntry(imgView, text);
    }
}
