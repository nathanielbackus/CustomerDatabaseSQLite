package controller;
import dao.*;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
    Customer customer;
    private ObservableList<Customer> observableCustomerList;
    private ObservableList<Appointment> observableInPersonAppointmentList, observableRemoteAppointmentList;
    @FXML
    private ToggleGroup InPersonAppointmentsTG;
    @FXML
    private TableView<Appointment> AllInPersonAppointmentsTableView;
    @FXML
    private TableColumn<?, ?> InPersonAppointmentsTBContact, InPersonAppointmentsTBCustomerID, InPersonAppointmentsTBDesc,
            InPersonAppointmentsTBID, InPersonAppointmentsTBLocation, InPersonAppointmentsTBTitle, InPersonAppointmentsTBType,
            InPersonAppointmentsTBUserID;
    @FXML
    private RadioButton AllInPersonAppointmentsRadio, WeekInPersonAppointmentsRadio, MonthInPersonAppointmentsRadio;
    @FXML
    private TableColumn<Appointment, String> InPersonAppointmentsTBStart, InPersonAppointmentsTBEnd;
    @FXML
    private ToggleGroup RemoteAppointmentsTG;
    @FXML
    private TableView<Appointment> AllRemoteAppointmentsTableView;
    @FXML
    private TableColumn<?, ?> RemoteAppointmentsTBContact, RemoteAppointmentsTBCustomerID, RemoteAppointmentsTBDesc,
            RemoteAppointmentsTBID, RemoteAppointmentsTBLocation, RemoteAppointmentsTBTitle, RemoteAppointmentsTBType,
            RemoteAppointmentsTBUserID;
    @FXML
    private RadioButton AllRemoteAppointmentsRadio, WeekRemoteAppointmentsRadio, MonthRemoteAppointmentsRadio;
    @FXML
    private TableColumn<Appointment, String> RemoteAppointmentsTBStart, RemoteAppointmentsTBEnd;
    @FXML
    private TextField OnActionSearchCustomers;
    @FXML
    private TableView<Customer> AllCustomersTableView;
    @FXML
    private TableColumn<?, ?> CustomerTBAddress, CustomerTBDivisionID, CustomerTBID, CustomerTBName, CustomerTBPhone,
            CustomerTBPostalCode;
    /**load add customer scene**/
    @FXML
    void OnActionAddCustomer(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("Customer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**load and populate update customer scene**/
    @FXML
    void OnActionUpdateCustomer(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Customer.fxml"));
        loader.load();
        CustomerController CController = loader.getController();
        CController.setCustomer(AllCustomersTableView.getSelectionModel().getSelectedItem());
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
                    observableCustomerList.remove(selectedCustomer);
                } else if (result.get() == ButtonType.CANCEL) {
                    return;
                }
            } else {
                JDBC.ErrorMessage("Error Deleting Customer", "Appointment associated with customer", "Please delete all appointments associated with this customer before trying to delete them again.");
            }
        }
    }
    @FXML
    void OnActionUsersContacts(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("UsersContacts.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**load add appointment scene**/
    @FXML
    void OnActionAddInPersonAppointment(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Appointment.fxml"));
        loader.load();
        AppointmentController AController = loader.getController();
        String appointmentType = "InPerson";
        AController.getAppointmentType(appointmentType);
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }
    @FXML
    void OnActionAddRemoteAppointment(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Appointment.fxml"));
        loader.load();
        AppointmentController AController = loader.getController();
        String appointmentType = "Remote";
        AController.getAppointmentType(appointmentType);
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**load and populate update appointment scene**/
    @FXML
    void OnActionUpdateInPersonAppointment(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Appointment.fxml"));
        loader.load();
        AppointmentController AController = loader.getController();
        String appointmentType = "InPerson";
        AController.getAppointmentType(appointmentType);
        AController.setAppointment(AllInPersonAppointmentsTableView.getSelectionModel().getSelectedItem());
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.showAndWait();
    }
    @FXML
    void OnActionUpdateRemoteAppointment(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Appointment.fxml"));
        loader.load();
        AppointmentController AController = loader.getController();
        String appointmentType = "Remote";
        AController.getAppointmentType(appointmentType);
        AController.setAppointment(AllRemoteAppointmentsTableView.getSelectionModel().getSelectedItem());
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.showAndWait();
    }

    /**delete selected item from appointment tableview and from database**/
    @FXML
    void OnActionDeleteInPersonAppointment(ActionEvent event) throws IOException, SQLException {
        Appointment selectedAppointment = AllInPersonAppointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete InPerson Appointment?");
            alert.setHeaderText("Are you sure you want to delete the selected appointment?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                AppointmentDAO.deleteAppointment(selectedAppointment);
                observableInPersonAppointmentList.remove(selectedAppointment);
            } else if (result.get() == ButtonType.CANCEL) {
                return;
            }
        }
    }
    @FXML
    void OnActionDeleteRemoteAppointment(ActionEvent event) throws IOException, SQLException {
        Appointment selectedAppointment = AllRemoteAppointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Remote Appointment?");
            alert.setHeaderText("Are you sure you want to delete the selected appointment?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                AppointmentDAO.deleteAppointment(selectedAppointment);
                observableRemoteAppointmentList.remove(selectedAppointment);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            CustomerDAO.getAllCustomers();
            observableCustomerList = FXCollections.observableArrayList(CustomerDAO.getAllCustomers());
            observableInPersonAppointmentList = FXCollections.observableArrayList(AppointmentDAO.getTimeQueryAppointments(0, "InPerson"));
            observableRemoteAppointmentList = FXCollections.observableArrayList(AppointmentDAO.getTimeQueryAppointments(0, "Remote"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        AllCustomersTableView.setItems(observableCustomerList);
        CustomerTBID.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        CustomerTBAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        CustomerTBDivisionID.setCellValueFactory(new PropertyValueFactory<>("DivisionID"));
        CustomerTBName.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        CustomerTBPhone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        CustomerTBPostalCode.setCellValueFactory(new PropertyValueFactory<>("PostalCode"));
        /**lambda to add an eventlistener so a user can easily select between the appointmenttableview data**/
        InPersonAppointmentsTG.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            RadioButton selectedRadioButton = (RadioButton) newValue;
            if (selectedRadioButton == AllInPersonAppointmentsRadio) {
                AllInPersonAppointmentsTableView.setItems(AppointmentDAO.getTimeQueryAppointments(0, "InPerson"));
            } else if (selectedRadioButton == WeekInPersonAppointmentsRadio) {
                AllInPersonAppointmentsTableView.setItems(AppointmentDAO.getTimeQueryAppointments(7, "InPerson"));
            } else if (selectedRadioButton == MonthInPersonAppointmentsRadio) {
                AllInPersonAppointmentsTableView.setItems(AppointmentDAO.getTimeQueryAppointments(30, "InPerson"));
            }
        });
        AllInPersonAppointmentsTableView.setItems(observableInPersonAppointmentList);
        InPersonAppointmentsTBID.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
        InPersonAppointmentsTBTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        InPersonAppointmentsTBDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        InPersonAppointmentsTBLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
        InPersonAppointmentsTBStart.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        InPersonAppointmentsTBEnd.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        InPersonAppointmentsTBCustomerID.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        InPersonAppointmentsTBUserID.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        InPersonAppointmentsTBContact.setCellValueFactory(new PropertyValueFactory<>("ContactID"));

        RemoteAppointmentsTG.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            RadioButton selectedRadioButton = (RadioButton) newValue;
            if (selectedRadioButton == AllRemoteAppointmentsRadio) {
                AllRemoteAppointmentsTableView.setItems(AppointmentDAO.getTimeQueryAppointments(0, "Remote"));
            } else if (selectedRadioButton == WeekRemoteAppointmentsRadio) {
                AllRemoteAppointmentsTableView.setItems(AppointmentDAO.getTimeQueryAppointments(7, "Remote"));
            } else if (selectedRadioButton == MonthRemoteAppointmentsRadio) {
                AllRemoteAppointmentsTableView.setItems(AppointmentDAO.getTimeQueryAppointments(30, "Remote"));
            }
        });
        AllRemoteAppointmentsTableView.setItems(observableRemoteAppointmentList);
        RemoteAppointmentsTBID.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
        RemoteAppointmentsTBTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        RemoteAppointmentsTBDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        RemoteAppointmentsTBLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
        RemoteAppointmentsTBStart.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        RemoteAppointmentsTBEnd.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        RemoteAppointmentsTBCustomerID.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        RemoteAppointmentsTBUserID.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        RemoteAppointmentsTBContact.setCellValueFactory(new PropertyValueFactory<>("ContactID"));

        FilteredList<Customer> searchedCustomers = new FilteredList<>(observableCustomerList, b -> true);

        OnActionSearchCustomers.textProperty().addListener((observable, oldValue, newValue) -> {
            searchedCustomers.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return item.getCustomerName().toLowerCase().contains(lowerCaseFilter) ||
                        item.getAddress().toLowerCase().contains(lowerCaseFilter);
            });
        });

        AllCustomersTableView.setItems(searchedCustomers);
    }}

