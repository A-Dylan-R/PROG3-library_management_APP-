import models.Author;
import models.Book;
import models.Visitor;
import repository.AuthorCrudOperations;
import repository.BookCrudOperations;
import repository.VisitorCrudOperations;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        // Configure logger
        configureLogger();

        // Test AuthorCrudOperations
        testAuthorCrudOperations();

        // Test BookCrudOperations
        testBookCrudOperations();

        // Test VisitorCrudOperations
        testVisitorCrudOperations();
    }

    private static void configureLogger() {
        // Configure logger to display messages of level INFO and above
        Logger rootLogger = Logger.getLogger("");
        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        rootLogger.addHandler(consoleHandler);
        rootLogger.setLevel(Level.ALL);
    }

    private static void testAuthorCrudOperations() {
        AuthorCrudOperations authorCrudOperations = new AuthorCrudOperations();

        // Test findAll
        List<Author> authors = authorCrudOperations.findAll();
        LOGGER.info("Authors found: " + authors);

        // Test save
        Author newAuthor = new Author(0, "New Author", "M");
        Author savedAuthor = authorCrudOperations.save(newAuthor);
        LOGGER.info("Author saved: " + savedAuthor);

        // Test delete if at least one author exists
        if (!authors.isEmpty()) {
            Author authorToDelete = authors.get(0);
            Author deletedAuthor = authorCrudOperations.delete(authorToDelete);
            LOGGER.info("Author deleted: " + deletedAuthor);
        } else {
            LOGGER.warning("No authors to delete. Ensure there is at least one author in the database.");
        }
    }


    private static void testBookCrudOperations() {
        LOGGER.info("=== Testing Book CRUD Operations ===");

        BookCrudOperations bookCrudOperations = new BookCrudOperations();
        AuthorCrudOperations authorCrudOperations = new AuthorCrudOperations();

        // Retrieve an existing author or create a new one
        List<Author> authors = authorCrudOperations.findAll();
        Author author = authors.isEmpty() ? new Author(0, "Default Author", "M") : authors.get(0);

        // Test findAll
        List<Book> books = bookCrudOperations.findAll();
        LOGGER.info("Books found: " + books);

        // Test save
        Book newBook = new Book(0, "New Book", 200, "OTHER", LocalDate.now(), "AVAILABLE", author);
        Book savedBook = bookCrudOperations.save(newBook);
        LOGGER.info("Book saved: " + savedBook);

        // Test delete
        Book bookToDelete = books.isEmpty() ? savedBook : books.get(0); //one book exists
        Book deletedBook = bookCrudOperations.delete(bookToDelete);
        LOGGER.info("Book deleted: " + deletedBook);
    }

    private static void testVisitorCrudOperations() {
        VisitorCrudOperations visitorCrudOperations = new VisitorCrudOperations();

        // Test findAll
        List<Visitor> visitors = visitorCrudOperations.findAll();
        LOGGER.info("Visitors found: " + visitors);

        // Test save
        Visitor newVisitor = new Visitor(0, "New Visitor", "Reference123");
        Visitor savedVisitor = visitorCrudOperations.save(newVisitor);
        LOGGER.info("Visitor saved: " + savedVisitor);

        // Test delete
        Visitor visitorToDelete = visitors.get(0); // Assuming at least one visitor exists
        Visitor deletedVisitor = visitorCrudOperations.delete(visitorToDelete);
        LOGGER.info("Visitor deleted: " + deletedVisitor);
    }
}
