package com.panthers.main.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.panthers.main.dataaccess.SeaWulfProperties;
import com.panthers.main.jobmodel.*;
import com.panthers.main.jpa.Dao;
import com.panthers.main.jpa.JpaJobDao;
import com.panthers.main.mapmodel.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Functions as the main point of the running process.
 * First in command. After a RESTful service is tapped into regarding the job run,
 * program will go through this job service.
 */

@Service
public class JobHandler{
    private DispatcherHandler dispatcherHandler;
    private States state;
    private Set<District> currentDistrictings;
    private List<Job> jobHistory;
    private List<Precinct> precincts;
    private List<District> districts;
    private RunResults runResults;
    private SeaWulfProperties properties;

    private static Dao<Job> jpaUserDao = new JpaJobDao();


    @Autowired
    public JobHandler(DispatcherHandler dispatcherHandler) {
        this.properties = new SeaWulfProperties();
        this.properties.getProperties();
        this.dispatcherHandler = dispatcherHandler;
        this.state = null;//Originally, no state is selected
        this.jobHistory = jpaUserDao.getAll();// Get job history from EM upon first load
        System.out.println("Job His Length " + jobHistory.size());
        /*loadPrecincts();
        loadDistricts();
        generateDummyRunResults();
        runResults.calculateCounties();
        runResults.sortResultDistrictings();*/
    }

    /*GETTERS/SETTERS*/
    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    public Set<District> getCurrentDistrictings() {
        return currentDistrictings;
    }

