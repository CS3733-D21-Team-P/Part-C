package edu.wpi.p.views.map.Filter;

import edu.wpi.p.AStar.Node;
import java.util.List;

public interface Criteria {
    public List<Node> meetCriteria(List<Node> nodes);

}
