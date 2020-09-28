package org.astashonok.library.service.impl;

import org.astashonok.library.model.Genre;
import org.astashonok.library.repository.abstracts.EntityRepository;
import org.astashonok.library.repository.api.GenreRepository;
import org.astashonok.library.repository.impl.GenreRepositoryImpl;
import org.astashonok.library.service.api.GenreService;

public class GenreServiceImpl implements GenreService {

    private GenreRepository genreRepository;

    public GenreServiceImpl() {
        this.genreRepository = new GenreRepositoryImpl();
    }

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public EntityRepository<Genre> getRepository() {
        return this.genreRepository;
    }
}
