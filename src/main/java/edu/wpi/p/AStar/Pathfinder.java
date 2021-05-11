package edu.wpi.p.AStar;

import edu.wpi.p.database.DBSettings;

import java.util.List;

public class Pathfinder {
    private NodeGraph nodeGraph;
    private SearchAlgorithm searchAlgorithm;
    private boolean handicapPath = false;
    private DBSettings savedSettings = DBSettings.getInstance();

    public Pathfinder(NodeGraph graph) {
        this.nodeGraph = graph;
        this.searchAlgorithm = new AStar();
    }

    public List<Node> findPath(Node start, Node end) {

        checkSavedAlgorithm();

        List<Node> path = searchAlgorithm.findPath(start, end);
        nodeGraph.resetNodeGraph();
        return path;
    }

    private void checkSavedAlgorithm() {
        String searchMethod = savedSettings.getSetting("searchAlgorithm");
        switch (searchMethod) {
            case "AStar":
                setSearchAStar();
                break;
            case "DFS":
                setSearchDFS();
                break;
            case "BFS":
                setSearchBFS();
                break;
            case "Greedy":
                setSearchGreedy();
                break;
            case "Dijkstra":
                setSearchDijkstra();
                break;
            default:
                setSearchAStar();
                break;
        }
    }

    public void setHandicapPath(boolean active) {
        handicapPath = active;
        nodeGraph.setTypeBlockade("STAI", active);
    }

    public boolean isHandicapPath() {
        return handicapPath;
    }

    public void setSearchDFS() {
        searchAlgorithm = new DFS();
        System.out.println("setSearchDFS");
    }

    public void setSearchBFS() {
        searchAlgorithm = new BFS();
        System.out.println("setSearchBFS");
    }

    public void setSearchGreedy() {
        searchAlgorithm = new Greedy();
        System.out.println("setSearchGreedy");
    }

    public void setSearchDijkstra() {
        searchAlgorithm = new Dijkstra();
        System.out.println("setSearchDijkstra");
    }

    public void setSearchAStar() {
        searchAlgorithm = new AStar();
        System.out.println("setSearchAStar");
    }
}
