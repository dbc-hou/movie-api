package DAO;

import java.sql.Driver;
import Config.Config;
import data.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class MySQLMoviesDAO implements MoviesDAO {

    private Connection conn;

    public MySQLMoviesDAO() {
        try {
            DriverManager.registerDriver(new Driver() {
                @Override
                public Connection connect(String url, Properties info) throws SQLException {
                    return null;
                }

                @Override
                public boolean acceptsURL(String url) throws SQLException {
                    return false;
                }

                @Override
                public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
                    return new DriverPropertyInfo[0];
                }

                @Override
                public int getMajorVersion() {
                    return 0;
                }

                @Override
                public int getMinorVersion() {
                    return 0;
                }

                @Override
                public boolean jdbcCompliant() {
                    return false;
                }

                @Override
                public Logger getParentLogger() throws SQLFeatureNotSupportedException {
                    return null;
                }
            });
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
            m.setActors(rs.getString("actors"));
            m.setPlot(rs.getString("plot"));
            m.setPoster(rs.getString("poster"));

            movies.add(m);
        }
        rs.close();
        ps.close();

        return movies;
    }

    @Override
    public Movie findOne(int id) {
        Movie m = new Movie();
        try {
            PreparedStatement ps = conn.prepareStatement("Select * from movies where id = ?");
            ResultSet rs = ps.executeQuery();
            ps.setInt(1, id);
            rs.next();

            m.setTitle(rs.getString("title"));
            m.setYear(rs.getInt("year"));
            m.setGenre(rs.getString("genre"));
            m.setRating(rs.getDouble("rating"));
            m.setDirector(rs.getString("director"));
            m.setActors(rs.getString("actors"));
            m.setPlot(rs.getString("plot"));
            m.setPoster(rs.getString("poster"));

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
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
        int currentIndex = 1;
        String qString = "Update movies set";
        if (movie.getTitle() != null) {
            qString += " title = ?,";
        }
        if (movie.getRating() != null) {
            qString += " rating = ?,";
        }
        qString = qString.substring(0,qString.length() - 1);
        qString += " where id = ?";
        PreparedStatement updateStmt = conn.prepareStatement(qString, Statement.RETURN_GENERATED_KEYS);
        if (movie.getTitle() != null) {
            updateStmt.setString(currentIndex, movie.getTitle());
            currentIndex++;
        }
        if (movie.getRating() != null) {
            updateStmt.setDouble(currentIndex, movie.getRating());
            currentIndex++;
        }
        updateStmt.setInt(currentIndex,movie.getId());
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


