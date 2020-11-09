package com.panthers.main.jobModel;

import java.util.List;

public class DistrictingPlans {
    private Job job;
    private List<DistrictingPlan> plans;
    private DistrictingPlan extremeDistricting;
    private DistrictingPlan averageDistricting;
    private DistrictingPlan randomDistricting;
    //private List<T> counties;
    private List<DistrictPlot> boxAndWhiskerData;

    public DistrictingPlans(Job job, List<DistrictingPlan> plans) {
        this.job = job;
        this.plans = plans;
        this.extremeDistricting = null;
        this.averageDistricting = null;
        this.randomDistricting = null;
        this.boxAndWhiskerData = null;
    }

    /*GETTERS/SETTERS*/
    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public List<DistrictingPlan> getPlans() {
        return plans;
    }

    public void setPlans(List<DistrictingPlan> plans) {
        this.plans = plans;
    }

    public DistrictingPlan getExtremeDistricting() {
        return extremeDistricting;
    }

    public void setExtremeDistricting(DistrictingPlan extremeDistricting) {
        this.extremeDistricting = extremeDistricting;
    }

    public DistrictingPlan getAverageDistricting() {
        return averageDistricting;
    }

    public void setAverageDistricting(DistrictingPlan averageDistricting) {
        this.averageDistricting = averageDistricting;
    }

    public DistrictingPlan getRandomDistricting() {
        return randomDistricting;
    }

    public void setRandomDistricting(DistrictingPlan randomDistricting) {
        this.randomDistricting = randomDistricting;
    }

    public List<DistrictPlot> getBoxAndWhiskerData() {
        return boxAndWhiskerData;
    }

    public void setBoxAndWhiskerData(List<DistrictPlot> boxAndWhiskerData) {
        this.boxAndWhiskerData = boxAndWhiskerData;
    }

    /*FUNCTIONS*/
    /**
     * function generates box plot; will generate data needed to generate a box plot on the client side.
     */
    public String generateBoxPlot(){
        return null;
    }

    /**
     * function calculates number of counties across our districting plans
     * @return returns number of counties in our districing plan
     */
    public int calculateCounties(){
        return -1;
    }

    /**
     * generates summary of key information about this result from a job run.
     * @return returns the summary requested
     */
    public String generateRunSummary(){
        return null;
    }

    /**
     * function determines a districting fitting the type requested (RANDOM, EXTREME, AVERAGE)
     * @param type the type requested from the districtings
     * @return returns the districtingplan that coinsides with the requested type.
     */
    public DistrictingPlan determineTypeDistricting(DistrictingType type){
        return null;
    }

    /**
     * function calculates current districtings association with this districting plan.
     */
    public void calculateDistrictingAssociation(){

    }


}