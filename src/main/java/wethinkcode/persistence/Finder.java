package wethinkcode.persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 3.3
 */
public class Finder {

    private final Connection connection;

    /**
     * Create an instance of the Finder object using the provided database connection
     *
     * @param connection The JDBC connection to use
     */
    public Finder(Connection connection) {
        this.connection = connection;
    }

    /**
     * 3.3 (part 1) Complete this method
     * <p>
     * Finds all genres in the database
     *
     * @return a list of `Genre` objects
     * @throws SQLException the query failed
     */
    public List<Genre> findAllGenres() throws SQLException {
        Statement results = Tables.runSQLWithResults(
                connection,
                Tables.pullSQLFromFile("sql/find_all_genres.sql")
        );

        return pullGenresFromResultSet(results.getResultSet());
    }

    /**
     * 3.3 (part 2) Complete this method
     * <p>
     * Finds all genres in the database that have specific substring in the description
     *
     * @param pattern The pattern to match
     * @return a list of `Genre` objects
     * @throws SQLException the query failed
     */
    public List<Genre> findGenresLike(String pattern) throws SQLException {
        Statement results = Tables.runSQLWithResults(
                connection,
                "SELECT * FROM Genres WHERE description LIKE '"+pattern+"'"
        );

        return pullGenresFromResultSet(results.getResultSet());
    }

    /**
     * 3.3 (part 3) Complete this method
     * <p>
     * Finds all books with their genres
     *
     * @return a list of `BookGenreView` objects
     * @throws SQLException the query failed
     */
    public List<BookGenreView> findBooksAndGenres() throws SQLException {
        Statement results = Tables.runSQLWithResults(
                connection,
                Tables.pullSQLFromFile("sql/find_all_books.sql")
        );

        return pullBookGenreViewFromResultSet(results.getResultSet());
    }

    /**
     * 3.3 (part 4) Complete this method
     * <p>
     * Finds the number of books in a genre
     *
     * @return the number of books in the genre
     * @throws SQLException the query failed
     */
    public int findNumberOfBooksInGenre(String genreCode) throws SQLException {
        Statement results = Tables.runSQLWithResults(
                connection,
                "SELECT COUNT(*) FROM Books " +
                "WHERE Books.genre_code ='"+genreCode+"'"
        );


        ResultSet resultSet = results.getResultSet();
        resultSet.next();

        return resultSet.getInt(1);
    }

    /**
     * Creates a list of Genre Objects from a valid result set
     * @param results the set of results
     * @return a list of genres
     * @throws SQLException sql error, i.e. bad result set
     */
    private List<Genre> pullGenresFromResultSet(ResultSet results) throws SQLException {
        List<Genre> genres = new ArrayList<>();

        while (results.next()){
            genres.add(new Genre(
                    results.getString("code"),
                    results.getString("description")
            ));
        }

        return genres;
    }

    /**
     * Creates a list of BookGenreView Objects from a valid result set
     * @param results the set of results
     * @return a list of book genre views
     * @throws SQLException sql error, i.e. bad result set
     */
    private List<BookGenreView> pullBookGenreViewFromResultSet(ResultSet results) throws SQLException {
        List<BookGenreView> books = new ArrayList<>();

        while (results.next()){
            books.add(new BookGenreView(
                    results.getString("title"),
                    results.getString("description")
            ));
        }

        return books;
    }
}
