package DAO;

import data.Movie;

import java.sql.SQLException;
import java.util.List;

public interface MoviesDAO {
    public List<Movie> all () throws SQLException;

    Movie findOne(int id);

    void insert(Movie movie) throws SQLException;

    void insertAll(Movie[] movies) throws SQLException;

    void update(Movie movie) throws SQLException;

    void delete(int id) throws SQLException;

    void cleanUp() throws SQLException;
}
