package AStar;

import edu.wpi.p.database.DBTable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NodeGraph {

    private List<Node> graph;
    private boolean directedPaths;
    private DBTable dbTable;

    public NodeGraph() {
        this.graph = new ArrayList<>();
        this.directedPaths = false;
        this.dbTable = new DBTable();
    }

    public void genGraph(boolean enableDirectedPaths) {
        graph = new ArrayList<>();
        this.directedPaths = enableDirectedPaths;

        //buildTestGraph();
        buildHospitalGraph();
    }

    public void resetNodeGraph() {
        for(Node n : graph) {
            n.resetGoals();
            n.setVisited(false);
        }
    }

    public Node getNodeByName(String name) {
        if(!graph.isEmpty()) {
            for(Node n : graph) {
                if(n.getName().equals(name)) {
                    return n;
                }
            }
        }
        return null;
    }

    public Node getNodeByID(String id) {
        if(!graph.isEmpty()) {
            for(Node n : graph) {
                if(n.getId().equals(id)) {
                    return n;
                }
            }
        }
        return null;
    }

    private void buildHospitalGraph() {
        initNodes();
        pairNodes();
    }

    private void initNodes() {
        List<List<String>> nodeData = dbTable.getNodeData();

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
        //nodeData.size() - 1
        for(int i = 1; i < nodeData.size(); i++) {
            List<String> nodeString = nodeData.get(i);

            Node node = new Node(
                    nodeString.get(nodeID),
                    nodeString.get(longName),
                    Integer.parseInt(nodeString.get(xcoord)),
                    Integer.parseInt(nodeString.get(ycoord)));

            this.graph.add(node);
        }
    }

    private void pairNodes() {
        List<List<String>> edgeData = dbTable.getEdgeData();

        //L1Edges columns
        int edgeID = 0;
        int startNode = 1;
        int endNode = 2;

        //connect nodes
        for(Node n : this.graph) {
            for(List<String> edgeString : edgeData) {
                //we are start
                if(n.getId().equals(edgeString.get(1))) {
                    //look for pair
                    for(Node pair : this.graph) {
                        if(pair.getId().equals(edgeString.get(2))) {

                            n.addNeighbour(pair);

                            if(!directedPaths) {
                                pair.addNeighbour(n);
                            }
                        }
                    }
                }
            }
        }
    }

    private void buildTestGraph() {

        this.graph = new ArrayList<>();

        //   B - C
        //  /   / \
        // A - E - D
        //  \  |  /
        //     F - G - H - I - J

        Node a = new Node("A1", "A", 0, 0);
        Node b = new Node("B1", "B", 1, 2);
        Node c = new Node("C1", "C", 3, 2);
        Node d = new Node("D1", "D", 4, 0);
        Node e = new Node("E1", "E", 2, 0);
        Node f = new Node("F1", "F", 2, -2);
        Node g = new Node("G1", "G", 4, -2);
        Node h = new Node("H1", "H", 6, -2);
        Node i = new Node("I1", "I", 8, -2);
        Node j = new Node("J1", "J", 10, -2);

        a.addNeighbour(b);
        a.addNeighbour(e);
        a.addNeighbour(f);

        b.addNeighbour(a);
        b.addNeighbour(c);

        c.addNeighbour(b);
        c.addNeighbour(e);
        c.addNeighbour(d);

        d.addNeighbour(c);
        d.addNeighbour(e);
        d.addNeighbour(f);

        e.addNeighbour(a);
        e.addNeighbour(c);
        e.addNeighbour(d);
        e.addNeighbour(f);

        f.addNeighbour(a);
        f.addNeighbour(d);
        f.addNeighbour(e);
        f.addNeighbour(g);

        g.addNeighbour(f);
        g.addNeighbour(h);

        h.addNeighbour(g);
        h.addNeighbour(i);

        i.addNeighbour(h);
        i.addNeighbour(j);

        j.addNeighbour(i);

        this.graph.add(a);
        this.graph.add(b);
        this.graph.add(c);
        this.graph.add(d);
        this.graph.add(e);
        this.graph.add(f);
    }
}
