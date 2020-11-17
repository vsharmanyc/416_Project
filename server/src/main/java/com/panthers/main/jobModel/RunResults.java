package com.panthers.main.jobModel;

import com.panthers.main.mapModel.District;
import com.panthers.main.mapModel.Precinct;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RunResults {
    private Job job;
    private List<DistrictingPlan> plans;
    private DistrictingPlan extremeDistricting;
    private DistrictingPlan averageDistricting;
    private DistrictingPlan randomDistricting;
    private List<Precinct> precinctGeometry;
    private List<List<List<District>>> districtingGraph;
    private int counties;
    //private List<BoxPlot> boxAndWhiskerData;
    //private Summary summary;
    private List<Double> averageDistrictingMVAP;

    public RunResults(Job job, List<DistrictingPlan> plans) {
        this.job = job;
        this.plans = plans;
        this.extremeDistricting = null;
        this.averageDistricting = null;
        this.randomDistricting = null;
        this.precinctGeometry = null;
        this.districtingGraph = null;
        this.averageDistrictingMVAP = null;
        this.counties = 0;
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

    /*FUNCTIONS*/
    /**
     * function generates box plot; will generate data needed to generate a box plot on the client side.
     */
    public String generateBoxPlot(){
        return null;
    }

    /**
     * function calculates number of counties across our districting plans
     */
    public void calculateCounties() {
        // Idea: From each districting plan, we calculate number of counties in a district.
        for (DistrictingPlan dp : plans) {
            List<District> dpDistricts = dp.getDistricts();
            for (District d : dpDistricts) {
                List<Precinct> precincts = d.getPrecincts();
                for (Precinct precinct : precincts) {
                    //If county isnt accounted for, add it to the district's list + increment the count
                    if (!d.checkIfCountedCounty(precinct.getCounty())) {
                        d.addCounty(precinct.getCounty());
                        d.incrementCountyCount();
                    }
                }
            }
        }
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

    /**
     * Sorts resulting districtings by increasing percentage voter age population by user selected minority(s)
     */
    public void sortResultDistrictings(){
        for (DistrictingPlan dp: plans){
            dp.sortDistrictsByMVAP();
        }
    }



}
