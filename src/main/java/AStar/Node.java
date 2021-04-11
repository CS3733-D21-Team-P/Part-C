package AStar;

import java.util.ArrayList;
import java.util.List;


public class Node {
    private String name;
    private int xCoord;
    private int yCoord;
    private Boolean visited;
    private Boolean blockade; //not used
    private Node parent;
    private float globalDist;
    private float localDist;
    private List<Node> neighbours;

    public Node (String name, int xCoord, int yCoord){
        this.name = name;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.visited = false;
        //Path dist to current
        this.localDist = Float.POSITIVE_INFINITY;
        //Euclidian dist to target + localDist
        this.globalDist = Float.POSITIVE_INFINITY;
        this.neighbours = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
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
}
