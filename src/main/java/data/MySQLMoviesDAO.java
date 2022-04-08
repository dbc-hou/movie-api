package data;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class MySQLMoviesDAO implements MoviesDAO {

    private Connection conn;

    public MySQLMoviesDAO() {
        try {
            DriverManager.registerDriver(new Driver());
            conn = DriverManager.getConnection(
                    Config.myDBConn,
                    Config.myDBID,
                    Config.myDBPW);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Movie> all() throws SQLException {
        ArrayList<Movie> movies = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement("Select * from movies");
        ResultSet rs = ps.executeQuery();
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
        ps.close();

        return movies;
    }

    @Override
    public Movie findOne(int id) {
        return null;
    }

    @Override
    public void insert(Movie movie) throws SQLException {
        PreparedStatement insertStmt = conn.prepareStatement("Insert into movies (title, year, genre, rating, director, poster, actors, plot) " +
                "values (?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
        insertStmt.setString(1,movie.getTitle());
        insertStmt.setInt(2,movie.getYear());
        insertStmt.setString(3,movie.getGenre());
        insertStmt.setDouble(4,movie.getRating());
        insertStmt.setString(5,movie.getDirector());
        insertStmt.setString(6,movie.getActors());
        insertStmt.setString(7,movie.getPlot());
        insertStmt.setString(8,movie.getPoster());
        insertStmt.executeUpdate();

        insertStmt.close();
    }

    @Override
    public void insertAll(Movie[] movies) throws SQLException {
        for (Movie m : movies) {
            insert(m);
        }
    }

    @Override
    public void update(Movie movie) throws SQLException {
        PreparedStatement updateStmt = conn.prepareStatement("Update movies set title = ?, " +
                "year = ?," +
                "genre = ?, " +
                "rating = ?, " +
                "director = ? " +
                "where id = ?", Statement.RETURN_GENERATED_KEYS);
        updateStmt.setString(1,movie.getTitle());
        updateStmt.setInt(2,movie.getYear());
        updateStmt.setString(3,movie.getGenre());
        updateStmt.setDouble(4,movie.getRating());
        updateStmt.setString(5,movie.getDirector());
        updateStmt.setInt(6,movie.getId());
        updateStmt.executeUpdate();

        updateStmt.close();
    }

    @Override
    public void delete(int id) throws SQLException {
        PreparedStatement deleteStmt = conn.prepareStatement("Delete from movies where id = " + id);
        deleteStmt.execute();

        deleteStmt.close();
    }

    public void cleanUp() throws SQLException {
        try {
            conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


