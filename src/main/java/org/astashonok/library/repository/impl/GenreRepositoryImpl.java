package org.astashonok.library.repository.impl;

import org.astashonok.library.model.Genre;
import org.astashonok.library.repository.api.GenreRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreRepositoryImpl implements GenreRepository {

    @Override
    public long add(Genre entity) {
        String sql = "INSERT INTO genre(name) VALUES (?)";
        long id;
        ResultSet generatedKeys = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getName());
            if (preparedStatement.executeUpdate() == 0) {
                throw new RuntimeException("Creating the genre is failed, no rows is affected");
            }
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
                entity.setId(id);
            } else {
                throw new RuntimeException("The genre key isn't fetched from database");
            }
        } catch (SQLException e) {
            throw new RuntimeException("The genre wasn't added");
        } finally {
            if (generatedKeys != null) {
                try {
                    generatedKeys.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return id;
    }

    @Override
    public Genre getById(long id) {
        String sql = "SELECT id, name FROM genre WHERE id = " + id;
        Genre genre = null;
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                genre = new Genre();
                genre.setId(resultSet.getLong("id"));
                genre.setName(resultSet.getString("name"));
             }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return genre;
    }

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT id, name FROM genre";
        List<Genre> genres = new ArrayList<>();
        Genre genre;
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                genre = new Genre();
                genre.setId(resultSet.getLong("id"));
                genre.setName(resultSet.getString("name"));
                genres.add(genre);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return genres;
    }

    @Override
    public boolean edit(Genre entity) {
        String sql = "UPDATE genre SET name = ? WHERE id = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(2, entity.getId());
            preparedStatement.setString(1, entity.getName());
            if (preparedStatement.executeUpdate() == 0) {
                throw new RuntimeException("Updating the genre is failed, no rows is affected");
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean remove(Genre entity) {
        String sql = "DELETE FROM genre WHERE id = " + entity.getId();
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (preparedStatement.executeUpdate() == 0) {
                throw new RuntimeException("Deleting the genre is failed, no rows is affected");
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
