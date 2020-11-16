package com.panthers.main.services;

import com.panthers.main.jobModel.RunResults;
import com.panthers.main.jobModel.Job;
import com.panthers.main.mapModel.District;

import java.util.List;

/**
 * Main branch of program that will run/extract results from algorithm run on SEAWULF.
 */
public class SeaWulfService {
    private Job job;
    private RunResults algorithmResults;
    private List<District> currentDistricting;

    public SeaWulfService(Job job) {
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
     * function is the main, overarching function for the congressional districting generation/analysis on seawulf.
     * will run algorithm and compile results.
     * @param job job that will be worked on
     * @return returns districting plans resulting from the job.
     */
    public RunResults executeJob(Job job){
        return null;
    }

    /**
     * method should aggregate necessary data to run the algorithm on the server
     */
    public void aggregateSeawulfData(){

    }

    /**
     * Method should update seawulf progress to the client side...somehow
     */
    public void updateSeaWulfProgress(){

    }

    /**
     * method would send the request to the seawulf, along with the necessary files
     */
    public void sendRequestToSeaWulf(){

    }

    /**
     * prepares seawulf for performing the requested job/algorithm running
     */
    public void prepareSeaWulf(){

    }

    /**
     * function would write the results of our algorithm running form the seawulf to the file syste,
     */
    public void writeToDirectory(RunResults algorithmResults){

    }

    /**
     * function will compile results and generate data required to show the box and whisker plot.
     * @param results results to compile the box and whisker plot from
     */
    public void generateDistrictingPlot(RunResults results){

    }
}
