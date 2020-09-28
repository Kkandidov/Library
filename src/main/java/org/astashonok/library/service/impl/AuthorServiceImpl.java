package org.astashonok.library.service.impl;

import org.astashonok.library.model.Author;
import org.astashonok.library.repository.abstracts.EntityRepository;
import org.astashonok.library.repository.api.AuthorRepository;
import org.astashonok.library.repository.impl.AuthorRepositoryImpl;
import org.astashonok.library.service.api.AuthorService;

public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    public AuthorServiceImpl() {
        this.authorRepository = new AuthorRepositoryImpl();
    }

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public EntityRepository<Author> getRepository() {
        return this.authorRepository;
    }
}
