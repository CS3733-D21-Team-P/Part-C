package AStar;

import java.util.*;

public class AStar {

    private Stack<Node> stack;
    private float pathLength;

    public AStar() {
        this.stack = new Stack<>();
        this.pathLength = Float.POSITIVE_INFINITY;
    }

    public List<Node> findShortestPath(Node rootNode, Node targetNode) {

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

        List<Node> path = getPath(targetNode);
        if(path != null) {
            System.out.println("Path: ");
            for (Node n : path) {
                System.out.print(n.getName() + " - ");
            }
            System.out.println();
        }

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

                if(!n.getVisited() && n.getGlobalDist() < this.pathLength) {
                    if(n == targetNode) {
                        this.pathLength = targetNode.getGlobalDist();
                        System.out.println("Path Found - Length:" + this.pathLength);
                    }
                    stack.push(n);
                    System.out.println("Add: " + n.getName());
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
                Node[] stackArray = new Node[stack.size()];
                stack.copyInto(stackArray);
                quicksort(stackArray, 0, stackArray.length - 1);

                //TODO: there should be a better way to do this
                stack.empty();
                stack = new Stack<>();
                for(int i = stackArray.length - 1; i >= 0; i--) {
                    stack.push(stackArray[i]);
                }

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

    private List<Node> getPath(Node endNode) {
        List<Node> path = new ArrayList<>();
        Node currentNode = endNode;
        while(currentNode.getParent() != null) {
            path.add(currentNode);
            currentNode = currentNode.getParent();
        }
        path.add(currentNode);

        Collections.reverse(path);
        return path;
    }

    private float dist(Node a, Node b) {
        return (float) Math.hypot(
                Math.abs(a.getXcoord() - b.getXcoord()),
                Math.abs(a.getYcoord() - b.getYcoord()));
    }

    private void quicksort(Node[] array, int low, int high) {
        //check for items left to sort
        if(low < high+1) {
            //get new pivot
            int pivot = partition(array, low, high);
            //sort partitions
            quicksort(array, low, pivot-1);
            quicksort(array, pivot+1, high);
        }
    }

    private int partition(Node[] array, int low, int high) {
        //get pivot, move it to low
        swap(array, low, getPivot(low, high));
        //get item right of pivot
        int p = low + 1;
        for(int i = p; i <= high; i++) {
            if(array[i].getGlobalDist() < array[low].getGlobalDist()) {
                swap(array, i, p++);
            }
        }
        swap(array, low, p-1);
        //return pivot
        return p-1;
    }

    private void swap(Node[] array, int i, int j) {
        Node temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private int getPivot(int low, int high) {
        Random rand = new Random();
        return rand.nextInt((high - low) + 1) + low;
    }
}
