package dm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Encapsulate JDBC settings for database access
 */
public class DB {
    private String driver;
    private String URLdb;
    private Connection connection;

    private DB(String dbName) throws ClassNotFoundException, SQLException {
        // Derby JDBC driver
        this.driver = "org.apache.derby.jdbc.ClientDriver";
        // JDBC URL to access Derby database (db will be created if it doesn't exist)
        this.URLdb = "jdbc:derby://localhost:1527/" + dbName + ";create=true";
        // driver loading
        Class.forName(driver);
        // connection to database
        this.connection = DriverManager.getConnection(URLdb);
    }

    /**
     * Factory method to create a DB object
     * @param dbName database name
     * @return DB object used to create prepared statement
     * @throws Exception if wrong parameters
     */
    public static DB createDB(String dbName) throws Exception {
        if (dbName == null || "".equals(dbName))
            throw new IllegalArgumentException("DB:: Database name is null or empty");
        else
            return new DB(dbName);
    }

    /**
     * Create a SQL prepared (templated) statement
     * @param stmtString SQL statement (with '?' for parameters)
     * @return a prepared statement
     * @throws SQLException for wrong statement
     */
    public PreparedStatement prepare(String stmtString)
            throws SQLException {
        return this.connection.prepareStatement(stmtString);
    }
}
