package edu.wpi.p.views.map;


import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeoApiContext.Builder;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.TravelMode;
import com.jfoenix.controls.JFXTextArea;

import java.util.ArrayList;
import java.util.List;

public class GoogleMaps {
    private static final String API_Key = "AIzaSyBEQCTGuuspvUVs9IBOpof00I35HG3HMCM";
//    private List<String> directionsText = new ArrayList<>();


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
                DirectionsStep[] steps = result.routes[0].legs[0].steps; //get steps of first route
                List<String> directionsText = new ArrayList<>(); //list to store different steps
                for (DirectionsStep step : steps) {
                    String stepText =getHTMLText(step.htmlInstructions); //convert html instruction to readable string
                    directionsText.add(stepText); //add to list
                }

                updateText(textArea,directionsText); //update textview on page

                System.out.println("found directions");
            }
            @Override
            public void onFailure(Throwable e) {
                System.out.println("calculateDirections"+ "calculateDirections: Failed to get directions: " + e.getMessage());
            }
        });
    }


    /**
     * update given text area with list of directions
     * @param textArea
     * @param directionsText
     */
    public void updateText(JFXTextArea textArea, List<String> directionsText){
        textArea.clear(); //clear previous text
        if(directionsText!=null && !directionsText.isEmpty()) {
            //create one string
            String text = "";
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
        return plainTextBody;
    }

}
