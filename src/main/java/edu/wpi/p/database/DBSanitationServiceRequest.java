package edu.wpi.p.database;

import edu.wpi.p.views.SanitationServiceRequest;

import java.util.ArrayList;
import java.util.List;

public class DBSanitationServiceRequest {

    private String sanitationTable = "ST";
    private List<DBColumn> sanitationColumn;


    private void init() {
        sanitationColumn = new ArrayList<>();
        sanitationColumn.add(new DBColumn("Full Name", "varchar(256)", ""));
        sanitationColumn.add(new DBColumn("Room Number", "varchar(256)", ""));
        sanitationColumn.add(new DBColumn("Type of Sanitation", "varchar(256)", ""));
        sanitationColumn.add(new DBColumn("Additional Information", "varchar(256)", ""));

    }


    public void clearSanitation() {
        DatabaseInterface.executeUpdate("DROP TABLE " + sanitationTable);
    }

    private void createTable(boolean doClear) {
        if (doClear) {
            clearSanitation();
        }
        init();
        DatabaseInterface.createTableIfNotExists(sanitationTable, this.sanitationColumn);

    }


    public DBSanitationServiceRequest(){
        createTable(false);
    }

    public DBSanitationServiceRequest(String tableName) {
        sanitationTable = tableName;
        createTable(false);
    }


    public void updateSanitation(SanitationServiceRequest san) {
        DatabaseInterface.executeUpdate("UPDATE " + sanitationTable + " SET NAME = '" + san.fullNameText.getText() + "' WHERE TITLE = '" + san.getTitle().getText() + "'");
        DatabaseInterface.executeUpdate("UPDATE " + sanitationTable + " SET ROOMNUMBER = '" + san.roomNumberText.getText()  + "' WHERE TITLE = '" + san.getTitle().getText()+ "'");
        DatabaseInterface.executeUpdate("UPDATE " + sanitationTable + " SET ADDITIONALINFO = '" + san.additionalInfoText.getText() + "' WHERE TITLE = '" + san.getTitle().getText()+ "'");
        DatabaseInterface.executeUpdate("UPDATE " + sanitationTable + " SET TYPEOFSANITATION = '" + san.typeOfSanitationBox.getItems() + "' WHERE TITLE = '" + san.getTitle().getText()+ "'");
    }


    public void addSanitation(SanitationServiceRequest sanitation) {
        String insertValue = "'" + sanitation.getFullNameText().getText() + "', '" +
                sanitation.getRoomNumberText().getText() + "', '" +
                sanitation.getTypeOfSanitationBox().getValue() + "', '" +
                sanitation.getAdditionalInfoText().getText();
        DatabaseInterface.insertIntoTable(sanitationTable, insertValue);
    }

    public void removeSanitation(String sanitationTitle) {
        String removeServiceRequestCommand = "DELETE FROM " + sanitationTable + " WHERE ID='" + sanitationTitle + "'";
        DatabaseInterface.executeUpdate(removeServiceRequestCommand);
    }

    private int indexOfColumnByName(List<DBColumn> columns, String targetName) {
        int i = 0;
        for(DBColumn c : columns) {
            if(c.getName().equals(targetName.toUpperCase())) {
                return i;
            }
            i++;
        }
        return -1;
    }



}
