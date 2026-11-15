package dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Base DAO class managing JDBC connection to MySQL.
 */
public class DAO4 {
    public static Connection con;

    private static final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/supermarket_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "vuighe.net";

    private static String dbDriver = DEFAULT_DRIVER;
    private static String dbUrl = DEFAULT_URL;
    private static String dbUser = DEFAULT_USER;
    private static String dbPassword = DEFAULT_PASSWORD;

    static {
        try (InputStream in = DAO4.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                Properties props = new Properties();
                props.load(in);
                dbDriver = props.getProperty("db.driver", DEFAULT_DRIVER).trim();
                dbUrl = props.getProperty("db.url", DEFAULT_URL).trim();
                dbUser = props.getProperty("db.user", DEFAULT_USER).trim();
                dbPassword = props.getProperty("db.password", DEFAULT_PASSWORD);
            }
            Class.forName(dbDriver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DAO4() {
        try {
            if (con == null || con.isClosed()) {
                con = openConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dedicated JDBC connection for thread-safe use inside try-with-resources.
     */
    protected Connection openConnection() throws SQLException {
        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found: " + dbDriver, e);
        }
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
}
