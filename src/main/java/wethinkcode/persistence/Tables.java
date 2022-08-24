package wethinkcode.persistence;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

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


    /**
     * Takes a sql statement, executes it and returns true if it completes
     * @param connection to the database
     * @param sql to be executed
     * @return true if successful
     */
    public static boolean runSQL(Connection connection, String sql){
        runSQLWithResults(connection, sql);
        return true;
    }

    /**
     * Takes a sql statement, executes it and returns that statement object
     * @param connection to the database
     * @param sql to be executed
     * @return the resultant statement, containing the result set, etc
     */
    public static PreparedStatement runSQLWithResults(Connection connection, String sql){
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.execute();
            return statement;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates an sql statement from a file given as a path from the resources folder
     * @param pathFromResources a file given as a path from the resources folder
     * @return sql statement
     */
    public static String pullSQLFromFile(String pathFromResources){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            List<String> lines = Files.readAllLines(
                    Path.of(
                            Objects.requireNonNull(
                                    classLoader.getResource(pathFromResources)
                            ).toURI()
                    )
            );

            StringBuilder sql = new StringBuilder();
            for (String line : lines){
                sql.append(line.replace("\n", "").trim()).append(" ");
            }

            return sql.toString();

        }  catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
