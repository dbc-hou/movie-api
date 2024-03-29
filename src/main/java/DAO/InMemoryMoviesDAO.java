package DAO;

import data.Movie;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InMemoryMoviesDAO implements MoviesDAO{
    ArrayList<Movie> movies = new ArrayList<>();
    int nextId = 1;
    @Override
    public List<Movie> all() throws SQLException {
        return movies;
    }

    @Override
    public Movie findOne(int id) {
        for (Movie m : movies) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    @Override
    public void insert(Movie movie) throws SQLException {
        movie.setId(nextId++);
        movies.add(movie);
    }

    @Override
    public void insertAll(Movie[] newMovies) throws SQLException {
        for (Movie movie : newMovies) {
            insert(movie);
        }
    }

    @Override
    public void update(Movie updatedMovie) throws SQLException {
        for (Movie m : movies) {
            if (m.getId() == updatedMovie.getId()) {
                if (updatedMovie.getTitle() != null) {
                    m.setTitle(updatedMovie.getTitle());
                }
                if (updatedMovie.getYear() != null) {
                    m.setYear(updatedMovie.getYear());
                }
                if (updatedMovie.getGenre() != null) {
                    m.setGenre(updatedMovie.getGenre());
                }
                if (updatedMovie.getRating() != null) {
                    m.setRating(updatedMovie.getRating());
                }
                if (updatedMovie.getDirector() != null) {
                    m.setDirector(updatedMovie.getDirector());
                }
                if (updatedMovie.getActors() != null) {
                    m.setActors(updatedMovie.getActors());
                }
                if (updatedMovie.getPlot() != null) {
                    m.setPlot(updatedMovie.getPlot());
                }
                if (updatedMovie.getPoster() != null) {
                    m.setPoster(updatedMovie.getPoster());
                }
            }
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        Movie foundMovie = new Movie();
        for (Movie m : movies) {
            if (m.getId() == id) {
                foundMovie = m;
            }
        }
        movies.remove(foundMovie);
    }

    @Override
    public void cleanUp() throws SQLException {

    }
}
