package edu.wpi.p.views;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.database.rowdata.ServiceRequest;

/**
 * ServiceRequests don't extend RecursiveTreeObject because they extend DBRow
 * This class just provides a wrapper for a ServiceRequest that extends RecursiveTreeObject so that we can use it in a JFXTreeTable
 */
public class ServiceRequestTableEntry extends RecursiveTreeObject<ServiceRequestTableEntry> {
    private ServiceRequest serviceRequest;

    public ServiceRequestTableEntry(ServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    public ServiceRequest getServiceRequest() {
        return this.serviceRequest;
    }
}
