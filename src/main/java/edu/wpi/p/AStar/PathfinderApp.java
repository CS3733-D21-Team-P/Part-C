package edu.wpi.p.AStar;
import java.nio.file.Path;
import java.util.List;

public class PathfinderApp {
    public static void main(String[] args) {

        NodeGraph graph = new NodeGraph();
        graph.genGraph(false);

        Pathfinder pathfinder = new Pathfinder(graph);
        //List<Node> path = pathfinder.findPath(graph.getNodeByName("Medical Records Conference Room Floor L1"), graph.getNodeByName("Vending Machine 1 L1"));
        List<Node> path;

        pathfinder.setSearchBFS();
        path = pathfinder.findPath(graph.getNodeByName("A"), graph.getNodeByName("D"));
        SearchAlgorithm.printPath(path);

        pathfinder.setSearchDFS();
        path = pathfinder.findPath(graph.getNodeByName("A"), graph.getNodeByName("D"));
        SearchAlgorithm.printPath(path);

        pathfinder.setSearchGreedy();
        path = pathfinder.findPath(graph.getNodeByName("A"), graph.getNodeByName("D"));
        SearchAlgorithm.printPath(path);

        pathfinder.setSearchDijkstra();
        path = pathfinder.findPath(graph.getNodeByName("A"), graph.getNodeByName("D"));
        SearchAlgorithm.printPath(path);

        pathfinder.setSearchAStar();
        path = pathfinder.findPath(graph.getNodeByName("A"), graph.getNodeByName("D"));
        SearchAlgorithm.printPath(path);
    }
}
