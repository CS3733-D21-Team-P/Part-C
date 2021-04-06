package AStar;

import java.util.ArrayList;
import java.util.List;

public class AStarApp {
    public static void main(String[] args) {
        //TODO use Hospital map
        List<Node> graph = new ArrayList<>();
        //   B - C
        //  /   / \
        // A - E - D
        //  \  |  /
        //     F - G - H - I - J

        Node a = new Node("A", 0, 0);
        Node b = new Node("B", 1, 2);
        Node c = new Node("C", 3, 2);
        Node d = new Node("D", 4, 0);
        Node e = new Node("E", 2, 0);
        Node f = new Node("F", 2, -2);
        Node g = new Node("G", 4, -2);
        Node h = new Node("H", 6, -2);
        Node i = new Node("I", 8, -2);
        Node j = new Node("J", 10, -2);

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

        graph.add(a);
        graph.add(b);
        graph.add(c);
        graph.add(d);
        graph.add(e);
        graph.add(f);

        //
        List<Node> path = new ArrayList<>();

        AStar search = new AStar();
        path = search.findShortestPath(a, d);

        System.out.println("Path: ");
        for (Node n : path) {
            System.out.print(n.getName() + " ");
        }

        //TODO reset nodes after search
    }
}
