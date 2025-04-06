package school.hei.pingpongspring.repository.dao;

public interface CrudDAO<E> {
    E findById(long id);

    void save(E toSave);
}