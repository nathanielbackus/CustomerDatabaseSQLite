package controller;
import dao.AppointmentDAO;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Contact;
import model.TypeMonthMatch;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {
    /**scene elements**/
    Stage stage;
    Parent scene;
    @FXML
    private TableView<TypeMonthMatch> Reports1TableView;
    @FXML
    private TableView Reports2TableView, Reports3TableView;
    @FXML
    private TableColumn<?, ?> Reports1AppointmentMonth, Reports1AppointmentType;
    @FXML
    private TableColumn<?, ?> Reports1TotalAppointments;
    @FXML
    private TableColumn<?, ?> Reports2CustomerID, Reports2TotalAppointments;
    @FXML
    private TableColumn<?, ?> Reports3AppointmentDescription, Reports3AppointmentID, Reports3AppointmentLocation,
            Reports3AppointmentTitle, Reports3AppointmentType, Reports3CustomerID, Reports3EndDate, Reports3StartDate,
            Reports3UserID;
    @FXML
    private ComboBox<Contact> Reports3ContactComboBox;
    /**load customerappointments scene**/
    @FXML
    void OnActionCustomerAppointments(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("CustomersAppointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**populate contact combobox with list of contacts, load all data from reportcontact, then populate tableview
     * <p>contains lambda to improve the code via an event listener for the contact combobox, easier for the user to load data belonging to a contact:</p>
     * <p>Reports3ContactComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {</p>
     * <p>     if (newValue == null) {</p>
     * <p>         return;</p>
     * <p>     }</p>
     * <p>     int contactId = newValue.getContactID();</p>
     * <p>     Reports3TableView.setItems(AppointmentDAO.getContactAppointments(contactId));</p>
     * <p>});</p>
     * **/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Contact> contactsNameList = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();
            String sql = "SELECT * FROM Contacts";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int contactID = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("email");
                contactsNameList.add(new Contact(contactID,name,email));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Reports3ContactComboBox.setItems(contactsNameList);
        Reports3ContactComboBox.getSelectionModel().select(0);
        Reports3TableView.setItems(AppointmentDAO.getContactAppointments(1));
        /**lambda to improve the code, made an event listener for the contact combobox, easier for the user to load data belonging to a contact**/
        Reports3ContactComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            int contactId = newValue.getContactID();
            Reports3TableView.setItems(AppointmentDAO.getContactAppointments(contactId));
        });
        Reports3AppointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        Reports3AppointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        Reports3AppointmentType.setCellValueFactory(new PropertyValueFactory<>("type"));
        Reports3AppointmentDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        Reports3AppointmentLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        Reports3StartDate.setCellValueFactory(new PropertyValueFactory<>("start"));
        Reports3EndDate.setCellValueFactory(new PropertyValueFactory<>("end"));
        Reports3CustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        Reports3UserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        Reports1TableView.setItems(AppointmentDAO.getTypeMonthAppointments());
        Reports1AppointmentType.setCellValueFactory(new PropertyValueFactory<>("type"));
        Reports1AppointmentMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
        Reports1TotalAppointments.setCellValueFactory(new PropertyValueFactory<>("count"));
        Reports2TableView.setItems(AppointmentDAO.getCustomerAppointmentTotal());
        Reports2CustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        Reports2TotalAppointments.setCellValueFactory(new PropertyValueFactory<>("appointmentCount"));
    }
}
