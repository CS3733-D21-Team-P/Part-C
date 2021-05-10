package edu.wpi.p.views;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ClippoController {

    private VBox currClippoTab;
    private VBox prevClippoTab;

    private String page = "clippoHome";

    private Clip clip;
    private AudioInputStream audioInputStream;

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
//        clippoHome.setVisible(false);
//        clippoServReq.setVisible(false);
//        clippoCOVID19.setVisible(false);
//        clippoMapEdit.setVisible(false);
//        clippoPathfind.setVisible(false);
//        clippoRequestLogs.setVisible(false);
//        clippoNodes.setVisible(false);
//        clippoCSV.setVisible(false);
//        clippoEdges.setVisible(false);
//        clippoFindTab.setVisible(false);
//        clippoTextDirections.setVisible(false);
//        clippoSaveLoc.setVisible(false);
//        clippoGoogleAPI.setVisible(false);
        currClippoTab = clippoButton;

    }

    @FXML public void serviceRequestHelpBtn(ActionEvent actionEvent) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        currClippoTab.setVisible(false);
        clippoServReq.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoServReq;
        clip.stop();
        playAudio("src/main/resources/audiofiles/ServiceRequests.wav");
    }

    @FXML public void covid19HelpBtn(ActionEvent actionEvent) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        currClippoTab.setVisible(false);
        clippoCOVID19.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoCOVID19;
        clip.stop();
        playAudio("src/main/resources/audiofiles/Covid-19.wav");
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

    @FXML public void pathfindingHelpBtn(ActionEvent actionEvent) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        currClippoTab.setVisible(false);
        clippoPathfind.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoPathfind;
        clip.stop();
        playAudio("src/main/resources/audiofiles/Pathfinding.wav");
    }

    @FXML public void editMapHelpBtn(ActionEvent actionEvent) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        currClippoTab.setVisible(false);
        clippoMapEdit.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoMapEdit;
        clip.stop();
        playAudio("src/main/resources/audiofiles/EditMap.wav");
    }

    @FXML public void requestLogsHelpBtn(ActionEvent actionEvent) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        currClippoTab.setVisible(false);
        clippoRequestLogs.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoRequestLogs;
        clip.stop();
        playAudio("src/main/resources/audiofiles/RequestLogs.wav");
    }

    @FXML public void closeClippo(ActionEvent actionEvent) {
        currClippoTab.setVisible(false);
        clippoButton.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoButton;
        clip.stop();
    }

    @FXML public void nodesHelpBtn(ActionEvent actionEvent) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        currClippoTab.setVisible(false);
        clippoNodes.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoNodes;
        clip.stop();
        playAudio("src/main/resources/audiofiles/Nodes.wav");
    }

    @FXML public void edgesHelpBtn(ActionEvent actionEvent) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        currClippoTab.setVisible(false);
        clippoEdges.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoEdges;
        clip.stop();
        playAudio("src/main/resources/audiofiles/Edges.wav");
    }

    @FXML public void CSVHelpBtn(ActionEvent actionEvent) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        currClippoTab.setVisible(false);
        clippoCSV.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoCSV;
        clip.stop();
        playAudio("src/main/resources/audiofiles/CSVFiles.wav");
    }

    @FXML public void findTabHelpBtn(ActionEvent actionEvent) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        currClippoTab.setVisible(false);
        clippoFindTab.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoFindTab;
        clip.stop();
        playAudio("src/main/resources/audiofiles/FindTab.wav");
    }

    @FXML public void textDirectionsHelpBtn(ActionEvent actionEvent) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        currClippoTab.setVisible(false);
        clippoTextDirections.setVisible(true);
        prevClippoTab = currClippoTab;
        currClippoTab = clippoTextDirections;
        clip.stop();
        playAudio("src/main/resources/audiofiles/TextDirections.wav");
    }

    @FXML public void clippoButtonPressed(ActionEvent actionEvent) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        switch(page){
            case "serviceRequests":
                currClippoTab.setVisible(false);
                clippoServReq.setVisible(true);
                prevClippoTab = currClippoTab;
                currClippoTab = clippoServReq;
                playAudio("src/main/resources/audiofiles/ServiceRequests.wav");
                break;
            case "requestLogs":
                currClippoTab.setVisible(false);
                clippoRequestLogs.setVisible(true);
                prevClippoTab = currClippoTab;
                currClippoTab = clippoRequestLogs;
                playAudio("src/main/resources/audiofiles/RequestLogs.wav");
                break;
            case "covidSurvey":
                currClippoTab.setVisible(false);
                clippoCOVID19.setVisible(true);
                prevClippoTab = currClippoTab;
                currClippoTab = clippoCOVID19;
                playAudio("src/main/resources/audiofiles/Covid-19.wav");
                break;
            case "editMap":
                currClippoTab.setVisible(false);
                clippoMapEdit.setVisible(true);
                prevClippoTab = currClippoTab;
                currClippoTab = clippoMapEdit;
                playAudio("src/main/resources/audiofiles/EditMap.wav");
                break;
            case "pathfinding":
                currClippoTab.setVisible(false);
                clippoPathfind.setVisible(true);
                prevClippoTab = currClippoTab;
                currClippoTab = clippoPathfind;
                playAudio("src/main/resources/audiofiles/Pathfinding.wav");
                break;
            case "accounts":
                currClippoTab.setVisible(false);
                clippoAccounts.setVisible(true);
                prevClippoTab = currClippoTab;
                currClippoTab = clippoAccounts;
                playAudio("src/main/resources/audiofiles/AccountsTable.wav");
                break;
            case "employees":
                currClippoTab.setVisible(false);
                clippoEmpTable.setVisible(true);
                prevClippoTab = currClippoTab;
                currClippoTab = clippoEmpTable;
                playAudio("src/main/resources/audiofiles/EmployeeTable.wav");
                break;
            default:
                currClippoTab.setVisible(false);
                clippoHome.setVisible(true);
                prevClippoTab = currClippoTab;
                currClippoTab = clippoHome;
                playAudio("src/main/resources/audiofiles/HomePage.wav");
        }
    }

    @FXML public void clippoBackButton(ActionEvent actionEvent) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        currClippoTab.setVisible(false);
        clippoHome.setVisible(true);
        currClippoTab = clippoHome;
        clip.stop();
        playAudio("src/main/resources/audiofiles/HomePage.wav");
    }

    public void setPage(String page) {
        this.page = page;
    }

    private void playAudio(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    }
}
