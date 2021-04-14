package edu.wpi.p.views;

import javafx.beans.property.SimpleStringProperty;
public class EdgeData {
    SimpleStringProperty edgeID;
    SimpleStringProperty startNode;
    SimpleStringProperty endNode;
    EdgeData(String edgeID, String startNode, String endNode) {
        this.edgeID = new SimpleStringProperty(edgeID);
        this.startNode = new SimpleStringProperty(startNode);
        this.endNode = new SimpleStringProperty(endNode);
    }
    public String getEdgeID(){
        return edgeID.get();
    }
    public void setEdgeID(String fname){
        edgeID.set(fname);
    }
    public String getStartNode(){
        return startNode.get();
    }
    public void setEndNode(String fpath){
        startNode.set(fpath);
    }
    public String getEndNode() {
        return endNode.get();
    }
    public String getSize(){
        return endNode.get();
    }
    public void setSize(String fsize){
        endNode.set(fsize);
    }
}