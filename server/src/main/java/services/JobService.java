package services;

import jobModel.DistrictingPlans;
import jobModel.Job;
import mapModel.Demographic;
import mapModel.District;
import mapModel.Precinct;
import mapModel.State;

import java.util.List;

/**
 * Functions as the main point of the running process.
 * First in command. After a RESTful service is tapped into regarding the job run,
 * program will go through this job service.
 */
public class JobService {
    private State state;
    private List<District> currentDistrictings;
    private List<Job> jobQueue;

    public JobService() {
        this.state = null;//Originally, no state is selected
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
     * @param name Name of Job
     * @param state State job is concerned with
     * @param demographicGroups User requested demographic groups to analyze
     * @param popEqThreshold User inputted population equality threshold
     * @param compactness User specified target compactness
     * @return returns true on successfull job creation, false otherwise.
     */
    public boolean createJob(String name, State state, List<Demographic> demographicGroups, double popEqThreshold,
                             String compactness){
        return false;
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

}
