package edu.wpi.p.AStar;

import java.util.*;

public abstract class SearchAlgorithm {
    Stack<Node> stack = new Stack<>();

    //returns null if no path found
    abstract List<Node> findPath(Node start, Node end);

    protected List<Node> getPath(Node endNode) {
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

    static void printPath(List<Node> path) {
        if(path != null) {
            System.out.println("Path: ");
            for (Node n : path) {
                System.out.print(n.getName() + " - ");
            }
            System.out.println();
        }
    }

    protected float dist(Node a, Node b) {
        return (float) Math.hypot(
                Math.abs(a.getXcoord() - b.getXcoord()),
                Math.abs(a.getYcoord() - b.getYcoord()));
    }

    protected void sortDist(Stack stack) {
        Node[] stackArray = new Node[stack.size()];
        stack.copyInto(stackArray);
        quicksort(stackArray, 0, stackArray.length - 1);
        stack.empty();
        stack = new Stack<>();
        for(int i = stackArray.length - 1; i >= 0; i--) {
            stack.push(stackArray[i]);
        }
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
