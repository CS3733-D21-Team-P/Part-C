package edu.wpi.p.AStar;

import edu.wpi.p.database.DBRow;

import java.util.ArrayList;
import java.util.List;


public class Node extends DBRow {
    private String nameCol = "LONGNAME";
    private String idCol = "NODEID";
    private String xcoordCol = "XCOORD";
    private String ycoordCol = "YCOORD";
    private String floorCol = "FLOOR";
    private String buildingCol = "BUILDING";
    private String nodeTypeCol = "NODETYPE";
    private String shortNameCol = "SHORTNAME";

    private String name;
    private String id;
    private int xcoord;
    private int ycoord;
    private Boolean visited;
    private Boolean blockade; //not used
    private Node parent;
    private float globalDist;
    private float localDist;
    private String floor;
    private String type;
    private String building;
    private String shortName;
    private List<Node> neighbours;

    public Node() {
        super();
    }
    public Node (String id, String name, int xcoord, int ycoord){
        super();
        this.addValue(nameCol, name);
        this.addValue(nameCol, name);
        this.addValue(idCol, id);
        this.addValue(xcoordCol, xcoord);
        this.addValue(ycoordCol, ycoord);
        this.visited = false;
        resetGoals();
        this.neighbours = new ArrayList<>();
    }

    public Node(String name, String id, int xcoord, int ycoord, String floor, String building, String type, String shortName) {
        super();
        this.addValue(nameCol, name);
        this.addValue(idCol, id);
        this.addValue(xcoordCol, xcoord);
        this.addValue(ycoordCol, ycoord);
        this.addValue(floorCol, floor);
        this.addValue(buildingCol, building);
        this.addValue(nodeTypeCol, type);
        this.addValue(shortNameCol, shortName);
        this.visited = false;
        resetGoals();
        this.neighbours = new ArrayList<>();
    }

    public void resetGoals() {
        //Path dist to current
        this.localDist = Float.POSITIVE_INFINITY;
        //Euclidean dist to target + localDist
        this.globalDist = Float.POSITIVE_INFINITY;
    }

    public String getName() {
        return (String) this.getValue(nameCol);
    }

    public void setName(String name) {
        this.changeValue(nameCol, name);
    }

    public String getId() {
        return (String) this.getValue(idCol);
    }

    public void setId(String id) {
        this.changeValue(idCol, id);
    }

    public int getXcoord() {
        return (int) this.getValue(xcoordCol);
    }

    public void setXcoord(int xcoord) {
        this.changeValue(xcoordCol, xcoord);
    }

    public int getYcoord() {
        return (int) this.getValue(ycoordCol);
    }

    public void setYcoord(int ycoord) {
        this.changeValue(ycoordCol, ycoord);
    }

    public Boolean getVisited() {
        return visited;
    }

    public void setVisited(Boolean visited) {
        this.visited = visited;
    }

    public Boolean getBlockade() {
        return blockade;
    }

    public void setBlockade(Boolean blockade) {
        this.blockade = blockade;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public float getLocalDist() {
        return localDist;
    }

    public void setLocalDist(float localDist) {
        this.localDist = localDist;
    }

    public float getGlobalDist() {
        return globalDist;
    }

    public void setGlobalDist(float globalDist) {
        this.globalDist = globalDist;
    }

    public List<Node> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<Node> neighbours) {
        this.neighbours = neighbours;
    }

    public void addNeighbour(Node neighbour) {
        this.neighbours.add(neighbour);
    }

    public String getFloor() {
        return (String) this.getValue(floorCol);
    }

    public void setFloor(String floor) {
        this.changeValue(floorCol, floor);
    }

    public String getType() {
        return (String) this.getValue(nodeTypeCol);
    }

    public void setType(String type) {
        this.changeValue(nodeTypeCol, type);
    }

    public String getBuilding() {
        return (String) this.getValue(buildingCol);
    }

    public void setBuilding(String building) {
        this.changeValue(buildingCol, building);
    }

    public String getShortName() {
        return (String) this.getValue(shortNameCol);
    }

    public void setShortName(String shortName) {
        this.changeValue(shortNameCol, shortName);
    }
}
