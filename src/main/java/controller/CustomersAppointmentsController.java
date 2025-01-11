package controller;
import dao.*;
import helper.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomersAppointmentsController implements Initializable {
    /**scene elements**/
    Stage stage;
    Parent scene;
    @FXML
    private TableView<Appointment> AllAppointmentsTableView;
    @FXML
    private ToggleGroup AppointmentsTG;
    @FXML
    private TableView<Customer> AllCustomersTableView;
    @FXML
    private TableColumn<?, ?> AppointmentsTBContact, AppointmentsTBCustomerID, AppointmentsTBDesc,
            AppointmentsTBID, AppointmentsTBLocation, AppointmentsTBTitle, AppointmentsTBType,
            AppointmentsTBUserID;
    @FXML
    private RadioButton AllAppointmentsRadio, WeekAppointmentsRadio, MonthAppointmentsRadio;
    @FXML
    private TableColumn<Appointment, String> AppointmentsTBStart, AppointmentsTBEnd;
    @FXML
    private TableColumn<?, ?> CustomerTBAddress, CustomerTBDivisionID, CustomerTBID, CustomerTBName, CustomerTBPhone,
            CustomerTBPostalCode;
    /**load add customer scene**/
    @FXML
    void OnActionAddCustomer(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AddCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**load and populate update customer scene**/
    @FXML
    void OnActionUpdateCustomer(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("UpdateCustomer.fxml"));
        loader.load();
        UpdateCustomerController UCController = loader.getController();
        UCController.setCustomer(AllCustomersTableView.getSelectionModel().getSelectedItem());
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.showAndWait();
    }
    /**delete selected tableview item from customer tableview and from database**/
    @FXML
    public void OnActionDeleteCustomer(ActionEvent event) throws IOException, SQLException {
        Customer selectedCustomer = AllCustomersTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            int customerID = selectedCustomer.getCustomerID();
            String sql = "SELECT * FROM appointments WHERE Customer_ID = ?;";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, customerID);
            ResultSet rowsAffected = ps.executeQuery();
            if (!rowsAffected.next()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Customer?");
                alert.setHeaderText("Are you sure you want to delete the selected customer?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    CustomerDAO.deleteCustomer(selectedCustomer);
                } else if (result.get() == ButtonType.CANCEL) {
                    return;
                }
            } else {
                JDBC.ErrorMessage("Error Deleting Customer", "Appointment associated with customer", "Please delete all appointments associated with this customer before trying to delete them again.");
            }
        }
    }
    /**load add appointment scene**/
    @FXML
    void OnActionAddAppointment(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AddAppointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**load and populate update appointment scene**/
    @FXML
    void OnActionUpdateAppointment(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("UpdateAppointment.fxml"));
        loader.load();
        UpdateAppointmentController UAController = loader.getController();
        UAController.setAppointment(AllAppointmentsTableView.getSelectionModel().getSelectedItem());
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.showAndWait();
    }
    /**delete selected item from appointment tableview and from database**/
    @FXML
    void OnActionDeleteAppointment(ActionEvent event) throws IOException, SQLException {
        Appointment selectedAppointment = AllAppointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Appointment?");
            alert.setHeaderText("Are you sure you want to delete the selected appointment?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                AppointmentDAO.deleteAppointment(selectedAppointment);
            } else if (result.get() == ButtonType.CANCEL) {
                return;
            }
        }
    }
    /**load reports scene**/
    @FXML
    void OnActionReports(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("Reports.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**load login scene**/
    @FXML
    void OnActionLogOut(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**close application**/
    @FXML
    void OnActionExit(ActionEvent event) throws IOException {
        JDBC.closeConnection();
        System.exit(0);
    }
    /**initialize loads all data for the tableviews, and holds toggleproperty for appointments in the next 7 days or 30 days
     * <p>contains a lambda to add an eventlistener so a user can easily select between the appointmenttableview data:</p>
     * <p>AppointmentsTG.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {</p>
     * <p>     if (newValue == null) {</p>
     * <p>         return;</p>
     * <p>     }</p>
     * <p>     RadioButton selectedRadioButton = (RadioButton) newValue;</p>
     * <p>     if (selectedRadioButton == AllAppointmentsRadio) {</p>
     * <p>     AllAppointmentsTableView.setItems(AppointmentDAOImpl.getAllAppointments(0));</p>
     * <p>     } else if (selectedRadioButton == WeekAppointmentsRadio) {</p>
     * <p>     AllAppointmentsTableView.setItems(AppointmentDAOImpl.getAllAppointments(7));</p>
     * <p>     } else if (selectedRadioButton == MonthAppointmentsRadio) {</p>
     * <p>     AllAppointmentsTableView.setItems(AppointmentDAOImpl.getAllAppointments(30));</p>
     * <p>     }</p>
     * <p>});</p>
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        JDBC.openConnection();
//        try {
//            CustomerDAOImpl.loadAllCustomers();
//            AppointmentDAOImpl.loadAllAppointments();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        AllCustomersTableView.setItems(CustomerDAOImpl.getAllCustomers());
//        CustomerTBID.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
//        CustomerTBAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
//        CustomerTBDivisionID.setCellValueFactory(new PropertyValueFactory<>("DivisionID"));
//        CustomerTBName.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
//        CustomerTBPhone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
//        CustomerTBPostalCode.setCellValueFactory(new PropertyValueFactory<>("PostalCode"));
//        /**lambda to add an eventlistener so a user can easily select between the appointmenttableview data**/
//        AppointmentsTG.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue == null) {
//                return;
//            }
//            RadioButton selectedRadioButton = (RadioButton) newValue;
//            if (selectedRadioButton == AllAppointmentsRadio) {
//                AllAppointmentsTableView.setItems(AppointmentDAOImpl.getAllAppointments(0));
//            } else if (selectedRadioButton == WeekAppointmentsRadio) {
//                AllAppointmentsTableView.setItems(AppointmentDAOImpl.getAllAppointments(7));
//            } else if (selectedRadioButton == MonthAppointmentsRadio) {
//                AllAppointmentsTableView.setItems(AppointmentDAOImpl.getAllAppointments(30));
//            }
//        });
//        AllAppointmentsTableView.setItems(AppointmentDAOImpl.getAllAppointments(0));
//        AppointmentsTBID.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
//        AppointmentsTBTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
//        AppointmentsTBDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
//        AppointmentsTBLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
//        AppointmentsTBType.setCellValueFactory(new PropertyValueFactory<>("Type"));
//        AppointmentsTBStart.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
//        AppointmentsTBEnd.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
//        AppointmentsTBCustomerID.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
//        AppointmentsTBUserID.setCellValueFactory(new PropertyValueFactory<>("UserID"));
//        AppointmentsTBContact.setCellValueFactory(new PropertyValueFactory<>("ContactID"));
    }}

