package wethinkcode.persistence;

import java.sql.Connection;

import static wethinkcode.persistence.SQLHandler.pullSQLFromFile;
import static wethinkcode.persistence.SQLHandler.runSQL;

/**
 * Exercise 3.1
 */
public class Tables {
    private final Connection connection;

    /**
     * Create an instance of the Tables object using the provided database connection
     * @param connection The JDBC connection to use
     */
    public Tables(Connection connection) {
        this.connection = connection;
    }

    /**
     * 3.1 Complete this method
     *
     * Create the Genres table
     * @return true if the table was successfully created, otherwise false
     */
    public boolean createGenres() {
        return createTable(pullSQLFromFile("sql/create_genres.sql"));
    }

    /**
     * 3.1 Complete this method
     *
     * Create the Books table
     * @return true if the table was successfully created, otherwise false
     */
    public boolean createBooks() {
        createGenres(); //I seem to need to if I keep the foreign key
        return createTable(pullSQLFromFile("sql/create_books.sql"));
    }

    /**
     * 3.1 Complete this method
     *
     * Execute a SQL statement containing an SQL command to create a table.
     * If the SQL statement is not a create statement, it should return false.
     *
     * @param sql the SQL statement containing the create command
     * @return true if the command was successfully executed, else false
     */
    protected boolean createTable(String sql) {
        if (!sql.toLowerCase().contains("create")){
            return false;
        }

        return runSQL(connection, sql);
    }
}
