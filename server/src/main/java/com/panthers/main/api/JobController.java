package com.panthers.main.api;

import com.panthers.main.handler.SeaWulfHandler;
import com.panthers.main.jobmodel.Job;
import com.panthers.main.jobmodel.RunResults;
import com.panthers.main.handler.JobHandler;
import com.panthers.main.mapmodel.Demographic;
import com.panthers.main.mapmodel.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/job")
public class JobController {
    @Autowired
    private JobHandler jobHandler;

    @Autowired
    public JobController(JobHandler jobHandler) {
        this.jobHandler = jobHandler;
    }

    /**
     * function takes parameters in to create a job on the server/seawulf. Returns id of the newly generated job.
     * @param newJob new job to be generated
     * @return returns the id of the newly generated job
     */
    @PostMapping("/createJob")
    @ResponseBody
    public List<Job> handleCreateJob(@RequestBody Job newJob){
        System.out.println(newJob);
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

    /**
     * Purely for testing methods.
     * @return returns runResults
     */
    @GetMapping("/randomRunResult")
    @ResponseBody
    public RunResults generateRandomRunResult(){
        return jobHandler.getRunResults();
    }

    /**
     * gets job history from the em
     * @return returns the persisted job history
     */
    @GetMapping("/getJobHistory")
    @ResponseBody
    public List<Job> getJobHistory(){
        return jobHandler.getJobHistory();
    }

    @PostMapping("/testParseJobData")
    @ResponseBody
    public void parseJobData(){
        ArrayList<Demographic> dem = new ArrayList<>();
        dem.add(Demographic.AFRICAN_AMERICAN);
//        dem.add(Demographic.HISPANIC_LATINO);
        SeaWulfHandler swh = new SeaWulfHandler(new Job(States.MD, 510, dem, 0.034,
               "Very Compact"));
        swh.getJobFromSeaWulf(1);
    }

    @PostMapping("/getSummaryFile")
    @ResponseBody
    public void getSummaryFile(@RequestBody Job job){
        jobHandler.transferSummaryFiles(job.getJobId());
    }

}
