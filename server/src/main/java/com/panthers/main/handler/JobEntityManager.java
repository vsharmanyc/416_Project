package com.panthers.main.handler;
import com.panthers.main.jobmodel.Job;
import com.panthers.main.jobmodel.JobStatus;
import com.panthers.main.mapmodel.Demographic;
import com.panthers.main.mapmodel.States;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Transient;
import javax.transaction.Transactional;
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

    @Transactional
    public void addJob(Job job){
        //em.persist(job);
    }

//    public Job getJob(Integer id){
////         System.out.println("Finding...");
////         Job ja =  em.getReference( Job.class, id );
////         System.out.println(ja);
////         Object o = em.find( Job.class, id );
////         System.out.println(em.find( Job.class, id ));
////         Job jj = new Job(1, "MD", 20000, "AFRICAN_AMERICAN",
////                 0.007, "Somewhat Compact", "QUEUED");
////         System.out.println(jj);
////         List<Demographic> demo = new ArrayList<>();
////         demo.add(Demographic.AFRICAN_AMERICAN);
////         return ja;
////    }
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
