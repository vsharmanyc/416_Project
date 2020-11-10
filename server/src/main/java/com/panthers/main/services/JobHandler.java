package com.panthers.main.services;

import com.panthers.main.jobModel.DistrictingPlans;
import com.panthers.main.jobModel.Job;
import com.panthers.main.jobModel.JobStatus;
import com.panthers.main.mapModel.Demographic;
import com.panthers.main.mapModel.District;
import com.panthers.main.mapModel.Precinct;
import com.panthers.main.mapModel.State;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Functions as the main point of the running process.
 * First in command. After a RESTful service is tapped into regarding the job run,
 * program will go through this job service.
 */
@Service
public class JobHandler {
    private State state;
    private List<District> currentDistrictings;
    private List<Job> jobHistory;

    public JobHandler() {
        this.state = null;//Originally, no state is selected
        this.jobHistory = getJobHistory();// Get job history from EM upon first load.

    }

    /*GETTERS/SETTERS*/
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<District> getCurrentDistrictings() {
        return currentDistrictings;
    }

    public void setCurrentDistrictings(List<District> currentDistrictings) {
        this.currentDistrictings = currentDistrictings;
    }

    /*FUNCTIONS*/
    /*
        + getDataFromDB(): JSON
        + updatePastRunResults(): void
        + refreshDataFromDB(): void
    */

    /**
     * Main function to have high-level overview of job creation/updating/returning. Creates job, queues it, eventually
     * returns the job to the client from the GET request.
     * @param name Name of Job
     * @param state State job is concerned with
     * @param demographicGroups User requested demographic groups to analyze
     * @param popEqThreshold User inputted population equality threshold
     * @param compactness User specified target compactness
     * @return returns true on successfull job creation, false otherwise.
     */
    public DistrictingPlans handleDataRequest(String name, State state, List<Demographic> demographicGroups,
                                              double popEqThreshold, String compactness){
        return null;
    }

    /**
     * Takes user input from front-end to create a 'Job' object.
     * @param job Job we are creating//creating ID for.
     * @return returns true on successfull job creation, false otherwise.
     */
    public List<Job> createJob(Job job){
        if (jobHistory.size() == 0)
            job.setJobId(1);//If no other jobs, its job id is 1!
        else
            job.setJobId(getNextJobId());//Sets the jobs id!
        //Attaching the job to the job history!
        job.setJobStatus(JobStatus.QUEUED);
        jobHistory.add(job);
        return jobHistory;
    }

    /**
     * Method sends the results of the job run to our client.
     * @return returns true if sending was successfull, false otherwise.
     */
    public boolean sendResultsToClient(){
        return false;
    }

    /**
     * Function will marshall districts enacted on specified state from the Database
     * @return returns the districts from the database.
     */
    public List<District> requestDistrictsFromDB(State state){
        return null;
    }

    /**
     * Function will marshall precincts enacted on specified state from the Database
     * @return returns the precincts from the database.
     */
    public List<Precinct> requestPrecinctsFromDB(State state){
        return null;
    }

    /**
     * Function simply places job on Job Queue
     * @param job job to be queued
     * @return returns true if job was indeed queued, false otherwise.
     */
    public boolean queueJob(Job job){
        return false;
    }

    /**
     * Marshals past run results, and returns them to client.
     * @return returns the past run results stored on DB
     */
    public List<DistrictingPlans> getPastRunResults(){
        return null;
    }

    /**
     * function talks to entity manager to get the job history
     * @return returns the job history from the EM
     */
    private List<Job> getJobHistory(){
        //Normally, we'd get this from the EM.
        return new ArrayList<Job>();
    }

    /**
     * function checks max job id of jobs in history, and returns that number +1 for the next job's id
     * @return returns the next job's id
     */
    private int getNextJobId(){
        int maxId = -1;

        //Determines max job id, returns that +1
        for (Job job : jobHistory) {
            if (job.getJobId() > maxId)
                maxId = job.getJobId();
        }
        return maxId+1;
    }
}
