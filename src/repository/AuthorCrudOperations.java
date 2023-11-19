package repository;

import dbConnection.DbConnection;
import models.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthorCrudOperations implements CrudOperations<Author> {
    private static final Logger LOGGER = Logger.getLogger(AuthorCrudOperations.class.getName());

    @Override
    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();
        String query = "SELECT * FROM author";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int authorId = resultSet.getInt("author_id");
                String name = resultSet.getString("name");
                String sex = resultSet.getString("sex");

                Author author = new Author(authorId, name, sex);
                authors.add(author);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while fetching authors", e);
        }

        return authors;
    }

    @Override
    public List<Author> saveAll(List<Author> toSave) {
        String query = "INSERT INTO author (name, sex) VALUES (?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            for (Author author : toSave) {
                preparedStatement.setString(1, author.getName());
                preparedStatement.setString(2, author.getSex());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while saving authors", e);
        }

        return toSave;
    }

    @Override
    public Author save(Author toSave) {
        String query = "INSERT INTO author (name, sex) VALUES (?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, toSave.getName());
            preparedStatement.setString(2, toSave.getSex());

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    toSave.setUserId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while saving an author", e);
        }

        return toSave;
    }

    @Override
    public Author delete(Author toDelete) {
        String query = "DELETE FROM author WHERE author_id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, toDelete.getUserId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while deleting an author", e);
        }

        return toDelete;
    }
}
