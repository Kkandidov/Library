package org.astashonok.library.repository.impl;

import org.astashonok.library.model.Author;
import org.astashonok.library.model.Book;
import org.astashonok.library.model.Genre;
import org.astashonok.library.repository.api.BookRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {

    @Override
    public long add(Book entity) {
        String sql = "INSERT INTO book(name, isbn, date) VALUES (?, ?, ?)";
        long id;
        ResultSet generatedKeys = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getIsbn());
            preparedStatement.setTimestamp(3, new Timestamp(entity.getDate().getTime()));
            if (preparedStatement.executeUpdate() == 0) {
                throw new RuntimeException("Creating the book is failed, no rows is affected");
            }
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
                entity.setId(id);
            } else {
                throw new RuntimeException("The book key isn't fetched from database");
            }
        } catch (SQLException e) {
            throw new RuntimeException("The book wasn't added");
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
    public Book getById(long id) {
        String sql = "SELECT id, name, isbn, date FROM book WHERE id = " + id;
        Book book = null;
        try (Connection connection = getConnection()) {
            try {
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    book = new Book();
                    long i = resultSet.getLong("id");
                    book.setId(i);
                    book.setName(resultSet.getString("name"));
                    book.setIsbn(resultSet.getString("isbn"));
                    book.setAuthors(getAuthorsByBookId(i, connection));
                    book.setGenres(getGenresByBookId(i, connection));
                    book.setDate(new java.util.Date(resultSet.getTimestamp("date").getTime()));
            }
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(e);
                }
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }

    @Override
    public List<Book> getAll() {
        String sql = "SELECT id, name, isbn, date FROM book";
        List<Book> books = new ArrayList<>();
        Book book;
        try (Connection connection = getConnection()) {
            try {
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    book = new Book();
                    long i = resultSet.getLong("id");
                    book.setId(i);
                    book.setName(resultSet.getString("name"));
                    book.setIsbn(resultSet.getString("isbn"));
                    book.setAuthors(getAuthorsByBookId(i, connection));
                    book.setGenres(getGenresByBookId(i, connection));
                    book.setDate(new java.util.Date(resultSet.getTimestamp("date").getTime()));
                    books.add(book);
                }
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(e);
                }
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return books;
    }

    @Override
    public boolean edit(Book entity) {
        String sql = "UPDATE book SET name = ?,  isbn = ?, date = ? WHERE id = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(4, entity.getId());
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getIsbn());
            preparedStatement.setTimestamp(3, new Timestamp(entity.getDate().getTime()));
            if (preparedStatement.executeUpdate() == 0) {
                throw new RuntimeException("Updating the book is failed, no rows is affected");
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean remove(Book entity) {
        String sql = "DELETE FROM book WHERE id = " + entity.getId();
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (preparedStatement.executeUpdate() == 0) {
                throw new RuntimeException("Deleting the book is failed, no rows is affected");
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Author> getAuthorsByBookId(long id, Connection connection) throws SQLException {
        String sql = "SELECT id, name, surname FROM author JOIN book_author ON id = authorId WHERE bookId = " + id;
        List<Author> authors = new ArrayList<>();
        Author author;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                author = new Author();
                author.setId(resultSet.getLong("id"));
                author.setName(resultSet.getString("name"));
                author.setSurname(resultSet.getString("surname"));
                authors.add(author);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authors;
    }

    private static List<Genre> getGenresByBookId(long id, Connection connection) throws SQLException {
        String sql = "SELECT id, name FROM genre JOIN book_genre ON id = genreId WHERE bookId = " + id;
        List<Genre> genres = new ArrayList<>();
        Genre genre;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
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


}
