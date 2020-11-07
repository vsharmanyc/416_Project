package com.panthers.main.services;

import com.panthers.main.jobModel.DistrictingPlans;
import com.panthers.main.jobModel.Job;

/**
 * Dispatcher controls where the job will be run, and performs that run depending!
 */
public class DispatcherService {
    /*
     * + job: Job + resultingDistrictingPlans: DistrinctingPlans = null
     */
    private Job job;
    private DistrictingPlans runResults;

    public DispatcherService(Job job) {
        this.job = job;
        this.runResults = null;
    }

    /*GETTERS/SETTERS*/
    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public DistrictingPlans getRunResults() {
        return runResults;
    }

    public void setRunResults(DistrictingPlans runResults) {
        this.runResults = runResults;
    }


    /**
     * function will simply compute best environment for the job to be processed; SeaWulf or Server
     */
    public void computeBestEnvironment(){

    }

    /**
     * function proceeds to start the job on the server.
     * @param job job to be started on server
     * @return returns run results from the algorithm
     */
    public DistrictingPlans runAlgorithmOnServer(Job job){
        return null;
    }

    /**
     * function proceeds to start the job on the seawulf.
     * @param job job to be started on seawulf
     * @return returns run results from the algorithm
     */
    public DistrictingPlans runAlgorithmOnSeaWulf(Job job){
        return null;
    }

    /**
     * function writes results of algorithm run to our database
     * @return returns true if writing was successful, false otherwise.
     */
    public boolean writeResultsToFileSystem(){
        return false;
    }

}
