package com.panthers.main.jpa;

import com.panthers.main.jobmodel.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    /**
     * gets all Jobs from the Database
     *
     * @return returns the jobs from the database.
     */
    public List<Job> getAllJobs(){
        List<Job> jobs = new ArrayList<Job>();
        jobRepository.findAll().forEach(jobs::add);
        return jobs;
    }

    /**
     * saves a job the Database
     *
     * @param job to save to database
     */
    public void addJob(Job job){
        jobRepository.save(job);
    }

    /**
     * get a job from the database
     *
     * @param id of the job to get from database
     * @return Job if Job exists or else null
     */
    public Job getJob(Integer id){
        return jobRepository.findById(id).orElse(null);
    }

    /**
     * updates a job in the Database if a job of it's matched id found
     * else saves argument as a new job in the Database
     *
     * @param job to update in the database
     */
    public void updateJob(Job job){
        jobRepository.save(job);
    }

    /**
     * deletes a job in the database
     *
     * @param id of the job to delete from Database
     */
    public void deleteJob(Integer id){
        jobRepository.deleteById(id);
    }
}
