package edu.wpi.p.AStar;

import edu.wpi.p.database.DBTable;

import java.util.ArrayList;
import java.util.List;

public class NodeGraph {

    private List<Node> graph;
    private boolean directedPaths;
    private DBTable dbTable;

    public NodeGraph() {
        this.graph = new ArrayList<>();
        this.directedPaths = false;
        this.dbTable = new DBTable();
    }

    public void addToGraph(Node node){
        graph.add(node);
    }

    public void genGraph(boolean enableDirectedPaths) {
        graph = new ArrayList<>();
        this.directedPaths = enableDirectedPaths;

        //buildTestGraph();
        buildHospitalGraph();
    }

    public List<Node> getGraph() {
        return graph;
    }

    public void scaleGraph(double scaleX, double scaleY) {
        for(Node n : graph) {
            n.setXcoord((int)(n.getXcoord() * scaleX));
            n.setYcoord((int)(n.getYcoord() * scaleY));
        }
    }

    public void resetNodeGraph() {
        for(Node n : graph) {
            n.resetGoals();
            n.setVisited(false);
            n.setParent(null);
        }
    }

    public void setTypeBlockade(String type, boolean active) {
        for(Node n : graph) {
            if(n.getType().equals(type)) {
                n.setBlockade(active);
            }
        }
    }

    public void clearBlockades() {
        for(Node n : graph) {
            n.setBlockade(false);
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
        List<Node> nodeData = dbTable.getNodes();

        for(Node n : nodeData) {
            this.graph.add(n);
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

        Node a = new Node("A1", "A", 50, 250);
        Node b = new Node("B1", "B", 100, 175);
        Node c = new Node("C1", "C", 200, 175);
        Node d = new Node("D1", "D", 250, 250);
        Node e = new Node("E1", "E", 150, 250);
        Node f = new Node("F1", "F", 150, 350);
        Node g = new Node("G1", "G", 250, 350);
        Node h = new Node("H1", "H", 350, 350);
        Node i = new Node("I1", "I", 450, 350);
        Node j = new Node("J1", "J", 550, 350);

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
        this.graph.add(g);
        this.graph.add(h);
        this.graph.add(i);
        this.graph.add(j);
    }
}
