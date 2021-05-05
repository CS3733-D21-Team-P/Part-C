package edu.wpi.p.views.map.Filter;

import edu.wpi.p.AStar.Node;

import java.util.ArrayList;
import java.util.List;

public class CriteriaNoHallways implements Criteria{
    @Override
    public List<Node> meetCriteria(List<Node> nodes) {
        List<Node> nonHallways = new ArrayList<>();

        for(Node n: nodes){
            if(!n.getType().equals("HALL")){
                nonHallways.add(n);
            }
        }

        return nonHallways;
    }
}
