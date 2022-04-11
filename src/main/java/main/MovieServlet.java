package main;

import DAO.MySQLMoviesDAO;
import com.google.gson.Gson;
import data.Movie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

// Updated version of the MovieServlet class which invokes methods from
// MySQLMoviesDAO; it can be modified to invoke similar functionality
// from other classes as well
@WebServlet(name = "MovieServlet", urlPatterns = "/movies/good")
public class MovieServlet extends HttpServlet {

// This instance variable is the object that provides data access methods:
// .all = fetch all records from the movies table
// .insertAll = add one or more records to the movies table
// .update = modify one or more fields in a specified movie
// .delete = delete a specified movie from the table

    private MySQLMoviesDAO dao = new MySQLMoviesDAO();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");

        try {
            PrintWriter out = response.getWriter();
            String movieString = new Gson().toJson(dao.all().toArray());
            out.println(movieString);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        BufferedReader br = request.getReader();

        Movie[] newMovies = new Gson().fromJson(br, Movie[].class);
//        for (Movie movie : newMovies) {
//            movie.setId(nextId++);
//            movies.add(movie);
        try {
            dao.insertAll(newMovies);
            PrintWriter out = response.getWriter();
            out.println("Movie(s) added");
        }
        catch(IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String [] uriParts = req.getRequestURI().split("/");
        int targetId = Integer.parseInt(uriParts[uriParts.length - 1]);
        Movie updatedMovie = new Gson().fromJson(req.getReader(),Movie.class);
//      Look through movies, find movie with targetId
        try {
            updatedMovie.setId(targetId);
            dao.update(updatedMovie);
            PrintWriter out = resp.getWriter();
            out.println(updatedMovie);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String [] uriParts = req.getRequestURI().split("/");
        int targetId = Integer.parseInt(uriParts[uriParts.length - 1]);
        Movie foundMovie = null;
        try {
            dao.delete(targetId);
            PrintWriter out = resp.getWriter();
            out.println(foundMovie.getTitle() + " has been deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
