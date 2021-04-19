package edu.wpi.p.database;

public class ServiceRequest {

    private String serviceRequestName;
    private String serviceRequestLocation;
    private String serviceRequestID;



    public ServiceRequest (String serviceRequestName, String serviceRequestLocation, String serviceRequestID) {
        this.serviceRequestName = serviceRequestName;
        this.serviceRequestLocation = serviceRequestLocation;
        this.serviceRequestID = serviceRequestID;

    }

    public String getServiceRequestName() {
        return serviceRequestName;
    }

    public void setServiceRequestName(String serviceRequestName) {
        this.serviceRequestName = serviceRequestName;
    }

    public String getServiceRequestLocation() {
        return serviceRequestLocation;
    }

    public void setServiceRequestLocation(String serviceRequestLocation) {
        this.serviceRequestLocation = serviceRequestLocation;
    }

    public String getServiceRequestID() {
        return serviceRequestID;
    }

    public void setServiceRequestID(String serviceRequestID) {
        this.serviceRequestID = serviceRequestID;
    }


}
