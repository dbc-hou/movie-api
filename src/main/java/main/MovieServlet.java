package main;

import com.google.gson.Gson;
import data.Config;
import data.Movie;
import data.MySQLMoviesDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@WebServlet(name = "MovieServlet", urlPatterns = "/movies/*")
public class MovieServlet extends HttpServlet {
    private MySQLMoviesDAO dao = new MySQLMoviesDAO();
//    ArrayList<Movie> movies = new ArrayList<>();
//    int nextId = 1;

    //  The next two methods, doGet and doPost, are copied from CodeupClassroom.
    //  They aren't very different from what I created, but they do at least work

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

//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String [] uriParts = req.getRequestURI().split("/");
//        int targetId = Integer.parseInt(uriParts[uriParts.length - 1]);
//        try {
//            PrintWriter out = response.getWriter();
//            out.println("Movie(s) added");
//        }
//    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String [] uriParts = req.getRequestURI().split("/");
        int targetId = Integer.parseInt(uriParts[uriParts.length - 1]);
        Movie newMovie = new Gson().fromJson(req.getReader(),Movie.class);
//      Look through movies, find movie with targetId
        try {
            dao.update(newMovie);
            PrintWriter out = resp.getWriter();
            out.println(newMovie);
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
