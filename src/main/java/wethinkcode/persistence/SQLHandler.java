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
import java.util.stream.Collectors;


/**
 * Contains static methods to make running sql code easier.
 */
public class SQLHandler {
    private static boolean hasResultSet = false;


    /**
     * Executes the SQL statement on this connection,
     * which must be an SQL Data Manipulation Language (DML) statement, such as INSERT, UPDATE or DELETE;
     * or an SQL statement that returns nothing, such as a DDL statement.
     *
     * @param connection to the database
     * @param sql to be executed
     * @return True if successful
     */
    public static boolean runSQL(Connection connection, String sql){
        try {
            runSQLWithResults(connection, sql);

            if (hasResultSet){
                hasResultSet = false;
                return false;
            }

            return true;
        } catch (RuntimeException e){
            e.printStackTrace();
            return false;
        }
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
            hasResultSet = statement.execute();
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("SQL Statement failed.");
        }
    }

    /**
     * Creates a sql statement from a file given as a path from the resources folder
     * @param pathFromResources a file given as a path from the resources folder
     * @return sql statement
     */
    public static String pullSQLFromFile(String pathFromResources){
        try {
            List<String> contents = Files.readAllLines(getPathOfFileFromResources(pathFromResources));

            return contents.stream().map(String::trim).collect(Collectors.joining(" "));
        }  catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Pulling SQL from file failed");
        }
    }

    /**
     * Gets a path from a string reference.
     * Starts in the main directory of the resources for this project
     * @param pathFromResources the string referencing a file you're looking for
     * @return a path object referencing that file.
     */
    public static Path getPathOfFileFromResources(String pathFromResources){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            return Path.of(
                    Objects.requireNonNull(
                            classLoader.getResource(pathFromResources)
                    ).toURI()
            );
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get path of file");
        }
    }
}
