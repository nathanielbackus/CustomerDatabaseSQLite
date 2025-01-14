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
    private static final String databaseName = "client_schedule.db"; // SQLite file name
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
                password TEXT NOT NULL,
                Create_Date DATETIME NOT NULL,
                created_by TEXT NOT NULL
            );
        """;

        String createContactsTable = """
            CREATE TABLE IF NOT EXISTS contacts (
                contact_id INTEGER PRIMARY KEY AUTOINCREMENT,
                contact_name TEXT NOT NULL,
                contact_email TEXT NOT NULL
            );
        """;

        String createAppointmentsTable = """
            CREATE TABLE IF NOT EXISTS appointments (
            appointment_id INTEGER PRIMARY KEY AUTOINCREMENT,
            customer_id INTEGER NOT NULL,
            user_id INTEGER NOT NULL,
            contact_id INTEGER NOT NULL,
            title TEXT NOT NULL,
            description TEXT NOT NULL,
            location TEXT NOT NULL,
            type TEXT NOT NULL,
            start DATETIME NOT NULL,
            end DATETIME NOT NULL,
            createdOn DATETIME NOT NULL,
            createdBy TEXT NOT NULL,
            updatedOn DATETIME NOT NULL,
            updatedBy TEXT NOT NULL,
            FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
            FOREIGN KEY (user_id) REFERENCES users(user_id),
            FOREIGN KEY (contact_id) REFERENCES contacts(contact_id)
            );
        """;


        String createCountryTable = """
            CREATE TABLE IF NOT EXISTS countries (
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
                phone TEXT NOT NULL,
                createdBy TEXT,
                createdOn DATETIME,
                updatedBy TEXT,
                updatedOn DATETIME,
                division_id INTEGER NOT NULL,
                FOREIGN KEY (division_id) REFERENCES divisions(division_id)
            );
        """;

        //division
        String createDivisionsTable = """
            CREATE TABLE IF NOT EXISTS divisions (
                division_id INTEGER PRIMARY KEY AUTOINCREMENT,
                division TEXT NOT NULL,
                createOn DATETIME NOT NULL,
                createdBy TEXT NOT NULL,
                updatedOn DATETIME NOT NULL,
                updatedBy TEXT NOT NULL,
                country_id INTEGER NOT NULL,
                FOREIGN KEY (country_id) REFERENCES countries(country_id)
            );
        """;

        //reportcontact

        //report customer

        //type month match??

        //relational tables?

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
            System.out.println("Foreign key constraints enabled.");
            stmt.execute(createUsersTable);
            stmt.execute(createContactsTable);
            stmt.execute(createCountryTable);
            stmt.execute(createDivisionsTable);
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


    //prepopulates first creation of database with country and division data once
    public static String populateCountry() {
        String sql = "INSERT INTO countries (Country_ID, Country_name, CreatedOn, CreatedBy, UpdatedOn, UpdatedBy) VALUES " +
                "(1, 'U.S', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script'), " +
                "(2, 'UK', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script'), " +
                "(3, 'Canada', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script')";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return "Country tables successfully populated";
    }

    public static String populateDivision() {
        String sql =
                "INSERT INTO divisions (division_ID, division, createOn, createdBy, updatedOn, updatedBy, country_ID) VALUES" +
                "(1, 'Alabama', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(2, 'Arizona', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(3, 'Arkansas', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(4, 'California', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(5, 'Colorado', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(6, 'Connecticut', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(7, 'Delaware', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(8, 'District of Columbia', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(9, 'Florida', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(10, 'Georgia', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(11, 'Idaho', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(12, 'Illinois', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(13, 'Indiana', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(14, 'Iowa', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(15, 'Kansas', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(16, 'Kentucky', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(17, 'Louisiana', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(18, 'Maine', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(19, 'Maryland', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(20, 'Massachusetts', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(21, 'Michigan', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(22, 'Minnesota', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(23, 'Mississippi', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(24, 'Missouri', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1)," +
                "(25, 'Montana', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(26, 'Nebraska', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(27, 'Nevada', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(28, 'New Hampshire', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(29, 'New Jersey', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(30, 'New Mexico', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(31, 'New York', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(32, 'North Carolina', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(33, 'North Dakota', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(34, 'Ohio', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(35, 'Oklahoma', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(36, 'Oregon', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(37, 'Pennsylvania', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(38, 'Rhode Island', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(39, 'South Carolina', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(40, 'South Dakota', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(41, 'Tennessee', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(42, 'Texas', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(43, 'Utah', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(44, 'Vermont', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(45, 'Virginia', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(46, 'Washington', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(47, 'West Virginia', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(48, 'Wisconsin', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(49, 'Wyoming', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(52, 'Hawaii', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(54, 'Alaska', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 1), " +
                "(60, 'Northwest Territories', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 3), " +
                "(61, 'Alberta', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 3)," +
                "(62, 'British Columbia', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 3)," +
                "(63, 'Manitoba', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 3)," +
                "(64, 'New Brunswick', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 3)," +
                "(65, 'Nova Scotia', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 3)," +
                "(66, 'Prince Edward Island', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 3)," +
                "(67, 'Ontario', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 3), " +
                "(68, 'Québec', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 3), " +
                "(69, 'Saskatchewan', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 3), " +
                "(70, 'Nunavut', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 3), " +
                "(71, 'Yukon', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 3), " +
                "(72, 'Newfoundland and Labrador', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 3), " +
                "(101, 'England', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 2), " +
                "(102, 'Wales', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 2), " +
                "(103, 'Scotland', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 2), " +
                "(104, 'Northern Ireland', '2024-06-09 17:16:00', 'script', '2024-06-09 17:16:00', 'script', 2);";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return "Division tables successfully populated";
    }

}
