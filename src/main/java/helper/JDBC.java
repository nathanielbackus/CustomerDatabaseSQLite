package helper;

import javafx.scene.control.Alert;
import model.Country;

import java.sql.*;
import java.time.*;

public abstract class JDBC {
    /** Interface for lambda conversion **/
    @FunctionalInterface
    public static interface LocalDateTimeConverter {
        LocalDateTime convert(LocalDateTime time);
    }

    private static final String protocol = "jdbc";
    private static final String vendor = ":sqlite:";
    private static final String databaseName = "client_schedule.sqlite"; // SQLite file name
    private static final String jdbcUrl = protocol + vendor + databaseName; // SQLite connection URL

    public static Connection connection;

    /** Opens a connection to the SQLite database and initializes it if necessary **/
    public static void openConnection() {
        try {
            connection = DriverManager.getConnection(jdbcUrl);
            System.out.println("Connected to SQLite database!");

            // Initialize database if needed
            initializeDatabase();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /** Closes the connection to the database **/
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /** Initializes the database schema if it doesn't already exist **/
    private static void initializeDatabase() {
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL
            );
        """;

        String createAppointmentsTable = """
            CREATE TABLE IF NOT EXISTS appointments (
                appointment_id INTEGER PRIMARY KEY AUTOINCREMENT,
                customer_id INTEGER NOT NULL,
                FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
                user_id INTEGER NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(user_id)
                contact_id INTEGER NOT NULL,
                FOREIGN KEY (contact_id) REFERENCES contacts(contact_id)
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                location TEXT NOT NULL,
                type TEXT NOT NULL,
                start DATETIME NOT NULL,
                end DATETIME NOT NULL,
                createdOn DATETIME NOT NULL,
                createdBy TEXT NOT NULL,
                updatedOn DATETIME NOT NULL,
                updatedBy TEXTNOT NULL
            );
        """;

        String createContactsTable = """
            CREATE TABLE IF NOT EXISTS contacts (
                contact_id INTEGER PRIMARY KEY AUTOINCREMENT,
                contact_name TEXT NOT NULL,
                contact_email TEXT NOT NULL
            );
        """;

        String createCountryTable = """
            CREATE TABLE IF NOT EXISTS country (
                country_id INTEGER PRIMARY KEY AUTOINCREMENT,
                country_name TEXT NOT NULL,
                createdOn DATETIME NOT NULL,
                createdBy TEXT NOT NULL,
                updatedOn DATETIME NOT NULL,
                updatedBy TEXT NOT NULL
            );
        """;

        String createCustomersTable = """
            CREATE TABLE IF NOT EXISTS customers (
                customer_id INTEGER PRIMARY KEY AUTOINCREMENT,
                customer_name TEXT NOT NULL,
                address TEXT NOT NULL,
                postal_code TEXT NOT NULL,
                phone TEXT NOT NULL
            );
        """;

        //division

        //reportcontact

        //report customer

        //type month match??

        //relational tables?

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createCustomersTable);
            stmt.execute(createAppointmentsTable);
            System.out.println("Database schema initialized.");
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    /** Helper function to show error alerts **/
    public static void ErrorMessage(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /** Helper function for information alerts **/
    public static void InformationMessage(String title, String header) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    /** Helper function to convert a timestamp to the user's time zone **/
    public static ZonedDateTime TimeStampToUserZone(Timestamp time) {
        LocalDateTime tempTime = time.toLocalDateTime();
        ZonedDateTime utcZonedDateTime = tempTime.atZone(ZoneId.of("UTC"));
        ZoneId userZoneId = ZoneId.systemDefault();
        return utcZonedDateTime.withZoneSameInstant(userZoneId);
    }

    /** Helper function to convert a LocalDateTime to UTC **/
    public static LocalDateTime convertToUTC(LocalDateTime localTime, ZoneId zoneId) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localTime, zoneId);
        ZonedDateTime utcZonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        return utcZonedDateTime.toLocalDateTime();
    }

    /** Helper function to convert a time to EST **/
    public static LocalTime convertToEST(LocalTime localTime, ZoneId zoneId) {
        LocalDate currentDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(currentDate, localTime);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
        ZonedDateTime estZonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        return estZonedDateTime.toLocalTime();
    }
}
