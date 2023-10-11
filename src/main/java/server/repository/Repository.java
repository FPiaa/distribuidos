package server.repository;

import server.exceptions.ResourceNotFoundException;
import server.exceptions.ServerResponseException;

import java.util.List;
import java.util.Optional;

public interface Repository<T, Id> {
    Optional<T> find(Id id);

    List<T> findAll();

    void create(T newInstance) throws ServerResponseException;

    void delete(T instance);

    void deleteById(Id id) throws ResourceNotFoundException;

    T update(Id id, T instance) throws ServerResponseException;
}
