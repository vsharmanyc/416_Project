package com.panthers.main.jobmodel;

import com.panthers.main.mapmodel.Demographic;
import com.panthers.main.mapmodel.States;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job {
    @Column(name = "num_districtings")
    private int numDistrictings;
    @Enumerated(EnumType.STRING)
    @Column(name="state")
    private States state;
    @ElementCollection(targetClass = Demographic.class)
    @Column(name = "demographic_groups")
    private List<Demographic> demographicGroups;
    @Column(name = "pop_eq_threshold")
    private Double popEqThreshold;
    @Column(name = "compactness")
    private String compactness;
    @Column(name="box_plot_string")
    private String boxPlotData;
    @Column(name="enacted_plan_string")
    private String enactedPlanData;
    @Column(name="random_plan_string")
    private String randomPlanData;
    @Column(name="extreme_plan_string")
    private String extremePlanData;
    @Column(name="average_plan_string")
    private String averagePlanData;
    @Column(name="sw_job_num")
    private int swJobNum;

    //@Id
   // @GeneratedValue(strategy = GenerationType.AUTO)
    //private int id;

    @Id
    @Column(name = "job_id")
    private int jobId;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_status")
    private JobStatus jobStatus;
    //private RunResults runResults;


    public Job(){}

    public Job(States state, int numDistrictings, List<Demographic> demographicGroups, Double popEqThreshold,
               String compactness) {
        this.state = state;
        this.numDistrictings = numDistrictings;
        this.demographicGroups = demographicGroups;
        this.popEqThreshold = popEqThreshold;
        this.compactness = compactness;
        this.jobId = -1;
        this.jobStatus = JobStatus.PENDING;
        this.boxPlotData = "NOT GENERATED";
        this.swJobNum = -1;
        this.enactedPlanData = "";
        this.extremePlanData = "";
        this.averagePlanData = "";
        this.randomPlanData = "";
    }

//    public Job(Integer jobId, String state, Integer numDistrictings, String demographicGroups, String popEqThreshold,
//               String compactness, String jobStatus) {
//        this.jobId = jobId;
//        this.state = States.valueOf(state);
//        this.demGroups = demographicGroups;
//        this.numDistrictings = numDistrictings;
////        this.demographicGroups = new ArrayList<>();
////        if (demographicGroups.contains(",")){
////            String[] list = demographicGroups.split(", ");
////            for (String s : list) {
////                this.demographicGroups.add(Demographic.valueOf(s));
////            }
////        }
////        else{
////            this.demographicGroups.add(Demographic.valueOf(demographicGroups));
////
////        }
//
//        this.popEqThreshold = popEqThreshold.toString();
//        this.compactness = compactness;
//    }
//
//    public Job(Integer jobId){
//        this.jobId = jobId;
//    }

    /*GETTERS/SETTERS*/

    public String getEnactedPlanData() {
        return enactedPlanData;
    }

    public void setEnactedPlanData(String enactedPlanData) {
        this.enactedPlanData = enactedPlanData;
    }

    public String getRandomPlanData() {
        return randomPlanData;
    }

    public void setRandomPlanData(String randomPlanData) {
        this.randomPlanData = randomPlanData;
    }

    public String getExtremePlanData() {
        return extremePlanData;
    }

    public void setExtremePlanData(String extremePlanData) {
        this.extremePlanData = extremePlanData;
    }

    public String getAveragePlanData() {
        return averagePlanData;
    }

    public void setAveragePlanData(String averagePlanData) {
        this.averagePlanData = averagePlanData;
    }

    public int getSwJobNum() {
        return swJobNum;
    }

    public void setSwJobNum(int swJobNum) {
        this.swJobNum = swJobNum;
    }

    public String getBoxPlotData() {
        return boxPlotData;
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    public List<Demographic> getDemographicGroups() {
        return demographicGroups;
    }

    public void setDemographicGroups(List<Demographic> demographicGroups) {
        this.demographicGroups = demographicGroups;
    }

    public Double getPopEqThreshold() {
        return popEqThreshold;
    }

    public void setPopEqThreshold(Double popEqThreshold) {
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

    public void setBoxPlotData(String boxPlotData){
        this.boxPlotData = boxPlotData;
    }

//    public void setDemGroups(){
//        demGroups = demographicGroups.toString();
//        System.out.println(demGroups);
//    }

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
                popEqThreshold + ",\n" + "Requested Compactness: " + compactness + "\n," + "State: " + state + ",\n"
                + ",\nStatus: " + jobStatus  +  "}";
    }
}
