package edu.wpi.p.AStar;

import java.util.ArrayList;
import java.util.List;

public class PathToText {
    float turnAngle = 30;
    List<String> directions;

    public void PrintPath() {
        for(String s : directions) {
            System.out.println(s);
        }
    }

    public List<String> getDirections(List<Node> path) {
        directions = new ArrayList<>();
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

                    System.out.println("from Rise: " + vy(from));
                    System.out.println("from Run: " + vx(from));

                    System.out.println("to Rise: " + vy(to));
                    System.out.println("to Run: " + vx(to));

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
                        System.out.println("Hit: Nudge");
                        nudge(from);
                    }
                    float[] lp = leftPerpendicular(from);
                    System.out.println("lp Rise: " + vy(lp));
                    System.out.println("lp Run: " + vx(lp));
                    float dot = dot(lp, to);
                    System.out.println("DOT: "+dot);
                    float lpAngle = (float)(Math.acos(dot / (mag(to) * mag(lp))) * (180/Math.PI));
                    System.out.println("angle: "+angle);
                    System.out.println("lpAngle: "+lpAngle);
                    if (lpAngle > 90) {
                        //left
                        angle *= -1;
                    }
                }
                directions.add(getInstruction(nextNode, angle));
            }
        }
        return directions;
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


    private String getInstruction(Node destination, Float angle) {
        String instruction = "";

        if (Math.abs(angle) >= turnAngle) {
            instruction = "Turn ";
            if (angle > 0) {
                //right
                instruction += "right ";
            } else {
                //left
                instruction += "left ";
            }
        }

        //Switch Node Type
        switch (destination.getType()) {
            case "HALL":
                instruction += hallwayDirection(destination.getName());
                break;

            case "STAI":
                instruction += stairDirection(destination.getName(), destination.getFloor());
                break;
//
//            case "ELEV":
//                ellevatorDirection(destination.getName());
//                break;

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
                instruction += "towards " + destination.getName();
                break;
        }

        return instruction;
    }

    private String hallwayDirection(String name) {
        return "down " + name;
    }

    private String stairDirection(String name, String floor) {
        return "take " + name + " to " + floor;
    }
}
