package edu.wpi.p.views.map;

import com.jfoenix.controls.*;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.AStar.NodeButton;
import edu.wpi.p.AStar.NodeGraph;
import edu.wpi.p.database.DBUser;
import edu.wpi.p.database.UserFromDB;
import edu.wpi.p.userstate.User;
import org.sqlite.core.DB;

import java.util.ArrayList;
import java.util.List;

public class ParkingSaving {
    private final DBUser dbuser = new DBUser();


    public void saveParkingAc(NodeButton nb) {
        Node node = nb.getNode();
//        dbuser.removeUser("tempUser'");
//        UserFromDB user = new UserFromDB("tempUser","tempUser", "tempUser", "tempUser", node.getId());
        String username = User.getInstance().getUsername();
        dbuser.setParkingNodeID(username, node.getId());
        nb.setSaved(true);
        nb.setButtonStyle();
    }
}
