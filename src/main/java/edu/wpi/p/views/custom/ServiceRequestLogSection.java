package edu.wpi.p.views.custom;

import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.rowdata.ServiceRequest;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServiceRequestLogSection extends VBox {

    public ServiceRequestLogSection(String name, List<ServiceRequest> requests) {
        super();
        Label heading = new Label(name);
        heading.setTextFill(Color.WHITE);
        this.getChildren().add(heading);
        GridPane requestSection = makeGridSection(requests);
        this.getChildren().add(requestSection);

    }

    private Set<String> getAllDetailColumns(List<ServiceRequest> requests) {
        Set<String> detailNames = new HashSet<>();
        for (ServiceRequest request : requests) {
            for (String key : request.getDetailsMap().keySet()) {
                detailNames.add(key);
            }
        }

        return detailNames;
    }

    private GridPane makeGridSection(List<ServiceRequest> requests) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Set<String> detailColumns = getAllDetailColumns(requests);
        int col = 0;
        int row = 0;
        for (String detailName : detailColumns) {
            Label name = new Label(detailName);
            name.setTextFill(Color.WHITE);
            grid.add(name, col, row);
            row++;
            for (ServiceRequest request : requests) {
                Label detail = new Label(request.getDetailsMap().get(detailName));
                detail.setTextFill(Color.WHITE);
                grid.add(detail, col, row);
                row++;
            }

            row = 0;
            col++;
        }

        Label assignedLabel = new Label("Assigned To");
        assignedLabel.setTextFill(Color.WHITE);
        grid.add(assignedLabel, col, row);
        row++;
        for (ServiceRequest request : requests) {
            Label detail = new Label(request.getAssignment());
            detail.setTextFill(Color.WHITE);
            grid.add(detail, col, row);
            row++;
        }
        row = 0;
        col++;

        Label completeLabel = new Label("Complete");
        completeLabel.setTextFill(Color.WHITE);
        grid.add(completeLabel, col, row);
        row++;
        for (ServiceRequest request : requests) {
            JFXToggleButton toggleButton = new JFXToggleButton();
            toggleButton.selectedProperty().set(request.getCompleted());

            toggleButton.armedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    request.completed();
                    DBServiceRequest dbServiceRequest = new DBServiceRequest();
                    dbServiceRequest.updateServiceRequest(request);
                }
            });
            grid.add(toggleButton, col, row);
            row++;
        }
        col++;


        return grid;
    }
}
