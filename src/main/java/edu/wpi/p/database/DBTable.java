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
    private List<List<String>> nodeData;
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

    public List<List<String>> getNodeData() {
        //parse L1Nodes
        this.nodeData = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("src/main/java/AStar/L1Nodes.csv"))) {
            while (scanner.hasNextLine()) {
                this.nodeData.add(getNodeString(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return nodeData;
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
