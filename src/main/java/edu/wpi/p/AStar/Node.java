package edu.wpi.p.AStar;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.util.ArrayList;
import java.util.List;


public class Node extends RecursiveTreeObject<Node> {
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

    public Node (String id, String name, int xcoord, int ycoord){
        this.name = name;
        this.id = id;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.visited = false;
        resetGoals();
        this.neighbours = new ArrayList<>();
    }

    public Node(String name, String id, int xcoord, int ycoord, String floor, String building, String type, String shortName) {
        this.name = name;
        this.id = id;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.floor = floor;
        this.building = building;
        this.type = type;
        this.shortName = shortName;
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getXcoord() {
        return xcoord;
    }

    public void setXcoord(int xcoord) {
        this.xcoord = xcoord;
    }

    public int getYcoord() {
        return ycoord;
    }

    public void setYcoord(int ycoord) {
        this.ycoord = ycoord;
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
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
