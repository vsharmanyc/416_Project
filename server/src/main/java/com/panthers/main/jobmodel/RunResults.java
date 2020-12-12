package com.panthers.main.jobmodel;

import com.panthers.main.mapmodel.District;
import com.panthers.main.mapmodel.Precinct;

import java.util.ArrayList;
import java.util.List;

/**
 * Data pulled from results of one algorithm run, either on seawulf or server.
 * Contains the job that initiated this run, as well as the districting plans generated as per the user requested params
 */
public class RunResults {
    private Job job;
    private List<DistrictingPlan> plans;
    private DistrictingPlan extremeDistricting;
    private DistrictingPlan averageDistricting;
    private DistrictingPlan randomDistricting;
    private List<Precinct> precinctGeometry;
    private List<List<List<District>>> districtingGraph;
    private int counties;
    private List<BoxPlot> boxAndWhiskerData;
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
        this.boxAndWhiskerData = new ArrayList<>();
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
    public void generateBoxPlot(){
        List<List<Double>> mvapList = new ArrayList<>();
        for (int i = 0; i < plans.size(); i++){
            List<Double> mvaps = new ArrayList<>();
            for (DistrictingPlan plan: plans){
                mvaps.add(plan.getNthDistrict(i).getPercentVap());
            }
            mvapList.add(mvaps);
        }
        List<BoxPlot> boxPlots = new ArrayList<>();
        for (int i = 0; i < mvapList.size(); i++) {
            BoxPlot bp = new BoxPlot(mvapList.get(i));
            bp.findMaxMVAP();
            bp.findMinMVAP();
            bp.findFirstQuart();
            bp.findMeanMVAP();
            bp.findThirdQuart();
            addToBoxPlotGraph(bp);
        }
    }

    /**
     * function calculates number of counties across our districting plans
     */
    public void calculateCounties() {
        // Idea: From each districting plan, we calculate number of counties in a district.
        for (DistrictingPlan dp : plans) {
            List<District> dpDistricts = dp.getDistricts();
            for (District d : dpDistricts) {
                d.calculateCounties();
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

    /**
     * function adds given precincts to precinct geometry.
     * @param geo the precincts we are to add to this runresults precinctgeometry.
     */
    public void addToPrecinctGeometry(List<Precinct> geo){
        precinctGeometry.addAll(geo);
    }

    /**
     * adds the given districts to this runresults districting graph (stored as adjacency list)
     * @param graph list of districts to add
     */
    public void addToDistrictingGraphs(List<List<District>> graph){
        districtingGraph.add(graph);
    }

    /**
     * Function will add this boxplot to the runresults box plot graph
     * @param bp boxplot to add
     */
    public void addToBoxPlotGraph(BoxPlot bp){
        this.boxAndWhiskerData.add(bp);
    }

    public List<BoxPlot> getBoxAndWhiskerData(){
        return this.boxAndWhiskerData;
    }

    /**
     * function will calculate average from sum of MVAPS and number of MVAPS, then add it to the
     * @param sumMVAP sum MVAP calculated previous to this method
     * @param length number of mvaps involved in sum, needed to determine average
     */
    public void addAverageToList(double sumMVAP, int length){
        double avg = sumMVAP / length;
        averageDistrictingMVAP.add(avg);
    }

    /**
     * function determines statistical average MVAP throughout the districtings generated.
     */
    public void determineStatisticalAverage(){

    }

    /**
     * function scores districting plans according to their deviation from the average districting, and stores this
     * deviation in the specific districtingplan
     */
    public void scoreDistrictingPlans(){

    }

    /**
     * function will sift through the districting plans and find the one with the lowest deviation from average
     * districting. this will represent our 'average districting'
     * @return returns the found-to-be average districting
     */
    public DistrictingPlan findLowestScoringDistricting(){
        return null;
    }


}
