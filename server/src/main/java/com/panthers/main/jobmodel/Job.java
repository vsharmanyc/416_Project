package com.panthers.main.jobmodel;

import com.panthers.main.mapmodel.Demographic;
import com.panthers.main.mapmodel.States;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job {
    private int numDistrictings;

    @Enumerated(EnumType.STRING)
    private States state;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Demographic.class)
    private List<Demographic> demographicGroups;
    private double popEqThreshold;
    private String compactness;

    //@Id
   // @GeneratedValue(strategy = GenerationType.AUTO)
    //private int id;

    @Id
    private int jobId;

    @Enumerated(EnumType.ORDINAL)
    private JobStatus jobStatus;
    //private RunResults runResults;


    public Job(States state, int numDistrictings, List<Demographic> demographicGroups, double popEqThreshold,
               String compactness) {
        this.state = state;
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
    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

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

    /**
     * Returns string representation of job
     * @return string representation of job
     */
    public String toString(){
        return "\n{\nJob #" + jobId + ",\n" + "Requested Districtings: " + numDistrictings + ",\n"+
                "Demographic Groups: " + demographicGroups + ",\n" + "Population Equality Threshold: " +
                popEqThreshold + ",\n" + "Requested Compactness: " + compactness + "\n}";
    }
}
