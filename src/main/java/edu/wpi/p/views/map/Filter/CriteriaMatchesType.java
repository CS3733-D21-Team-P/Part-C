package edu.wpi.p.views.map.Filter;

import edu.wpi.p.AStar.Node;

import java.util.ArrayList;
import java.util.List;

public class CriteriaMatchesType implements Criteria {
    public void setType(String type) {
        this.type = type;
    }

    private String type = "1";

    public CriteriaMatchesType(String type){
        this.type = type;
    }

    @Override
    public List<Node> meetCriteria(List<Node> nodes) {
        List<Node> nodesOfType = new ArrayList<>();

        for (Node n : nodes) {
            if(n.getType().toLowerCase().equals(this.type.toLowerCase())){
                nodesOfType.add(n);
            }
        }
        return nodesOfType;
    }
}
