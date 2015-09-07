package ru.company.beans;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ApplicationScoped
public class Genres {

    private List<Genre> genreList = new ArrayList<>();

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/library", "root", "");
        return connection;
    }

    private List<Genre> getGenres() {
        String query = "SELECT * FROM genre ORDER BY name";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {

            while (result.next()) {
                Genre genre = new Genre();
                genre.setId(result.getLong("id"));
                genre.setName(result.getString("name"));
                genreList.add(genre);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return genreList;
    }

    public List<Genre> getGenreList() {

        if (genreList.isEmpty()) {
            return getGenres();
        }

        return genreList;
    }

}
