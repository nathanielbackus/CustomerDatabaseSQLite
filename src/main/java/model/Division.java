package model;

public class Division {
    private int DivisionID, CountryID;
    private String DivisionName;
    public Division(int DivisionID, String DivisionName){
        this.DivisionID = DivisionID;
        this.DivisionName = DivisionName;
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

    @Override
    public String toString() {
        return DivisionName;
    }
}
