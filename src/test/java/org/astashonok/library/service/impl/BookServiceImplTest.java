package org.astashonok.library.service.impl;

import org.astashonok.library.model.Author;
import org.astashonok.library.model.Book;
import org.astashonok.library.model.Genre;
import org.astashonok.library.repository.api.BookRepository;
import org.astashonok.library.repository.impl.BookRepositoryImpl;
import org.astashonok.library.service.api.BookService;
import org.astashonok.library.util.SqlReader;
import org.astashonok.library.util.pool.DBCPPool;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class BookServiceImplTest {

    private static BookService bookService;

    @BeforeClass
    public static void init() throws SQLException {
        bookService = new BookServiceImpl();
        resetDatabase();
    }

    @After
    public void initAfterTest() throws SQLException {
        resetDatabase();
    }

    private static void resetDatabase() throws SQLException {
        ArrayList<String> read = SqlReader.read("src/main/resources/populatedb.sql");
        try (Connection connection = DBCPPool.getInstance().getConnection()) {
            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {
                for (String sql : read) {
                    statement.addBatch(sql);
                }
                statement.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void add() throws ParseException {
        Book expected = Book.builder()
                .name("rgregtrgrgrg")
                .authors(Arrays.asList(
                        Author.builder()
                                .name("William")
                                .surname("Shakespeare")
                                .build(),
                        Author.builder()
                                .name("grgrgrg")
                                .surname("Chewdewddee")
                                .build(),
                        Author.builder()
                                .name("Barbara")
                                .surname("Cartland")
                                .build()
                ))
                .genres(Arrays.asList(
                        Genre.builder()
                                .name("erttrt")
                                .build(),
                        Genre.builder()
                                .name("Adventure")
                                .build()
                ))
                .isbn("654-345-87")
                .date(new SimpleDateFormat("yyyy-M-dd").parse("1789-05-11"))
                .build();
        long bookId = bookService.add(expected);
        Book actual = bookService.getById(bookId);
        assertEquals(expected, actual);
    }

    @Test
    public void getById() throws ParseException {
        Book expected = Book.builder()
                .name("Watchmen")
                .authors(Arrays.asList(
                        Author.builder()
                                .name("William")
                                .surname("Shakespeare")
                                .build(),
                        Author.builder()
                                .name("Agatha")
                                .surname("Christie")
                                .build(),
                        Author.builder()
                                .name("Barbara")
                                .surname("Cartland")
                                .build()
                ))
                .genres(Arrays.asList(
                        Genre.builder()
                                .name("Fantasy")
                                .build(),
                        Genre.builder()
                                .name("Adventure")
                                .build()
                ))
                .isbn("453-56-18")
                .date(new SimpleDateFormat("yyyy-M-dd").parse("2011-01-12"))
                .build();
        Book actual = bookService.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    public void getAll() {
        int expected = 5;
        int actual = bookService.getAll().size();
        assertEquals(expected, actual);
    }

    @Test
    public void edit() throws ParseException {
        Book expected = Book.builder()
                .id(3)
                .name("thththth")
                .isbn("23-54-123")
                .date(new SimpleDateFormat("yyyy-M-dd").parse("1857-08-11"))
                .authors(Arrays.asList(
                        Author.builder()
                                .name("Barbara")
                                .surname("Cartland")
                                .build(),
                        Author.builder()
                                .name("William")
                                .surname("Shakespeare")
                                .build()
                ))
                .genres(Arrays.asList(
                        Genre.builder()
                                .name("Romance")
                                .build()
                ))
                .build();
        Book actual = null;
        if (bookService.edit(expected)) {
            actual = bookService.getById(3);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void remove() {
        Book book = Book.builder()
                .id(2)
                .build();
        if (bookService.remove(book)) {
            assertNull(bookService.getById(2));
        }
    }
}