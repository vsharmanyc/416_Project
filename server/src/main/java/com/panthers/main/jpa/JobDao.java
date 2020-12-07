package com.panthers.main.jpa;

import com.panthers.main.jobmodel.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JobDao implements Dao<Job> {

    private List<Job> jobs = new ArrayList<>();

    public JobDao() {

    }

    @Override
    public Optional<Job> get(int id) {
        return Optional.ofNullable(jobs.get(id));
    }

    @Override
    public List<Job> getAll() {
        return jobs;
    }

    @Override
    public void save(Job job) {
        jobs.add(job);
    }

    @Override
    public void update(Job job) {
        jobs.add(job);
    }

    @Override
    public void delete(Job user) {
        jobs.remove(user);
    }
}