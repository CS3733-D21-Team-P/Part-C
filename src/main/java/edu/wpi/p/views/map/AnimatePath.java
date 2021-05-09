package edu.wpi.p.views.map;

import edu.wpi.p.AStar.Node;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class AnimatePath {
    final ArrayList<PathTransition> pathTransitions = new ArrayList<>();
    PathfindingMap pathfindingMap;

    public void animatePath(List<Node> path) {
        clearAnimations();
        if (path == null || path.size() < 2) {
            return;
        }

        Path thePath = createPathForAnimation(path);
        if (!(thePath.getElements().size() < 2)) {
            double startX = ((MoveTo) thePath.getElements().get(0)).getX();
            double startY = ((MoveTo) thePath.getElements().get(0)).getY();

            for (int i =0; i<thePath.getElements().size() *3; i++) {
                Circle aCircle = new Circle(
                        startX,
                        startY,
                        5,
                        javafx.scene.paint.Color.color(0/255f, 0/255f,0/255f));
                aCircle.setRadius(8);
                pathfindingMap.btnPane.getChildren().add(aCircle);
                PathTransition pl = new PathTransition();
                pl.setNode(aCircle);
                pl.setPath(createPathForAnimation(path));
                pl.setDuration(Duration.seconds(path.size()/50));
                pl.setAutoReverse(false);
                pl.setCycleCount(Animation.INDEFINITE);
                pl.setDelay(Duration.seconds(i*path.size()/(50*thePath.getElements().size())));
                pl.play();

                pathTransitions.add(pl);
            }
        }
    }

    private void clearAnimations() {
        for (PathTransition pl : pathTransitions) {
            pl.stop();
        }

//        for (int i = linePane.getChildren().size() -1; i >0; i--) {
//            if (pathfindingMap.btnPane.getChildren().get(i) instanceof Circle) {
//                pathfindingMap.btnPane.getChildren().remove(pathfindingMap.btnPane.getChildren().get(i));
//            }
//        }
    }

    public Path createPathForAnimation(List<Node> path) {
        Path onePath = new Path();
        for (int i = 0; i < path.size() - 1; i++) {
//            if (path.get(i).getFloor().equals(getCurrFloorVal())
//                    && path.get(i+1).getFloor().equals(getCurrFloorVal())) {
                MoveTo moveTo = new MoveTo();
                moveTo.setX((float) path.get(i).getXcoord());
                moveTo.setY((float) path.get(i).getYcoord());

                LineTo lineTo = new LineTo();
                lineTo.setX((float) path.get(i+1).getXcoord());
                lineTo.setY((float) path.get(i+1).getYcoord());

                onePath.getElements().add(onePath.getElements().size(), moveTo);
                onePath.getElements().add(onePath.getElements().size(), lineTo);
//            }
        }
        return onePath;
    }
}
