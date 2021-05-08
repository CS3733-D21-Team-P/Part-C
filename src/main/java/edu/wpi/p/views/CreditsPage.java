package edu.wpi.p.views;

import edu.wpi.p.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class CreditsPage {

    Hyperlink link = new Hyperlink("https://developers.google.com/maps");

    public void backButtonAc(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void googleMapsAc(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://developers.google.com/maps"));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
    }
    public void apacheDerybyAc(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://db.apache.org/derby/manuals/"));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
    }
    public void JfeoAc(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI("http://www.jfoenix.com/documentation.html"));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
    }
    public void JFXAc(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://openjfx.io/"));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
    }
    public void JunitAc(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://junit.org/junit4/"));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
    }
    public void intelAc(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://www.jetbrains.com/idea/"));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
    }
    public void SBAc(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://gluonhq.com/products/scene-builder/"));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
    }
    public void GHac(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/"));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
    }


    public void learnMoreButtonAc(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/CreditsPage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
