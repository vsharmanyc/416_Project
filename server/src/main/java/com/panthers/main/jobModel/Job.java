package com.panthers.main.jobModel;

import com.panthers.main.mapModel.Demographic;
import com.panthers.main.mapModel.State;

import javax.swing.*;
import java.util.Date;
import java.util.List;

public class Job {
    private int numDistrictings;
    private List<Demographic> demographicGroups;
    private double popEqThreshold;
    private String compactness;
    private int jobId;
    private JobStatus jobStatus;
    //private DistrictingPlans runResults;


    public Job(int numDistrictings, List<Demographic> demographicGroups, double popEqThreshold,
               String compactness) {
        this.numDistrictings = numDistrictings;
        this.demographicGroups = demographicGroups;
        this.popEqThreshold = popEqThreshold;
        this.compactness = compactness;
        this.jobId = -1;
        this.jobStatus = JobStatus.PENDING;
    }

    public Job(int jobId){
        this.jobId = jobId;
    }

    /*GETTERS/SETTERS*/
    public int getName() {
        return numDistrictings;
    }

    public void setName(int numDistrictings) {
        this.numDistrictings = numDistrictings;
    }

    public List<Demographic> getDemographicGroups() {
        return demographicGroups;
    }

    public void setDemographicGroups(List<Demographic> demographicGroups) {
        this.demographicGroups = demographicGroups;
    }

    public double getPopEqThreshold() {
        return popEqThreshold;
    }

    public void setPopEqThreshold(double popEqThreshold) {
        this.popEqThreshold = popEqThreshold;
    }

    public String getCompactness() {
        return compactness;
    }

    public void setCompactness(String compactness) {
        this.compactness = compactness;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public int getNumDistrictings() {
        return numDistrictings;
    }

    public void setNumDistrictings(int numDistrictings) {
        this.numDistrictings = numDistrictings;
    }

    /*FUNCTIONS*/

    /**
     * Generates a job summary for display on the Client Side.
     * @return String pertaining to Job Summary
     */
    public String generateJobSummary(){
        return null;
    }
}
