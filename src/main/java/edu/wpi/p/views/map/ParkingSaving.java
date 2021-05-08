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
    NodeButton oldSpot;

    /**
     * saves given nodebutton and unsaves previous. Updates the user database if user is not a guest
     * @param nb
     */
    public void saveParkingAc(NodeButton nb) {
        Node node = nb.getNode();
        String oldParkingID = User.getInstance().getParkingNodeID();
        if(oldSpot!=null){ //already exists
            //unhighlight
            oldSpot.setSaved(false);
            oldSpot.setButtonStyle();
        }
        oldSpot = nb;

        String username = User.getInstance().getUsername();
        if(username!="") {
            dbuser.setParkingNodeID(username, node.getId());
        }
        User.getInstance().setParkingNodeID(node.getId());
        nb.setSaved(true);
        nb.setButtonStyle();
    }

    /**
     * unsaves given nodebutton and updates the user database if user is not a guest
     * @param nb
     */
    public void unsaveParkingAc(NodeButton nb) {
        oldSpot = null;
        String username = User.getInstance().getUsername();
        //update database
        if(username!="") {
            dbuser.setParkingNodeID(username, null);
        }
        User.getInstance().setParkingNodeID(null); //set users parking spot

        //change button style
        nb.setSaved(false);
        nb.setButtonStyle();
    }
}
