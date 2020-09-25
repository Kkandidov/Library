package org.astashonok.library.repository.impl;

import org.astashonok.library.model.Genre;
import org.astashonok.library.repository.api.GenreRepository;
import org.astashonok.library.util.SqlReader;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class GenreRepositoryImplTest {

    private static GenreRepository genreRepository;

    @BeforeClass
    public static void init() throws SQLException {
        genreRepository = new GenreRepositoryImpl();
        resetDatabase();
    }

    @After
    public void initAfterTest() throws SQLException {
        resetDatabase();
    }

    private static void resetDatabase() throws SQLException {
        ArrayList<String> read = SqlReader.read("src/main/resources/populatedb.sql");
        try (Connection connection = genreRepository.getConnection()) {
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
        Genre expected = Genre.builder()
                .name("wdewewe")
                .build();
        long id = genreRepository.add(expected);
        Genre actual = genreRepository.getById(id);
        assertEquals(expected, actual);
    }

    @Test
    public void getById() {
        Genre expected = Genre.builder()
                .name("Fantasy")
                .build();
        Genre actual = genreRepository.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    public void getAll() {
        int expected = 5;
        int actual = genreRepository.getAll().size();
        assertEquals(expected, actual);
    }

    @Test
    public void edit() {
        Genre expected = Genre.builder()
                .id(5)
                .name("Horror")
                .build();
        Genre actual = null;
        if (genreRepository.edit(expected)) {
            actual = genreRepository.getById(5);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void remove() {
        Genre genre = Genre.builder()
                .id(4)
                .name("Mystery")
                .build();
        if (genreRepository.remove(genre)) {
            assertNull(genreRepository.getById(4));
        }
    }
}