package com.panthers.main.jobModel;

import com.panthers.main.mapModel.Demographic;
import com.panthers.main.mapModel.State;

import java.util.List;

public class Job {
    private String name;
    private State state;
    private List<Demographic> demographicGroups;
    private double popEqThreshold;
    private CompactnessMeasure compactness;
    private int jobId;
    private JobStatus jobStatus;
    //private DistrictingPlans runResults;


    public Job(String name, State state, List<Demographic> demographicGroups, double popEqThreshold,
               CompactnessMeasure compactness, int jobId) {
        this.name = name;
        this.state = state;
        this.demographicGroups = demographicGroups;
        this.popEqThreshold = popEqThreshold;
        this.compactness = compactness;
        this.jobId = jobId;
        this.jobStatus = JobStatus.PENDING;
    }

    /*GETTERS/SETTERS*/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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

    public CompactnessMeasure getCompactness() {
        return compactness;
    }

    public void setCompactness(CompactnessMeasure compactness) {
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
    /*FUNCTIONS*/

    /**
     * Generates a job summary for display on the Client Side.
     * @return String pertaining to Job Summary
     */
    public String generateJobSummary(){
        return null;
    }
}
