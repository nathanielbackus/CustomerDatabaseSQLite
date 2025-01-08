package model;

public class Division {
    private int DivisionID, CountryID;
    private String DivisionName, DivisionCreateDate, DivisionCreatedBy, DivisionLastUpdate, DivisionLastUpdatedBy;
    public Division(int DivisionID, String DivisionName){
        this.DivisionID = DivisionID;
        this.DivisionName = DivisionName;
        this.DivisionCreateDate = DivisionCreateDate;
        this.DivisionCreatedBy = DivisionCreatedBy;
        this.DivisionLastUpdate = DivisionLastUpdate;
        this.DivisionLastUpdatedBy = DivisionLastUpdatedBy;
        this.CountryID = CountryID;
    }

    public int getDivisionID() {
        return DivisionID;
    }

    public void setDivisionID(int divisionID) {
        DivisionID = divisionID;
    }

    public int getCountryID() {
        return CountryID;
    }

    public void setCountryID(int countryID) {
        CountryID = countryID;
    }

    public String getDivisionName() {
        return DivisionName;
    }

    public void setDivisionName(String divisionName) {
        DivisionName = divisionName;
    }

    public String getDivisionCreateDate() {
        return DivisionCreateDate;
    }

    public void setDivisionCreateDate(String divisionCreateDate) {
        DivisionCreateDate = divisionCreateDate;
    }

    public String getDivisionCreatedBy() {
        return DivisionCreatedBy;
    }

    public void setDivisionCreatedBy(String divisionCreatedBy) {
        DivisionCreatedBy = divisionCreatedBy;
    }

    public String getDivisionLastUpdate() {
        return DivisionLastUpdate;
    }

    public void setDivisionLastUpdate(String divisionLastUpdate) {
        DivisionLastUpdate = divisionLastUpdate;
    }

    public String getDivisionLastUpdatedBy() {
        return DivisionLastUpdatedBy;
    }

    public void setDivisionLastUpdatedBy(String divisionLastUpdatedBy) {
        DivisionLastUpdatedBy = divisionLastUpdatedBy;
    }
    @Override
    public String toString() {
        return DivisionName;
    }
}
