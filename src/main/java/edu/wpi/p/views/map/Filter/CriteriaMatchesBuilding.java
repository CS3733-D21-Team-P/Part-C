package edu.wpi.p.views.map.Filter;

import edu.wpi.p.AStar.Node;

import java.util.ArrayList;
import java.util.List;

public class CriteriaMatchesBuilding implements Criteria {
    public void setBuilding(String building) {
        this.building = building;
    }

    private String building = "1";

    public CriteriaMatchesBuilding(String building){
        this.building = building;
    }

    @Override
    public List<Node> meetCriteria(List<Node> nodes) {
        List<Node> nodesInBuilding = new ArrayList<>();

        for (Node n : nodes) {
            if(n.getBuilding().toLowerCase().equals(this.building.toLowerCase())){
                nodesInBuilding.add(n);
            }
        }
        return nodesInBuilding;
    }
}
