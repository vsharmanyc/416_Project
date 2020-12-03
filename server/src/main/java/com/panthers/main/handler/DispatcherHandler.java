package com.panthers.main.handler;

import com.panthers.main.dataaccess.SeaWulfProperties;
import com.panthers.main.jobmodel.RunResults;
import com.panthers.main.jobmodel.Job;
import com.panthers.main.mapmodel.States;
import org.springframework.stereotype.Service;

/**
 * Dispatcher controls where the job will be run, and performs that run depending!
 */
@Service
public class DispatcherHandler {
    private RunResults runResults;
    private boolean runOnSeaWulf;
    private SeaWulfProperties properties;

    public DispatcherHandler() {
        this.runResults = null;
        this.runOnSeaWulf = false;//Assume we don't run on seawulf
        this.properties = new SeaWulfProperties();
        this.properties.getProperties();
    }

    /*GETTERS/SETTERS*/
    public RunResults getRunResults() {
        return runResults;
    }

    public void setRunResults(RunResults runResults) {
        this.runResults = runResults;
    }


    /**
     * function will simply compute best environment for the job to be processed; SeaWulf or Server
     */
    public void computeBestEnvironment(Job job) {
        //If we are to generate more than 5000 districtings, well send it to the seawulf
        if (job.getNumDistrictings() > properties.getSeaWulfDistrictingThreshold())
            runOnSeaWulf = true;
        else
            runOnSeaWulf = false;
    }

    /**
     * function proceeds to start the job on the server.
     *
     * @param job job to be started on server
     * @return returns run results from the algorithm
     */
    public RunResults runAlgorithmOnServer(Job job) {
        return null;
    }

    /**
     * function proceeds to start the job on the seawulf.
     *
     * @param job job to be started on seawulf
     * @return returns run results from the algorithm
     */
    public RunResults runAlgorithmOnSeaWulf(Job job) {
        return null;
    }

    /**
     * function writes results of algorithm run to our database
     *
     * @return returns true if writing was successful, false otherwise.
     */
    public boolean writeResultsToFileSystem() {
        return false;
    }

    /**
     * function dispatches job to determined environment.
     *
     * @param job job to be dispatched
     */
    public void dispatchJob(Job job) {
        computeBestEnvironment(job);

        if (runOnSeaWulf) {
            new Thread(() -> {
                SeaWulfHandler swh = new SeaWulfHandler(job);
                System.out.println("Mapping execution for job " + job.getJobId() + " to SeaWulf");
                swh.executeJob();

            }).start();
        } else {
            new Thread(() -> {
                ServerHandler svh = new ServerHandler(job);
                System.out.println("Mapping execution for job " + job.getJobId() + " to Server");
                svh.executeJob();
            }).start();
        }
    }

    public void cancelJob(Job job){
        computeBestEnvironment(job);
        if (runOnSeaWulf) {
            new Thread(() -> {
                SeaWulfHandler swh = new SeaWulfHandler(job);
                System.out.println("Cancelling job " + job.getJobId() + " on SeaWulf");
                swh.cancelJob();

            }).start();
        } else {
            new Thread(() -> {
                ServerHandler svh = new ServerHandler(job);
                System.out.println("Cancelling job " + job.getJobId() + " on Server");
                svh.cancelJob();
            }).start();
        }
    }

}
