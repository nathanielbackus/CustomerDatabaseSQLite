package dao;
import model.Appointment;
import helper.JDBC;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppointmentDAOTest {

    @BeforeAll
    static void setup() {
        JDBC.openConnection();
    }

    @AfterAll
    static void tearDown() {
        JDBC.closeConnection();
    }

    @Test
    void testAddAppointment() throws SQLException {
        int newID = AppointmentDAO.appointmentGenerateID();
        Appointment testAppointment = new Appointment(newID, "Test Meeting", "Test Description", "Test Location", "InPerson",
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), 1, 1, 1);

        int rowsAffected = AppointmentDAO.addAppointment(testAppointment.getAppointmentID(), testAppointment.getTitle(),
                testAppointment.getDescription(), testAppointment.getLocation(), testAppointment.getType(),
                testAppointment.getStartTime(), testAppointment.getEndTime(), "TestUser", testAppointment.getCustomerID(),
                testAppointment.getUserID(), testAppointment.getContactID());

        assertEquals(1, rowsAffected);
    }

    @Test
    void testGetAllAppointments() throws SQLException {
        List<Appointment> appointments = AppointmentDAO.getTypedAppointments("All");
        assertNotNull(appointments);
        assertTrue(appointments.size() > 0);
    }

    @Test
    void testDeleteAppointment() throws SQLException {
        List<Appointment> appointments = AppointmentDAO.getTypedAppointments("All");
        if (!appointments.isEmpty()) {
            Appointment lastAppointment = appointments.get(appointments.size() - 1);
            boolean deleted = AppointmentDAO.deleteAppointment(lastAppointment);
            assertTrue(deleted);
        }
    }
}
