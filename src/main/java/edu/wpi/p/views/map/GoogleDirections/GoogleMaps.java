package edu.wpi.p.views.map.GoogleDirections;


import com.google.maps.*;
import com.google.maps.GeoApiContext.Builder;
import com.google.maps.errors.ZeroResultsException;
import com.google.maps.model.*;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.p.AStar.Node;
import edu.wpi.p.views.map.DirectionTableEntry;
import javafx.application.Platform;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.net.URI;
import java.util.List;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class GoogleMaps {
    private static String API_Key ;
//    private List<String> directionsText = new ArrayList<>();
    private AutoCompletePopup autoCompletePopup;
    private String latestDirectionsText;
    private boolean currentValidStartLocation= false;

    public GoogleMaps(){
        //get API key
        try {
            API_Key= Files.lines(Paths.get("gmapsapi.txt")).findFirst().get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDirectionsLink(String startLocation, String endLocation, TravelMode mode){
        String link;
        endLocation = endLocation.replaceAll(" ", "+");

        if(startLocation.equals("")){
            link = "https://www.google.com/maps/dir/?api=1&" +
                    "destination="+endLocation +
                    "&mode="+mode;
        }
        else {
            startLocation = startLocation.replaceAll(" ", "+");
            link = "https://www.google.com/maps/dir/?api=1&" +
                    "origin=" + startLocation + "&destination=" + endLocation +
                    "&mode=" + mode ;
        }
        return link;
    }

    public void directionsLink(String startLocation, String endLocation, TravelMode mode){
        String link = getDirectionsLink(startLocation,endLocation,mode);

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(link));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }


    public File createQRCode(String startLocation, String endLocation, TravelMode mode, String imagePath){
        // The data that the QR code will contain
        String data = getDirectionsLink(startLocation,endLocation,mode);

        // The path where the image will get saved
        String path = imagePath;

        // Encoding charset
        String charset = "UTF-8";

        Map<EncodeHintType, ErrorCorrectionLevel> hashMap
                = new HashMap<EncodeHintType,
                                ErrorCorrectionLevel>();

        hashMap.put(EncodeHintType.ERROR_CORRECTION,
                ErrorCorrectionLevel.L);

        // Create the QR code and save
        // in the specified file path
        // as a png file
        File file = createQR(data, path, charset, hashMap, 400, 400);
        System.out.println("QR Code Generated!!!\n Start: "+ startLocation+ " \n" +
                "End Location: "+ endLocation);

        return file;
    }

    // Function to create the QR code
    public static File createQR(String data, String path, String charset, Map hashMap, int height, int width)
    {
        BitMatrix matrix = null;
        //Make QR Code matrix
        try {
            matrix = new MultiFormatWriter().encode(
                    new String(data.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }

        //Make Matrix into image
        try {
            //check if file exists
            File file = new File(path);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
            file.mkdirs();

            Path getPath = FileSystems.getDefault().getPath(path); //get file path
            MatrixToImageWriter.writeToPath( //update image with QR code
                    matrix,
                    "png",
                    getPath);
            return file;
        } catch (IOException e) {
            System.out.println("IO exception");
            e.printStackTrace();
        }
        return null;
    }


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

//        PlacesApi.findPlaceFromText(context, startLocation, FindPlaceFromTextRequest.InputType.TEXT_QUERY);

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
    public void getDirections(String startLocation, String endLocation, TravelMode mode, JFXTextArea textArea, DirectionsList directionsList){
//        Builder builder = new GeoApiContext.Builder();
//        builder.apiKey(API_Key);
//        GeoApiContext context = builder.build();
//        if(!currentValidStartLocation){
//            System.out.println("not a valid start location cant find directions");
//            return;
//        }
        DirectionsApiRequest directionsApiRequest=requestDirections(startLocation, endLocation, mode);

        directionsApiRequest.setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                if(result.routes.length!=0) {
                    DirectionsStep[] steps = result.routes[0].legs[0].steps; //get steps of first route
                    List<String> directionsText = new ArrayList<>(); //list to store different steps
                    for (DirectionsStep step : steps) {
                        String stepText = getHTMLText(step.htmlInstructions); //convert html instruction to readable string
                        if(stepText.contains("Destination will be on the")){
                            String[] parts = stepText.split("Destination");
                            directionsText.add(parts[0]);
                            directionsText.add("Destination"+parts[1]);
                        }
                        else {
                            directionsText.add(stepText); //add to list
                        }
                    }
                    String aboutRoute = "About Route: \n";
                    aboutRoute += "Start: " + result.routes[0].legs[0].startAddress +"\n";
                    aboutRoute += "End: " + result.routes[0].legs[0].endAddress +"\n";
                    aboutRoute += "Duration: " + result.routes[0].legs[0].duration.humanReadable +"\n";
                    aboutRoute += "Distance: " + result.routes[0].legs[0].distance.humanReadable +"\n";

                    updateText(textArea, directionsText, aboutRoute); //update textview on page
                    directionsList.updateDirectionsTable(directionsList.makeGoogleDirections(directionsText));
//                    directionsList.updateDirectionsTable(makeDirections(directionsText));

                    System.out.println("found directions");
                }
            }
            @Override
            public void onFailure(Throwable e) {
                List<String> errorText = new ArrayList<>(); //list to store different steps
                try {
                    String message = e.getCause().getMessage();
                    errorText.add(message);
                }
                catch (Exception exception){
                    System.out.println("no message");
                }
                if(e instanceof ZeroResultsException){
                    errorText.add("No Results Found");
                }
                updateText(textArea, errorText, "ERROR"); //update textview on page
                System.out.println("calculateDirections: Failed to get directions: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }



    //Not allowed to use geocoding api
    public void checkValidRequest(String startLocation){
        Builder builder = new GeoApiContext.Builder();
        builder.apiKey(API_Key);
        GeoApiContext context = builder.build();
        GeocodingApiRequest geocodingApiRequest = new GeocodingApiRequest(context);
        geocodingApiRequest.address(startLocation);
        geocodingApiRequest.region("us");

        geocodingApiRequest.setCallback(new PendingResult.Callback<GeocodingResult[]>() {
            @Override
            public void onResult(GeocodingResult[] result) {
                System.out.println(startLocation+ " is a valid request");
                currentValidStartLocation = true;
            }

            @Override
            public void onFailure(Throwable e) {
                System.out.println(startLocation+ " is NOT a valid request");
                System.out.println(e.getMessage());
                currentValidStartLocation = false;

            }
        });
    }


    /**
     * update given text area with list of directions
     * @param textArea
     * @param directionsText
     */
    public void updateText(JFXTextArea textArea, List<String> directionsText, String aboutRoute){
        System.out.println("updating text");

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
            latestDirectionsText =text;
            //because the user interface cannot be directly updated from a non-application thread.
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //set the text of the text area
                    textArea.setText(latestDirectionsText);
                }
            });

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

//        checkValidRequest(currentText);

        request.setCallback(new PendingResult.Callback<AutocompletePrediction[]>() {
            @Override
            public void onResult(AutocompletePrediction[] result) {
                //because the user interface cannot be directly updated from a non-application thread.
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        autoCompletePopup.clearSuggestions();
                        for(int i = 0; i<12 && i<result.length; i++){
                            if(!autoCompletePopup.getSuggestions().contains(result[i].description)) {
        //                        System.out.println(result[i].description);
                                autoCompletePopup.getSuggestions().add(result[i].description);
                            }
                        }

                        autoCompletePopup.updateFilter();
                    }
                });
            }

            @Override
            public void onFailure(Throwable e) {
                System.out.println("Error: Get autocomplete options request failed");
                try {
                    System.out.println(e.getClass().getName());
                    System.out.println(e.getMessage());
                }
                catch (Exception exception){
                    System.out.println("no message");
                }
            }
        });

    }

}
