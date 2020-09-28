package org.astashonok.library.service.impl;

import org.astashonok.library.model.Author;
import org.astashonok.library.repository.api.AuthorRepository;
import org.astashonok.library.repository.impl.AuthorRepositoryImpl;
import org.astashonok.library.service.api.AuthorService;
import org.astashonok.library.util.SqlReader;
import org.astashonok.library.util.pool.DBCPPool;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class AuthorServiceImplTest {

    private static AuthorService authorService;

    @BeforeClass
    public static void init() throws SQLException {
        authorService = new AuthorServiceImpl();
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
    public void add() {
        Author expected = Author.builder()
                .name("wdewewe")
                .surname("wewewe")
                .build();
        long id = authorService.add(expected);
        Author actual = authorService.getById(id);
        assertEquals(expected, actual);
    }

    @Test
    public void getById() {
        Author expected = Author.builder()
                .name("William")
                .surname("Shakespeare")
                .build();
        Author actual = authorService.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    public void getAll() {
        int expected = 5;
        int actual = authorService.getAll().size();
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
        if (authorService.edit(expected)) {
            actual = authorService.getById(5);
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
        if (authorService.remove(author)) {
            assertNull(authorService.getById(4));
        }
    }
}