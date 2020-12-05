package com.panthers.main.jpa;

import com.panthers.main.jobmodel.Job;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Component
public class JpaJobDao implements Dao<Job> {

    private EntityManager entityManager;

    public JpaJobDao(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Jobs");
        this.entityManager = emf.createEntityManager();
    }

    @Override
    public Optional<Job> get(int id) {
        return Optional.ofNullable(entityManager.find(Job.class, id));
    }

    @Override
    public List<Job> getAll() {
        Query query = entityManager.createQuery("SELECT e FROM Job e");
        return query.getResultList();
    }

    @Override
    public void save(Job job) {
        executeInsideTransaction(entityManager -> entityManager.persist(job));
    }

    @Override
    public void update(Job job, String[] params) {
        executeInsideTransaction(entityManager -> entityManager.merge(job));
    }

    @Override
    public void delete(Job job) {
        executeInsideTransaction(entityManager -> entityManager.remove(job));
    }

    private void executeInsideTransaction(Consumer<EntityManager> action) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            action.accept(entityManager);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        }
    }
}
