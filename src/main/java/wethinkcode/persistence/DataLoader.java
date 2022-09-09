package wethinkcode.persistence;

import java.sql.*;
import java.util.List;
import java.util.Map;

import static wethinkcode.persistence.SQLHandler.runSQL;
import static wethinkcode.persistence.SQLHandler.runSQLWithResults;

/**
 * Exercise 3.2
 */
public class DataLoader {
    private final Connection connection;

    /**
     * These are the Genres that must be persisted to the database
     */
    private final Map<String, Genre> genres = Map.of(
            "PROG", new Genre("PROG", "Programming"),
            "BIO", new Genre("BIO", "Biography"),
            "SCIFI", new Genre("SCIFI", "Science Fiction"));

    /**
     * These are the Books that must be persisted to the database
     */
    private final List<Book> books = List.of(
            new Book("Test Driven Development", genres.get("PROG")),
            new Book("Programming in Haskell", genres.get("PROG")),
            new Book("Scatterlings of Africa", genres.get("BIO")));

    /**
     * Create an instance of the DataLoader object using the provided database connection
     *
     * @param connection The JDBC connection to use
     */
    public DataLoader(Connection connection) {
        this.connection = connection;
    }

    /**
     * 3.2 (part 1) Complete this method
     * <p>
     * Inserts data from the `Genres` collection into the `Genres` table.
     *
     * @return true if the data was successfully inserted, otherwise false
     */
    public boolean insertGenres() {
        boolean result = true;

        for (Genre genre : genres.values()){
            result = insertGenre(genre);
            if (!result) break;
        }

        return result;
    }

    /**
     * Inserts a genre to the Genres table
     * @param genre to be inserted
     * @return true if it succeeds
     */
    public boolean insertGenre(Genre genre){
        return runSQL(
                connection,
                "INSERT INTO Genres " +
                    "(code, description) " +
                "VALUES " +
                    "('"+genre.getCode()+"', '"+genre.getDescription()+"')"
        );
    }

    /**
     * 3.2 (part 1) Complete this method
     * <p>
     * Inserts data from the `Books` collection into the `Books` table.
     *
     * @return true if the data was successfully inserted, otherwise false
     */
    public List<Book> insertBooks() {
        boolean result = insertGenres();

        for (Book book : books){
            if (!result) break;
            result = insertBook(book);
        }

        return books;
    }

    /**
     * Inserts a book to the Books table, and assigns that book an ID
     * @param book to be inserted
     * @return true if it succeeds
     */
    public boolean insertBook(Book book){
        PreparedStatement results = runSQLWithResults(
                connection,
                "INSERT INTO Books " +
                    "(title, genre_code) " +
                "VALUES " +
                    "('"+book.getTitle()+"', '"+book.getGenre().getCode()+"');"
        );

        try {
            book.assignId(getGeneratedId(results));
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }


    /**
     * Get the last id generated from the prepared statement
     *
     * @param s the prepared statement
     * @return the last id generated
     * @throws SQLException if the id was not generated
     */
    private int getGeneratedId(PreparedStatement s) throws SQLException {
        ResultSet generatedKeys = s.getGeneratedKeys();
        if (!generatedKeys.next()) throw new SQLException("Id was not generated");
        return generatedKeys.getInt(1);
    }
}


