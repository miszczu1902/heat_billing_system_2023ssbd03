package pl.lodz.p.it.ssbd2023.ssbd03.common;

import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

public abstract class AbstractFacade<T> {
    public Class<T> entityClass;

    protected abstract EntityManager getEntityManager();

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void create(T entity) {
        try {
        getEntityManager().persist(entity);
        getEntityManager().flush();
        } catch (OptimisticLockException e) {
            throw AppException.createOptimisticLockAppException();
        }
    }

    public void edit(T entity) {
        try {
            getEntityManager().merge(entity);
            getEntityManager().flush();
        } catch (OptimisticLockException e) {
            throw AppException.createOptimisticLockAppException();
        }
    }

    public void remove(T entity) {
        try {
            getEntityManager().remove(getEntityManager().merge(entity));
            getEntityManager().flush();
        } catch (OptimisticLockException e) {
            throw AppException.createOptimisticLockAppException();
        }
    }

    public T find(Object id) {
        try {
            return getEntityManager().find(entityClass, id);
        } catch (OptimisticLockException e) {
            throw AppException.createOptimisticLockAppException();
        }
    }
}
