package edu.wpi.p.views;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ClippoController {

    private VBox currClippoTab;
    private VBox prevClippoTab;

    private String page = "clippoHome";

    @FXML public VBox clippoButton;
    @FXML public Pane clippoPane;
    @FXML public VBox clippoHome;
    @FXML public VBox clippoServReq;
    @FXML public VBox clippoCOVID19;
    @FXML public VBox clippoMapEdit;
    @FXML public VBox clippoPathfind;
    @FXML public VBox clippoRequestLogs;
    @FXML public VBox clippoNodes;
    @FXML public VBox clippoCSV;
    @FXML public VBox clippoEdges;
    @FXML public VBox clippoFindTab;
    @FXML public VBox clippoTextDirections;
    @FXML public VBox clippoEmpTable;
    @FXML public VBox clippoAccounts;
    @FXML public VBox clippoSaveLoc;
    @FXML public VBox clippoGoogleAPI;

    @FXML public void initialize(){
        clippoHome.setVisible(false);
        clippoServReq.setVisible(false);
        clippoCOVID19.setVisible(false);
        clippoMapEdit.setVisible(false);
        clippoPathfind.setVisible(false);
        clippoRequestLogs.setVisible(false);
        clippoNodes.setVisible(false);
        clippoCSV.setVisible(false);
        clippoEdges.setVisible(false);
        clippoFindTab.setVisible(false);
        clippoTextDirections.setVisible(false);
        currClippoTab = clippoButton;

    }

    @FXML public void serviceRequestHelpBtn(ActionEvent actionEvent) {
        currClippoTab.setVisible(false);
        clippoServReq.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoServReq;
    }

    @FXML public void covid19HelpBtn(ActionEvent actionEvent) {
        currClippoTab.setVisible(false);
        clippoCOVID19.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoCOVID19;
    }

    @FXML public void saveLocHelpBtn(ActionEvent actionEvent) {
        currClippoTab.setVisible(false);
        clippoSaveLoc.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoSaveLoc;
    }

    @FXML public void googleAPIHelpBtn(ActionEvent actionEvent) {
        currClippoTab.setVisible(false);
        clippoGoogleAPI.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoGoogleAPI;
    }

    @FXML public void pathfindingHelpBtn(ActionEvent actionEvent) {
        currClippoTab.setVisible(false);
        clippoPathfind.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoPathfind;
    }

    @FXML public void editMapHelpBtn(ActionEvent actionEvent) {
        currClippoTab.setVisible(false);
        clippoMapEdit.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoMapEdit;
    }

    @FXML public void requestLogsHelpBtn(ActionEvent actionEvent) {
        currClippoTab.setVisible(false);
        clippoRequestLogs.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoRequestLogs;
    }

    @FXML public void closeClippo(ActionEvent actionEvent) {
        currClippoTab.setVisible(false);
        clippoButton.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoButton;
    }

    @FXML public void nodesHelpBtn(ActionEvent actionEvent) {
        currClippoTab.setVisible(false);
        clippoNodes.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoNodes;
    }

    @FXML public void edgesHelpBtn(ActionEvent actionEvent) {
        currClippoTab.setVisible(false);
        clippoEdges.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoEdges;
    }

    @FXML public void CSVHelpBtn(ActionEvent actionEvent) {
        currClippoTab.setVisible(false);
        clippoCSV.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoCSV;
    }

    @FXML public void findTabHelpBtn(ActionEvent actionEvent) {
        currClippoTab.setVisible(false);
        clippoFindTab.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoFindTab;
    }

    @FXML public void textDirectionsHelpBtn(ActionEvent actionEvent) {
        currClippoTab.setVisible(false);
        clippoTextDirections.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoTextDirections;
    }

    @FXML public void clippoButtonPressed(ActionEvent actionEvent) {
        switch(page){
            case "serviceRequests":
                currClippoTab.setVisible(false);
                clippoServReq.setVisible(true);
                prevClippoTab = currClippoTab;
                currClippoTab = clippoServReq;
                break;
            case "requestLogs":
                currClippoTab.setVisible(false);
                clippoRequestLogs.setVisible(true);
                prevClippoTab = currClippoTab;
                currClippoTab = clippoRequestLogs;
                break;
            case "covidSurvey":
                currClippoTab.setVisible(false);
                clippoCOVID19.setVisible(true);
                prevClippoTab = currClippoTab;
                currClippoTab = clippoCOVID19;
                break;
            case "editMap":
                currClippoTab.setVisible(false);
                clippoMapEdit.setVisible(true);
                prevClippoTab = currClippoTab;
                currClippoTab = clippoMapEdit;
                break;
            case "pathfinding":
                currClippoTab.setVisible(false);
                clippoPathfind.setVisible(true);
                prevClippoTab = currClippoTab;
                currClippoTab = clippoPathfind;
                break;
            case "accounts":
                currClippoTab.setVisible(false);
                clippoAccounts.setVisible(true);
                prevClippoTab = currClippoTab;
                currClippoTab = clippoAccounts;
                break;
            case "employees":
                currClippoTab.setVisible(false);
                clippoEmpTable.setVisible(true);
                prevClippoTab = currClippoTab;
                currClippoTab = clippoEmpTable;
                break;
            default:
                currClippoTab.setVisible(false);
                clippoHome.setVisible(true);
                prevClippoTab = currClippoTab;
                currClippoTab = clippoHome;
        }
    }

    @FXML public void clippoBackButton(ActionEvent actionEvent) {
        currClippoTab.setVisible(false);
        clippoHome.setVisible(true);
        currClippoTab = clippoHome;
    }

    public void setPage(String page) {
        this.page = page;
    }

}
