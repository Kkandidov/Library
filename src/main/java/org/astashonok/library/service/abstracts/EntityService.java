package org.astashonok.library.service.abstracts;

import org.astashonok.library.model.abstracts.Entity;
import org.astashonok.library.repository.abstracts.EntityRepository;

import java.util.List;

public interface EntityService<T extends Entity> {

    EntityRepository<T> getRepository();

    default long add(T entity){
        return getRepository().add(entity);
    }

    default T getById(long id){
        return getRepository().getById(id);
    }

    default List<T> getAll(){
        return getRepository().getAll();
    }

    default boolean edit(T entity){
        return getRepository().edit(entity);
    }

    default boolean remove(T entity){
        return getRepository().remove(entity);
    }
}
