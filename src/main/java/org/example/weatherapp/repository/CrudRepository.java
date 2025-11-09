package org.example.weatherapp.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<E, K extends Serializable> {

    Optional<E> findById(K id);

    List<E> findAll();

    E save(E entity);

    void update(E entity);

    void delete(E entity);

}
