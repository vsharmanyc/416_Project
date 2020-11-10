package com.panthers.main.api;

import com.panthers.main.jobModel.Job;
import com.panthers.main.mapModel.Demographic;
import com.panthers.main.services.JobHandler;
import com.panthers.main.services.MapHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/job")
public class JobController {
    private JobHandler jobHandler;

    @Autowired
    public JobController(JobHandler jobHandler) {
        this.jobHandler = jobHandler;
    }

    /*+ handleDataRequest(): int
+ handleCreateJob(int numDistrictings,...): void
+ handleDeleteJob(int jobID): void"
+ handleCancelJob(int jobID): void
+ handleDisplayJob(int jobID): JSON
+ handleGetGraphData(int jobID): JSON*/

    /**
     * function takes parameters in to create a job on the server/seawulf. Returns id of the newly generated job.
     * @param newJob
     * @return returns the id of the newly generated job
     */
    @PostMapping("/createJob")
    @ResponseBody
    public List<Job> handlerCreateJob(@RequestBody Job newJob){
        return jobHandler.createJob(newJob);
    }
}
