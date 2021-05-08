package edu.wpi.p.AStar;

import java.util.List;
import java.util.Stack;

public class DFS extends SearchAlgorithm {

    public List<Node> findPath(Node start, Node end) {
        if(start == null || end == null) {
            System.out.println("null node");
            return null;
        }
        stack = new Stack<>();
        dfs(start, end);
        return getPath(end);
    }

    private void dfs(Node rootNode, Node targetNode) {
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
                    stack.add(n);
                }
            }
        }

        while(!stack.isEmpty()) {
            Node n = stack.firstElement();
            stack.remove(0);

            dfs(n, targetNode);
        }
    }
}
