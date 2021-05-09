package edu.wpi.p.AStar;

import edu.wpi.p.views.map.AnimatePath;
import edu.wpi.p.views.map.DirectionTableEntry;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class PathToText {
    float turnAngle = 30;
    List<DirectionTableEntry> tableEntryDirections = new ArrayList<>();

    public void PrintPath() {
        for(DirectionTableEntry entry : tableEntryDirections) {
            System.out.println(entry.getInstruction());
        }
    }

    public List<DirectionTableEntry> getTableDirections() { return tableEntryDirections;}

    public List<DirectionTableEntry> makeDirections(List<Node> path) {
        tableEntryDirections = new ArrayList<>();
        for (int i = 0; i < path.size() - 1; i++) {
            if (i != path.size() - 1) {
                Node currentNode = path.get(i);
                Node nextNode = path.get(i + 1);
                Node prevNode = currentNode;
                float[] to = new float[] {0,0,0,0};
                float[] from = new float[] {0,0,0,0};
                float angle = 0;
                if (i > 0) {
                    prevNode = path.get(i - 1);
                    to = new float[]{currentNode.getXcoord(), currentNode.getYcoord(), nextNode.getXcoord(), nextNode.getYcoord()};
                    from = new float[]{prevNode.getXcoord(), prevNode.getYcoord(), currentNode.getXcoord(), currentNode.getYcoord()};
                    float dot = dot(to, from);
                    if(dot == 0) {
                        angle = 90;
                    } else {
                        //angle should always be positive
                        angle = (float)(Math.acos(dot / (mag(to) * mag(from))) * (180/Math.PI));
                    }
                }

                //determine left or right
                if (angle >= turnAngle) {
                    if(angle == 90) {
                        nudge(from);
                    }
                    float[] lp = leftPerpendicular(from);
                    float dot = dot(lp, to);
                    float lpAngle = (float)(Math.acos(dot / (mag(to) * mag(lp))) * (180/Math.PI));
                    if (lpAngle > 90) {
                        //left
                        angle *= -1;
                    }
                }
                tableEntryDirections.add(getTableInstruction(currentNode, nextNode, angle));
            }
        }
        return tableEntryDirections;
    }

    private float[] leftPerpendicular(float[] v) {
        return new float[] { 0, 0, -vy(v), vx(v) };
    }

    private void nudge(float[] v) {
        v[0] += 1;
        v[1] += 2;
    }

    private float vx(float[] v) {
        return v[2] - v[0];
    }

    private float vy(float[] v) {
        return v[3] - v[1];
    }

    private float mag(float[] v) {
        return (float)Math.sqrt(Math.pow(vx(v), 2) + Math.pow(vy(v), 2));
    }

    private float dot(float[] a, float[] b) {
        return (vx(a) * vx(b)) + (vy(a) * vy(b));
    }


    private DirectionTableEntry getTableInstruction(Node current, Node destination, Float angle) {
        boolean turning = false;
        String instruction = "";
        ImageView imgView = new ImageView();
        imgView.setFitWidth(50);
        imgView.setFitHeight(50);

        if (Math.abs(angle) >= turnAngle) {
            turning = true;
            instruction = "Turn ";
            if (angle > 0) {
                //right
                instruction += "right";
                imgView.setImage(new Image("edu/wpi/p/fxml/image/icons/arrow_turn_right.png"));
            } else {
                //left
                instruction += "left";
                imgView.setImage(new Image("edu/wpi/p/fxml/image/icons/arrow_turn_left.png"));
            }
        }

        //Switch Node Type
        switch (destination.getType()) {
            case "HALL":
                instruction += hallwayDirection(destination.getName(), turning);
                break;

            case "STAI":
                instruction += stairDirection(imgView, current.getFloor(), destination.getName(), destination.getFloor(), turning);
                break;

            case "ELEV":
                instruction += elevatorDirection(imgView, current.getFloor(), destination.getName(), destination.getFloor(), turning);
                break;

            //case "DEPT":
            //case "EXIT":
            //case "INFO":
            //case "LABS":
            //case "REST":
            //case "RETL":
            //case "STAI"
            //case "SERV"
            //case "BATH"
            //case "PARK"
            //case "WALK"

            default:
                if(!turning) {
                    instruction += "Head";
                }
                instruction += " towards " + destination.getName();
                break;
        }
        return new DirectionTableEntry(imgView, instruction);
    }

    private String hallwayDirection(String name, boolean turning) {
        String direction = "";
        if(!turning) {
            direction += "Head";
        }
        direction += " down " + name;
        return direction;
    }

    private String stairDirection(ImageView imgView, String currentFloor, String name, String nextFloor, Boolean turning) {

        String direction = "";
        if(!currentFloor.equals(nextFloor)) {
            direction +=  " take " + name + " to " + nextFloor;
            imgView.setImage(new Image("edu/wpi/p/fxml/image/icons/stairs.png"));
        } else {
            direction += " towards " + name;
        }
        return direction;
    }

    private String elevatorDirection(ImageView imgView, String currentFloor, String name, String nextFloor, Boolean turning) {

        String direction = "";
        if(!currentFloor.equals(nextFloor)) {
            direction +=  " take " + name + " to " + nextFloor;
            imgView.setImage(new Image("edu/wpi/p/fxml/image/icons/elevator.png"));
        } else {
            direction += " towards " + name;
        }
        return direction;
    }
}
