package org.astashonok.library.service.impl;

import org.astashonok.library.model.Book;
import org.astashonok.library.repository.abstracts.EntityRepository;
import org.astashonok.library.repository.api.BookRepository;
import org.astashonok.library.repository.impl.BookRepositoryImpl;
import org.astashonok.library.service.api.BookService;

public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    public BookServiceImpl() {
        this.bookRepository = new BookRepositoryImpl();
    }

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public EntityRepository<Book> getRepository() {
        return this.bookRepository;
    }
}
