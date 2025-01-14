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
    public static ObservableList<Division> getSelectedDivisions(int country_id) {
        ObservableList<Division> divisionReturnList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM divisions WHERE Country_ID = ?;";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, country_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int DivisionID = rs.getInt("Division_id");
                String DivisionName = rs.getString("division");
                Division newDivision = new Division(DivisionID, DivisionName);
                divisionReturnList.add(newDivision);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return divisionReturnList;
    }
}