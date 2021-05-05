package edu.wpi.p.views.custom;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.util.List;

public class FlexiblePane extends Pane {

    private int numColumns;
    private int numRows;
    private double columnWidth;
    private double columnPadding;
    private double rowHeight;
    private double rowPadding;
    public FlexiblePane() {
        super();
//        this.setMaxSize(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
//        this.setMinSize(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

    }

    public int getNumColumns() {
        requestLayout();
        return numColumns;
    }

    public void setNumColumns(int numColumns) {
        requestLayout();
        this.numColumns = numColumns;
    }

    @Override
    protected double computePrefWidth(double height) {
//        System.out.println("computing pref width, height is: " + height);
        this.getParent().boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
            requestLayout();
        });
        this.getParent().boundsInLocalProperty().addListener((observable, oldValue, newValue) -> {
            requestLayout();
        });

        return numColumns * columnWidth + (numColumns + 1) * columnPadding;
    }

    @Override
    protected double computePrefHeight(double width) {
//        System.out.println("computing pref height, width is: " + width);
        double ourHeight = this.getParent().getLayoutBounds().getHeight();
        rowHeight = maxChildHeight();
        rowPadding = (ourHeight - (numRows * rowHeight)) / (numRows + 1);
        int numRows = (int) Math.ceil(getManagedChildren().size() / 2.0);
        return rowPadding + numRows * (rowHeight + rowPadding);
    }

    private double maxChildWidth() {
        double width = 0;
        for (Node child : getManagedChildren()) {
            width = Math.max(child.prefWidth(-1), width);
        }
        return width;
    }
    private double maxChildHeight() {
        double height = 0;
        for (Node child : getManagedChildren()) {
            height = Math.max(child.prefHeight(-1), height);
        }
        return height;
    }


    @Override
    protected void layoutChildren() {
//        System.out.println("Flexible pane: layout children");
        List<Node> children = getManagedChildren();
        double ourWidth = this.getParent().getLayoutBounds().getWidth();
        double ourHeight = this.getBoundsInLocal().getHeight();//this.getParent().getLayoutBounds().getHeight();
//        System.out.println("our width: " + ourWidth);
//        System.out.println("our height: " + ourHeight);
//        System.out.println("number of columns: " + numColumns);
        int numChildren = children.size();
        int numRows = (int) Math.ceil(getManagedChildren().size() / 2.0);
//        System.out.println("numChildren: " + numChildren);
        columnWidth = maxChildWidth();
        rowHeight = maxChildHeight();
//        System.out.println("rowHeight: " + rowHeight);
//        System.out.println("columnWidth: " + columnWidth);

        int row = 0;
        int col = 0;
        double totalPadding = ourWidth - (numColumns * columnWidth);
        columnPadding = totalPadding / (numColumns + 1);
        rowPadding = (ourHeight - (numRows * rowHeight)) / (numRows + 1);
//        System.out.println("rowPadding: " + rowPadding);
        for (Node child: children) {
            double x = columnPadding + col * (columnWidth + columnPadding);
            double y = rowPadding + row * (rowHeight + rowPadding);
            System.out.println("setting child x: " + x + " y: " + y);
            child.resizeRelocate(x, y, columnWidth, rowHeight);
            col += 1;
            if (col == numColumns) {
                col = 0;
                row++;
            }
        }
    }
}
