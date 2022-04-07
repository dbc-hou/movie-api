package data;

import java.sql.*;
import java.util.ArrayList;

import com.mysql.cj.jdbc.Driver;
// ...

public class MovieJDBC {

    public static void main(String[] args) throws SQLException {
        DriverManager.registerDriver(new Driver());
        Connection connection = DriverManager.getConnection(
                Config.myDBConn,
                Config.myDBID,
                Config.myDBPW);
        ArrayList<Movie> myMovies = fetchAllRecords(connection);
// List all movies
        System.out.println("This is the current list of movies:");
        for (Movie m : myMovies) {
            System.out.println(m);
        }
        System.out.println();
// Add a movie
        addMovie(connection);
        System.out.println();
// Modify a movie
        updateMovie(connection,3);
        System.out.println();
// Delete all movies with id greater than 3
        deleteMovie(connection,3);
        connection.close();

    }

    public static ArrayList<Movie> fetchAllRecords(Connection conn) throws SQLException {
        ArrayList<Movie> movies = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("Select * from movies");
        while (rs.next()) {
            Movie m = new Movie();
            m.setId(rs.getInt("id"));
            m.setTitle(rs.getString("title"));
            m.setYear(rs.getInt("year"));
            m.setGenre(rs.getString("genre"));
            m.setRating(rs.getDouble("rating"));
            m.setDirector(rs.getString("director"));
            movies.add(m);
        }
        rs.close();
        stmt.close();
        return movies;
    }

    public static void addMovie (Connection conn) throws SQLException {
        Statement insertStmt = conn.createStatement();
        insertStmt.executeUpdate("Insert into movies (title, year, genre, rating, director) " +
                "values ('The Player', 1992, 'Mystery', 4.4, 'Robert Altman')",Statement.RETURN_GENERATED_KEYS);
        ArrayList<Movie> movies = fetchAllRecords(conn);
        System.out.println("This is the list of movies with a new one added.");
        for (Movie m : movies) {
            System.out.println(m);
        }
        insertStmt.close();
    }

    public static void updateMovie (Connection conn, int id) throws SQLException {
        Statement updateStmt = conn.createStatement();
        updateStmt.executeUpdate("Update movies set title = 'My Cousin Vinny', " +
                "genre = 'Comedy', director = 'Jonathan Lynn' " +
                "where id = " + id, Statement.RETURN_GENERATED_KEYS);
        ArrayList<Movie> movies = fetchAllRecords(conn);
        System.out.println("This is the list of movies with a new one modified.");
        for (Movie m : movies) {
            System.out.println(m);
        }
        updateStmt.close();
    }

    public static void deleteMovie (Connection conn, int id) throws SQLException {
        Statement deleteStmt = conn.createStatement();
        deleteStmt.execute("Delete from movies where id > " + id);
        ArrayList<Movie> movies = fetchAllRecords(conn);
        System.out.println("This is the list of movies with everything above id 3 deleted.");
        for (Movie m : movies) {
            System.out.println(m);
        }
        deleteStmt.close();
    }
}
