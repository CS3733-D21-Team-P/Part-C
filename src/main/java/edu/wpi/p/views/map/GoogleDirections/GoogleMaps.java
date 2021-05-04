package edu.wpi.p.views.map.GoogleDirections;


import com.google.maps.*;
import com.google.maps.GeoApiContext.Builder;
import com.google.maps.model.*;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoogleMaps {
    private static final String API_Key = "AIzaSyBEQCTGuuspvUVs9IBOpof00I35HG3HMCM";
//    private List<String> directionsText = new ArrayList<>();
    private AutoCompletePopup autoCompletePopup;

    /**
     * makes a request for directions
     * @param startLocation
     * @param endLocation
     * @param mode
     * @return returns the request
     */
    public DirectionsApiRequest requestDirections(String startLocation, String endLocation, TravelMode mode){
        //create context
        Builder builder = new GeoApiContext.Builder();
        builder.apiKey(API_Key);
        GeoApiContext context = builder.build();

        //create request
        DirectionsApiRequest directionsApiRequest= new DirectionsApiRequest(context);
        directionsApiRequest.origin(startLocation); //set start location
        directionsApiRequest.destination(endLocation); //set end location
        directionsApiRequest.region("us"); //set region to be the us

        return directionsApiRequest;
    }

    /**
     * makes request and updates textArea when directions are found
     * @param startLocation
     * @param endLocation
     * @param mode
     * @param textArea
     */
    public void getDirections(String startLocation, String endLocation, TravelMode mode, JFXTextArea textArea){
        DirectionsApiRequest directionsApiRequest=requestDirections(startLocation, endLocation, mode);

        directionsApiRequest.setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                if(result.routes.length!=0) {
                    DirectionsStep[] steps = result.routes[0].legs[0].steps; //get steps of first route
                    List<String> directionsText = new ArrayList<>(); //list to store different steps
                    for (DirectionsStep step : steps) {
                        String stepText = getHTMLText(step.htmlInstructions); //convert html instruction to readable string
                        directionsText.add(stepText); //add to list
                    }
                    String aboutRoute = "About Route: \n";
                    aboutRoute += "Start: " + result.routes[0].legs[0].startAddress +"\n";
                    aboutRoute += "End: " + result.routes[0].legs[0].endAddress +"\n";
                    aboutRoute += "Duration: " + result.routes[0].legs[0].duration.humanReadable +"\n";
                    aboutRoute += "Distance: " + result.routes[0].legs[0].distance.humanReadable +"\n";

                    updateText(textArea, directionsText, aboutRoute); //update textview on page

                    System.out.println("found directions");
                }
            }
            @Override
            public void onFailure(Throwable e) {
                List<String> errorText = new ArrayList<>(); //list to store different steps
                errorText.add(e.getCause().getMessage());
                updateText(textArea,errorText, "ERROR"); //update textview on page
                System.out.println("calculateDirections"+ "calculateDirections: Failed to get directions: " + e.getMessage());
            }
        });
    }


    /**
     * update given text area with list of directions
     * @param textArea
     * @param directionsText
     */
    public void updateText(JFXTextArea textArea, List<String> directionsText, String aboutRoute){
        textArea.clear(); //clear previous text
        //create one string
        String text = "";

        text+=aboutRoute+"\n"+"Directions: \n";

        if(directionsText!=null && !directionsText.isEmpty()) {

            int directionNum = 1;
            for (String s : directionsText) {
                text += directionNum + ". " + s + "\n"; //add numbers to each line
                directionNum += 1;
            }

            //set the text of the text area
            textArea.setText(text);
        }
    }

    /**
     * gets rid of all html tags
     * @param html
     * @return String without html
     */
    public String getHTMLText(String html){
        String htmlBody = html.replaceAll("<hr>", ""); // one off for horizontal rule lines
        String plainTextBody = htmlBody.replaceAll("<[^<>]+>([^<>]*)<[^<>]+>", "$1");
        plainTextBody = plainTextBody.replaceAll("<br ?/>", ""); //get rid of break tabs
        plainTextBody = plainTextBody.replaceAll("</b>", "");
        plainTextBody = plainTextBody.replaceAll("</div>", ""); //replace div tabs
        if(plainTextBody.contains("Destination will be on the")){
            String[] parts = plainTextBody.split("Destination");
            plainTextBody = parts[0]+"\nDestination"+parts[1];
//            plainTextBody.replaceFirst()
        }
        return plainTextBody;
    }

    public void getOptions(String currentText, AutoCompletePopup autoCompletePopup){
        this.autoCompletePopup = autoCompletePopup;
        System.out.println("options request");
        Builder builder = new GeoApiContext.Builder();
        builder.apiKey(API_Key);
        GeoApiContext context = builder.build();
        QueryAutocompleteRequest request = PlacesApi.queryAutocomplete(context,currentText);
        LatLng latLng = new LatLng(42.338208024320735, -71.10549945775972); //set to prefer locations near hospital
        request.location(latLng);
        request.radius(3); //set to search for locations close to location

        request.setCallback(new PendingResult.Callback<AutocompletePrediction[]>() {
            @Override
            public void onResult(AutocompletePrediction[] result) {
                autoCompletePopup.getSuggestions().clear();
                for(int i = 0; i<12 && i<result.length; i++){
                    if(!autoCompletePopup.getSuggestions().contains(result[i].description)) {
                        System.out.println(result[i].description);
                        autoCompletePopup.getSuggestions().add(result[i].description);
                    }
                }

                //because the user interface cannot be directly updated from a non-application thread.
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        autoCompletePopup.updateFilter();
                    }
                });
            }

            @Override
            public void onFailure(Throwable e) {
                System.out.println("request failed");
                System.out.println(e.getMessage());
            }
        });

    }

}
