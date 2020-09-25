package org.astashonok.library.repository.impl;

import org.astashonok.library.model.Author;
import org.astashonok.library.repository.api.AuthorRepository;
import org.astashonok.library.util.SqlReader;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AuthorRepositoryImplTest {

    private static AuthorRepository authorRepository;

    @BeforeClass
    public static void init() throws SQLException {
        authorRepository = new AuthorRepositoryImpl();
        resetDatabase();
    }

    @After
    public void initAfterTest() throws SQLException {
        resetDatabase();
    }

    private static void resetDatabase() throws SQLException {
        ArrayList<String> read = SqlReader.read("src/main/resources/populatedb.sql");
        try (Connection connection = authorRepository.getConnection()) {
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
    public void add() {
        Author expected = Author.builder()
                .name("wdewewe")
                .surname("wewewe")
                .build();
        long id = authorRepository.add(expected);
        Author actual = authorRepository.getById(id);
        assertEquals(expected, actual);
    }

    @Test
    public void getById() {
        Author expected = Author.builder()
                .name("William")
                .surname("Shakespeare")
                .build();
        Author actual = authorRepository.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    public void getAll() {
        int expected = 5;
        int actual = authorRepository.getAll().size();
        assertEquals(expected, actual);
    }

    @Test
    public void edit() {
        Author expected = Author.builder()
                .id(5)
                .name("Harold")
                .surname("efrefrefre")
                .build();
        Author actual = null;
        if (authorRepository.edit(expected)) {
            actual = authorRepository.getById(5);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void remove() {
        Author author = Author.builder()
                .id(4)
                .name("Danielle")
                .surname("Steel")
                .build();
        if (authorRepository.remove(author)) {
            assertNull(authorRepository.getById(4));
        }
    }
}