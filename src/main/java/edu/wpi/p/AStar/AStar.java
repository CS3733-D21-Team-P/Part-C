package edu.wpi.p.AStar;

import java.util.*;

public class AStar extends SearchAlgorithm {

    private float pathLength;

    public AStar() {
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

        System.out.println("END");
        System.out.println("DONE");

        return getPath(targetNode);
    }

    private void search(Node rootNode, Node targetNode) {

        rootNode.setVisited(true);
        System.out.println(rootNode.getName());

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

            float newLocalDist = rootNode.getLocalDist() + dist(rootNode, n);
            System.out.println(rootNode.getName() + " - " + n.getName() + ": " + newLocalDist);

            if(newLocalDist < n.getLocalDist()) {
                //found shorter route to node
                n.setParent(rootNode);
                n.setLocalDist(newLocalDist);
                n.setGlobalDist(dist(n, targetNode) + n.getLocalDist());

                if(n.getGlobalDist() < this.pathLength) {
                    if(n == targetNode) {
                        this.pathLength = targetNode.getGlobalDist();
                        System.out.println("Path Found - Length:" + this.pathLength);
                        printPath(getPath(targetNode));
                    } else if (!n.getVisited()) {
                        stack.push(n);
                        System.out.println("Add: " + n.getName());
                    }
                }
            }
        }

        if(!stack.isEmpty()) {
            System.out.println("Stack: ");
            for(Node n : stack) {
                System.out.print(n.getName() + " ");
            }
            System.out.println();

            if(stack.size() > 1) {
                //sort by globalDist
                sortDist();

                System.out.println("Sorted Stack: ");
                for(Node n : stack) {
                    System.out.print(n.getName() + "[" + n.getGlobalDist() + "] ");
                }
                System.out.println();

            }

            //down we go
            search(stack.pop(), targetNode);
        }
    }

    private void sortDist() {
        Node[] stackArray = new Node[stack.size()];
        stack.copyInto(stackArray);
        quicksort(stackArray, 0, stackArray.length - 1);
        stack.empty();
        stack = new Stack<>();
        for(int i = stackArray.length - 1; i >= 0; i--) {
            stack.push(stackArray[i]);
        }
    }
}
