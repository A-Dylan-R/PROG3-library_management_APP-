package repository;

import dbConnection.DbConnection;
import models.Visitor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VisitorCrudOperations implements CrudOperations<Visitor> {
    private static final Logger LOGGER = Logger.getLogger(VisitorCrudOperations.class.getName());

    @Override
    public List<Visitor> findAll() {
        List<Visitor> visitors = new ArrayList<>();
        String query = "SELECT * FROM visitor";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int visitorId = resultSet.getInt("visitor_id");
                String name = resultSet.getString("name");
                String reference = resultSet.getString("reference");

                Visitor visitor = new Visitor(visitorId, name, reference);
                visitors.add(visitor);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while fetching visitors", e);
        }

        return visitors;
    }

    @Override
    public List<Visitor> saveAll(List<Visitor> toSave) {
        String query = "INSERT INTO visitor (name, reference) VALUES (?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            for (Visitor visitor : toSave) {
                preparedStatement.setString(1, visitor.getName());
                preparedStatement.setString(2, visitor.getReference());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while saving visitors", e);
        }

        return toSave;
    }

    @Override
    public Visitor save(Visitor toSave) {
        String query = "INSERT INTO visitor (name, reference) VALUES (?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, toSave.getName());
            preparedStatement.setString(2, toSave.getReference());

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    toSave.setUserId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while saving a visitor", e);
        }

        return toSave;
    }

    @Override
    public Visitor delete(Visitor toDelete) {
        String query = "DELETE FROM visitor WHERE visitor_id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, toDelete.getUserId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while deleting a visitor", e);
        }

        return toDelete;
    }
}
