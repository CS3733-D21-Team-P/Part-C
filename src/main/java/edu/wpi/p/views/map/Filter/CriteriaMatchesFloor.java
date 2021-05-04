package edu.wpi.p.views.map.Filter;

import edu.wpi.p.AStar.Node;

import java.util.ArrayList;
import java.util.List;

public class CriteriaMatchesFloor implements Criteria {
    public void setFloor(String floor) {
        this.floor = floor;
    }

    private String floor = "1";

    public CriteriaMatchesFloor(String floor){
        this.floor = floor;
    }

    @Override
    public List<Node> meetCriteria(List<Node> nodes) {
        List<Node> nodesOnFloor = new ArrayList<>();

        for (Node n : nodes) {
            if(n.getFloor().equals(this.floor)){
                nodesOnFloor.add(n);
            }
        }
        return nodesOnFloor;
    }
}
