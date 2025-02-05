package model;

import helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Country {
    private int CountryID;
    private String CountryName;
    public Country(int CountryID, String Country){
        this.CountryID = CountryID;
        this.CountryName = Country;
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

    @Override
    public String toString() {
        return CountryName;
    }
}
