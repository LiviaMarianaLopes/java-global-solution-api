package org.example.Repositories;

import org.example.entities._BaseEntity;

import java.util.List;
import java.util.Optional;

public interface _BaseRepository<T extends _BaseEntity> {
    public void create(T entity);

    public List<T> readAll();

    public void update(int id, T entity);

    public void delete(int id);

    public Optional<T> getById(int id);


}
