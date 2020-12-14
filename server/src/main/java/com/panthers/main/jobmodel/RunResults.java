package com.panthers.main.jobmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panthers.main.dataaccess.SeaWulfProperties;
import com.panthers.main.jpa.Dao;
import com.panthers.main.jpa.JpaJobDao;
import com.panthers.main.mapmodel.District;
import com.panthers.main.mapmodel.Precinct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Data pulled from results of one algorithm run, either on seawulf or server.
 * Contains the job that initiated this run, as well as the districting plans generated as per the user requested params
 */
public class RunResults {
    private Job job;
    @JsonIgnore
    private List<DistrictingPlan> plans;
    private DistrictingPlan extremeDistricting;
    private DistrictingPlan averageDistricting;
    private DistrictingPlan randomDistricting;
    @JsonIgnore
    private List<Precinct> precinctGeometry;
    @JsonIgnore
    private List<List<List<District>>> districtingGraph;
    @JsonIgnore
    private int counties;
    @JsonIgnore
    private List<BoxPlot> boxAndWhiskerData;
    //private Summary summary;
    @JsonIgnore
    private List<Double> averageDistrictingMVAP;
    @JsonIgnore
    private static Dao<Job> jpaUserDao = new JpaJobDao();
    @JsonIgnore
    private SeaWulfProperties properties;

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
        this.properties = new SeaWulfProperties();
        this.properties.getProperties();
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
        System.out.println("Generating Box Plot for Run Result.");
        List<List<Double>> mvapList = new ArrayList<>();
        int size = plans.get(0).getDistricts().size();
        for (int i = 0; i < size; i++){
            List<Double> mvaps = new ArrayList<>();
            for (DistrictingPlan plan: plans){
                mvaps.add(plan.getNthDistrict(i).getPercentVap());
            }
            mvapList.add(mvaps);
        }
        System.out.println(mvapList);
        List<BoxPlot> boxPlots = new ArrayList<>();
        for (int i = 0; i < mvapList.size(); i++) {
            BoxPlot bp = new BoxPlot(mvapList.get(i), (i+1));
            bp.findMaxMVAP();
            bp.findMinMVAP();
            bp.findFirstQuart();
            bp.findSecondQuart();
            bp.findThirdQuart();
            addToBoxPlotGraph(bp);
        }

    }

    /**
     * function calculates number of counties across our districting plans
     */
    public void calculateCounties() {
        // Idea: From each districting plan, we calculate number of counties in a district.
        System.out.println("Calculating counties for all districts.");
        for (DistrictingPlan dp : plans) {
            List<District> dpDistricts = dp.getDistricts();
            for (District d : dpDistricts) {
                d.calculateCounties();
            }
        }
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
        System.out.println("Calculating statistical averages...");
        int districts = this.plans.get(0).getDistricts().size();
        List<Double> statisticalAverage = new ArrayList<>();
        for (int i = 0; i < districts; i++){
            double totalMvap = 0.0;
            for (DistrictingPlan dp: this.plans){
                totalMvap += dp.getNthDistrict(i).getPercentVap();
            }
            totalMvap /= districts;
            //this is totalMvap for district 'i'
            statisticalAverage.add(totalMvap);
        }
        this.averageDistrictingMVAP = statisticalAverage;
        System.out.println("Averages are:" + statisticalAverage);
    }

    /**
     * function scores districting plans according to their deviation from the average districting, and stores this
     * deviation in the specific districtingplan
     */
    public void scoreDistrictingPlans(){
        System.out.println("Scoring districting plans based off of their deviation from the average");
        for (DistrictingPlan dp: this.plans){
            double deviation = 0.0;
            List<District> currentDistrictings = dp.getDistricts();
            for (int i = 0; i < currentDistrictings.size(); i++){
                deviation += Math.abs(currentDistrictings.get(i).getPercentVap() - this.averageDistrictingMVAP.get(i));
            }
            dp.setDeviationFromAverage(deviation);
        }
    }

    /**
     * function will sift through the districting plans and find the one with the lowest deviation from average
     * districting. this will represent our 'average districting'
     * @return returns the found-to-be average districting
     */
    public void findLowestScoringDistricting(){
        System.out.println("Finding the average districting/one with least deviation.");
        DistrictingPlan minDp = this.plans.get(0);
        int index = 0;
        for (int i = 0; i < this.plans.size(); i++){
            if (this.plans.get(i).getDeviationFromAverage() < minDp.getDeviationFromAverage()){
                minDp = this.plans.get(i);
                index = i;
            }
        }
        minDp.setDistrictingPlanID(index);
        this.averageDistricting = minDp;
        this.plans.remove(index);
        minDp.setType(DistrictingType.AVERAGE);
        System.out.println("Average Districting:" + minDp);
    }

    public void findHighestScoringDistricting(){
        System.out.println("Finding the average districting/one with most deviation.");
        DistrictingPlan maxDp = this.plans.get(0);
        int index = 0;
        for (int i = 0; i < this.plans.size(); i++){
            if (this.plans.get(i).getDeviationFromAverage() > maxDp.getDeviationFromAverage()){
                maxDp = this.plans.get(i);
                index = i;
            }
        }
        maxDp.setDistrictingPlanID(index);
        this.extremeDistricting = maxDp;
        this.plans.remove(index);
        maxDp.setType(DistrictingType.EXTREME);
        System.out.println("Extreme Districting:" + maxDp);
    }

    public void findRandomDistricting(){
        System.out.println("Finding a random districting");
        int rand_index = (int)(Math.random() * this.plans.size());
        DistrictingPlan randDp = this.plans.get(rand_index);
        randDp.setDistrictingPlanID(rand_index);
        this.randomDistricting = randDp;
        randDp.setType(DistrictingType.RANDOM);
        System.out.println("Random Districting:" + randDp);
    }

    public void addDistrictingsBack(){
        //removed for JSON output. Add them back now
        plans.add(randomDistricting);
        plans.add(extremeDistricting);
        plans.add(averageDistricting);
    }

    public void storeBoxPlotInJob(){
        String bpData = aggregateBoxPlotData();
        System.out.println(bpData);
        job.setBoxPlotData(bpData);
        jpaUserDao.update(job);
    }

    public String aggregateBoxPlotData(){
        String bpData[] = new String[boxAndWhiskerData.size()];
        for (int i = 0; i < boxAndWhiskerData.size(); i++){
            bpData[i] = boxAndWhiskerData.get(i).compileToCanvasJSFormat();
        }
        return Arrays.toString(bpData);
    }
    @Override
    public String toString(){
        return "districtingPlans:";
    }

    public void generateSummary(){
        System.out.println("Generating Summary for job" + job.getJobId());
        String path = System.getProperty("java.class.path").split("server")[0] + properties.getServerStaticWd();

        ProcessBuilder pb = new ProcessBuilder("python3", path + properties.getGenerateSummaryScript() + " "
                + String.format(properties.getGenerateSummaryScriptInput(), job.getState().toString(), job.getJobId()));

        System.out.println("Generating Job Summary now. \nEST: NY: 10 min | PA: 7 min | MD: 3 min");
        pb.directory(new File(path));
        pb.redirectErrorStream(true);
        try{
            pb.start();
        }
        catch (Exception io){
            io.printStackTrace();
        }
    }

    public void writeSummaryToSeaWulf(){
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            String p = System.getProperty("java.class.path").split("server")[0] + properties.getServerStaticWd() + "/results";
            Path path = Paths.get(p);
            path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
            while (1==1) {
                WatchKey key;
                key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> eventType = event.kind();
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();
                    if (eventType == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    } else if (eventType == StandardWatchEventKinds.ENTRY_CREATE ||
                            eventType == StandardWatchEventKinds.ENTRY_MODIFY) {
                        final Path itemChanged = (Path) event.context();
                        if (itemChanged.endsWith(String.format(properties.getSummaryFileName(), job.getJobId(), job.getState().toString()))) {
                            System.out.println("Summary file has been generated. Transferring summary files to the seawulf");
                            String transferPath = System.getProperty("java.class.path").split("server")[0] + properties.getServerStaticWd();

                            buildTransferToSWScript(transferPath);
                            ProcessBuilder pb = new ProcessBuilder("expect", transferPath + properties.getTransferSummaryFilesToSeaWulfFile());

                            System.out.println("Transferring files to SeaWulf. Expect a DUO prompt...");
                            pb.directory(new File(transferPath));
                            pb.redirectErrorStream(true);
                            try{
                                pb.start();
                            }
                            catch (Exception io){
                                io.printStackTrace();
                            }
                        }
                    }
                }
                boolean changed = key.reset();
                if (!changed) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildTransferToSWScript(String path){
            File bash = new File(path + properties.getTransferSummaryFilesToSeaWulfFile());
            FileWriter bashOut;
            try {
                bashOut = new FileWriter(bash);
                ObjectMapper objmp = new ObjectMapper();
                //grabbing script from system properties file.
                String script = String.format(properties.getTransferSummaryFilesToSeaWulfBash(),
                        job.getJobId(), job.getState().toString(), job.getJobId(), job.getState().toString(),
                        job.getJobId(), job.getState().toString(), job.getJobId(), job.getState().toString(),
                        properties.getNetID(), job.getState().toString(), job.getJobId(), properties.getNetID(),
                        properties.getPassword());
                bashOut.write(script);

                bashOut.close();
            }

            catch (Exception e){
                e.printStackTrace();
            }
    }

}
