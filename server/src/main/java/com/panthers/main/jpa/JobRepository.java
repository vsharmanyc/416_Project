package com.panthers.main.jpa;

import com.panthers.main.jobmodel.Job;
import org.springframework.data.repository.CrudRepository;

public interface JobRepository extends CrudRepository<Job, Integer>{

}
