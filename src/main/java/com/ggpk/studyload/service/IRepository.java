package com.ggpk.studyload.service;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * Interface for services that have repository methods
 *
 * @param <T> Enity
 * @param <I> I type
 */
@NoRepositoryBean

public interface IRepository<T, I> {


    <S extends T> S save(S entity);

    <S extends T> List<S> saveAll(Iterable<S> entities);

    Optional<T> getById(I id);

    boolean existsById(I id);

    List<T> getAll();

    long count();

    void deleteById(I id);

    void delete(T entity);

    void deleteAll(Iterable<? extends T> entities);

    void deleteAll();


}
