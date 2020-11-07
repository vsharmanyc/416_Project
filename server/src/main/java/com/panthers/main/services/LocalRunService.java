package com.panthers.main.services;

import com.panthers.main.jobModel.DistrictingPlans;
import com.panthers.main.jobModel.Job;
import com.panthers.main.mapModel.District;
import com.panthers.main.mapModel.State;

import java.util.List;

/**
 * Main branch of program that will run/extract results from algorithm run on SERVER.
 */
public class LocalRunService {
    private Job job;
    private DistrictingPlans algorithmResults;
    private List<District> currentDistricting;

    public LocalRunService(Job job) {
        this.job = job;
        this.algorithmResults = null;
        this.currentDistricting = null;
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
     * function is the main, overarching function for the congressional districting generation/analysis on server.
     * will run algorithm and compile results.
     * @param job job that will be worked on
     * @return
     */
    public DistrictingPlans executeJob(Job job){
        return null;
    }

    /**
     * function will start the main algorithm to create the specified districtings.
     * @param job job to be performed on
     * @param currentDistricting data from database, utilized in algorithm
     * @return results of the algorithm
     */
    public DistrictingPlans startAlgorithm(Job job, List<District> currentDistricting){
        return null;
    }

    /**
     * Method writes our results to storage
     * @param results results to be written to storage
     * @return returns true if writing was successful, false otherwise.
     */
    public boolean writeResultsToStorage(DistrictingPlans results){
        return false;
    }

    /**
     * function will compile results and generate data required to show the box and whisker plot.
     * @param results results to compile the box and whisker plot from
     */
    public void generateDistrictingPlot(DistrictingPlans results){

    }

}
