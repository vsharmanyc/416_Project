package com.panthers.main.jpa;
import com.panthers.main.jobmodel.Job;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class JobEntityManager {
    EntityManager em;

    public JobEntityManager(EntityManager em) {
        this.em = em;
    }

    public List<Job> getAllJobs(){
        Query query = em.createQuery("SELECT e FROM Job e");
        return (List<Job>) query.getResultList();
    }

    public void addJob(Job job){
        em.persist(job);
    }

    public Job getJob(Integer id){
        return em.find( Job.class, id );
    }

    public void updateJob(Job job){
        em.merge(job);
    }

    public void deleteJob(Integer id){
        Job job = em.find( Job.class, id );
        em.remove(job);
    }
}
