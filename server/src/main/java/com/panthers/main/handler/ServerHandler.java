package com.panthers.main.handler;

import com.panthers.main.jobmodel.RunResults;
import com.panthers.main.jobmodel.Job;
import com.panthers.main.mapmodel.District;
import com.panthers.main.mapmodel.State;

import java.util.List;

/**
 * Main branch of program that will run/extract results from algorithm run on SERVER.
 */
public class ServerHandler {
    private Job job;

    public ServerHandler(Job job) {
        this.job = job;
    }

    /*GETTERS/SETTERS*/
    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    /*FUNCTIONS*/
    /**
     * Method effectively pulls data required to perform algorithm from the DB
     */
    public void marshallDataFromDB(State state){

    }

    /**
     * Method writes our results to storage
     * @param results results to be written to storage
     * @return returns true if writing was successful, false otherwise.
     */
    public boolean writeResultsToStorage(RunResults results){
        return false;
    }

    /**
     * function will compile results and generate data required to show the box and whisker plot.
     * @param results results to compile the box and whisker plot from
     */
    public void generateSummary(RunResults results){

    }

    public void executeJob(){

    }

    public void cancelJob(){

    }

}
