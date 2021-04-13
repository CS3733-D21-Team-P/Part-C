package edu.wpi.p.database;

import AStar.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DBTable {
    private String name;
    private List<DBColumn> columns;
    private List<Node> nodeList;
    private List<List<String>> edgeData;


//    public DBTable(String name, List<DBColumn> columns) {
//        this.name = name;
//        this.columns = columns;
//    }

    public void updateNode(int nodeID, Node newNode) {

    }

    public void addNode(Node newNode) {

    }

    public void addEdge(Node node1, Node node2) {

    }

    public void removeEdge(Node node1, Node node2) {

    }

    public void removeNode(Node node) {

    }

    public List<Node> getNodes() {
        //parse L1Nodes
        List<List<String>> nodeData = new ArrayList<>();
        this.nodeList = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File("src/main/java/AStar/L1Nodes.csv"))) {
            while (scanner.hasNextLine()) {
                nodeData.add(getNodeString(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //L1Nodes columns
        int nodeID = 0;
        int xcoord = 1;
        int ycoord = 2;
        int floor = 3;
        int building = 4;
        int nodeType = 5;
        int longName = 6;
        int shortName = 7;

        //create nodes
        for(int i = 1; i < nodeData.size(); i++) {
            List<String> nodeString = nodeData.get(i);

            Node node = new Node(
                    nodeString.get(nodeID),
                    nodeString.get(longName),
                    Integer.parseInt(nodeString.get(xcoord)),
                    Integer.parseInt(nodeString.get(ycoord)));
            this.nodeList.add(node);
        }

        return nodeList;
    }

    public List<List<String>> getEdgeData() {
        //parse L1Edges
        this.edgeData = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("src/main/java/AStar/L1Edges.csv"))) {
            while (scanner.hasNextLine()) {
                this.edgeData.add(getNodeString(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return edgeData;
    }

    private List<String> getNodeString(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }
}
