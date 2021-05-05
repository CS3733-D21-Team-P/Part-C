package edu.wpi.p.AStar;

import java.util.List;

public class Pathfinder {
    NodeGraph nodeGraph;
    SearchAlgorithm searchAlgorithm;

    public Pathfinder(NodeGraph graph) {
        this.nodeGraph = graph;
        this.searchAlgorithm = new AStar();
    }

    public List<Node> findPath(Node start, Node end) {
        List<Node> path = searchAlgorithm.findPath(start, end);
        nodeGraph.resetNodeGraph();
        return path;
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
