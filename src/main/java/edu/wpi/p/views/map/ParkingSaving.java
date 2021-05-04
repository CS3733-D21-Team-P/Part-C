package edu.wpi.p.views.map;

import com.jfoenix.controls.*;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.AStar.NodeButton;
import edu.wpi.p.database.DBUser;
import edu.wpi.p.database.UserFromDB;

public class ParkingSaving {
    JFXButton saveParking;
    private final DBUser dbuser = new DBUser();

    public void saveParkingAc(NodeButton nb) {
        UserFromDB user = new UserFromDB("tempUser", "tempUser");
        Node node = nb.getNode();
        user.setParkingNodeID(node.getId());
        dbuser.addUser(user);
        nb.toggleIsSelected(true);
        nb.setButtonStyle();
    }
}
