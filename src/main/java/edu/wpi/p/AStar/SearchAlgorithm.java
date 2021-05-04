package edu.wpi.p.AStar;

import java.util.*;

public abstract class SearchAlgorithm {
    Stack<Node> stack = new Stack<>();
    static int stairFloorCost = 400;
    static int elevatorFloorCost = 400;

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
        float cost = (float) Math.hypot(
                Math.abs(a.getXcoord() - b.getXcoord()),
                Math.abs(a.getYcoord() - b.getYcoord()));

        if(!a.getFloor().equals(b.getFloor())) {
            System.out.println("FLOOR CHANGE");
            if(a.getType().equals("STAI") || b.getType().equals("STAI")) {
                cost += stairFloorCost;
                System.out.println("stai change");
            }
            if(a.getType().equals("ELEV") || b.getType().equals("ELEV")) {
                cost += elevatorFloorCost;
                System.out.println("elev change");
            }
        }

        return cost;
    }

    protected void quicksort(Node[] array, int low, int high) {
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
