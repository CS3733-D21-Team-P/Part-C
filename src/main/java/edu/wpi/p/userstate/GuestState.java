package edu.wpi.p.userstate;

import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.rowdata.ServiceRequest;

import java.util.List;
import java.util.stream.Collectors;

public class GuestState extends UserState {

    public GuestState() {
        super();

        // sets the users approved for entry state based on the database
        ServiceRequest userCovidEntry = getCurrentUserCovidEntryRequest();

        if (userCovidEntry != null) {
            System.out.println("user covid entry is not null");
            User user = User.getInstance();
            user.setApprovedForEntry(userCovidEntry.getCompleted());
            if (userCovidEntry.getCompleted()) {
                System.out.println("user covid entry is completed");
                if (userCovidEntry.getDetailNames().contains("Is Covid Risk")) {
                    user.setEntryLocation(UserEntryLocation.EMERGENCY_ENTRANCE);
                }
                else {
                    user.setEntryLocation(UserEntryLocation.MAIN_ENTRANCE);
                }
            }
            else {
                System.out.println("user is not approved to enter");
                User.getInstance().setApprovedForEntry(false);
            }
        }
        else {
            System.out.println("user is not approved to enter");
            User.getInstance().setApprovedForEntry(false);
        }

    }

    private ServiceRequest getCurrentUserCovidEntryRequest() {
        List<ServiceRequest> covidEntryRequests = getCovidEntryServiceRequests();
        for(ServiceRequest covidRequest : covidEntryRequests) {
            String id = covidRequest.getDetailsMap().get("UserID");
            if (id != null && id.equals(User.getInstance().getId())) {
                return covidRequest;
            }
        }
        return null;
    }

    private List<ServiceRequest> getCovidEntryServiceRequests() {
        DBServiceRequest dbServiceRequest = DBServiceRequest.getInstance();
        List<ServiceRequest> requestList = dbServiceRequest.getServiceRequests();
        String entryRequestName = "Covid Entry Request";
        return requestList.stream().filter(serviceRequest -> serviceRequest.getName().equals(entryRequestName)).collect(Collectors.toList());
    }

    @Override
    boolean isGuest() {
        return true;
    }

    @Override
    boolean isLoggedIn() {
        return false;
    }

    @Override
    void login(String username, String password){
        // can't log in while a guest, must log out first
    }

    @Override
    void logout() {
        User u = User.getInstance();
        u.reset();
        u.changeState(new LoggedOutState());
    }


}
