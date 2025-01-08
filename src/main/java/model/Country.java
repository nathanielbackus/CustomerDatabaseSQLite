package model;

import helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Country {
    private int CountryID;
    private String CountryName, CountryCreateDate, CountryCreatedBy, CountryLastUpdate, CountryLastUpdatedBy;
    public Country(int CountryID, String Country){
        this.CountryID = CountryID;
        this.CountryName = Country;
        this.CountryCreateDate = CountryCreateDate;
        this.CountryCreatedBy = CountryCreatedBy;
        this.CountryLastUpdate = CountryLastUpdate;
        this.CountryLastUpdatedBy = CountryLastUpdatedBy;
    }

    public int getCountryID() {
        return CountryID;
    }

    public void setCountryID(int countryID) {
        CountryID = countryID;
    }
    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(int countryName) {
        CountryID = countryName;
    }

    public java.lang.String getCountryCreateDate() {
        return CountryCreateDate;
    }

    public void setCountryCreateDate(java.lang.String countryCreateDate) {
        CountryCreateDate = countryCreateDate;
    }

    public java.lang.String getCountryCreatedBy() {
        return CountryCreatedBy;
    }

    public void setCountryCreatedBy(java.lang.String countryCreatedBy) {
        CountryCreatedBy = countryCreatedBy;
    }

    public java.lang.String getCountryLastUpdate() {
        return CountryLastUpdate;
    }

    public void setCountryLastUpdate(java.lang.String countryLastUpdate) {
        CountryLastUpdate = countryLastUpdate;
    }

    public java.lang.String getCountryLastUpdatedBy() {
        return CountryLastUpdatedBy;
    }

    public void setCountryLastUpdatedBy(java.lang.String countryLastUpdatedBy) {
        CountryLastUpdatedBy = countryLastUpdatedBy;
    }


    @Override
    public String toString() {
        return CountryName;
    }
}
