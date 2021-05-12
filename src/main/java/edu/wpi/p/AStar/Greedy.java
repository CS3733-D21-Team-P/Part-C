package edu.wpi.p.AStar;

import java.util.List;
import java.util.Stack;

public class Greedy extends SearchAlgorithm {

    public List<Node> findPath(Node start, Node end) {

        if(start == null || end == null) {
            System.out.println("null node");
            return null;
        }

        stack = new Stack<>();
        greedy(start, end);
        return getPath(end);
    }

    private void greedy(Node rootNode, Node targetNode) {
        rootNode.setVisited(true);
        System.out.println("visit: "+rootNode.getName());

        //we are target node
        if(rootNode == targetNode) {
            stack.clear();
            return;
        }

        //Search other nodes
        if(!rootNode.getNeighbours().isEmpty()) {
            for (Node n : rootNode.getNeighbours()) {
                if(!n.getVisited() && !n.getBlockade()) {
                    //add to stack
                    n.setVisited(true);
                    n.setParent(rootNode);
                    System.out.println(rootNode.getName() + " parent of " + n.getName());
                    n.setGlobalDist(dist(n, targetNode));
                    stack.add(n);
                }
            }
        }

        while(!stack.isEmpty()) {

            //sort by globalDist
            sortDist();

            Node n = stack.firstElement();
            stack.remove(0);

            greedy(n, targetNode);
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
