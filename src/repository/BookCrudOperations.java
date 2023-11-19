package repository;

import dbConnection.DbConnection;
import models.Author;
import models.Book;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookCrudOperations implements CrudOperations<Book> {
    private static final Logger LOGGER = Logger.getLogger(BookCrudOperations.class.getName());

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM book";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                String bookName = resultSet.getString("book_name");
                int pageNumbers = resultSet.getInt("page_numbers");
                String topic = resultSet.getString("topic");
                Date releaseDate = resultSet.getDate("release_date");
                LocalDate localReleaseDate = releaseDate.toLocalDate();
                String status = resultSet.getString("status");
                int authorId = resultSet.getInt("author_id");

                Author author = findAuthorById(authorId);

                Book book = new Book(bookId, bookName, pageNumbers, topic, localReleaseDate, status, author);
                books.add(book);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while fetching books", e);
        }

        return books;
    }

    @Override
    public List<Book> saveAll(List<Book> toSave) {
        String query = "INSERT INTO book (book_name, page_numbers, topic, release_date, status, author_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            for (Book book : toSave) {
                preparedStatement.setString(1, book.getBookName());
                preparedStatement.setInt(2, book.getPageNumbers());
                preparedStatement.setString(3, book.getTopic());
                preparedStatement.setDate(4, Date.valueOf(book.getReleaseDate()));
                preparedStatement.setString(5, book.getStatus());

                if (book.getAuthor() != null) {
                    preparedStatement.setInt(6, book.getAuthor().getUserId());
                } else {
                    preparedStatement.setNull(6, Types.INTEGER);
                }

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while saving books", e);
        }

        return toSave;
    }

    @Override
    public Book save(Book toSave) {
        String query = "INSERT INTO book (book_name, page_numbers, topic, release_date, status, author_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, toSave.getBookName());
            preparedStatement.setInt(2, toSave.getPageNumbers());
            preparedStatement.setString(3, toSave.getTopic());
            preparedStatement.setDate(4, Date.valueOf(toSave.getReleaseDate()));
            preparedStatement.setString(5, toSave.getStatus());

            if (toSave.getAuthor() != null) {
                preparedStatement.setInt(6, toSave.getAuthor().getUserId());
            } else {
                preparedStatement.setNull(6, Types.INTEGER);
            }

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    toSave.setBookId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while saving a book", e);
        }

        return toSave;
    }

    @Override
    public Book delete(Book toDelete) {
        String query = "DELETE FROM book WHERE book_id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, toDelete.getBookId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while deleting a book", e);
        }

        return toDelete;
    }

    private Author findAuthorById(int authorId) {
        String query = "SELECT * FROM author WHERE author_id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, authorId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("author_id");
                    String name = resultSet.getString("name");
                    String sex = resultSet.getString("sex");
                    return new Author(userId, name, sex);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while fetching an author", e);
        }

        return null;
    }
}