    public void setCurrentDistrictings(Set<District> currentDistrictings) {
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
     *
     * @param name              Name of Job
     * @param state             State job is concerned with
     * @param demographicGroups User requested demographic groups to analyze
     * @param popEqThreshold    User inputted population equality threshold
     * @param compactness       User specified target compactness
     * @return returns true on successfull job creation, false otherwise.
     */
    public RunResults handleDataRequest(String name, State state, List<Demographic> demographicGroups,
                                        double popEqThreshold, String compactness) {
        return null;
    }

    /**
     * Takes user input from front-end to create a 'Job' object.
     *
     * @param job Job we are creating//creating ID for.
     * @return returns true on successful job creation, false otherwise.
     */
    public List<Job> createJob(Job job) {
        if (jobHistory.size() == 0)
            job.setJobId(1);//If no other jobs, its job id is 1
        else
            job.setJobId(getNextJobId());//Sets the jobs id

        job.setJobStatus(JobStatus.QUEUED);
        System.out.println("Created job:" + job);
        jobHistory.add(job);

        dispatcherHandler.dispatchJob(job);
        System.out.println("Dispatched job #" + job.getJobId());

        jpaUserDao.save(job);

        /*Object[] jobs = jpaUserDao.getAll().toArray();
        for(int i = 0; i < jobs.length; i++)
            System.out.println(jobs[i].toString());*/

        return jobHistory;
    }

    /**
     * Method sends the results of the job run to our client.
     *
     * @return returns true if sending was successfull, false otherwise.
     */
    public boolean sendResultsToClient() {
        return false;
    }

    /**
     * Function will marshall districts enacted on specified state from the Database
     *
     * @return returns the districts from the database.
     */
    public List<District> requestDistrictsFromDB(State state) {
        return null;
    }

    /**
     * Function will marshall precincts enacted on specified state from the Database
     *
     * @return returns the precincts from the database.
     */
    public List<Precinct> requestPrecinctsFromDB(State state) {
        return null;
    }

    /**
     * Function simply places job on Job Queue
     *
     * @param job job to be queued
     * @return returns true if job was indeed queued, false otherwise.
     */
    public boolean queueJob(Job job) {
        return false;
    }

    /**
     * Marshals past run results, and returns them to client.
     *
     * @return returns the past run results stored on DB
     */
    public List<RunResults> getPastRunResults() {
        return null;
    }

    /**
     * function talks to entity manager to get the job history
     *
     * @return returns the job history from the EM
     */
    public List<Job> getJobHistory() {
        for (Job j: this.jobHistory){
            if (j.getSwJobNum() != -1){
                if (j.getJobStatus() == JobStatus.RUNNING){
                    boolean finished = monitorSeaWulfProgress(j);
                    if (finished){
                        SeaWulfHandler swh = new SeaWulfHandler(j);
                        //Job finished. Post process it.
                        swh.getJobFromSeaWulf(j.getJobId());
                    }
                    else{
                        System.out.println("Job #" + j.getJobId() + " still running.");
                    }
                }
            }
        }
        return this.jobHistory;
    }

    /**
     * function checks max job id of jobs in history, and returns that number +1 for the next job's id
     *
     * @return returns the next job's id
     */
    private int getNextJobId() {
        int maxId = -1;

        //Determines max job id, returns that +1
        for (Job job : jobHistory) {
            if (job.getJobId() > maxId)
                maxId = job.getJobId();
        }
        return maxId + 1;
    }

    /**
     * function cancels the execution of a job
     *
     * @param jobId jobId of job to cancel
     * @return the newly updated job history, with this cancelled job change
     */
    public List<Job> cancelJob(int jobId) {
        int index = findJob(jobId);

        if (index == -1)
            return jobHistory;

        jobHistory.get(index).setJobStatus(JobStatus.CANCELLED);
        jpaUserDao.update(jobHistory.get(index));
        dispatcherHandler.cancelJob(jobHistory.get(index));
        System.out.println("Cancelled execution of Job #" + jobId);
        return jobHistory;
    }

    /**
     * function deletes the job from the job history
     *
     * @param jobId jobId of job to delete
     * @return the newly updated job history, with this deleted job change
     */
    public List<Job> deleteJob(int jobId) {
        int index = findJob(jobId);

        if (index == -1)
            return jobHistory;//Some error occurred, just ignore the call.

        jpaUserDao.delete(jobHistory.get(index));
        jobHistory.remove(index);
        System.out.println("Deleted Job #" + jobId + " from Job History.");
        return jobHistory;
    }

    /**
     * function finds index of job in job history list.
     *
     * @param jobId job to search for
     * @return index in job history of the requested job
     */
    private int findJob(int jobId) {
        for (int i = 0; i < jobHistory.size(); i++) {
            if (jobHistory.get(i).getJobId() == jobId)
                return i;
        }
        return -1;
    }

    private Job getJob(int jobId){
        for (int i = 0; i < jobHistory.size(); i++) {
            if (jobHistory.get(i).getJobId() == jobId)
                return jobHistory.get(i);
        }
        return null;
    }



    /* TESTING METHODS*/

//    private void loadPrecincts() {
//        List<Precinct> precincts = new ArrayList<>();
//        String path = "/Users/james/Documents/Code/University/416_Project/server/src/main/resources/static/MD_Precinct_data.json";
//        // String path = System.getProperty("java.class.path").split("server")[0] + "server/src/main/resources/static/MD_Precinct_data.json";
//        try {
//            path = new String(Files.readAllBytes(Paths.get(path)));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        JSONObject obj = new JSONObject(path);
//        JSONArray features = obj.getJSONArray("precincts");
//        for (int i = 0; i < features.length(); i++) {
//            JSONObject precinct = features.getJSONObject(i);
//            String name = precinct.getString("PRECINCT");
//            int population = precinct.getInt("TOTAL");
//            int vap = precinct.getInt("TOTVAP");
//            HashMap<Demographic, Integer> mpop = new HashMap<>();
//            mpop.put(Demographic.WHITE, precinct.getInt("WTOT"));
//            mpop.put(Demographic.AFRICAN_AMERICAN, precinct.getInt("BTOT"));
//            mpop.put(Demographic.ASIAN, precinct.getInt("ATOT"));
//            mpop.put(Demographic.AM_INDIAN_AK_NATIVE, precinct.getInt("AIANTOT"));
//            mpop.put(Demographic.HISPANIC_LATINO, precinct.getInt("HTOT"));
//            mpop.put(Demographic.NH_OR_OPI, precinct.getInt("NHOPTOT"));
//
//            HashMap<Demographic, Integer> mvappop = new HashMap<>();
//            mvappop.put(Demographic.WHITE, precinct.getInt("WVAP"));
//            mvappop.put(Demographic.AFRICAN_AMERICAN, precinct.getInt("BVAP"));
//            mvappop.put(Demographic.ASIAN, precinct.getInt("AVAP"));
//            mvappop.put(Demographic.AM_INDIAN_AK_NATIVE, precinct.getInt("AIANVAP"));
//            mvappop.put(Demographic.HISPANIC_LATINO, precinct.getInt("HVAP"));
//            mvappop.put(Demographic.NH_OR_OPI, precinct.getInt("NHOPVAP"));
//
//            String precinctID = precinct.getString("PRECINCTID");
//
//
//            Population pop = new Population(population, vap, mpop, mvappop, precinctID);
//            Precinct p = new Precinct(precinct.getString("PRECINCT"), new ArrayList<Precinct>(), precinctID,
//                    pop, population, vap, mpop, mvappop, null, precinct.getInt("DISTRICTID"),
//                    precinct.getString("COUNTY"));
//            precincts.add(p);
//        }
//        this.precincts = precincts;
//    }
//
//    private void loadDistricts() {
//        List<District> districts = new ArrayList<>();
//        String path = "/Users/james/Documents/Code/University/416_Project/server/src/main/resources/static/MD_Districts_data.json";
//        // String path = System.getProperty("java.class.path").split("server")[0] + "server/src/main/resources/static/MD_Precinct_data.json";
//        try {
//            path = new String(Files.readAllBytes(Paths.get(path)));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        JSONObject obj = new JSONObject(path);
//        JSONArray features = obj.getJSONArray("districts");
//
//        for (int i = 0; i < features.length(); i++) {
//            JSONObject district = features.getJSONObject(i);
//            int id = district.getInt("DISTRICTID");
//            String state = district.getString("STATE");
//            District d = new District(state, id, null, null, null);
//            districts.add(d);
//        }
//
//        //Set precincts to a district
//        for (District district : districts) {
//            List<Precinct> precincts = new ArrayList<>();
//            int did = district.getDistrictNum();
//            for (Precinct precinct : this.precincts) {
//                if (precinct.getDistrictID() == did)
//                    precincts.add(precinct);
//            }
//            district.setPrecincts(precincts);
//        }
//        this.districts = districts;
//        //Want 231 precincts/district. 1849 precincts, 8 districts.
//
//    }

//    private void generateDummyRunResults() {
//        // We need to generate dummy districting plans, well make 10.
//        List<Demographic> dg = new ArrayList<>();
//        dg.add(Demographic.AFRICAN_AMERICAN);
//        dg.add(Demographic.ASIAN);
//
//        Job job = new Job(States.MD, 10, "AA", 0.003, "Somewhat Compact", JobStatus.QUEUED);
//        job.setJobId(10);
//        job.setJobStatus(JobStatus.COMPLETED);
//        job.setName(24);
//
//        List<DistrictingPlan> plans = new ArrayList<>();
//        List<Precinct> ps = new ArrayList<>(precincts);
//        int counter = 0;
//        Random rand = new Random();
//        for (int i = 0; i < 10; i++) {
//            List<District> planDistricts = new ArrayList<>();
//            for (int j = 0; j < districts.size(); j++) {
//                List<Precinct> districtPrecincts = new ArrayList<>();
//                for (int k = 0; k < 231; k++) {
//                    int index = rand.nextInt(ps.size());
//                    districtPrecincts.add(ps.remove(index));
//                }
//                District d = new District("MD", counter, null, districtPrecincts, null);
//                d.calculatePercentMVAP(job.getDemographicGroups());
//                planDistricts.add(d);
//                counter++;
//                ps = new ArrayList<>(precincts);
//            }
//
//            plans.add(new DistrictingPlan(States.MD, planDistricts, 0.003,
//                    "Somewhat Compact", DistrictingType.RANDOM, 0));
//        }
//        this.runResults = new RunResults(job, plans);
//    }

    public RunResults getRunResults() {
        return runResults;
    }

    public List<BoxPlot> generateBoxPlot(){
        this.runResults.generateBoxPlot();
        return runResults.getBoxAndWhiskerData();
    }

    public void transferSummaryFiles(int jobId){
        Job job = getJob(jobId);
        System.out.println("Transferring Summary from SeaWulf for Job#"+job.getJobId());
        String path = System.getProperty("java.class.path").split("server")[0] + properties.getServerStaticWd();
        ProcessBuilder pb = new ProcessBuilder("expect", path + properties.getTransferSummaryBash());

        buildTransferScript(path, job);

        System.out.println("Sending files to SeaWulf. Expect a DUO Push...unless your on VPN :)");
        pb.directory(new File(path));
        pb.redirectErrorStream(true);
        try{
            pb.start();
        }
        catch (IOException io){
            io.printStackTrace();
        }
    }

    private void buildTransferScript(String path, Job job){
        File bash = new File(path + properties.getTransferSummaryBash());
        FileWriter bashOut;
        try {
            bashOut = new FileWriter(bash);
            ObjectMapper objmp = new ObjectMapper();
            //grabbing script from system properties file.
            String script = String.format(properties.getSwSummaryTransferFile(), properties.getNetID(), job.getState().toString(), job.getJobId(),
                    job.getJobId(), job.getState(), properties.getNetID(), job.getState().toString(), job.getJobId(),
                    job.getJobId(), job.getState(), properties.getNetID(), job.getState().toString(), job.getJobId(),
                    job.getJobId(), job.getState(), properties.getNetID(), job.getState().toString(), job.getJobId(),
                    job.getJobId(), job.getState(),
                    properties.getNetID(), properties.getPassword(), properties.getNetID(), properties.getPassword(), properties.getNetID(), properties.getPassword(), properties.getNetID(), properties.getPassword());
            bashOut.write(script);

            bashOut.close();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean monitorSeaWulfProgress(Job job){
        System.out.println("Transferring Summary from SeaWulf for Job");
        String path = System.getProperty("java.class.path").split("server")[0] + properties.getServerStaticWd();
        ProcessBuilder pb = new ProcessBuilder("expect", path + properties.getMonitorSwProgressBash());

        buildProgressScript(path, job.getJobId());

        System.out.println("Checking if job#" + job.getJobId() + "is finished.");
        pb.directory(new File(path));
        pb.redirectErrorStream(true);
        try{
            pb.start();
        }
        catch (IOException io){
            io.printStackTrace();
        }
        //read the transferred progress.txt
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(path+"/progress.txt"));
            String line = reader.readLine();
            int districtingsComplete = Integer.parseInt(line);
            if (districtingsComplete >= job.getNumDistrictings())
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void buildProgressScript(String path, int jobId){
        File bash = new File(path + properties.getMonitorSwProgressBash());
        FileWriter bashOut;
        try {
            bashOut = new FileWriter(bash);
            ObjectMapper objmp = new ObjectMapper();
            //grabbing script from system properties file.
            String script = String.format(properties.getMonitorSwProgressFile(), properties.getNetID(), properties.getNetID(),
                    properties.getPassword(), jobId, properties.getNetID(), jobId, properties.getNetID(),
                    properties.getPassword());
            bashOut.write(script);

            bashOut.close();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }
}
