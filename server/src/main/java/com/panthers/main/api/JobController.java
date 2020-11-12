package com.panthers.main.api;

import com.panthers.main.jobModel.Job;
import com.panthers.main.mapModel.Demographic;
import com.panthers.main.services.JobHandler;
import com.panthers.main.services.MapHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
+ handleDeleteJob(int jobID): void
+ handleDisplayJob(int jobID): JSON
+ handleGetGraphData(int jobID): JSON*/

    /**
     * function takes parameters in to create a job on the server/seawulf. Returns id of the newly generated job.
     * @param newJob new job to be generated
     * @return returns the id of the newly generated job
     */
    @PostMapping("/createJob")
    @ResponseBody
    public List<Job> handleCreateJob(@RequestBody Job newJob){
        return jobHandler.createJob(newJob);
    }

    /**
     * function cancels the job with the given job id
     * @param job jobId to cancel the job for.
     * @return returns new job history with the cancelled job.
     */
    @PostMapping("/cancelJob")
    @ResponseBody
    public List<Job> handleCancelJob(@RequestBody Job job){
        return jobHandler.cancelJob(job.getJobId());
    }

    /**
     * function deletes the job with the given job id
     * @param job jobId to delete the job for.
     * @return returns new job history with the deleted job.
     */
    @PostMapping("/deleteJob")
    @ResponseBody
    public List<Job> deleteCancelJob(@RequestBody Job job){
        return jobHandler.deleteJob(job.getJobId());
    }


}
