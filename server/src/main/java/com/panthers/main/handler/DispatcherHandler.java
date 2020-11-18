package com.panthers.main.handler;

import com.panthers.main.jobModel.RunResults;
import com.panthers.main.jobModel.Job;
import com.panthers.main.mapModel.States;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * Dispatcher controls where the job will be run, and performs that run depending!
 */
@Service
public class DispatcherHandler {
    private RunResults runResults;
    private boolean runOnSeaWulf;

    public DispatcherHandler() {
        this.runResults = null;
        this.runOnSeaWulf = false;//Assume we don't run on seawulf
    }

    /*GETTERS/SETTERS*/
//    public Job getJob() {
//        return job;
//    }
//
//    public void setJob(Job job) {
//        this.job = job;
//    }

    public RunResults getRunResults() {
        return runResults;
    }

    public void setRunResults(RunResults runResults) {
        this.runResults = runResults;
    }


    /**
     * function will simply compute best environment for the job to be processed; SeaWulf or Server
     */
    public void computeBestEnvironment(Job job){
        //If we are to generate more than 5000 districtings, well send it to the seawulf
        if (job.getNumDistrictings() > 5000)
            runOnSeaWulf = true;
        else
            runOnSeaWulf = false;
    }

    /**
     * function proceeds to start the job on the server.
     * @param job job to be started on server
     * @return returns run results from the algorithm
     */
    public RunResults runAlgorithmOnServer(Job job){
        return null;
    }

    /**
     * function proceeds to start the job on the seawulf.
     * @param job job to be started on seawulf
     * @return returns run results from the algorithm
     */
    public RunResults runAlgorithmOnSeaWulf(Job job){
        return null;
    }

    /**
     * function writes results of algorithm run to our database
     * @return returns true if writing was successful, false otherwise.
     */
    public boolean writeResultsToFileSystem(){
        return false;
    }

    /**
     * function dispatches job to determined environment.
     * @param job job to be dispatched
     */
    public void dispatchJob(Job job, States state){
        computeBestEnvironment(job);

        //Route execution to a separate thread, which will begin execution.
        if (runOnSeaWulf){
            new Thread(new Runnable() {
                public void run(){
                    SeaWulfHandler swh = new SeaWulfHandler(job);
                    System.out.println("Mapping execution for job " + job.getJobId() + " to SeaWulf");
                    swh.executeJob();

                }
            }).start();
        }
        else{
            new Thread(new Runnable() {
                public void run(){
                    System.out.println("Mapping execution for job " + job.getJobId() + " to Server");
                    for (int i = 0; i < 5; i++){
                        System.out.println("Work" + i);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

}
