package org.astashonok.library.repository.impl;

import org.astashonok.library.model.Author;
import org.astashonok.library.model.Book;
import org.astashonok.library.model.Genre;
import org.astashonok.library.model.abstracts.Entity;
import org.astashonok.library.repository.api.BookRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {

    @Override
    public long add(Book entity) {
        String sql = "INSERT INTO book(name, isbn, date) VALUES (?, ?, ?)";
        long id;
        ResultSet generatedKeys = null;
        try (Connection connection = getConnection()) {
            try {
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
                for (Author author : entity.getAuthors()) {
                    long i = getAuthorId(author, connection);
                    if (i == 0) {
                        i = addAuthor(author, connection);
                    }
                    addBookAuthor(entity.getId(), i, connection);
                }
                for (Genre genre : entity.getGenres()) {
                    long i = getGenreId(genre, connection);
                    if (i == 0) {
                        i = addGenre(genre, connection);
                    }
                    addBookGenre(entity.getId(), i, connection);
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("The book wasn't added", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
                    book = getBook(resultSet, connection);
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
                    book = getBook(resultSet, connection);
                    books.add(book);
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return books;
    }

    private static Book getBook(ResultSet resultSet, Connection connection) throws SQLException {
        long i = resultSet.getLong("id");
        Book build = Book.builder()
                .id(i)
                .name(resultSet.getString("name"))
                .isbn(resultSet.getString("isbn"))
                .authors(getAuthorsByBookId(i, connection))
                .genres(getGenresByBookId(i, connection))
                .date(new Date(resultSet.getTimestamp("date").getTime()))
                .build();
        return build;
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

    private static List<Author> getAuthorsByBookId(long id, Connection connection) {
        String sql = "SELECT id, name, surname FROM author JOIN book_author ON id = authorId WHERE bookId = " + id;
        List<Author> authors = new ArrayList<>();
        Author author;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                author = Author.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .surname(resultSet.getString("surname"))
                        .build();
                authors.add(author);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authors;
    }

    private static List<Genre> getGenresByBookId(long id, Connection connection) {
        String sql = "SELECT id, name FROM genre JOIN book_genre ON id = genreId WHERE bookId = " + id;
        List<Genre> genres = new ArrayList<>();
        Genre genre;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                genre = Genre.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .build();
                genres.add(genre);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return genres;
    }

    private static long getGenreId(Genre genre, Connection connection) {
        String sql = "SELECT id FROM genre WHERE name = '" + genre.getName() + "'";
        return getId(sql, connection);
    }

    private static long getAuthorId(Author author, Connection connection) {
        String sql = "SELECT id FROM author WHERE name = '" + author.getName() + "' AND surname = '" + author.getSurname() + "'";
        return getId(sql, connection);
    }

    private static long getId(String sql, Connection connection) {
        long result = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                result = resultSet.getLong("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private static void addBookAuthor(long bookId, long authorId, Connection connection) {
        String sql = "INSERT INTO book_author (bookId, authorId) VALUES(" + bookId + ", " + authorId + ")";
        add(sql, connection);
    }

    private static void addBookGenre(long bookId, long genreId, Connection connection) {
        String sql = "INSERT INTO book_genre (bookId, genreId) VALUES(" + bookId + ", " + genreId + ")";
        add(sql, connection);
    }

    private static void add(String sql, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (preparedStatement.executeUpdate() == 0) {
                throw new RuntimeException("Creating the entry is failed, no rows is affected");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static long addAuthor(Author author, Connection connection) {
        String sql = "INSERT INTO author(name, surname) VALUES ('" + author.getName() + "', '" + author.getSurname() + "')";
        return add(sql, author, connection);
    }

    private static long addGenre(Genre genre, Connection connection) {
        String sql = "INSERT INTO genre(name) VALUES ('" + genre.getName() + "')";
        return add(sql, genre, connection);
    }

    private static long add(String sql, Entity entity, Connection connection) {
        long id;
        ResultSet generatedKeys = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (preparedStatement.executeUpdate() == 0) {
                throw new RuntimeException("Creating the entity is failed, no rows is affected");
            }
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
                entity.setId(id);
            } else {
                throw new RuntimeException("The entity key isn't fetched from database");
            }
        } catch (SQLException e) {
            throw new RuntimeException("The entity wasn't added", e);
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
}
