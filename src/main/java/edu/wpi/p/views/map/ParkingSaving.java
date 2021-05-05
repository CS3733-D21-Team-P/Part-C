package edu.wpi.p.views.map;

import com.jfoenix.controls.*;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.AStar.NodeButton;
import edu.wpi.p.AStar.NodeGraph;
import edu.wpi.p.database.DBUser;
import edu.wpi.p.database.UserFromDB;
import org.sqlite.core.DB;

import java.util.ArrayList;
import java.util.List;

public class ParkingSaving {
    private final DBUser dbuser = new DBUser();


    public void saveParkingAc(NodeButton nb) {
        Node node = nb.getNode();
        UserFromDB user = new UserFromDB("tempUser","tempUser", "tempUser", "tempUser", node.getId());
        user.setParkingNodeID(node.getId());
        dbuser.addUser(user);
        nb.toggleIsSelected(true);
        nb.setButtonStyle();
    }
}
