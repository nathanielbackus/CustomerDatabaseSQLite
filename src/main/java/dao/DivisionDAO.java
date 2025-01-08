package dao;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DivisionDAO {
    /**collect all division information for use in the customer add and update fields**/
    public static ObservableList<Division> getAllUSDivisions() {
        ObservableList<Division> USDivisions = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = 1;";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int DivisionID = rs.getInt("Division_ID");
                String DivisionName = rs.getString("Division");
                Division division = new Division(DivisionID, DivisionName);
                USDivisions.add(division);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return USDivisions;
    }
    public static ObservableList<Division> getAllUKDivisions() {
        ObservableList<Division> UKDivisions = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = 2;";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int DivisionID = rs.getInt("Division_ID");
                String DivisionName = rs.getString("Division");
                Division division = new Division(DivisionID, DivisionName);
                UKDivisions.add(division);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return UKDivisions;
    }
    public static ObservableList<Division> getAllCADivisions() {
        ObservableList<Division> CADivisions = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = 3;";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int DivisionID = rs.getInt("Division_ID");
                String DivisionName = rs.getString("Division");
                Division division = new Division(DivisionID, DivisionName);
                CADivisions.add(division);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return CADivisions;
    }
}