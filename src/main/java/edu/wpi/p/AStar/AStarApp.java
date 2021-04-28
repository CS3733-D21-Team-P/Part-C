package edu.wpi.p.AStar;

import java.io.FileNotFoundException;
import java.util.List;

public class AStarApp {
    public static void main(String[] args) throws FileNotFoundException {

        NodeGraph graph = new NodeGraph();
        graph.genGraph(false);

        AStar search = new AStar();
        List<Node> path = search.findShortestPath(graph.getNodeByName("Medical Records Conference Room Floor L1"), graph.getNodeByName("Vending Machine 1 L1"));

        if(path != null) {
            System.out.println("Path: ");
            for (Node n : path) {
                System.out.print(n.getName() + " - ");
            }
            System.out.println();
        }

        //reset node goals after search
        graph.resetNodeGraph();
    }
}
