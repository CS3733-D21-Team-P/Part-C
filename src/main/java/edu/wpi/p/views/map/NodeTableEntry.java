package edu.wpi.p.views.map;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.database.rowdata.ServiceRequest;

/**
 * ServiceRequests don't extend RecursiveTreeObject because they extend DBRow
 * This class just provides a wrapper for a ServiceRequest that extends RecursiveTreeObject so that we can use it in a JFXTreeTable
 */
public class NodeTableEntry extends RecursiveTreeObject<NodeTableEntry> {
    private Node node;

    public NodeTableEntry(Node n) {
        this.node = n;
    }

    public Node getNode() {
        return this.node;
    }
}