package repository;

import java.util.List;

public interface IRepository<ID, T> {
    int size();
    void save(T entity);
    T delete(ID id);
    T update(T entity);
    T findOne(ID id);
    //Iterable<T> getAll();
    List<T> getAll();
}
