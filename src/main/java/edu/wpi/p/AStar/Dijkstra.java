package edu.wpi.p.AStar;

import java.util.List;
import java.util.Stack;

public class Dijkstra extends SearchAlgorithm {

    private float pathLength;

    public Dijkstra() {
        this.stack = new Stack<>();
        this.pathLength = Float.POSITIVE_INFINITY;
    }

    public List<Node> findPath(Node start, Node end) {
        return findShortestPath(start, end);
    }

    private List<Node> findShortestPath(Node rootNode, Node targetNode) {

        this.pathLength = Float.POSITIVE_INFINITY;

        if(rootNode == null || targetNode == null) {
            System.out.println("null node");
            return null;
        }

        stack = new Stack<>();

        //set start
        rootNode.setLocalDist(0);
        rootNode.setGlobalDist(dist(rootNode, targetNode));

        //recursive search
        search(rootNode, targetNode);
        System.out.println("DONE");

        return getPath(targetNode);
    }

    private void search(Node rootNode, Node targetNode) {

        rootNode.setVisited(true);
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(rootNode.getName() + " Local Dist: " + rootNode.getLocalDist() + " PathLength = " + pathLength);
        System.out.println(" ");
        System.out.println(" ");

        //no need to stick around if we are the target node
        if(rootNode == targetNode || rootNode.getLocalDist() >= pathLength) {
            if(!stack.isEmpty()) {
                //removing this line will stop the search immediately if target is found
                search(stack.pop(), targetNode);
            }
            return;
        }

        //search all neighbours
        for(Node n : rootNode.getNeighbours()) {
            if(!n.getBlockade()) {
                float newLocalDist = rootNode.getLocalDist() + dist(rootNode, n);
                System.out.println(rootNode.getName() + " - " + n.getName() + ": " + newLocalDist);

                if (newLocalDist < n.getLocalDist()) {
                    //found shorter route to node
                    n.setParent(rootNode);
                    n.setLocalDist(newLocalDist);
                    n.setGlobalDist(dist(n, targetNode) + n.getLocalDist());

                    if (!n.getVisited() && n.getGlobalDist() < this.pathLength) {
                        if (n == targetNode) {
                            this.pathLength = targetNode.getGlobalDist();
                            System.out.println("Path Found - Length:" + this.pathLength);
                        }
                        stack.push(n);
                        System.out.println("Add: " + n.getName());
                    }
                }
            }
        }

        while(!stack.isEmpty()) {
            Node first = stack.firstElement();
            stack.remove(0);

            for(Node n : stack) {
                System.out.print(n.getName() + " ");
            }

            search(first, targetNode);
        }
    }
}
