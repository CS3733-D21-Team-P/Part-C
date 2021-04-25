package edu.wpi.p.database;

public class Edge extends DBRow{
    private String edgeIDCol = "EDGEID";
    private String startNodeCol = "STARTNODE";
    private String endNodeCol = "ENDNODE";

    public Edge() {
        super();
    }
    public Edge(String edgeID, String startNode, String endNode) {
        super();
        this.addValue(edgeIDCol, edgeID);
        this.addValue(startNodeCol, startNode);
        this.addValue(endNodeCol, endNode);
    }

    public String getEdgeID() {
        return this.getValue(edgeIDCol);
    }

    public void setEdgeID(String edgeID) {
        this.changeValue(edgeIDCol, edgeID);
    }

    public String getStartNode() {
        return this.getValue(startNodeCol);
    }

    public void setStartNode(String startNode) {
        this.changeValue(startNodeCol, startNode);
    }

    public String getEndNode() {
        return this.getValue(endNodeCol);
    }

    public void setEndNode(String endNode) {
        this.changeValue(endNodeCol, endNode);
    }

    public String toString() {
        return edgeIDCol + ": " + this.getEdgeID() + ", "
                + startNodeCol + ": " + this.getStartNode() + ", "
                + endNodeCol + ": " + this.getEndNode();
    }
}
